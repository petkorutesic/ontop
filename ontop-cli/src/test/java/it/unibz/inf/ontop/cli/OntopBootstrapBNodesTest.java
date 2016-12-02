package it.unibz.inf.ontop.cli;

/**
 *
 * The purpose of this test is to create mappings making use of blank nodes
 *
 * We create an H2 database with simple table without primary keys,
 * that will be used for testing.
 *
 * Then, we apply ontop bootstrap on that database and create corresponding
 * owl and obda files and r2rml files which corresponding to
 * the direct mapping of the database
 */

import it.unibz.inf.ontop.model.OBDAModel;
import it.unibz.inf.ontop.owlapi.bootstrapping.DirectMappingBootstrapper;
import it.unibz.inf.ontop.r2rml.R2RMLWriter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class OntopBootstrapBNodesTest {



    private Connection conn;

    Logger log = LoggerFactory.getLogger(this.getClass());

    private String mappingFile = "src/test/resources/test/bootstrapped-test-bnodes1.obda";
    private final String owlFile = "src/test/resources/test/bootstrapped-test-bnodes1.owl";

    private final String jdbcPassword = "";
    private final String jdbcUserName = "sa";
    private final String jdbcUrl = "jdbc:h2:mem:questjunitdb-noprimarykeys";
    private final String baseUri = "http://www.example.org/";
    private final String jdbcDriverClass = "org.h2.Drive";
    private String r2rmlFile = "src/test/resources/test/bootstrapped-test-r2rml.ttl";
    private String prettifiedr2rmlFile = "src/test/resources/test/bootstrapped-test-pretty-r2rml.ttl";

    private String materializedDatabaseFile = "src/test/resources/test/bootstrapped-test-materialized-database.ttl";


    @Before
    public void setUp() throws Exception {


    /*
     * Initializing and H2 database with data
     */
        conn = DriverManager.getConnection(jdbcUrl, jdbcUserName, jdbcPassword);
        Statement st = conn.createStatement();

        FileReader reader = new FileReader("src/test/resources/test/simpleDBNoPK-create-h2.sql");
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

        FileReader reader = new FileReader("src/test/resources/test/simpleDBNoPK-drop-h2.sql");
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

    /**
     * For the given database the Ontop bootstrap creates the mapping and ontology files.
     * In fact, it creates the mapping and ontology which corresponds to the direct mapping of this
     * database.
     * Since the database contains a table without primary key, we get the mapping which
     * uses blank nodes with place holders

    */
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

    /**
     * For the given simple database with no primary keys
     * Ontop creates an r2rml file which can be used for the direct mapping.
     * Later that file is also "prettified" to be more readable.

     */

    @Test
    public  void testBootstrapR2RML() throws Exception {

        DirectMappingBootstrapper dm = new DirectMappingBootstrapper(
                baseUri, jdbcUrl, jdbcUserName, jdbcPassword, jdbcDriverClass);
        OBDAModel model = dm.getModel();

        URI sourceID = model.getSources().iterator().next().getSourceID();

        R2RMLWriter writer = new R2RMLWriter( model, sourceID, dm.getOntology());

        writer.write(new File(r2rmlFile));

        //Prettifying r2rml file
        String[] argv = {"mapping", "pretty-r2rml",
                "-i", r2rmlFile,
                "-o", prettifiedr2rmlFile
        };

        Ontop.main(argv);
    }


    /**
     * This test carries out the "materialization" of the database using the r2rml
     * file which is already generated in testBootstrapR2RML().
     *
     * Accordingly, testBootstrapR2RML() test must be executed prior to this one
     * and the file prettifiedr2rmlFile have to be created
     *
     */

    @Test
    public  void testBootstrapMaterialize() throws Exception {

        String[] argv = {"materialize",

               // "-b", baseUri,
                "-m", prettifiedr2rmlFile,
            //    "-t", owlFile,
                "-l", jdbcUrl,
                "-u", jdbcUserName,
                "-p", jdbcPassword,
                "-d", jdbcDriverClass,

                "-f","turtle",
                "-o", materializedDatabaseFile
        };
        Ontop.main(argv);
    }

  }
