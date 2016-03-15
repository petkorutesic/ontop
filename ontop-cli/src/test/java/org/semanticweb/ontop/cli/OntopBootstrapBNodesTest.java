package org.semanticweb.ontop.cli;

/**
 *
 * Purpose of this test is crating of mapping with B-Nodes
 *
 * We are going to create an H2 database with simple table without primary keys, the .sql file is fixed.
 * Then, we are going to apply ontop bootstrap on that database and create corresponding
 * owl and obda files
 * there and then query on top.
 */

import it.unibz.krdb.obda.model.OBDAModel;
import it.unibz.krdb.obda.owlapi3.bootstrapping.DirectMappingBootstrapper;
import it.unibz.krdb.obda.r2rml.R2RMLWriter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.model.OWLOntology;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class OntopBootstrapBNodesTest {



    private Connection conn;

    Logger log = LoggerFactory.getLogger(this.getClass());
    private String mappingFile =
             "src/test/resources/test/bootstrapped-test-bnodes1.obda";
    private final String jdbcPassword = "";
    private final String jdbcUserName = "sa";
    private final String jdbcUrl = "jdbc:h2:mem:questjunitdb-noprimarykeys";
    private final String baseUri = "http://www.example.org/";
    private final String owlFile = "src/test/resources/test/bootstrapped-test-bnodes1.owl";
    private final String jdbcDriverClass = "org.h2.Drive";

    @Before
    public void setUp() throws Exception {


    /*
     * Initializing and H2 database with data
     */
        // String driver = "org.h2.Driver";
        String url = "jdbc:h2:mem:questjunitdb-noprimarykeys";
        String username = "sa";
        String password = "";

        conn = DriverManager.getConnection(url, username, password);
        Statement st = conn.createStatement();

        FileReader reader = new FileReader("src/test/resources/test/db-noprimarykeys-create-h2.sql");
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


    }

    @After
    public void tearDown() throws Exception {

        dropTables();
        conn.close();

    }

    private void dropTables() throws SQLException, IOException {

        Statement st = conn.createStatement();

        FileReader reader = new FileReader("src/test/resources/test/db-noprimarykeys-drop-h2.sql");
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
    public void testBootstrap() throws Exception {

        String[] argv = {"bootstrap",
                    "-b", baseUri,
                    "-m", mappingFile,
                    "-t", owlFile,
                    "-l", jdbcUrl,
                    "-u", jdbcUserName,
                    "-p", jdbcPassword,
                    "-d", jdbcDriverClass
            };
            Ontop.main(argv);

    }

    @Test
    public  void testBootstrapR2RML() throws Exception {
        File owl = new File(owlFile);
        File obda = new File(mappingFile);
        DirectMappingBootstrapper dm = new DirectMappingBootstrapper(
                baseUri, jdbcUrl, jdbcUserName, jdbcPassword, jdbcDriverClass);
        OBDAModel model = dm.getModel();

        URI sourceID = model.getSources().iterator().next().getSourceID();

        R2RMLWriter writer = new R2RMLWriter( model, sourceID, dm.getOntology());

        writer.write(new File("r2rml.ttl"));

    }
  }
