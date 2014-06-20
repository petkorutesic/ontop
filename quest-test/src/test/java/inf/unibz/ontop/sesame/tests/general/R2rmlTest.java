package inf.unibz.ontop.sesame.tests.general;

/*
 * #%L
 * ontop-test
 * %%
 * Copyright (C) 2009 - 2014 Free University of Bozen-Bolzano
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import it.unibz.krdb.obda.model.OBDADataFactory;
import it.unibz.krdb.obda.model.OBDADataSource;
import it.unibz.krdb.obda.model.OBDAModel;
import it.unibz.krdb.obda.model.Predicate;
import it.unibz.krdb.obda.model.impl.OBDADataFactoryImpl;
import it.unibz.krdb.obda.model.impl.RDBMSourceParameterConstants;
import it.unibz.krdb.obda.ontology.BasicClassDescription;
import it.unibz.krdb.obda.ontology.Ontology;
import it.unibz.krdb.obda.ontology.Property;
import it.unibz.krdb.obda.owlapi3.OWLAPI3Translator;
import it.unibz.krdb.obda.owlrefplatform.core.QuestConstants;
import it.unibz.krdb.obda.owlrefplatform.core.QuestPreferences;
import it.unibz.krdb.obda.owlrefplatform.core.dagjgrapht.Equivalences;
import it.unibz.krdb.obda.owlrefplatform.core.dagjgrapht.TBoxReasoner;
import it.unibz.krdb.obda.owlrefplatform.core.dagjgrapht.TBoxReasonerImpl;
import it.unibz.krdb.obda.owlrefplatform.owlapi3.QuestOWL;
import it.unibz.krdb.obda.owlrefplatform.owlapi3.QuestOWLConnection;
import it.unibz.krdb.obda.owlrefplatform.owlapi3.QuestOWLFactory;
import it.unibz.krdb.obda.owlrefplatform.owlapi3.QuestOWLResultSet;
import it.unibz.krdb.obda.owlrefplatform.owlapi3.QuestOWLStatement;
import it.unibz.krdb.obda.sesame.r2rml.R2RMLReader;

import java.io.File;
import java.net.URI;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***
 * Test returns  empty concepts and roles, based on the mappings.
 * Given an ontology, which is connected to a database via mappings,
 * generate a suitable set of queries that test if there are empty concepts,
 *  concepts that are no populated to anything.
 */

public class R2rmlTest {

	private OBDADataFactory fac;
	private QuestOWLConnection conn;
	private Connection connection;

	Logger log = LoggerFactory.getLogger(this.getClass());
	private OBDAModel obdaModel;
	private OWLOntology ontology;

//	final String owlfile = "src/test/resources/emptiesDatabase.owl";
//	final String obdafile = "src/test/resources/emptiesDatabase.obda";
	
	 final String owlfile =
			 "src/test/resources/example/exampleBooks.owl";
	 final String obdafile =
	 "src/test/resources/example/Books-mappings.ttl";
	
	private List<String> emptyConcepts = new ArrayList<String>();
	private List<String> emptyRoles = new ArrayList<String>();
	private Set<BasicClassDescription> emptyBasicConcepts = new HashSet<BasicClassDescription>();
	private Set<Property> emptyProperties = new HashSet<Property>();

	private QuestOWL reasoner;
	private Ontology onto;

	@Before
	public void setUp() throws Exception {



		// Loading the OWL file
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		ontology = manager.loadOntologyFromOntologyDocument((new File(owlfile)));


		R2RMLReader reader = new R2RMLReader(obdafile);
	    String sourceUrl = obdafile;
		OBDADataFactory f = OBDADataFactoryImpl.getInstance();
		OBDADataSource dataSource = f.getJDBCDataSource("http://meraka/moss/books", "jdbc:mysql://10.7.20.39/books", "fish", "fish", "com.mysql.jdbc.Driver");
		
		obdaModel = reader.readModel(dataSource);
	
		QuestPreferences pref = new QuestPreferences();
		
		pref.setCurrentValueOf(QuestPreferences.ABOX_MODE, QuestConstants.VIRTUAL);
		pref.setCurrentValueOf(QuestPreferences.OBTAIN_FULL_METADATA, QuestConstants.FALSE);
		
		
		// Creating a new instance of the reasoner
		QuestOWLFactory factory = new QuestOWLFactory();
		factory.setOBDAController(obdaModel);

		factory.setPreferenceHolder(pref);

		reasoner = (QuestOWL) factory.createReasoner(ontology, new SimpleConfiguration());

		// Now we are ready for querying
		conn = reasoner.getConnection();

		OWLAPI3Translator translator = new OWLAPI3Translator();

		onto = translator.translate(ontology);
	}

	@After
	public void tearDown() throws Exception {
		try {
			reasoner.dispose();
		} catch (Exception e) {
			log.debug(e.getMessage());
		}

	}



	private boolean runSPARQLConceptsQuery(String description) throws Exception {
		String query = "SELECT ?x WHERE {?x a " + description + ".}";
		QuestOWLStatement st = conn.createStatement();
		try {
			QuestOWLResultSet rs = st.executeTuple(query);
			return (rs.nextRow());

		} catch (Exception e) {
			throw e;
		} finally {
			try {

			} catch (Exception e) {
				st.close();
			}
			st.close();
			// conn.close();

		}

	}

	private boolean runSPARQLRolesQuery(String description) throws Exception {
		String query = "SELECT * WHERE {?x " + description + " ?y.}";
		QuestOWLStatement st = conn.createStatement();
		try {
			QuestOWLResultSet rs = st.executeTuple(query);
			return (rs.nextRow());

		} catch (Exception e) {
			throw e;
		} finally {
			try {

			} catch (Exception e) {
				st.close();
			}
			// conn.close();
			st.close();

		}

	}

	/**
	 * Test numbers of empty concepts
	 * 
	 * @throws Exception
	 */
	@Test
	public void testEmptyConcepts() throws Exception {
		int c = 0; // number of empty concepts
		for (Predicate concept : onto.getConcepts()) {

			if (!runSPARQLConceptsQuery("<" + concept.getName() + ">")) {
				emptyConcepts.add(concept.getName());
				c++;
			}
		}
		log.info(c + " Empty concept/s: " + emptyConcepts);

	}

	/**
	 * Test numbers of empty roles
	 * 
	 * @throws Exception
	 */
	@Test
	public void testEmptyRoles() throws Exception {
		int r = 0; // number of empty roles
		for (Predicate role : onto.getRoles()) {
			if (!runSPARQLRolesQuery("<" + role.getName() + ">")) {
				emptyRoles.add(role.getName());
				r++;
			}
		}
		log.info(r + " Empty role/s: " + emptyRoles);

	}

	/**
	 * Test numbers of empty concepts and roles
	 * 
	 * @throws Exception
	 */
	@Test
	public void testEmpties() throws Exception {

		int c = 0; // number of empty concepts
		for (Predicate concept : onto.getConcepts()) {
			if (!runSPARQLConceptsQuery("<" + concept.getName() + ">")) {
				emptyConcepts.add(concept.getName());
				c++;
			}
		}
		log.info(c + " Empty concept/s: " + emptyConcepts);

		int r = 0; // number of empty roles
		for (Predicate role : onto.getRoles()) {
			if (!runSPARQLRolesQuery("<" + role.getName() + ">")) {
				emptyRoles.add(role.getName());
				r++;
			}
		}
		log.info(r + " Empty role/s: " + emptyRoles);

	}

	/**
	 * Test numbers of empty concepts and roles considering existential and
	 * inverses
	 * Cannot work until inverses and existentials are considered  in the Abox
	 * @throws Exception
	 */
	// @Test
	public void testEmptiesWithInverses() throws Exception {
		TBoxReasoner tboxreasoner = new TBoxReasonerImpl(onto);
		System.out.println();
		System.out.println(tboxreasoner.getProperties());

		int c = 0; // number of empty concepts
		for (Equivalences<BasicClassDescription> concept : tboxreasoner.getClasses()) {
			BasicClassDescription representative = concept.getRepresentative();
			if ((!representative.getPredicate().isDataTypePredicate()) && !runSPARQLConceptsQuery("<" + concept.getRepresentative().toString() + ">")) {
				emptyBasicConcepts.addAll(concept.getMembers());
				c += concept.size();
			}
		}
		log.info(c + " Empty concept/s: " + emptyConcepts);

		int r = 0; // number of empty roles
		for (Equivalences<Property> properties : tboxreasoner.getProperties()) {
			if (!runSPARQLRolesQuery("<" + properties.getRepresentative().toString() + ">")) {
				emptyProperties.addAll(properties.getMembers());
				r += properties.size();
			}
		}
		log.info(r + " Empty role/s: " + emptyRoles);
	}

}
