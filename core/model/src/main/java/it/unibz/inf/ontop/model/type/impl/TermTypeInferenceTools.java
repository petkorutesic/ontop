package it.unibz.inf.ontop.model.type.impl;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import it.unibz.inf.ontop.model.term.functionsymbol.BNodePredicate;
import it.unibz.inf.ontop.model.term.functionsymbol.DatatypePredicate;
import it.unibz.inf.ontop.model.term.functionsymbol.Predicate;
import it.unibz.inf.ontop.model.term.functionsymbol.Predicate.COL_TYPE;
import it.unibz.inf.ontop.model.term.TermConstants;
import it.unibz.inf.ontop.model.term.functionsymbol.URITemplatePredicate;
import it.unibz.inf.ontop.model.term.*;
import it.unibz.inf.ontop.model.type.TermType;
import it.unibz.inf.ontop.exception.IncompatibleTermException;
import it.unibz.inf.ontop.utils.TreeNode;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static it.unibz.inf.ontop.model.OntopModelSingletons.TYPE_FACTORY;
import static it.unibz.inf.ontop.model.term.functionsymbol.Predicate.COL_TYPE.*;

public class TermTypeInferenceTools {

    /**
     * This table is not "ground truth" and deserve to be discussed (is it good enough or not?)
     */
    private static final ImmutableTable<COL_TYPE, COL_TYPE, COL_TYPE> DATATYPE_DENOMINATORS = generateDatatypeDenominators();

    private static ImmutableTable<COL_TYPE, COL_TYPE, COL_TYPE> generateDatatypeDenominators() {

        // Child: Parent
        Map<COL_TYPE, COL_TYPE> datatypeHierarchy = ImmutableMap.<COL_TYPE, COL_TYPE>builder()
                .put(LITERAL_LANG, LITERAL)
                .put(STRING, LITERAL)
                .put(BOOLEAN, LITERAL)
                .put(DATE, LITERAL)
                .put(DATETIME, LITERAL)
                .put(DATETIME_STAMP, DATETIME)
                .put(TIME, LITERAL)
                .put(YEAR, LITERAL)
                .put(DOUBLE, LITERAL)
                .put(FLOAT, DOUBLE) // Type promotion (https://www.w3.org/TR/xpath20/#dt-type-promotion)
                .put(DECIMAL, FLOAT) // Type promotion
                .put(INTEGER, DECIMAL) // Subtype substitution (https://www.w3.org/TR/xpath20/#dt-subtype-substitution)
                .put(LONG, INTEGER) // Subtype substitution
                .put(INT, LONG) // Subtype substitution
                .put(NON_NEGATIVE_INTEGER, INTEGER) // Subtype substitution
                .put(POSITIVE_INTEGER, NON_NEGATIVE_INTEGER) // Subtype substitution
                .put(NON_POSITIVE_INTEGER, INTEGER) // Subtype substitution
                .put(NEGATIVE_INTEGER, NON_POSITIVE_INTEGER) // Subtype substitution
                .put(UNSIGNED_INT, NON_NEGATIVE_INTEGER) // Subtype substitution
                .build();

        TreeNode<COL_TYPE> literalHierarchy = new TreeNode(LITERAL);
        literalHierarchy.addChild(LITERAL_LANG);
        literalHierarchy.addChild(STRING);
        literalHierarchy.addChild(BOOLEAN);
        literalHierarchy.addChild(DATE);
        TreeNode<COL_TYPE> datetime = new TreeNode(DATETIME);
        literalHierarchy.addChild(datetime);
        datetime.addChild(DATETIME_STAMP);
        literalHierarchy.addChild(TIME);
        literalHierarchy.addChild(YEAR);
        TreeNode<COL_TYPE> doubleType = new TreeNode(DOUBLE);
        literalHierarchy.addChild(doubleType);
        TreeNode<COL_TYPE> floatType = new TreeNode(FLOAT);
        doubleType.addChild(floatType);
        TreeNode<COL_TYPE> decimalType = new TreeNode(DECIMAL);
        floatType.addChild(decimalType);
        TreeNode<COL_TYPE> integerType = new TreeNode(INTEGER);
        decimalType.addChild(integerType);
        TreeNode<COL_TYPE> longType = new TreeNode(LONG);
        integerType.addChild(longType);
        longType.addChild(INT);
        TreeNode<COL_TYPE> nonNegativeInteger = new TreeNode(NON_NEGATIVE_INTEGER);
        integerType.addChild(nonNegativeInteger);
        TreeNode<COL_TYPE> positiveIntegerType = new TreeNode(POSITIVE_INTEGER);
        nonNegativeInteger.addChild(positiveIntegerType);
        TreeNode<COL_TYPE> nonPositiveInteger = new TreeNode(NON_POSITIVE_INTEGER);
        integerType.addChild(nonPositiveInteger);
        TreeNode<COL_TYPE> negativeInteger = new TreeNode(NEGATIVE_INTEGER);
        nonPositiveInteger.addChild(negativeInteger);
        TreeNode<COL_TYPE> unsignedInt = new TreeNode(UNSIGNED_INT);
        nonNegativeInteger.addChild(unsignedInt);

        Table<COL_TYPE, COL_TYPE, COL_TYPE> literalHierarchySaturatedTable = HashBasedTable.create();
        List<TreeNode<COL_TYPE>> literalHierarchyList = literalHierarchy.preOrderTraversal();
        literalHierarchyList.stream().forEach(
                treeNode1 -> literalHierarchyList.stream().forEach(
                        treeNode2 -> {
                            Optional<COL_TYPE> commonType = treeNode1.lowestCommonAncestor(treeNode2);
                            if(commonType.isPresent()) {
                                if(!literalHierarchySaturatedTable.contains(treeNode1.getChildren(),treeNode2.getChildren()))
                                    literalHierarchySaturatedTable.put(treeNode1.getData(), treeNode2.getData(), commonType.get());
                                if(! literalHierarchySaturatedTable.contains(treeNode2.getChildren(),treeNode1.getChildren()))
                                    literalHierarchySaturatedTable.put(treeNode2.getData(), treeNode1.getData(), commonType.get());
                            }
                        }
                )
        );

        ImmutableTable.Builder<COL_TYPE, COL_TYPE, COL_TYPE> saturatedHierarchyBuilder = ImmutableTable.builder();


        datatypeHierarchy.forEach((child, parent) -> {
            saturatedHierarchyBuilder.put(child, child, child);

            // Non-final
            COL_TYPE ancestor = parent;
            // Transitive closure
            while (ancestor != null) {
                saturatedHierarchyBuilder.put(child, ancestor, ancestor);
                saturatedHierarchyBuilder.put(ancestor, child, ancestor);
                ancestor = datatypeHierarchy.get(ancestor);
            }
        });

        ImmutableTable<COL_TYPE, COL_TYPE, COL_TYPE> saturatedHierarchy = saturatedHierarchyBuilder.build();

        ImmutableTable.Builder<COL_TYPE, COL_TYPE, COL_TYPE> tableBuilder = ImmutableTable.<COL_TYPE, COL_TYPE, COL_TYPE>builder()
                // Base COL_TYPES
                .put(LITERAL, LITERAL, LITERAL)
                .put(OBJECT, OBJECT, OBJECT)
                .put(BNODE, BNODE, BNODE)
                .put(NULL, NULL, NULL)
                .put(UNSUPPORTED, UNSUPPORTED, UNSUPPORTED)
                .putAll(saturatedHierarchy);

        /**
         * Other literal type combinations
         */

        COL_TYPE.LITERAL_TYPES.stream().forEach(
                type1 -> COL_TYPE.LITERAL_TYPES.stream().forEach(
                            type2 -> {
                                if ((!type1.equals(type2) && (!saturatedHierarchy.contains(type1, type2)))) {
                                    tableBuilder.put(type1, type2, LITERAL);
                                }
                            }
                    )
        );

        ImmutableTable.Builder<COL_TYPE, COL_TYPE, COL_TYPE> newTableBuilder = ImmutableTable.<COL_TYPE, COL_TYPE, COL_TYPE>builder()
                // Base COL_TYPES
                //  .put(LITERAL, LITERAL, LITERAL)
                .put(OBJECT, OBJECT, OBJECT)
                .put(BNODE, BNODE, BNODE)
                .put(NULL, NULL, NULL)
                .put(UNSUPPORTED, UNSUPPORTED, UNSUPPORTED)
                // .putAll(saturatedHierarchy);
                .putAll(literalHierarchySaturatedTable);

       ImmutableTable<COL_TYPE,COL_TYPE,COL_TYPE> newTable = newTableBuilder.build();
       ImmutableTable<COL_TYPE,COL_TYPE,COL_TYPE> oldTable = tableBuilder.build();

       AtomicReference<Integer> i = new AtomicReference<>(0);
       System.out.println("In new table but not in old");
       newTable.rowMap().forEach( (type1,map1)  -> {
           map1.forEach( (type2, result) -> {
              if (!oldTable.contains(type1,type2))
                  System.out.println("Completely new " + type1.toString() + ", " + type2.toString() + " -> " + result.toString());
              else if (oldTable.get(type1,type2)!= result) {
                  i.set(i.get()+1);
                  System.out.println("new " + type1.toString() + ", " + type2.toString() + " -> " + result.toString());
                  System.out.println("old " + type1.toString() + ", " + type2.toString() + " -> " + oldTable.get(type1, type2).toString());
              }
           });

       });

       System.out.println("In total " + i);
       System.out.println();

       AtomicReference<Integer> j = new AtomicReference<>(0);
       System.out.println("In old table but not in the new one");
       oldTable.rowMap().forEach( (type1,map1)  -> {
           map1.forEach( (type2, result) -> {
              if (!newTable.contains(type1,type2))
                  System.out.println("Completely old " + type1.toString() + ", " + type2.toString() + " -> " + result.toString());
              else if (newTable.get(type1,type2)!= result) {
                  j.set(j.get()+1);
                  System.out.println("old " + type1.toString() + ", " + type2.toString() + " -> " + result.toString());
                  System.out.println("new " + type1.toString() + ", " + type2.toString() + " -> " + newTable.get(type1, type2).toString());
              }
           });

       });
       System.out.println("In total " + j);
       System.out.println();

        return oldTable;
    }

    private static final Optional<TermType> OPTIONAL_OBJECT_TERM_TYPE = Optional.of(TYPE_FACTORY.getTermType(OBJECT));
    private static final Optional<TermType> OPTIONAL_BNODE_TERM_TYPE = Optional.of(TYPE_FACTORY.getTermType(BNODE));
    private static final Optional<TermType> OPTIONAL_NULL_TERM_TYPE = Optional.of(TYPE_FACTORY.getTermType(NULL));

    private static final DatatypePredicate LITERAL_LANG_PREDICATE = TYPE_FACTORY
            .getTypePredicate(LITERAL_LANG);

    /**
     * TODO: find a better name
     */
    public static Optional<COL_TYPE> getCommonDenominatorType(COL_TYPE type1, COL_TYPE type2) {
        if (type1 == null) {
            return Optional.ofNullable(type2);
        } else if (type2 == null) {
            return Optional.of(type1);
        }
        else {
            return Optional.ofNullable(DATATYPE_DENOMINATORS.get(type1, type2));
        }
    }

    /**
     * TODO: simplify this method
     */
    public static Optional<TermType> inferType(Term term) throws IncompatibleTermException {
        if(term instanceof Function){
            Function f = (Function)term;
            Predicate typePred = f.getFunctionSymbol();

            /**
             * TODO: generalize this
             */
            if(f instanceof Expression) {
                return ((Expression) f).getOptionalTermType();
            }
            else if (f.isDataTypeFunction()){
                /**
                 * Special case: langString
                 */
                if (typePred.equals(LITERAL_LANG_PREDICATE)) {
                    if (f.getTerms().size() != 2) {
                        throw new IllegalStateException("A lang literal function should have two arguments");
                    }
                    Term secondArgument = f.getTerms().get(1);
                    return Optional.of(TYPE_FACTORY.getTermType(secondArgument));
                }
                return Optional.of(TYPE_FACTORY.getTermType(f.getFunctionSymbol().getType(0)));

            } else if (typePred instanceof URITemplatePredicate) {
                return  OPTIONAL_OBJECT_TERM_TYPE;
            } else if (typePred instanceof BNodePredicate){
                return OPTIONAL_BNODE_TERM_TYPE;
            }
            else {
                throw new IllegalArgumentException("Unexpected functional term: " + term);
            }
        }
        else if(term instanceof Variable){
            return Optional.empty();
        } else if(term instanceof ValueConstant){
            /**
             * Deals with the ugly definition of the NULL constant.
             * COL_TYPE of NULL should be NULL!
             */
            if (term == TermConstants.NULL) {
                return OPTIONAL_NULL_TERM_TYPE;
            }
            else {
                return Optional.of(TYPE_FACTORY.getTermType(((ValueConstant) term).getType()));
            }
        } else if(term instanceof URIConstant){
            return OPTIONAL_OBJECT_TERM_TYPE;
        }
        else {
            throw new IllegalStateException("Unexpected term: " + term);
        }
    }

    protected static TermType castStringLangType(TermType termType) {
        switch (termType.getColType()) {
            case LITERAL:
            case LITERAL_LANG:
            case STRING:
                return termType;
            default:
                return TYPE_FACTORY.getTermType(LITERAL);
        }
    }
}
