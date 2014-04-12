package it.unibz.krdb.obda.reformulation.owlapi3;

import it.unibz.krdb.obda.io.ModelIOManager;
import it.unibz.krdb.obda.model.OBDAException;
import it.unibz.krdb.obda.model.OBDAModel;
import it.unibz.krdb.obda.model.impl.OBDADataFactoryImpl;
import it.unibz.krdb.obda.owlrefplatform.core.QuestConstants;
import it.unibz.krdb.obda.owlrefplatform.core.QuestPreferences;
import it.unibz.krdb.obda.owlrefplatform.owlapi3.QuestOWL;
import it.unibz.krdb.obda.owlrefplatform.owlapi3.QuestOWLConnection;
import it.unibz.krdb.obda.owlrefplatform.owlapi3.QuestOWLFactory;
import it.unibz.krdb.obda.owlrefplatform.owlapi3.QuestOWLResultSet;
import it.unibz.krdb.obda.owlrefplatform.owlapi3.QuestOWLStatement;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;

import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

public class SWRLNPDVirtualABoxTest {

	private String owlfile = "/Users/mariano/Dropbox/NPD-Benchmark/npd-v2-ql_a-swrl.owl";
	private String obdafile = "/Users/mariano/Dropbox/NPD-Benchmark/npd-v2-ql_a.obda";
	private String queriesDir = "/Users/mariano/Dropbox/NPD-Benchmark/12queries.1";
	private String logFileName = "testout.txt";
	
	
	private QuestOWL reasoner;
	private OWLOntology ontology;
	private OWLOntologyManager manager;
	private OBDAModel obdaModel;

    private Connection conn;

	QuestPreferences p;
	
	String prefix = "http://meraka/moss/exampleBooks.owl#";

	@Before
	public void setUp() throws Exception {


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
			e.printStackTrace();
		}
	}

	
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
	public void testSWRLNPD() throws OWLException, OBDAException, IOException {
		startReasoner();
		QuestOWLConnection connection = reasoner.getConnection();
		QuestOWLStatement stmt = connection.createStatement();
		
		File queryFolder = new File(queriesDir);
		File[] queryFiles = queryFolder.listFiles();
		
		BufferedWriter out = new BufferedWriter(new FileWriter(logFileName));
		
		for (File queryFile: queryFiles) {
			
			if (!queryFile.getAbsolutePath().endsWith(".txt")) {
				continue;
			}
			
			String query = readFile(queryFile);
			System.out.println("file:" + query);
			out.write("file: " + queryFile.toString() + "\n");
			out.flush();
			long start = System.currentTimeMillis();
			QuestOWLResultSet rs = stmt.executeTuple(query);
			long end = System.currentTimeMillis();
			
			out.write("time elapsed: " + (end-start) + "\n");
			out.flush();
			System.out.println("time elapsed: " + (end-start) + "\n");
			start = System.currentTimeMillis();
			int count = 0;
			while(rs.nextRow()){
				count += 1;
			}
			end = System.currentTimeMillis();
			out.write("count time: " + (end-start) + "\n");
			out.write("count: " + count + "\n");
			out.flush();
			
			System.out.println("count time: " + (end-start) + "\n");
			System.out.println("count: " + count + "\n");
			
			
			String sqlquery = stmt.getUnfolding(query);
			BufferedWriter outSQL = new BufferedWriter(new FileWriter(queryFile.getAbsoluteFile() + ".sql"));
			outSQL.write(sqlquery);
			outSQL.newLine();
			outSQL.flush();
			outSQL.close();
			
			rs.close();
			
		}
		out.close();
		stmt.close();
		connection.close();
		reasoner.dispose();
		
		
	} 
	
	private String readFile(File file) throws IOException {
		BufferedReader buf = new BufferedReader(new FileReader(file));
		StringBuffer str = new StringBuffer();
		
		String line = buf.readLine();
		while (line != null) {
			str.append(line + "\n");
			line = buf.readLine();
		}
		
		buf.close();
		return str.toString();
	}
	
	public static void main(String[] args) throws Exception{
		SWRLNPDVirtualABoxTest benchmark = new SWRLNPDVirtualABoxTest();
		if(args.length == 4 ){
			 benchmark.owlfile = args[0];
			 benchmark.obdafile = args[1];
			 benchmark.queriesDir = args[2];
			 benchmark.logFileName = args[3];
		} else {
			System.err.println("Usage: main owlfile obdafile queriesDir logFileName");
			System.exit(0);
		}
		benchmark.setUp();
		benchmark.testSWRLNPD();
	}
	
}
