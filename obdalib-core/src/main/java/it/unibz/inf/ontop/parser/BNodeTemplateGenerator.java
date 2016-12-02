package it.unibz.inf.ontop.parser;

import it.unibz.inf.ontop.model.*;
import it.unibz.inf.ontop.model.impl.NumberedBNodePredicateImpl;
import it.unibz.inf.ontop.sql.*;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.util.TablesNamesFinder;

import java.util.*;

/**
 *
 * Changes unlabeled blank nodes according to the corresponding
 * target mapping part and the type of a database
 *
 *
 */
public class BNodeTemplateGenerator {
    private final DBMetadata metadata;
    private OBDAMappingAxiom mapping;

    public BNodeTemplateGenerator(DBMetadata metadata, OBDAMappingAxiom mapping) {
        this.metadata = metadata;
        this.mapping = mapping;
    }

    public void replaceUnlabeledBNodes(Select select, List<Function> targetQuery, OBDADataFactory dfac){
        Map<Term, Term> map = getBNodeTemplateReplacementMap(select, targetQuery, dfac);
        for (Function atom : targetQuery) {
            for (int i = 0 ; i < atom.getTerms().size(); i++){
                final Term term = atom.getTerm(i);
                if (map.containsKey(term)){
                    atom.setTerm(i, map.get(term));
                }
            }
        }
    }

    private Map<Term, Term> getBNodeTemplateReplacementMap(Select select, List<Function> targetQuery, OBDADataFactory dfac) {
        Map<Term, Term> map = new HashMap<>();
        for (Function atom : targetQuery) {
            for (Term term : atom.getTerms()) {
                if (map.containsKey(term))
                    continue;

                if (term instanceof Function) {
                    Function function = (Function) term;

                    Predicate pred = function.getFunctionSymbol();
                    // if it's unlabeled blank node change it with the node
                    // of the the type (BNode_{}_{}, att1, att2)
                    if (pred instanceof NumberedBNodePredicateImpl) {
                        // Find a name of the relation in the FROM part of the sql statement
                        // under assumption that sqlquery is just simple query with only
                        // one relation in the FROM part and with no WITH statement
                        SelectBody selectBody = select.getSelectBody();
                        if (selectBody instanceof PlainSelect) {
                            //FromItem table = ((PlainSelect) selectBody).getFromItem();
                            TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
                            List<String> tableList = tablesNamesFinder.getTableList(select);
                            QuotedIDFactory idfac = metadata.getQuotedIDFactory();
                            //Table tableName = (Table) table;
                            //TODO: Is this schema ok and is it that important
                            String schemaName = "";
                            List<String> listOfAttributes = new ArrayList<>();
                            for (String tableName : tableList){
                                RelationID tableId = idfac.createRelationID(schemaName, tableName);
                                // Checks if the table has primary key
                                UniqueConstraint primaryKey = metadata.getDatabaseRelation(tableId).getPrimaryKey();

                                if (primaryKey != null) {
                                    for (Attribute attr : primaryKey.getAttributes()){
                                        //listOfAttributes.add(attr.getID().getName());
                                        listOfAttributes.add(attr.getQualifiedID().toString());
                                    }
                                }
                            }

                            Term newBnode = constructNewBNode(dfac, listOfAttributes);
                            map.put(term, newBnode);
                        }
                    }
                }
            }
        }
        return map;
    }

    /**
        Constructs a blank node of the type (unlabeled_{mapping_id}_{attr1}_{attr2}
        where those attributes are given as a parameter of the method
     */
    private Term constructNewBNode(OBDADataFactory dfac, List<String> listOfAttributes) {
        StringBuilder bNodePattern = new StringBuilder();
        bNodePattern = bNodePattern.append("unlabeled_").append(mapping.getId());
        List<Term> terms = new LinkedList<>();
        final String PLACEHOLDER = "{}";
        //Create Blank node of the type (BNode_{}_{}, att1, att2)
        for (String attr :listOfAttributes) {
            bNodePattern = bNodePattern.append("_");
            bNodePattern = bNodePattern.append(PLACEHOLDER);
            Variable column = dfac.getVariable(attr);
            terms.add(column);
        }
        ValueConstant bNodeTemplate = dfac.getConstantLiteral(bNodePattern.toString());
        terms.add(0, bNodeTemplate);
        return dfac.getBNodeTemplate(terms);
    }

}
