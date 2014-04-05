package it.unibz.krdb.obda.reformulation.owlapi3;

import it.unibz.krdb.obda.io.ModelIOManager;
import it.unibz.krdb.obda.model.OBDADataFactory;
import it.unibz.krdb.obda.model.OBDAException;
import it.unibz.krdb.obda.model.OBDAModel;
import it.unibz.krdb.obda.model.impl.OBDADataFactoryImpl;
import it.unibz.krdb.obda.owlrefplatform.core.QuestConstants;
import it.unibz.krdb.obda.owlrefplatform.core.QuestPreferences;
import it.unibz.krdb.obda.owlrefplatform.owlapi3.*;

import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class LinearRecursiveSWRLVirtualABoxTest {

	private String owlfile = "src/test/resources/test/swrl/linear-recursive-swrl.owl";
	private String obdafile = "src/test/resources/test/swrl/linear-recursive-swrl.obda";
	private QuestOWL reasoner;
	private OWLOntology ontology;
	private OWLOntologyManager manager;
	private OBDAModel obdaModel;

    private OBDADataFactory fac;
    private Connection conn;

	QuestPreferences p;
	
	String prefix = "http://www.example.org/linear-recursive-swrl.owl#";

	@Before
	public void setUp() throws Exception {

        String url = "jdbc:hsqldb:mem:questjunitdb";
        String username = "sa";
        String password = "";

        fac = OBDADataFactoryImpl.getInstance();

        conn = DriverManager.getConnection(url, username, password);
//        String sqlFile_create = "src/test/resources/test/swrl/exampleSWRL_create.sql";
//        String sqlFile_insert = "src/test/resources/test/swrl/exampleSWRL_insert.sql";
//
//        executeSQLFile(sqlFile_create);
//        executeSQLFile(sqlFile_insert);

		p = new QuestPreferences();
		p.setCurrentValueOf(QuestPreferences.ABOX_MODE, QuestConstants.VIRTUAL);
		p.setCurrentValueOf(QuestPreferences.OPTIMIZE_EQUIVALENCES, QuestConstants.TRUE);
		p.setCurrentValueOf(QuestPreferences.OPTIMIZE_TBOX_SIGMA, QuestConstants.TRUE);
		p.setCurrentValueOf(QuestPreferences.OBTAIN_ABOX_ASSERTIONS_FROM_ONTOLOGY, QuestConstants.FALSE);
		p.setCurrentValueOf(QuestPreferences.SWRL_ENTAILMENT, QuestConstants.TRUE);
		
		manager = OWLManager.createOWLOntologyManager();
		try {
			ontology = manager.loadOntologyFromOntologyDocument(new File(owlfile));
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


//	private void executeSQLFile(String sqlFile) throws SQLException,
//			FileNotFoundException, IOException {
//		Statement st = conn.createStatement();
//		FileReader reader = new FileReader(sqlFile);
//        BufferedReader in = new BufferedReader(reader);
//        StringBuilder bf = new StringBuilder();
//        String line = in.readLine();
//        while (line != null) {
//            bf.append(line);
//            line = in.readLine();
//        }
//
//        st.executeUpdate(bf.toString());
//        conn.commit();
//	}
//	
	
	private void startReasoner(){
		QuestOWLFactory questOWLFactory = new QuestOWLFactory();
		questOWLFactory.setPreferenceHolder(p);
		obdaModel = OBDADataFactoryImpl.getInstance().getOBDAModel();
		ModelIOManager mng = new ModelIOManager(obdaModel);
		try {
			mng.load(new File(obdafile));
			questOWLFactory.setOBDAController(obdaModel);
			reasoner = (QuestOWL) questOWLFactory.createReasoner(ontology);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testSWRLDriver() throws OWLException, OBDAException {
		startReasoner();
		QuestOWLConnection connection = reasoner.getConnection();
		QuestOWLStatement stmt = connection.createStatement();
		String query = "PREFIX : <http://www.example.org/linear-recuresive-swrl.owl#> " +
				//"SELECT ?subject ?name ?age"
				"SELECT ?x ?xname ?y ?yname"
				+ " WHERE { ?x :reach ?y. "
				+ "?x :name ?xname . "
				+ "?y :name ?yname .}";
		QuestOWLResultSet rs = stmt.executeTuple(query);
		while(rs.nextRow()){
			System.out.print(rs.getOWLIndividual(1));
			System.out.print(", ");
			System.out.print(rs.getOWLIndividual(2));
			System.out.println();
		}
	} 
	
	
}
