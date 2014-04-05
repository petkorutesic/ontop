/*
 * Copyright (C) 2009-2013, Free University of Bozen Bolzano This source code is
 * available under the terms of the Affero General Public License v3.
 * 
 * Please see LICENSE.txt for full license terms, including the availability of
 * proprietary exceptions.
 */
package it.unibz.krdb.obda.owlapi3.swrl;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLDataPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLDataRange;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentDataPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLInverseObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.OWLSubDataPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;
import org.semanticweb.owlapi.model.SWRLArgument;
import org.semanticweb.owlapi.model.SWRLAtom;
import org.semanticweb.owlapi.model.SWRLDArgument;
import org.semanticweb.owlapi.model.SWRLIArgument;
import org.semanticweb.owlapi.model.SWRLRule;

/**
 * A translator of OWLAxioms to equivalent SWRL rules (w.r.t. to data only). The
 * current implementation is incomplete.
 * 
 * @author Mariano Rodriguez Muro <mariano.muro@gmail.com>
 * 
 */
public class OWLToSWRLTranslator {

	private OWLDataFactory fac;

	private SWRLIArgument var1;

	private SWRLArgument var2;

	// urn:swrl#

	/**
	 * 
	 */
	public OWLToSWRLTranslator(OWLDataFactory fac) {
		this.fac = fac;

		var1 = fac.getSWRLVariable(IRI.create("urn:swrl#x"));
		var2 = fac.getSWRLVariable(IRI.create("urn:swrl#y"));
	}

	public Set<SWRLRule> translate(OWLOntology ontology) {

		Set<OWLAxiom> axioms = ontology.getAxioms();

		Set<SWRLRule> rules = new HashSet<SWRLRule>();
		for (OWLAxiom axiom : axioms) {
			if (axiom instanceof OWLSubClassOfAxiom) {

				rules.addAll(translate((OWLSubClassOfAxiom) axiom));

			} else if (axiom instanceof OWLSubObjectPropertyOfAxiom) {

				rules.addAll(translate((OWLSubObjectPropertyOfAxiom) axiom));

			} else if (axiom instanceof OWLSubDataPropertyOfAxiom) {

				rules.addAll(translate((OWLSubDataPropertyOfAxiom) axiom));

			} else if (axiom instanceof OWLInverseObjectPropertiesAxiom) {

				rules.addAll(translate((OWLInverseObjectPropertiesAxiom) axiom));

			} else if (axiom instanceof OWLEquivalentClassesAxiom) {

				rules.addAll(translate((OWLEquivalentClassesAxiom) axiom));

			} else if (axiom instanceof OWLEquivalentDataPropertiesAxiom) {

				rules.addAll(translate((OWLEquivalentDataPropertiesAxiom) axiom));

			} else if (axiom instanceof OWLEquivalentObjectPropertiesAxiom) {

				rules.addAll(translate((OWLEquivalentObjectPropertiesAxiom) axiom));

			} else if (axiom instanceof OWLObjectPropertyDomainAxiom) {

				rules.addAll(translate((OWLObjectPropertyDomainAxiom) axiom));

			} else if (axiom instanceof OWLObjectPropertyRangeAxiom) {

				rules.addAll(translate((OWLObjectPropertyRangeAxiom) axiom));

			} else if (axiom instanceof OWLDataPropertyDomainAxiom) {

				rules.addAll(translate((OWLDataPropertyDomainAxiom) axiom));

			} else if (axiom instanceof OWLDataPropertyRangeAxiom) {

				rules.addAll(translate((OWLDataPropertyRangeAxiom) axiom));

			}
		}

		return rules;
	}

	public Set<SWRLRule> translate(OWLSubClassOfAxiom ax) {
		SWRLAtom body = fac.getSWRLClassAtom(ax.getSubClass(), var1);
		SWRLAtom head = fac.getSWRLClassAtom(ax.getSuperClass(), var1);
		SWRLRule rule = fac.getSWRLRule(Collections.singleton(body), Collections.singleton(head));
		return Collections.singleton(rule);
	}

	public Set<SWRLRule> translate(OWLSubObjectPropertyOfAxiom ax) {
		SWRLAtom body = fac.getSWRLObjectPropertyAtom(ax.getSubProperty(), var1, (SWRLIArgument) var2);
		SWRLAtom head = fac.getSWRLObjectPropertyAtom(ax.getSuperProperty(), var1, (SWRLIArgument) var2);
		SWRLRule rule = fac.getSWRLRule(Collections.singleton(body), Collections.singleton(head));
		return Collections.singleton(rule);
	}

	public Set<SWRLRule> translate(OWLSubDataPropertyOfAxiom ax) {
		SWRLAtom body = fac.getSWRLDataPropertyAtom(ax.getSubProperty(), var1, (SWRLDArgument) var2);
		SWRLAtom head = fac.getSWRLDataPropertyAtom(ax.getSuperProperty(), var1, (SWRLDArgument) var2);
		SWRLRule rule = fac.getSWRLRule(Collections.singleton(body), Collections.singleton(head));
		return Collections.singleton(rule);
	}

	public Set<SWRLRule> translate(OWLInverseObjectPropertiesAxiom ax) {
		SWRLAtom body = fac.getSWRLObjectPropertyAtom(ax.getFirstProperty(), var1, (SWRLIArgument) var2);
		SWRLAtom head = fac.getSWRLObjectPropertyAtom(ax.getFirstProperty(), (SWRLIArgument) var2, var1);

		SWRLRule rule1 = fac.getSWRLRule(Collections.singleton(body), Collections.singleton(head));
		SWRLRule rule2 = fac.getSWRLRule(Collections.singleton(head), Collections.singleton(body));

		Set<SWRLRule> rules = new HashSet<SWRLRule>();
		rules.add(rule1);
		rules.add(rule2);
		return rules;
	}

	public Set<SWRLRule> translate(OWLEquivalentClassesAxiom ax) {
		List<SWRLAtom> atoms = new LinkedList<SWRLAtom>();
		for (OWLClassExpression ex : ax.getClassExpressions()) {
			atoms.add(fac.getSWRLClassAtom(ex, var1));
		}

		Set<SWRLRule> rules = new HashSet<SWRLRule>();
		for (int i = 0; i < atoms.size(); i++) {
			SWRLAtom head = atoms.get(i);
			for (int j = i + 1; j < atoms.size(); j++) {
				SWRLAtom body = atoms.get(j);

				SWRLRule rule1 = fac.getSWRLRule(Collections.singleton(body), Collections.singleton(head));
				SWRLRule rule2 = fac.getSWRLRule(Collections.singleton(head), Collections.singleton(body));

				rules.add(rule1);
				rules.add(rule2);
			}
		}
		return rules;

	}

	public Set<SWRLRule> translate(OWLEquivalentDataPropertiesAxiom ax) {
		List<SWRLAtom> atoms = new LinkedList<SWRLAtom>();
		for (OWLDataPropertyExpression ex : ax.getProperties()) {
			atoms.add(fac.getSWRLDataPropertyAtom(ex, var1, (SWRLDArgument) var2));
		}

		Set<SWRLRule> rules = new HashSet<SWRLRule>();
		for (int i = 0; i < atoms.size(); i++) {
			SWRLAtom head = atoms.get(i);
			for (int j = i + 1; j < atoms.size(); j++) {
				SWRLAtom body = atoms.get(j);

				SWRLRule rule1 = fac.getSWRLRule(Collections.singleton(body), Collections.singleton(head));
				SWRLRule rule2 = fac.getSWRLRule(Collections.singleton(head), Collections.singleton(body));

				rules.add(rule1);
				rules.add(rule2);
			}
		}
		return rules;
	}

	public Set<SWRLRule> translate(OWLEquivalentObjectPropertiesAxiom ax) {
		List<SWRLAtom> atoms = new LinkedList<SWRLAtom>();
		for (OWLObjectPropertyExpression ex : ax.getProperties()) {
			atoms.add(fac.getSWRLObjectPropertyAtom(ex, var1, (SWRLIArgument) var2));
		}

		Set<SWRLRule> rules = new HashSet<SWRLRule>();
		for (int i = 0; i < atoms.size(); i++) {
			SWRLAtom head = atoms.get(i);
			for (int j = i + 1; j < atoms.size(); j++) {
				SWRLAtom body = atoms.get(j);

				SWRLRule rule1 = fac.getSWRLRule(Collections.singleton(body), Collections.singleton(head));
				SWRLRule rule2 = fac.getSWRLRule(Collections.singleton(head), Collections.singleton(body));

				rules.add(rule1);
				rules.add(rule2);
			}
		}
		return rules;
	}

	public Set<SWRLRule> translate(OWLObjectPropertyDomainAxiom ax) {
		OWLClassExpression cexpression = ax.getDomain();
		OWLObjectPropertyExpression pexpression = ax.getProperty();
		SWRLAtom head = fac.getSWRLClassAtom(cexpression, var1);
		SWRLAtom body = fac.getSWRLObjectPropertyAtom(pexpression, var1, (SWRLIArgument) var2);
		SWRLRule rule = fac.getSWRLRule(Collections.singleton(body), Collections.singleton(head));
		return Collections.singleton(rule);

	}

	public Set<SWRLRule> translate(OWLObjectPropertyRangeAxiom ax) {
		OWLClassExpression cexpression = ax.getRange();
		OWLObjectPropertyExpression pexpression = ax.getProperty();
		SWRLAtom head = fac.getSWRLClassAtom(cexpression, (SWRLIArgument) var2);
		SWRLAtom body = fac.getSWRLObjectPropertyAtom(pexpression, var1, (SWRLIArgument) var2);
		SWRLRule rule = fac.getSWRLRule(Collections.singleton(body), Collections.singleton(head));
		return Collections.singleton(rule);
	}

	public Set<SWRLRule> translate(OWLDataPropertyDomainAxiom ax) {
		OWLClassExpression cexpression = ax.getDomain();
		OWLDataPropertyExpression pexpression = ax.getProperty();
		SWRLAtom head = fac.getSWRLClassAtom(cexpression, var1);
		SWRLAtom body = fac.getSWRLDataPropertyAtom(pexpression, var1, (SWRLDArgument) var2);
		SWRLRule rule = fac.getSWRLRule(Collections.singleton(body), Collections.singleton(head));
		return Collections.singleton(rule);
	}

	public Set<SWRLRule> translate(OWLDataPropertyRangeAxiom ax) {
		OWLDataRange cexpression = ax.getRange();
		OWLDataPropertyExpression pexpression = ax.getProperty();
		SWRLAtom head = fac.getSWRLDataRangeAtom(cexpression, (SWRLDArgument) var2);
		SWRLAtom body = fac.getSWRLDataPropertyAtom(pexpression, var1, (SWRLDArgument) var2);
		SWRLRule rule = fac.getSWRLRule(Collections.singleton(body), Collections.singleton(head));
		return Collections.singleton(rule);
	}

}
