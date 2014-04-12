/*
 * Copyright (C) 2009-2013, Free University of Bozen Bolzano This source code is
 * available under the terms of the Affero General Public License v3.
 * 
 * Please see LICENSE.txt for full license terms, including the availability of
 * proprietary exceptions.
 */
package it.unibz.krdb.obda.owlapi3.swrl;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.SWRLRule;

/**
 * 
 * @author Mariano Rodriguez Muro <mariano.muro@gmail.com>
 * 
 */
public class OWLToSWRLOntologyTranslator {

	/**
	 * 
	 */
	public OWLToSWRLOntologyTranslator(OWLDataFactory fac) {

	}

	public static void main(String[] args) {
		File inputFile = new File(args[0]);
		File outputFile = new File(args[1]);

		try {

			// Get hold of an ontology manager
			OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
			// Let's load an ontology from the web

			OWLOntology inputOntology = manager.loadOntologyFromOntologyDocument(inputFile);

			System.out.println("Loaded ontology: " + inputOntology);

			OWLDataFactory owlDataFactory = manager.getOWLDataFactory();
			Set<OWLAxiom> axioms = new HashSet<OWLAxiom>();
			OWLToSWRLTranslator tr = new OWLToSWRLTranslator(owlDataFactory);

			Set<SWRLRule> rules = tr.translate(inputOntology);

			axioms.addAll(rules);
			for (OWLAxiom ax : inputOntology.getAxioms()) {
				if (ax instanceof OWLDeclarationAxiom)
				{
					axioms.add(ax);
				}
			}

			OWLOntology result = manager.createOntology(axioms);

			manager.saveOntology(result, IRI.create(outputFile.toURI()));

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

}
