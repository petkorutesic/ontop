package it.unibz.inf.ontop.obda;


import it.unibz.inf.ontop.io.ModelIOManager;
import it.unibz.inf.ontop.model.OBDADataFactory;
import it.unibz.inf.ontop.model.OBDAModel;
import it.unibz.inf.ontop.model.impl.OBDADataFactoryImpl;
import it.unibz.inf.ontop.owlrefplatform.core.QuestConstants;
import it.unibz.inf.ontop.owlrefplatform.core.QuestPreferences;
import it.unibz.inf.ontop.owlrefplatform.owlapi.*;
import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PreProcessProjectionTest {
    private OBDADataFactory fac;

    Logger log = LoggerFactory.getLogger(this.getClass());
    private OBDAModel obdaModel;
    private OWLOntology ontology;

    final String owlFile = "src/test/resources/northwind/northwind.owl";
    final String obdaFile = "src/test/resources/mappingStars.obda";

    @Before
    public void setUp() throws Exception {

		// String driver = "org.h2.Driver";
		// String url = "jdbc:h2:mem:stars";
		// String username = "sa";
		// String password = "";
		// Connection conn = DriverManager.getConnection(url, username, password);
    	// execute(conn, "src/test/resources/mappingStars-create-h2.sql");
    	
        fac = OBDADataFactoryImpl.getInstance();

        // Loading the OWL file
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        ontology = manager.loadOntologyFromOntologyDocument((new File(owlFile)));

        // Loading the OBDA data
        obdaModel = fac.getOBDAModel();
        ModelIOManager ioManager = new ModelIOManager(obdaModel);
        ioManager.load(obdaFile);


    }

	private static void execute(Connection conn, String filename) throws IOException, SQLException {		
		
		Statement st = conn.createStatement();
		int i = 1;
		
		FileReader reader = new FileReader(filename);
		
		StringBuilder bf = new StringBuilder();
		try (BufferedReader in = new BufferedReader(reader)) {
			for (String line = in.readLine(); line != null; line = in.readLine()) {
				bf.append(line + "\n");
				if (line.startsWith("--")) {
					System.out.println("EXECUTING " + i++ + ":\n" + bf.toString());
					//try {
						st.executeUpdate(bf.toString());
						conn.commit();
					//}
					//catch (SQLException e) {
					//	System.out.println(e);
					//}
					bf = new StringBuilder();
				}
			}
		}
	}
  
    private int runTests(QuestPreferences p, String query) throws Exception {

        // Creating a new instance of the reasoner
        QuestOWLFactory factory = new QuestOWLFactory();

		p.setProperty(QuestPreferences.PRINT_KEYS, QuestConstants.TRUE);

        QuestOWLConfiguration config = QuestOWLConfiguration.builder().obdaModel(obdaModel).preferences(p).build();

        QuestOWL reasoner = factory.createReasoner(ontology, config);

        // Now we are ready for querying
        QuestOWLConnection conn = reasoner.getConnection();
        QuestOWLStatement st = conn.createStatement();

        int results = 0;

        try {
           results= executeQueryAssertResults(query, st);

        } catch (Exception e) {
            st.close();
            e.printStackTrace();
            assertTrue(false);


        } finally {

            conn.close();
            reasoner.dispose();
        }
        return results;

    }

    private int executeQueryAssertResults(String query, QuestOWLStatement st) throws Exception {
        QuestOWLResultSet rs = st.executeTuple(query);
        int count = 0;
        while (rs.nextRow()) {
            count++;
//            for (int i = 1; i <= rs.getColumnCount(); i++) {
//
//                log.debug(rs.getSignature().get(i-1) + "=" + rs.getOWLObject(i));
//
//            }

        }
        rs.close();

        return count;

    }



    @Test
    public void testSimpleQuery() throws Exception {


        QuestPreferences p = new QuestPreferences();
        String query = "PREFIX : <http://www.semanticweb.org/vidar/ontologies/2014/11/northwind-handmade#>" +
                " select * {?x a :Category}";
        int nResults = runTests(p, query);
        assertEquals(8, nResults);
    }

    @Test
    public void testSimpleQueryJoin() throws Exception {


        QuestPreferences p = new QuestPreferences();
        String query = "PREFIX : <http://www.semanticweb.org/vidar/ontologies/2014/11/northwind-handmade#>" +
                " select * {?x a :Customer}";
        int nResults = runTests(p, query);
        assertEquals(2155, nResults);
    }

    @Test
    public void testSimpleQueryAlias() throws Exception {


        QuestPreferences p = new QuestPreferences();
        String query = "PREFIX : <http://www.semanticweb.org/vidar/ontologies/2014/11/northwind-handmade#>" +
                " select * {?x :locationRegion ?y}";
        int nResults = runTests(p, query);
        assertEquals(53, nResults);
    }

    @Test
    public void testSimpleQueryView() throws Exception {


        QuestPreferences p = new QuestPreferences();
        String query = "PREFIX : <http://www.semanticweb.org/vidar/ontologies/2014/11/northwind-handmade#>" +
                " select * {?x :orderDetailDiscount ?y}";
        int nResults = runTests(p, query);
        assertEquals(2155, nResults);
    }

    @Test
    public void testComplexQueryView() throws Exception {


        QuestPreferences p = new QuestPreferences();
        String query = "PREFIX : <http://www.semanticweb.org/vidar/ontologies/2014/11/northwind-handmade#>" +
                " select * {?x a :Location}";
        int nResults = runTests(p, query);
        assertEquals(91, nResults);
    }

    @Test
    public void testjoinWithSameName() throws Exception {


        QuestPreferences p = new QuestPreferences();
        String query = "PREFIX : <http://www.semanticweb.org/vidar/ontologies/2014/11/northwind-handmade#>" +
                " select * {?x a :OrderDetail}";
        int nResults = runTests(p, query);
        assertEquals(4310, nResults);
    }

    @Test
    public void testjoinWithAliasInSubQuery() throws Exception {


        QuestPreferences p = new QuestPreferences();
        String query = "PREFIX : <http://www.semanticweb.org/vidar/ontologies/2014/11/northwind-handmade#>" +
                " select * {?x :locationAddress ?y}";
        int nResults = runTests(p, query);
        assertEquals(19, nResults);
    }




}

