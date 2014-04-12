package it.unibz.krdb.obda.owlapi3.swrl;

import it.unibz.krdb.obda.model.CQIE;
import it.unibz.krdb.obda.model.Function;
import it.unibz.krdb.obda.model.OBDADataFactory;
import it.unibz.krdb.obda.model.Predicate;
import it.unibz.krdb.obda.model.Term;
import it.unibz.krdb.obda.model.impl.OBDADataFactoryImpl;
import it.unibz.krdb.obda.model.impl.OBDAVocabulary;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.SWRLArgument;
import org.semanticweb.owlapi.model.SWRLAtom;
import org.semanticweb.owlapi.model.SWRLBuiltInAtom;
import org.semanticweb.owlapi.model.SWRLClassAtom;
import org.semanticweb.owlapi.model.SWRLDataPropertyAtom;
import org.semanticweb.owlapi.model.SWRLDataRangeAtom;
import org.semanticweb.owlapi.model.SWRLDifferentIndividualsAtom;
import org.semanticweb.owlapi.model.SWRLIndividualArgument;
import org.semanticweb.owlapi.model.SWRLLiteralArgument;
import org.semanticweb.owlapi.model.SWRLObjectPropertyAtom;
import org.semanticweb.owlapi.model.SWRLObjectVisitor;
import org.semanticweb.owlapi.model.SWRLRule;
import org.semanticweb.owlapi.model.SWRLSameIndividualAtom;
import org.semanticweb.owlapi.model.SWRLVariable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

/**
 * SWRLVisitor class visits the SWRL rules in the ontology to obtain the
 * datalog. Translate separately head and body.
 * 
 * Unsupported atoms:
 * <ul>
 * <li>
 * sameAs(x,y) S(x) = S(y)</li>
 * <li>
 * differentFrom(x,y) S(x) ≠ S(y)</li>
 * <li>
 * builtIn(r,z1,...,zn)</li>
 * </ul>
 */
public class SWRLVisitor implements SWRLObjectVisitor {

	// Datalog elements
	OBDADataFactory fac;
	Function head;
	List<Function> body;
	Function function;
	List<Term> terms;
	Predicate predicate;
	
	Collection<CQIE> rules;

	List<String> errors = new LinkedList<String>();
	private static Logger log = LoggerFactory.getLogger(SWRLVisitor.class);

	public SWRLVisitor() {

		fac = OBDADataFactoryImpl.getInstance();
		rules = Lists.newArrayList();

	}

	/**
	 * Translate the swrl_rules contained in the ontology Return a datalog
	 * program containing the supported datalog facts
	 * 
	 * @param onto
	 *            an OWLOntology
	 * @return DatalogProgram
	 */
	public Collection<CQIE> createDatalog(OWLOntology onto) {

		for (OWLAxiom axiom : onto.getAxioms()) {

			if (axiom.getAxiomType().equals(AxiomType.SWRL_RULE)) {

				SWRLRule rule = (SWRLRule) axiom;
				rule.accept(this);

				if (!errors.isEmpty()) {
					log.warn("Ignorning rules with unsupported features: " + errors);
					errors.clear();
				}

			}
		}

		return rules;
		


	}

	/**
	 * Translate the swrl_rule Return a datalog program containing the supported
	 * datalog facts
	 * 
	 * @param onto
	 *            an OWLOntology
	 * @return DatalogProgram
	 */
	public Collection<CQIE> createDatalog(SWRLRule rule) {

		rule.accept(this);

		if (!errors.isEmpty()) {
			log.warn("Not Supported Translation of: " + errors);
			errors.clear();
		}

		return rules;
		
		

	}

	private void getHead(SWRLAtom atoms) {

		atoms.accept(this);
		head = function;

	}

	private void getBody(Set<SWRLAtom> atoms) {

		// do not execute again if the body has already been assigned (multiple
		// facts in the head)

		if (body.isEmpty()) {

			for (SWRLAtom a : atoms) {
				a.accept(this);
				if (function != null) {
					body.add(function);
					function = null;
				}
			}
		}

	}

	@Override
	public void visit(SWRLRule node) {

		head = null;
		body = new LinkedList<Function>();

		for (SWRLAtom a : node.getHead()) {

			// transform SWRL head in Function
			getHead(a);

			// transform SWRL body in list Function
			getBody(node.getBody());

			if (head == null)
			{ continue; }
			
			
			
			rules.add(fac.getCQIE(head, body));
		}

	}

	@Override
	public void visit(SWRLClassAtom node) {

		// we consider only namedOwlClass (we do not support for example
		// ObjectMinCardinality)
		if (!node.getPredicate().isAnonymous()) {

			// get predicate for datalog
			Predicate predicate = fac.getClassPredicate(node.getPredicate().asOWLClass().toStringID());

			terms = new ArrayList<Term>();
			// get terms for datalog
			for (SWRLArgument argument : node.getAllArguments()) {
				argument.accept(this);

			}

			function = fac.getFunction(predicate, terms);
		}
		else {
			errors.add(node.toString());
		}

	}

	// Data range is not supported
	@Override
	public void visit(SWRLDataRangeAtom node) {
		errors.add(node.toString());
	}

	@Override
	public void visit(SWRLObjectPropertyAtom node) {

		// we consider only namedOwlObjectProperty example not an object
		// property expression such as inv(p)
		if (!node.getPredicate().isAnonymous()) {

			predicate = fac.getObjectPropertyPredicate(node.getPredicate().asOWLObjectProperty().toStringID());

			terms = new ArrayList<Term>();
			// get terms for datalog
			for (SWRLArgument argument : node.getAllArguments()) {
				argument.accept(this);
			}
			function = fac.getFunction(predicate, terms);
		} else {
			errors.add(node.toString());
		}

	}

	@Override
	public void visit(SWRLDataPropertyAtom node) {

		// we consider only named OwlDataProperty
		if (!node.getPredicate().isAnonymous()) {

			// get predicate for datalog
			predicate = fac.getDataPropertyPredicate(node.getPredicate().asOWLDataProperty().toStringID());

			terms = new ArrayList<Term>();
			// get terms for datalog
			for (SWRLArgument argument : node.getAllArguments()) {
				argument.accept(this);

			}
			function = fac.getFunction(predicate, terms);
		} else {
			errors.add(node.toString());
		}

	}

	// we do not support swrl built-in atom and personalized one
	@Override
	public void visit(SWRLBuiltInAtom node) {
		errors.add(node.toString());
	}

	/**
	 * 
	 * converts the SWRL variables. NOTE that we only consider the fragment of
	 * the IRI of the variables
	 * 
	 */
	@Override
	public void visit(SWRLVariable node) {
		terms.add(fac.getVariable(node.getIRI().getFragment()));
	}

	@Override
	public void visit(SWRLIndividualArgument node) {
		// get the id without the quotes <>
		terms.add(fac.getConstantLiteral(node.getIndividual().toStringID(), Predicate.COL_TYPE.STRING));
	}

	// TODO: add types
	@Override
	public void visit(SWRLLiteralArgument node) {

		OWLLiteral literal = node.getLiteral();

		if (literal.isBoolean()) {
			if (literal.parseBoolean()) {
				terms.add(fac.getFunction(OBDAVocabulary.XSD_BOOLEAN, fac.getConstantTrue()));
			} else {
				terms.add(fac.getFunction(OBDAVocabulary.XSD_BOOLEAN, fac.getConstantFalse()));
			}
		} else if (literal.hasLang()) {
			terms.add(fac.getConstantLiteral(literal.getLiteral(), literal.getLang()));
		} else if (literal.isDouble()) {
			terms.add(fac.getConstantLiteral(literal.getLiteral(), Predicate.COL_TYPE.DOUBLE));
		} else if (literal.isFloat()) {
			terms.add(fac.getConstantLiteral(literal.getLiteral(), Predicate.COL_TYPE.DECIMAL));
		} else if (literal.isInteger()) {
			terms.add(fac.getConstantLiteral(literal.getLiteral(), Predicate.COL_TYPE.INTEGER));
		} else {
			fac.getConstantLiteral(literal.getLiteral());
		}

	}

	// we do not support swrl same as
	@Override
	public void visit(SWRLSameIndividualAtom node) {
		errors.add(node.toString());
	}

	// we do not support swrl different from
	@Override
	public void visit(SWRLDifferentIndividualsAtom node) {
		errors.add(node.toString());

	}

}
