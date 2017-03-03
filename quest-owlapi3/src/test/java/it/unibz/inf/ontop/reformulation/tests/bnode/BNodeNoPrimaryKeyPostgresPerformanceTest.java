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
import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.sql.Connection;

/***
 * Test ensures that the system can work with unlabeled blank nodes using PostgreSQL database
 * The testing database doesn't have primary key and in the mapping file
   blank nodes are used to refer to different columns.

   Query is just a simple SPARQL query.
 */
public class BNodeNoPrimaryKeyPostgresPerformanceTest extends TestCase {
    final static Logger log = LoggerFactory.getLogger(BNodeNoPrimaryKeyPostgresPerformanceTest.class);


    private Connection conn;
    private OBDAModel obdaModel;
    private OWLOntology ontology;
    private OBDADataFactory fac;

    final String owlfile = "src/test/resources/bnode/simpleDBNoPK.owl";
    final String obdafile = "src/test/resources/bnode/simpleDBNoPKPerformanceTestPostgres.obda";


    @Before
    public void setUp() throws Exception {

        ThreadMXBean mxBean = ManagementFactory.getThreadMXBean();
        double startTime = mxBean.getCurrentThreadCpuTime();
        long start = System.nanoTime();

        // Loading the OWL file
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        ontology = manager.loadOntologyFromOntologyDocument((new File(owlfile)));

        // Loading the OBDA data
        fac = OBDADataFactoryImpl.getInstance();
        obdaModel = fac.getOBDAModel();

        ModelIOManager ioManager = new ModelIOManager(obdaModel);
        ioManager.load(obdafile);

        double endTime = mxBean.getCurrentThreadCpuTime();
        System.out.println("Loading time "+ (endTime - startTime)/1000000 + "ms");
        System.out.println("Loading real time " + ((double)System.nanoTime()-start)/1000000 + "ms");
    }


    @Test
    public void testQuery() throws Exception {
        ThreadMXBean mxBean = ManagementFactory.getThreadMXBean();
        double startTime = mxBean.getCurrentThreadCpuTime();
        long start = System.nanoTime();
        // Creating a new instance of the reasoner
        QuestOWLFactory factory = new QuestOWLFactory();
        QuestOWLConfiguration config = QuestOWLConfiguration.builder().obdaModel(obdaModel).build();
        QuestOWL reasoner = factory.createReasoner(ontology, config);

        // Now we are ready for querying
        QuestOWLConnection conn = reasoner.getConnection();
        QuestOWLStatement st = conn.createStatement();

        String query = "PREFIX : <http://it.unibz.krdb/obda/test/simple#> SELECT * WHERE { ?x  :text ?y }";
        try {


            QuestOWLResultSet rs = st.executeTuple(query);
            String sqlQuery = st.getUnfolding(query);
            System.out.println("SQL: ");
            System.out.println(sqlQuery);

            long counter = 0;
            while(rs.nextRow()) {
                ++counter;
                OWLObject ind1 = rs.getOWLObject("x");
                //System.out.println(ToStringRenderer.getInstance().getRendering(ind1));
            }
            double endTime = mxBean.getCurrentThreadCpuTime();
            System.out.println("Number of retreived blank nodes: " + counter);
            System.out.println("Process took "+ (endTime - startTime)/1000000 + "ms");
            System.out.println("Simple process time " + ((double)System.nanoTime()-start)/1000000 + "ms");
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
