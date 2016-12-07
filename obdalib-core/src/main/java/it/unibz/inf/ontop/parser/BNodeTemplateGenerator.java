package it.unibz.inf.ontop.parser;

import it.unibz.inf.ontop.model.*;
import it.unibz.inf.ontop.model.impl.NumberedBNodePredicateImpl;
import it.unibz.inf.ontop.model.impl.OBDADataFactoryImpl;
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
 * target mapping part and the type of a database
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
                        // Find a name of the relation in the FROM part of the sql statement
                        Select select = sqlQueryParsed.getStatement();
                        SelectBody selectBody = select.getSelectBody();
                        if (selectBody instanceof PlainSelect) {
                            /*TODO Do we have here more tables then necessary and how about aliases of
                            atribute names */
                            //Going throught the list of all tables in the FROM clause
                            Iterator tableSourceIterator = sqlQueryParsed.getTables().entrySet().iterator();
                            List<String> listOfAttributes = new ArrayList<String>();
                            while (tableSourceIterator.hasNext()){
                                Map.Entry<RelationID,RelationID> table = (Map.Entry<RelationID, RelationID>)tableSourceIterator.next();
                                UniqueConstraint primaryKey = metadata.getDatabaseRelation(table.getValue()).getPrimaryKey();

                                // Checks if the table has primary key
                                if (primaryKey == null) {
                                    //collect those variables in any atom of the targetQuery
                                    // which refer to columns of this table without primary key
                                    List<Column> orderByAttributes = getUsedColumnsInTargetQuery(table);
                                    String rowNumAttributeName = addRownumExpression(select , orderByAttributes);
                                    listOfAttributes.add(rowNumAttributeName);
                                }else{
                                    listOfAttributes.addAll(primaryKey.getAttributes().stream()
                                            .map(attr -> table.getKey().getTableName() + "." + attr.getID().getName())
                                            .collect(Collectors.toList()));
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

    /**
     * Method adds rownum analytic function with names of columns which are extracted from
     * a table without primary keys
     * @param selectQuery the source query
     * @return  the query with columns or functions in the projection part
     */
    public String addRownumExpression(Select selectQuery, List<Column> listOfRownumAttributes) {
        String rowNumAttributeName = "rownumber";
        AnalyticExpression rownumExpression = new AnalyticExpression();
        rownumExpression.setName("rownum");
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

    private class AddRownumAnalyticFuncton  implements SelectVisitor {

        private final List<String> ListOfOrderByAttributes;

        private AddRownumAnalyticFuncton(List<String> listOfOrderByAttributes) {
            ListOfOrderByAttributes = listOfOrderByAttributes;
        }

        @Override
        public void visit(PlainSelect plainSelect) {

        }

        @Override
        public void visit(SetOperationList setOpList) {

        }

        @Override
        public void visit(WithItem withItem) {

        }
    }
    /**
     * implements the case-insensitive comparison
     * (to be replaced in the future)
     */
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
     * collect all variables which belong to this table
     * and appear in some atom of the targetQuery
     *
     */
    private List<Column> getUsedColumnsInTargetQuery(Map.Entry<RelationID,RelationID> table){
        List<Column> usedColumns = new ArrayList<>();
        Set<Variable> variables = new HashSet<>();
        for (Function atom1 : targetQuery)
            TermUtils.addReferencedVariablesTo(variables, atom1);
        TargetVariableSet targetVariables = new TargetVariableSet(variables);

        Table tableName = new Table(table.getKey().getTableName());

        List<Attribute> allAttributesOfTable = metadata.getDatabaseRelation(table.getValue()).getAttributes();
        for (Attribute attribute : allAttributesOfTable){
            //TODO alias columns should be processed also and we should look at SelectItems part
            String attributeName = table.getKey().getTableName() + "." + attribute.getID().getName();
            if (targetVariables.contains(attributeName, attribute.getID().getName())) {
                Column column = new Column(tableName, attribute.getID().getSQLRendering());
                usedColumns.add(column);
            }
        }
        return usedColumns;
    }

}
