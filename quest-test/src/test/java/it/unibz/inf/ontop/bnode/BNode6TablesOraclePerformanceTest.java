package it.unibz.inf.ontop.bnode;

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
 * Performance test for an use of unlabeled blank nodes in mappings with an Oracle database
 * comprised of 6 tables, all of them without primary keys.
 * Rows are randomly generated by
 * https://github.com/marianormuro/onto-wisbench/tree/master/wisonsin/data-generator/src/main/java/generate.java
 *  Few queries are executed over this platform.
 * To compare generation of unlabeled blank nodes over tables with and without primary keys.
 * all tests are executed over the same database just with added primary keys 
 */
public class BNode7TablesOraclePerformanceTest {
    final static Logger log = LoggerFactory.getLogger(BNode7TablesOraclePerformanceTest.class);


    private Connection conn;
    private OBDAModel obdaModel;
    private OWLOntology ontology;
    private OBDADataFactory fac;

    final String owlfile = "src/test/resources/bnode/performanceTestMoreTablesOntology.owl";
    final String obdafile = "src/test/resources/bnode/ontoWisbench7TablesPerformanceTestOracle.obda";


    @Before
    public void setUp() throws Exception {

        ThreadMXBean mxBean = ManagementFactory.getThreadMXBean();
        double startTime = mxBean.getCurrentThreadCpuTime();

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

    }

    public void runTest(String query, String variable) throws Exception {
        ThreadMXBean mxBean = ManagementFactory.getThreadMXBean();
        double startTime = mxBean.getCurrentThreadCpuTime();
        // Creating a new instance of the reasoner
        QuestOWLFactory factory = new QuestOWLFactory();
        QuestOWLConfiguration config = QuestOWLConfiguration.builder().obdaModel(obdaModel).build();
        QuestOWL reasoner = factory.createReasoner(ontology, config);

        // Now we are ready for querying
        QuestOWLConnection conn = reasoner.getConnection();
        QuestOWLStatement st = conn.createStatement();

        try {


            QuestOWLResultSet rs = st.executeTuple(query);
            String sqlQuery = st.getUnfolding(query);
            System.out.println("SQL: ");
            System.out.println(sqlQuery);

            long counter = 0;
            while(rs.nextRow()) {
                ++counter;
                OWLObject ind1 = rs.getOWLObject(variable);
                //System.out.println(ToStringRenderer.getInstance().getRendering(ind1));
            }
            double endTime = mxBean.getCurrentThreadCpuTime();
            System.out.println("Number of retreived blank nodes: " + counter);
            System.out.println("Process took "+ (endTime - startTime)/1000000 + "ms");

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
    public void q1Test() throws Exception{
       String query = "PREFIX : <http://it.unibz.krdb/obda/test/simple#> SELECT ?x  WHERE{ ?x  a :A1} ";
       String resultVariable = "x";
       try {
           runTest(query, resultVariable);
       }catch (Exception e){
           throw e;
       }
    }

    @Test
    public void q2Test() throws Exception{
       String query = "PREFIX : <http://it.unibz.krdb/obda/test/simple#> SELECT ?x  WHERE{ ?x  a :A2} ";
       String resultVariable = "x";
       try {
           runTest(query, resultVariable);
       }catch (Exception e){
           throw e;
       }
    }
    @Test
    public void q3Test() throws Exception{
       String query = "PREFIX : <http://it.unibz.krdb/obda/test/simple#> SELECT ?x  WHERE{ ?x  a :A3} ";
       String resultVariable = "x";
       try {
           runTest(query, resultVariable);
       }catch (Exception e){
           throw e;
       }
    }

    @Test
    public void q4Test() throws Exception{
       String query = "PREFIX : <http://it.unibz.krdb/obda/test/simple#> SELECT ?x  WHERE{ ?x  a :A4} ";
       String resultVariable = "x";
       try {
           runTest(query, resultVariable);
       }catch (Exception e){
           throw e;
       }
    }

    @Test
    public void q5Test() throws Exception{
       String query = "PREFIX : <http://it.unibz.krdb/obda/test/simple#> SELECT ?x  WHERE{ ?x  a :A5} ";
       String resultVariable = "x";
       try {
           runTest(query, resultVariable);
       }catch (Exception e){
           throw e;
       }
    }

    @Test
    public void q6Test() throws Exception{
        String query = "PREFIX : <http://it.unibz.krdb/obda/test/simple#> SELECT ?x  WHERE{ " +
                "{?x  a :A1 }" +
                " UNION { ?x a :A2} " +
                "} ";
        String resultVariable = "x";
        try {
            runTest(query, resultVariable);
        }catch (Exception e){
            throw e;
        }
    }

    @Test
    public void q7Test() throws Exception{
        String query = "PREFIX : <http://it.unibz.krdb/obda/test/simple#> SELECT ?x  WHERE{ " +
                "{?x  a :A1 }" +
                " UNION { ?x a :A2} " +
                " UNION { ?x a :A3} " +
                "} ";
        String resultVariable = "x";
        try {
            runTest(query, resultVariable);
        }catch (Exception e){
            throw e;
        }
    }

    @Test
    public void q8Test() throws Exception{
        String query = "PREFIX : <http://it.unibz.krdb/obda/test/simple#> SELECT ?x ?y WHERE{ " +
                "?x  a :A1 ; :Property1 ?y ." +
                "} ";
        String resultVariable = "x";
        try {
            runTest(query, resultVariable);
        }catch (Exception e){
            throw e;
        }
    }

    @Test
    public void q9Test() throws Exception{
        String query = "PREFIX : <http://it.unibz.krdb/obda/test/simple#> SELECT ?x1 ?x2 ?y1 ?y2 WHERE{ " +
                "?x1  a :A1 . ?x2  a :A2.  ?x1 :Property1 ?y1 . ?x2 :Property2 ?y2 ." +
                "} ";
        String resultVariable = "x1";
        try {
            runTest(query, resultVariable);
        }catch (Exception e){
            throw e;
        }
    }
    
    @Test
    public void q10Test() throws Exception{
        String query = "PREFIX : <http://it.unibz.krdb/obda/test/simple#> SELECT ?x ?y WHERE{ " +
                "?x :Property3 ?y.  "+
                "} ";
        String resultVariable = "x";
        try {
            runTest(query, resultVariable);
        }catch (Exception e){
            throw e;
        }
    }
}
