package it.unibz.ontop.sesame.completeness.test;

import it.unibz.krdb.obda.model.OBDAException;
import it.unibz.krdb.obda.model.OBDAModel;
import it.unibz.krdb.obda.owlrefplatform.core.QuestPreferences;
import it.unibz.krdb.obda.owlrefplatform.owlapi3.QuestOWL;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;

import org.junit.Before;
import org.junit.Test;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.sparql.SPARQLRepository;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

public class SWRLNPDStardogHTTPTest {

	private String owlfile = "/Users/mariano/Dropbox/NPD-Benchmark/npd-v2-ql_a-swrl.owl";
	private String obdafile = "/Users/mariano/Dropbox/NPD-Benchmark/npd-v2-ql_a.obda";
	private QuestOWL reasoner;
	private OWLOntology ontology;
	private OWLOntologyManager manager;
	private OBDAModel obdaModel;

    private Connection conn;

	QuestPreferences p;
	
	String prefix = "http://meraka/moss/exampleBooks.owl#";

	@Before
	public void setUp() throws Exception {


	}


	
	
	private void startReasoner(){

	}
	
	@Test
	public void testSWRLDriver() throws IOException, RepositoryException, MalformedQueryException, QueryEvaluationException {
		
		SPARQLRepository repo = new SPARQLRepository("http://localhost:5820/npdplain/");
		repo.setUsernameAndPassword("admin", "admin");
		repo.initialize();
		
		RepositoryConnection conn = repo.getConnection();
		
		
		File queryFolder = new File("/Users/mariano/Dropbox/NPD-Benchmark/12queries.1");
		File[] queryFiles = queryFolder.listFiles();
		
		BufferedWriter out = new BufferedWriter(new FileWriter("testout-stardog.txt"));
		
		for (File queryFile: queryFiles) {
			
			if (!queryFile.getAbsolutePath().endsWith(".txt")) {
				continue;
			}

			String query = readFile(queryFile);
			System.out.println("file:" + query);
			out.write("file: " + queryFile.toString() + "\n");
			out.flush();
			long start = System.currentTimeMillis();
			TupleQuery q = conn.prepareTupleQuery(QueryLanguage.SPARQL, query);
			TupleQueryResult r = null;
			try {
			 r = q.evaluate();
			} catch (QueryEvaluationException e) {
				e.printStackTrace();
			}
			
			long end = System.currentTimeMillis();
			
			out.write("time elapsed: " + (end-start) + "\n");
			out.flush();
			System.out.println("time elapsed: " + (end-start) + "\n");
			start = System.currentTimeMillis();
			int count = 0;
			while(r.hasNext()){
				count += 1;
				r.next();
			}
			end = System.currentTimeMillis();
			out.write("count time: " + (end-start) + "\n");
			out.write("count: " + count + "\n");
			out.flush();
			
			System.out.println("count time: " + (end-start) + "\n");
			System.out.println("count: " + count + "\n");
			
			
			
			r.close();
		}
		conn.close();
		out.flush();
		out.close();
		
		
		
		
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
	
}
