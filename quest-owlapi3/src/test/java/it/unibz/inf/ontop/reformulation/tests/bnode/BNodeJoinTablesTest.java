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
import it.unibz.inf.ontop.owlrefplatform.owlapi.*;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.ToStringRenderer;
import org.semanticweb.owlapi.model.OWLObject;
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


/* The testing database has primary key and in the mapping file
   blank nodes are used for referencing different columns.

   Query is just a simple SPARQL query.
 */
public class BNodeJoinTablesTest extends TestCase {
    final static Logger log = LoggerFactory.getLogger(BNodeJoinTablesTest.class);


    private Connection conn;
    private OBDAModel obdaModel;
    private OWLOntology ontology;
    private OBDADataFactory fac;

    final String owlfile = "src/test/resources/bnode/threeTablesTestDatabase.owl";
    final String obdafile = "src/test/resources/bnode/threeTablesTestDatabase.obda";
    private final String dbCreateFile = "src/test/resources/bnode/threeTablesTestDatabase-create-h2.sql";
    private final String dbDropDatabase = "src/test/resources/bnode/threeTablesTestDatabase-drop-h2.sql";

    private final String jdbcPassword = "";
    private final String jdbcUserName = "sa";
    private final String jdbcUrl = "jdbc:h2:mem:questjunitdb";
    private final String jdbcDriverClass = "org.h2.Drive";


    @Before
    public void setUp() throws Exception {

    /*
     * Initializing and H2 database with data
     */

        conn = DriverManager.getConnection(jdbcUrl, jdbcUserName, jdbcPassword);
        Statement st = conn.createStatement();

        FileReader reader = new FileReader(dbCreateFile);
        BufferedReader in = new BufferedReader(reader);
        StringBuilder bf = new StringBuilder();
        String line = in.readLine();
        while (line != null) {
            bf.append(line);
            line = in.readLine();
        }
        in.close();

        st.executeUpdate(bf.toString());
        conn.commit();

        // Loading the OWL file
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        ontology = manager.loadOntologyFromOntologyDocument((new File(owlfile)));

        // Loading the OBDA data
        fac = OBDADataFactoryImpl.getInstance();
        obdaModel = fac.getOBDAModel();

        ModelIOManager ioManager = new ModelIOManager(obdaModel);
        ioManager.load(obdafile);


    }

    @After
    public void tearDown() throws Exception {

        dropTables();
        conn.close();

    }

    private void dropTables() throws SQLException, IOException {

        Statement st = conn.createStatement();

        FileReader reader = new FileReader(dbDropDatabase);
        BufferedReader in = new BufferedReader(reader);
        StringBuilder bf = new StringBuilder();
        String line = in.readLine();
        while (line != null) {
            bf.append(line);
            line = in.readLine();
        }
        in.close();

        st.executeUpdate(bf.toString());
        st.close();
        conn.commit();
    }


    @Test
    public void testQuery() throws Exception {

        // Creating a new instance of the reasoner
        QuestOWLFactory factory = new QuestOWLFactory();
        QuestOWLConfiguration config = QuestOWLConfiguration.builder().obdaModel(obdaModel).build();
        QuestOWL reasoner = factory.createReasoner(ontology, config);

        // Now we are ready for querying
        QuestOWLConnection conn = reasoner.getConnection();
        QuestOWLStatement st = conn.createStatement();

        String query = "PREFIX : <http://www.semanticweb.org/smallDatabase#> SELECT * WHERE { ?x a :Student  }";
        try {

            QuestOWLResultSet rs = st.executeTuple(query);

            String sqlQuery = st.getUnfolding(query);
            System.out.println("SQL: ");
            System.out.println(sqlQuery);

            assertTrue(rs.nextRow());
            OWLObject ind1 = rs.getOWLObject("x");
            assertEquals("_:b0", ToStringRenderer.getInstance().getRendering(ind1));

            assertTrue(rs.nextRow());
            ind1 = rs.getOWLObject("x");
            assertEquals("_:b1", ToStringRenderer.getInstance().getRendering(ind1));

            assertTrue(rs.nextRow());
            ind1 = rs.getOWLObject("x");
            assertEquals("_:b2", ToStringRenderer.getInstance().getRendering(ind1));

            assertTrue(rs.nextRow());
            ind1 = rs.getOWLObject("x");
            assertEquals("_:b3", ToStringRenderer.getInstance().getRendering(ind1));

            assertTrue(rs.nextRow());
            ind1 = rs.getOWLObject("x");
            assertEquals("_:b4", ToStringRenderer.getInstance().getRendering(ind1));

            assertTrue(rs.nextRow());
            ind1 = rs.getOWLObject("x");
            assertEquals("_:b5", ToStringRenderer.getInstance().getRendering(ind1));

            assertTrue(rs.nextRow());
            ind1 = rs.getOWLObject("x");
            assertEquals("_:b6", ToStringRenderer.getInstance().getRendering(ind1));
/*
            There are 7 blank nodes altogether
            3 from Student table and
            4 from passed Exams
 */

            assertFalse(rs.nextRow());


        } catch (Exception e) {
            throw e;
        } finally {
            try {
                st.close();
            } catch (Exception e) {
                throw e;
            } finally {
                conn.close();
                reasoner.dispose();
            }

        }

    }
        @Test
        public void testQueryExams() throws Exception {

            // Creating a new instance of the reasoner
            QuestOWLFactory factory = new QuestOWLFactory();
            QuestOWLConfiguration config = QuestOWLConfiguration.builder().obdaModel(obdaModel).build();
            QuestOWL reasoner = factory.createReasoner(ontology, config);

            // Now we are ready for querying
            QuestOWLConnection conn = reasoner.getConnection();
            QuestOWLStatement st = conn.createStatement();

            String query = "PREFIX : <http://www.semanticweb.org/smallDatabase#> SELECT * WHERE { ?x a :Exam}";
            try {

                QuestOWLResultSet rs = st.executeTuple(query);

                String sqlQuery = st.getUnfolding(query);
                System.out.println("SQL: ");
                System.out.println(sqlQuery);

                assertTrue(rs.nextRow());
                OWLObject ind1 = rs.getOWLObject("x");
                System.out.print(ToStringRenderer.getInstance().getRendering(ind1));
                assertEquals("<http://www.semanticweb.org/smallDatabase#exam1>",
                        ToStringRenderer.getInstance().getRendering(ind1));

                assertTrue(rs.nextRow());
                ind1 = rs.getOWLObject("x");
                assertEquals("<http://www.semanticweb.org/smallDatabase#exam1>",
                        ToStringRenderer.getInstance().getRendering(ind1));

                assertTrue(rs.nextRow());
                ind1 = rs.getOWLObject("x");
                assertEquals("<http://www.semanticweb.org/smallDatabase#exam2>",
                        ToStringRenderer.getInstance().getRendering(ind1));

                assertTrue(rs.nextRow());
                ind1 = rs.getOWLObject("x");
                assertEquals("<http://www.semanticweb.org/smallDatabase#exam3>",
                        ToStringRenderer.getInstance().getRendering(ind1));

                assertTrue(rs.nextRow());
                ind1 = rs.getOWLObject("x");
                assertEquals("_:b0", ToStringRenderer.getInstance().getRendering(ind1));

                assertTrue(rs.nextRow());
                ind1 = rs.getOWLObject("x");
                assertEquals("_:b1", ToStringRenderer.getInstance().getRendering(ind1));

                assertTrue(rs.nextRow());
                ind1 = rs.getOWLObject("x");
                assertEquals("_:b2", ToStringRenderer.getInstance().getRendering(ind1));
/*
            There are 7 blank nodes altogether
            3 from Student table and
            4 from passed Exams
 */

                assertFalse(rs.nextRow());


            } catch (Exception e) {
                throw e;
            } finally {
                try {
                    st.close();
                } catch (Exception e) {
                    throw e;
                } finally {
                    conn.close();
                    reasoner.dispose();
                }

            }
    }

}
