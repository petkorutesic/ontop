package it.unibz.inf.ontop.parser;

import it.unibz.inf.ontop.model.*;
import it.unibz.inf.ontop.model.impl.NumberedBNodePredicateImpl;
import it.unibz.inf.ontop.sql.*;
import it.unibz.inf.ontop.sql.api.ParsedSQLQuery;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.util.TablesNamesFinder;

import java.util.*;
import java.util.stream.Collectors;

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

    public void replaceUnlabeledBNodes(ParsedSQLQuery sqlQueryParsed, List<Function> targetQuery, OBDADataFactory dfac)
    throws JSQLParserException{
        Map<Term, Term> map = getBNodeTemplateReplacementMap(sqlQueryParsed, targetQuery, dfac);
        for (Function atom : targetQuery) {
            for (int i = 0 ; i < atom.getTerms().size(); i++){
                final Term term = atom.getTerm(i);
                if (map.containsKey(term)){
                    atom.setTerm(i, map.get(term));
                }
            }
        }
    }

    private Map<Term, Term> getBNodeTemplateReplacementMap(ParsedSQLQuery sqlQueryParsed, List<Function> targetQuery, OBDADataFactory dfac)
                    throws JSQLParserException {
        Map<Term, Term> map = new HashMap<>();
        for (Function atom : targetQuery) {
            for (Term term : atom.getTerms()) {
                if (map.containsKey(term))
                    continue;
                if (term instanceof Function) {
                    Function function = (Function) term;
                    Predicate pred = function.getFunctionSymbol();

                    if (pred instanceof NumberedBNodePredicateImpl) {
                        // Find a name of the relation in the FROM part of the sql statement
                        Select select = sqlQueryParsed.getStatement();
                        SelectBody selectBody = select.getSelectBody();
                        if (selectBody instanceof PlainSelect) {
                            /*TODO Do we have here more tables then necessary and how about aliases of
                            atribute names */
                            //Going throught the list of all tables
                            Iterator it = sqlQueryParsed.getTables().entrySet().iterator();
                            List<String> listOfAttributes = new ArrayList<String>();
                            while (it.hasNext()){
                                Map.Entry<RelationID,RelationID> table = (Map.Entry<RelationID, RelationID>)it.next();
                                UniqueConstraint primaryKey = metadata.getDatabaseRelation(table.getValue()).getPrimaryKey();
                                // Checks if the table has primary key
                                if (primaryKey != null) {
                                    listOfAttributes.addAll(primaryKey.getAttributes().stream().map(
                                            attr -> table.getKey().getTableName() + "." + attr.getID().getName()).collect(Collectors.toList()));
                                }
                            }
                            Term newBnode = constructNewBNode(dfac,((NumberedBNodePredicateImpl) pred).getId() ,listOfAttributes);
                            map.put(term, newBnode);
                        }
                    }
                }
            }
        }
        return map;
    }

    /**
        Constructs a blank node of the type (unlabeled_{mapping_id}_{ordNumber}_{attr1}_{attr2}
        where those attributes are given as a parameter of the method
     */
    private Term constructNewBNode(OBDADataFactory dfac, Integer ordNumber, List<String> listOfAttributes) {
        StringBuilder bNodePattern = new StringBuilder();
        bNodePattern = bNodePattern.append("unlabeled_").append(mapping.getId()).append("_").append(ordNumber.toString());
        List<Term> terms = new LinkedList<>();
        final String PLACEHOLDER = "{}";
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
