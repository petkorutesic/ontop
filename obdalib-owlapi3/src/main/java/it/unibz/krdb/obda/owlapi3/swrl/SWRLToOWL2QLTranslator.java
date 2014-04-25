/*
 * Copyright (C) 2009-2013, Free University of Bozen Bolzano This source code is
 * available under the terms of the Affero General Public License v3.
 * 
 * Please see LICENSE.txt for full license terms, including the availability of
 * proprietary exceptions.
 */
package it.unibz.krdb.obda.owlapi3.swrl;

import it.unibz.krdb.obda.model.Predicate;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLDataPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLDataRange;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.OWLSubDataPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubPropertyAxiom;
import org.semanticweb.owlapi.model.SWRLAtom;
import org.semanticweb.owlapi.model.SWRLBinaryAtom;
import org.semanticweb.owlapi.model.SWRLClassAtom;
import org.semanticweb.owlapi.model.SWRLDArgument;
import org.semanticweb.owlapi.model.SWRLDataPropertyAtom;
import org.semanticweb.owlapi.model.SWRLDataRangeAtom;
import org.semanticweb.owlapi.model.SWRLIArgument;
import org.semanticweb.owlapi.model.SWRLObjectPropertyAtom;
import org.semanticweb.owlapi.model.SWRLRule;
import org.semanticweb.owlapi.model.SWRLUnaryAtom;
import org.semanticweb.owlapi.model.SWRLVariable;
import org.semanticweb.owlapi.vocab.OWL2Datatype;

import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

/**
 * @author Mariano Rodriguez Muro <mariano.muro@gmail.com>
 * 
 */
public class SWRLToOWL2QLTranslator {

	OWLDataFactory fac = OWLDataFactoryImpl.getInstance();

	OWLOntology input = null;

	OWLOntology result = null;

	OWLOntologyManager man = null;

	private Collection<Predicate> recursivePredicates;

	public SWRLToOWL2QLTranslator(OWLOntology input) throws OWLException {
		this(input, null);
	}
	
	/**
	 * Returns the OWL 2 QL axiom equivalent to the SWRL rule. If there is no
	 * such axiom returns null. Note, not all syntactic variations of OWL 2 QL
	 * rules are handled here.
	 */
	public SWRLToOWL2QLTranslator(OWLOntology input, Collection<Predicate> recursivePredicates) throws OWLException {

		this.input = input;
		
		if (recursivePredicates == null) {
			this.recursivePredicates = new LinkedList<Predicate>();
		} else {
			this.recursivePredicates = recursivePredicates;
		}

		man = input.getOWLOntologyManager();

		translateOntology();

	}

	/***
	 * Copies the ontology axioms and, for each SWLR rule r that can be
	 * translated into an OWL 2 QL axiom q, removes r and adds q.
	 * 
	 * @throws OWLException
	 */
	private void translateOntology() throws OWLException {

		result = man.createOntology();
		man.addAxioms(result,input.getAxioms());

		Set<OWLAxiom> toDelete = new HashSet<OWLAxiom>();
		Set<OWLAxiom> toAdd = new HashSet<OWLAxiom>();

		for (OWLAxiom axiom : result.getAxioms()) {
			if (!(axiom instanceof SWRLRule)) {
				continue;
			}

			SWRLRule rule = (SWRLRule) axiom;
			OWLAxiom ruleAsAxiom = getOWL2QLAxiom(rule);
			
			if (ruleAsAxiom == null)
				continue;
			
			
			
			toDelete.add(rule);
			toAdd.add(ruleAsAxiom);

		}
		
		man.addAxioms(result, toAdd);
		man.removeAxioms(result, toDelete);

	}

	public OWLOntology getResult() {
		return result;
	}

	public OWLAxiom getOWL2QLAxiom(SWRLRule rule) {
		OWLAxiom result = null;

		Set<SWRLAtom> bodyatoms = rule.getBody();
		Set<SWRLAtom> headatoms = rule.getHead();

		if (bodyatoms.size() > 1 || bodyatoms.size() == 0) {
			return null;
		} else if (headatoms.size() == 0 || headatoms.size() > 1) {
			return null;
		}

		SWRLAtom body = bodyatoms.iterator().next();
		SWRLAtom head = headatoms.iterator().next();

		
		
		if (head instanceof SWRLUnaryAtom && body instanceof SWRLUnaryAtom) {
			result = getOWLSubClassOfAxiom((SWRLClassAtom) head, (SWRLClassAtom) body);
		} else if (head instanceof SWRLUnaryAtom && body instanceof SWRLBinaryAtom) {
			result = getOWLDomainRangeAxiom((SWRLUnaryAtom) head, (SWRLBinaryAtom) body);
		} else if (head instanceof SWRLBinaryAtom && body instanceof SWRLBinaryAtom) {
			result = getOWLSubPropertyOfAxiom((SWRLBinaryAtom) head, (SWRLBinaryAtom) body);
		}

		return result;
	}

	public OWLAxiom getOWLDomainRangeAxiom(SWRLUnaryAtom head, SWRLBinaryAtom body) {

		if (body.getFirstArgument().equals(body.getSecondArgument())) {
			return null;
		}

		if (body.getFirstArgument().equals(head.getArgument()) && body instanceof SWRLObjectPropertyAtom && head instanceof SWRLClassAtom) {

			return getOWLObjectPropertyDomainAxiom((SWRLClassAtom) head, (SWRLObjectPropertyAtom) body);

		} else if (body.getSecondArgument().equals(head.getArgument()) && body instanceof SWRLObjectPropertyAtom
				&& head instanceof SWRLClassAtom) {

			return getOWLObjectPropertyRangeAxiom((SWRLClassAtom) head, (SWRLObjectPropertyAtom) body);

		} else if (body.getFirstArgument().equals(head.getArgument()) && body instanceof SWRLDataPropertyAtom
				&& head instanceof SWRLClassAtom) {

			return getOWLDataPropertyDomainAxiom((SWRLClassAtom) head, (SWRLDataPropertyAtom) body);

		} else if (body.getSecondArgument().equals(head.getArgument()) && body instanceof SWRLDataPropertyAtom
				&& head instanceof SWRLDataRangeAtom) {

			return getOWLDataPropertyRangeAxiom((SWRLDataRangeAtom) head, (SWRLDataPropertyAtom) body);

		}

		return null;
	}

	public OWLAxiom getOWLAxiom(SWRLAtom head, SWRLAtom body) {

		if (head instanceof SWRLClassAtom && body instanceof SWRLClassAtom) {

			return getOWLSubClassOfAxiom((SWRLClassAtom) head, (SWRLClassAtom) body);

		} else if (head instanceof SWRLBinaryAtom && body instanceof SWRLUnaryAtom) {

			return getOWLSubClassOfAxiom((SWRLBinaryAtom) head, (SWRLClassAtom) body);

		} else if (head instanceof SWRLUnaryAtom && body instanceof SWRLBinaryAtom) {

			return getOWLDomainRangeAxiom((SWRLUnaryAtom) head, (SWRLBinaryAtom) body);

		} else if (head instanceof SWRLBinaryAtom && body instanceof SWRLBinaryAtom) {

			return getOWLSubPropertyOfAxiom((SWRLBinaryAtom) head, (SWRLBinaryAtom) body);

		}

		return null;

	}

	public OWLObjectPropertyDomainAxiom getOWLObjectPropertyDomainAxiom(SWRLClassAtom head, SWRLObjectPropertyAtom body) {

		OWLClassExpression domain = head.getPredicate();
		SWRLIArgument harg = head.getArgument();

		OWLObjectPropertyExpression property = body.getPredicate();
		SWRLIArgument barg = body.getFirstArgument();

		// argument must be a variable and it must be the same in head and body.
		if (!harg.equals(barg) || !(harg instanceof SWRLVariable)) {
			return null;
		}
		
		//TODO remove, inneficient for RR paper only
		/* dont translate rles that define recursive predicates */
		for (OWLClass c : domain.getClassesInSignature()) {
			for (Predicate p : recursivePredicates)
				if (c.getIRI().toString().equals(p.getName()))
					return null;
		}

		return fac.getOWLObjectPropertyDomainAxiom(property, domain);
	}

	public OWLObjectPropertyRangeAxiom getOWLObjectPropertyRangeAxiom(SWRLClassAtom head, SWRLObjectPropertyAtom body) {

		OWLClassExpression domain = head.getPredicate();
		SWRLIArgument harg = head.getArgument();

		OWLObjectPropertyExpression property = body.getPredicate();
		SWRLIArgument barg = body.getSecondArgument();

		// argument must be a variable and it must be the same in head and body.
		if (!harg.equals(barg) || !(harg instanceof SWRLVariable)) {
			return null;
		}
		
		//TODO remove, inneficient for RR paper only
		/* dont translate rles that define recursive predicates */
		for (OWLClass c : domain.getClassesInSignature()) {
			for (Predicate p : recursivePredicates)
				if (c.getIRI().toString().equals(p.getName()))
					return null;
		}

		return fac.getOWLObjectPropertyRangeAxiom(property, domain);
	}

	public OWLDataPropertyDomainAxiom getOWLDataPropertyDomainAxiom(SWRLClassAtom head, SWRLDataPropertyAtom body) {
		OWLClassExpression domain = head.getPredicate();
		SWRLIArgument harg = head.getArgument();

		OWLDataPropertyExpression property = body.getPredicate();
		SWRLIArgument barg = body.getFirstArgument();

		// argument must be a variable and it must be the same in head and body.
		if (!harg.equals(barg) || !(harg instanceof SWRLVariable)) {
			return null;
		}
		
		//TODO remove, inneficient for RR paper only
		/* dont translate rles that define recursive predicates */
		for (OWLClass c : domain.getClassesInSignature()) {
			for (Predicate p : recursivePredicates)
				if (c.getIRI().toString().equals(p.getName()))
					return null;
		}

		return fac.getOWLDataPropertyDomainAxiom(property, domain);
	}

	public OWLDataPropertyRangeAxiom getOWLDataPropertyRangeAxiom(SWRLDataRangeAtom head, SWRLDataPropertyAtom body) {

		OWLDataRange domain = head.getPredicate();
		SWRLDArgument harg = head.getArgument();

		OWLDataPropertyExpression property = body.getPredicate();
		SWRLDArgument barg = body.getSecondArgument();

		// argument must be a variable and it must be the same in head and body.
		if (!harg.equals(barg) || !(harg instanceof SWRLVariable)) {
			return null;
		}

		return fac.getOWLDataPropertyRangeAxiom(property, domain);
	}

	public OWLSubPropertyAxiom getOWLSubPropertyOfAxiom(SWRLBinaryAtom head, SWRLBinaryAtom body) {

		if (head instanceof SWRLObjectPropertyAtom && body instanceof SWRLObjectPropertyAtom) {

			return getOWLSubObjectPropertyOfAxiom((SWRLObjectPropertyAtom) head, (SWRLObjectPropertyAtom) body);

		} else if (head instanceof SWRLDataPropertyAtom && body instanceof SWRLDataPropertyAtom) {

			return getOWLSubDataPropertyOfAxiom((SWRLDataPropertyAtom) head, (SWRLDataPropertyAtom) body);

		}

		return null;
	}

	public OWLSubObjectPropertyOfAxiom getOWLSubObjectPropertyOfAxiom(SWRLObjectPropertyAtom head, SWRLObjectPropertyAtom body) {
		OWLObjectPropertyExpression hexp = head.getPredicate();
		SWRLIArgument harg1 = head.getFirstArgument();
		SWRLIArgument harg2 = head.getSecondArgument();

		OWLObjectPropertyExpression bexp = body.getPredicate();
		SWRLIArgument barg1 = head.getFirstArgument();
		SWRLIArgument barg2 = head.getSecondArgument();

		// argument must be a variable and it must be the same in head and body.
		if (!harg1.equals(barg1) || !(harg1 instanceof SWRLVariable)) {
			return null;
		}
		// argument must be a variable and it must be the same in head and body.
		if (!harg2.equals(barg2) || !(harg2 instanceof SWRLVariable)) {
			return null;
		}

		//TODO remove, inneficient for RR paper only
		/* dont translate rles that define recursive predicates */
		for (OWLObjectProperty c : hexp.getObjectPropertiesInSignature()) {
			for (Predicate p : recursivePredicates)
				if (c.getIRI().toString().equals(p.getName()))
					return null;
		}
		
		return fac.getOWLSubObjectPropertyOfAxiom(bexp, hexp);
	}

	public OWLSubDataPropertyOfAxiom getOWLSubDataPropertyOfAxiom(SWRLDataPropertyAtom head, SWRLDataPropertyAtom body) {
		OWLDataPropertyExpression hexp = head.getPredicate();
		SWRLIArgument harg1 = head.getFirstArgument();
		SWRLDArgument harg2 = head.getSecondArgument();

		OWLDataPropertyExpression bexp = body.getPredicate();
		SWRLIArgument barg1 = head.getFirstArgument();
		SWRLDArgument barg2 = head.getSecondArgument();

		// argument must be a variable and it must be the same in head and body.
		if (!harg1.equals(barg1) || !(harg1 instanceof SWRLVariable)) {
			return null;
		}
		// argument must be a variable and it must be the same in head and body.
		if (!harg2.equals(barg2) || !(harg2 instanceof SWRLVariable)) {
			return null;
		}
		
		//TODO remove, inneficient for RR paper only
		/* dont translate rles that define recursive predicates */
		for (OWLDataProperty c : hexp.getDataPropertiesInSignature()) {
			for (Predicate p : recursivePredicates)
				if (c.getIRI().toString().equals(p.getName()))
					return null;
		}

		return fac.getOWLSubDataPropertyOfAxiom(bexp, hexp);
	}

	public OWLSubClassOfAxiom getOWLSubClassOfAxiom(SWRLClassAtom head, SWRLClassAtom body) {

		OWLClassExpression hexp = head.getPredicate();
		SWRLIArgument harg = head.getArgument();

		OWLClassExpression bexp = body.getPredicate();
		SWRLIArgument barg = body.getArgument();

		// argument must be a variable and it must be the same in head and body.
		if (!harg.equals(barg) || !(harg instanceof SWRLVariable)) {
			return null;
		}
		
		//TODO remove, inneficient for RR paper only
		/* dont translate rles that define recursive predicates */
		for (OWLClass c : hexp.getClassesInSignature()) {
			for (Predicate p : recursivePredicates)
				if (c.getIRI().toString().equals(p.getName()))
					return null;
		}

		return fac.getOWLSubClassOfAxiom(bexp, hexp);
	}

	/***
	 * Returns an A subClassOf R some Thing or S some Literal
	 * 
	 * @param head
	 * @param body
	 * @return
	 */
	public OWLSubClassOfAxiom getOWLSubClassOfAxiom(SWRLBinaryAtom head, SWRLClassAtom body) {

		OWLClassExpression bexp = body.getPredicate();
		SWRLIArgument barg = body.getArgument();

		// argument must be a variable and it must be the same in head and body.
		if (!(barg instanceof SWRLVariable)) {
			return null;
		}

		if (head instanceof SWRLObjectPropertyAtom) {
			SWRLObjectPropertyAtom dhead = (SWRLObjectPropertyAtom) head;
			OWLObjectPropertyExpression hexp = dhead.getPredicate();
			SWRLIArgument harg1 = dhead.getFirstArgument();
			SWRLIArgument harg2 = dhead.getSecondArgument();

			if (harg1.equals(harg2))
				return null;
			
			//TODO remove, inneficient for RR paper only
			/* dont translate rles that define recursive predicates */
			for (OWLObjectProperty c : hexp.getObjectPropertiesInSignature()) {
				for (Predicate p : recursivePredicates)
					if (c.getIRI().toString().equals(p.getName()))
						return null;
			}

			if (barg.equals(harg1)) {
				return fac.getOWLSubClassOfAxiom(bexp, fac.getOWLObjectSomeValuesFrom(hexp, fac.getOWLThing()));
			} else if (barg.equals(harg2)) {
				return fac.getOWLSubClassOfAxiom(bexp, fac.getOWLObjectSomeValuesFrom(fac.getOWLObjectInverseOf(hexp), fac.getOWLThing()));
			}

		} else if (head instanceof SWRLDataPropertyAtom) {
			SWRLDataPropertyAtom dhead = (SWRLDataPropertyAtom) head;
			OWLDataPropertyExpression hexp = dhead.getPredicate();
			SWRLIArgument harg1 = dhead.getFirstArgument();
			SWRLDArgument harg2 = dhead.getSecondArgument();

			if (harg1.equals(harg2))
				return null;
			
			//TODO remove, inneficient for RR paper only
			/* dont translate rles that define recursive predicates */
			for (OWLDataProperty c : hexp.getDataPropertiesInSignature()) {
				for (Predicate p : recursivePredicates)
					if (c.getIRI().toString().equals(p.getName()))
						return null;
			}

			if (barg.equals(harg1)) {
				return fac.getOWLSubClassOfAxiom(bexp,
						fac.getOWLDataSomeValuesFrom(hexp, fac.getOWLDatatype(OWL2Datatype.RDFS_LITERAL.getIRI())));
			} else if (barg.equals(harg2)) {
				return null;
			}
		}

		return null;
	}

}
