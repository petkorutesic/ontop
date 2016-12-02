package it.unibz.inf.ontop.reformulation.tests.bnode;

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

import it.unibz.inf.ontop.io.ModelIOManager;
import it.unibz.inf.ontop.model.OBDADataFactory;
import it.unibz.inf.ontop.model.OBDAModel;
import it.unibz.inf.ontop.model.impl.OBDADataFactoryImpl;
import it.unibz.inf.ontop.ontology.ClassExpression;
import it.unibz.inf.ontop.ontology.Description;
import it.unibz.inf.ontop.ontology.Ontology;
import it.unibz.inf.ontop.owlrefplatform.core.QuestConstants;
import it.unibz.inf.ontop.owlrefplatform.core.QuestPreferences;
import it.unibz.inf.ontop.owlrefplatform.owlapi.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/***
 * Test executes queries over the database with a mapping
 * in which blank nodes have been used.
 * Test uses a simple in memory database
 */

public class BnodeMappingTest {

	private OBDADataFactory fac;
	private QuestOWLConnection conn;
	private Connection connection;

	Logger log = LoggerFactory.getLogger(this.getClass());
	private OBDAModel obdaModel;
	private OWLOntology ontology;

	final String owlfile = "src/test/resources/bnode/simpleBNodeTestDatabase.owl";
	final String obdafile = "src/test/resources/bnode/simpleBNodeTestDatabase.obda";

	private List<String> emptyConcepts = new ArrayList<String>();
	private List<String> emptyRoles = new ArrayList<String>();
	private Set<ClassExpression> emptyBasicConcepts = new HashSet<ClassExpression>();
	private Set<Description> emptyProperties = new HashSet<Description>();

	private QuestOWL reasoner;
	private Ontology onto;

	@Before
	public void setUp() throws Exception {

		String driver = "org.h2.Driver";
		String url = "jdbc:h2:mem:questjunitdb;";
		String username = "sa";
		String password = "";

		fac = OBDADataFactoryImpl.getInstance();

		connection = DriverManager.getConnection(url, username, password);
		Statement st = connection.createStatement();

		FileReader reader = new FileReader("src/test/resources/bnode/simpleBNodeTestDatabase-h2.sql");
		BufferedReader in = new BufferedReader(reader);
		StringBuilder bf = new StringBuilder();
		String line = in.readLine();
		while (line != null) {
			bf.append(line);
			line = in.readLine();
		}
		in.close();

		st.executeUpdate(bf.toString());
		connection.commit();

		// Loading the OWL file
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		ontology = manager.loadOntologyFromOntologyDocument((new File(owlfile)));

		// Loading the OBDA data
		fac = OBDADataFactoryImpl.getInstance();
		obdaModel = fac.getOBDAModel();

		ModelIOManager ioManager = new ModelIOManager(obdaModel);
		ioManager.load(obdafile);

		QuestPreferences p = new QuestPreferences();
		p.setCurrentValueOf(QuestPreferences.ABOX_MODE, QuestConstants.VIRTUAL);
		p.setCurrentValueOf(QuestPreferences.OBTAIN_FULL_METADATA, QuestConstants.FALSE);
		// Creating a new instance of the reasoner
		QuestOWLFactory factory = new QuestOWLFactory();

		QuestOWLConfiguration config = QuestOWLConfiguration.builder().preferences(p).obdaModel(obdaModel).build();

		reasoner = (QuestOWL) factory.createReasoner(ontology, config);

		// Now we are ready for querying
		conn = reasoner.getConnection();

		//onto = OWLAPI3TranslatorUtility.translate(ontology);
	}

	@After
	public void tearDown() throws Exception {
		
			dropTables();
			reasoner.dispose();
			connection.close();

	}

	private void dropTables() throws SQLException, IOException {

		Statement st = connection.createStatement();

		FileReader reader = new FileReader("src/test/resources/bnode/simpleBNodeTestDatabase-drop-h2.sql");
		BufferedReader in = new BufferedReader(reader);
		StringBuilder bf = new StringBuilder();
		String line = in.readLine();
		while (line != null) {
			bf.append(line);
			line = in.readLine();
		}

		st.executeUpdate(bf.toString());
		st.close();
		connection.commit();
	}

	private int runTests(String query) throws Exception {
		QuestOWLStatement st = conn.createStatement();
		int results=0;
		try {
			QuestOWLResultSet rs = st.executeTuple(query);
			while (rs.nextRow()){
				OWLIndividual ind1 = rs.getOWLIndividual("x");
				log.debug(ind1.toString());
				results++;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			try {
			} catch (Exception e) {
				st.close();
			}
			conn.close();
			reasoner.dispose();
		}
		return results;
	}


	/**
	 * Even though the class MicroTrade is not defined in the owl file, the query works correctly
	 * 
	 * @throws Exception
	 */
	@Test
	public void testBNodesMapping() throws Exception {
		String query = "PREFIX : <http://www.semanticweb.org/smallDatabase#> SELECT ?x WHERE { ?x a :MicroTrade}";
		int numberResults = runTests(query);
		assertEquals(2, numberResults);
	}

	@Test
	public void testBNodesMappingNotDefinedProperty() throws Exception {
		String query = "PREFIX : <http://www.semanticweb.org/smallDatabase#> SELECT ?x WHERE { ?x :marketShare ?y}";
		int numberResults = runTests(query);
		assertEquals(2, numberResults);
	}

	@Test
	public void testBNodesMapping1() throws Exception {
		String query = "PREFIX : <http://www.semanticweb.org/smallDatabase#> SELECT ?x WHERE {?x a :Fairtrade.}";
		int numberResults = runTests(query);
		assertEquals(2, numberResults);
	}

	@Test
	public void testBNodesMapping2() throws Exception {
		String query = "PREFIX : <http://www.semanticweb.org/smallDatabase#> SELECT ?x WHERE {?x :hasSupplier _:k.}";
		int numberResults = runTests(query);
		assertEquals(5, numberResults);
	}


	@Test
	public void testBNodesMapping3() throws Exception {
		String query = "PREFIX : <http://www.semanticweb.org/smallDatabase#> SELECT ?x WHERE {?x a _:k.}";
		int numberResults = runTests(query);
		assertEquals(30, numberResults);
	}

}
