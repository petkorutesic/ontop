package it.unibz.inf.ontop.parser;

import it.unibz.inf.ontop.model.*;
import it.unibz.inf.ontop.model.impl.NumberedBNodePredicateImpl;
import it.unibz.inf.ontop.model.impl.OBDADataFactoryImpl;
import it.unibz.inf.ontop.model.impl.RDBMSourceParameterConstants;
import it.unibz.inf.ontop.model.impl.TermUtils;
import it.unibz.inf.ontop.sql.Attribute;
import it.unibz.inf.ontop.sql.DBMetadata;
import it.unibz.inf.ontop.sql.RelationID;
import it.unibz.inf.ontop.sql.UniqueConstraint;
import it.unibz.inf.ontop.sql.api.ParsedSQLQuery;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.AnalyticExpression;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * Changes unlabeled blank nodes according to the corresponding
 * mapping and the type of tables in a database
 *
 *
 */
public class BNodeTemplateGenerator {
    private final DBMetadata metadata;
    private OBDAMappingAxiom mapping;
    private List<Function> targetQuery;
    private OBDADataFactory dfac;

    public BNodeTemplateGenerator(DBMetadata metadata, OBDAMappingAxiom mapping) {
        this.metadata = metadata;
        this.mapping = mapping;
        this.targetQuery = mapping.getTargetQuery();
        this.dfac = OBDADataFactoryImpl.getInstance();
    }

    public String replaceUnlabeledBNodes(ParsedSQLQuery sqlQueryParsed)
    throws JSQLParserException{
        Map<Term, Term> map = getBNodeTemplateReplacementMap(sqlQueryParsed);
        for (Function atom : targetQuery) {
            for (int i = 0 ; i < atom.getTerms().size(); i++){
                final Term term = atom.getTerm(i);
                if (map.containsKey(term)){
                    atom.setTerm(i, map.get(term));
                }
            }
        }
        return sqlQueryParsed.toString();
    }

    private Map<Term, Term> getBNodeTemplateReplacementMap(ParsedSQLQuery sqlQueryParsed)
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
                        Select select = sqlQueryParsed.getStatement();
                        SelectBody selectBody = select.getSelectBody();
                        if (selectBody instanceof PlainSelect) {

                            List<String> listOfAttributes = new ArrayList<String>();
                            List<Column> rowNumberOrderByAttributes = new ArrayList<>();
                            Boolean requiredRownum = false;
                            //Going throught the list of all tables in the FROM clause
                            Iterator tableSourceIterator = sqlQueryParsed.getTables().entrySet().iterator();
                            while (tableSourceIterator.hasNext()){
                                Map.Entry<RelationID,RelationID> table = (Map.Entry<RelationID, RelationID>)tableSourceIterator.next();
                                UniqueConstraint primaryKey = metadata.getDatabaseRelation(table.getValue()).getPrimaryKey();
                                /* If table doesn't have a primary primary key then row_number() window
                                 * function is used for identifying blank nodes, or in a case of oracle dbms
                                 * ROWID pseudo column is used in parameters.
                                 * on the other hand, if primary key exists then attributes that comprise primary key
                                 * are used for the identification of unlabeled blank nodes
                                 */
                                if (primaryKey == null) {
                                    //Not a perfect way to check if a corresponding DBMS is oracle
                                    if (metadata.getDbmsProductName()=="Oracle") {
                                        String rowidAttribute = addROWIDColumnExpression(select,table.getKey().getTableName());
                                        listOfAttributes.add(rowidAttribute);
                                    }else {
                                        requiredRownum = true;
                                        //collects those variables in any atom of the targetQuery
                                        // which refer to columns of this table without primary key
                                        rowNumberOrderByAttributes.addAll(getUsedColumnsInTargetQuery(table));
                                    }
                                }else{
                                    listOfAttributes.addAll(primaryKey.getAttributes().stream()
                                            .map(attr -> table.getKey().getTableName() + "." + attr.getID().getName())
                                            .collect(Collectors.toList()));
                                }
                            }
                            //Creation of rownum expression item has to be outside of the while loop above,
                            // because we have to make rownum() over (attrib1, attrib2)
                            // for all attributes of all tables that have no primary key
                            if (requiredRownum) {
                                String rowNumAttributeName = addRownumExpression(select, rowNumberOrderByAttributes);
                                listOfAttributes.add(rowNumAttributeName);
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

    /**
     * Method adds rownum analytic function with names of columns which are extracted from
     * a table without primary keys
     * The rownum analytic function is added into the selectQuery
     * @param selectQuery the source query
     * @param listOfRownumAttributes the list of attributes to be inserted in order by clause
     * @return rowNumAttributeName the alias name of the row_number function
     */
    public String addRownumExpression(Select selectQuery, List<Column> listOfRownumAttributes) {
        AnalyticExpression rownumExpression = new AnalyticExpression();
        String rowNumAttributeName = mapping.getId()+"_row_id";
        rownumExpression.setName("row_number");

        List<OrderByElement> listOfOrderByElements = new ArrayList<>();
        for (Column newColumn : listOfRownumAttributes) {
            OrderByElement orderByElement = new OrderByElement();
            orderByElement.setExpression(newColumn);
            listOfOrderByElements.add(orderByElement);
        }
        rownumExpression.setOrderByElements(listOfOrderByElements);

        SelectExpressionItem selectItem = new SelectExpressionItem();
        selectItem.setExpression(rownumExpression);
        selectItem.setAlias(new Alias(rowNumAttributeName));

        ((PlainSelect)(selectQuery.getSelectBody())).getSelectItems().add(selectItem);

        return rowNumAttributeName;
    }

   /**
     * Method adds rowid  pseudo column  of a given table in the selectQuery
     * @param selectQuery the source query
     * @return rowidAttributeName the alias name of the rowid pseudo column
     */
    public String addROWIDColumnExpression(Select selectQuery, String tableName) {
        Column  rowidItem = new Column();
        String rowidAttributeName = mapping.getId()+"_"+tableName+"_row_id";
        rowidItem.setColumnName(tableName+".ROWID");

        SelectExpressionItem selectItem = new SelectExpressionItem();
        selectItem.setExpression(rowidItem);
        selectItem.setAlias(new Alias(rowidAttributeName));

        ((PlainSelect)(selectQuery.getSelectBody())).getSelectItems().add(selectItem);

        return rowidAttributeName;
    }



    private static class TargetVariableSet {

        private Set<String> variableNames = new HashSet<>();

        TargetVariableSet(Set<Variable> variables) {
            for (Variable var : variables)
                variableNames.add(var.getName().toLowerCase());
        }

        boolean contains(String qualifiedColumnName, String columnName) {
            return variableNames.contains(qualifiedColumnName.toLowerCase())
                    || variableNames.contains(columnName.toLowerCase());
        }
    }

    /**
     *
     * collects all variables which belong to the given table
     * and occur in some of atoms of the targetQuery
     *
     */
    private List<Column> getUsedColumnsInTargetQuery(Map.Entry<RelationID,RelationID> table){
        List<Column> usedColumns = new ArrayList<>();
        Set<Variable> variables = new HashSet<>();
        RelationID relationId = table.getValue();
        RelationID aliasId = table.getKey();
        for (Function atom1 : targetQuery)
            TermUtils.addReferencedVariablesTo(variables, atom1);
        TargetVariableSet targetVariables = new TargetVariableSet(variables);
        Table tableName = new Table(table.getKey().getTableName());

        List<Attribute> allAttributesOfTable = metadata.getDatabaseRelation(relationId).getAttributes();
        for (Attribute attribute : allAttributesOfTable){
            //TODO alias columns should be processed also and we should look at SelectItems part
            String attributeName = aliasId.getTableName() + "." + attribute.getID().getName();
            if (targetVariables.contains(attributeName, attribute.getID().getName())) {
                Column column = new Column(tableName, attribute.getID().getSQLRendering());
                usedColumns.add(column);
            }
        }
        return usedColumns;
    }
}
