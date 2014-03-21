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

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

public class SWRLVClassicTest {

	private String owlfile = "src/test/resources/test/swrl/exampleSWRL.owl";
	//private String obdafile = "src/test/resources/example/exampleBooks.obda";
	private QuestOWL reasoner;
	private OWLOntology ontology;
	private OWLOntologyManager manager;
	private OBDAModel obdaModel;
	
	QuestPreferences p;
	
	String prefix = "http://meraka/moss/exampleBooks.owl#";

	@Before
	public void setUp() throws Exception {

		p = new QuestPreferences();
		p.setCurrentValueOf(QuestPreferences.ABOX_MODE, QuestConstants.CLASSIC);
		p.setCurrentValueOf(QuestPreferences.OPTIMIZE_EQUIVALENCES, QuestConstants.TRUE);
		p.setCurrentValueOf(QuestPreferences.OPTIMIZE_TBOX_SIGMA, QuestConstants.TRUE);
		p.setCurrentValueOf(QuestPreferences.OBTAIN_FROM_ONTOLOGY, QuestConstants.TRUE);
		
		manager = OWLManager.createOWLOntologyManager();
		try {
			ontology = manager.loadOntologyFromOntologyDocument(new File(owlfile));
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private void startReasoner(){
		QuestOWLFactory questOWLFactory = new QuestOWLFactory();
		questOWLFactory.setPreferenceHolder(p);
		obdaModel = OBDADataFactoryImpl.getInstance().getOBDAModel();
		ModelIOManager mng = new ModelIOManager(obdaModel);
		try {
			//mng.load(new File(obdafile));
			//questOWLFactory.setOBDAController(obdaModel);
			reasoner = (QuestOWL) questOWLFactory.createReasoner(ontology);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testDisjointClassInconsistency() throws OWLException, OBDAException {
		startReasoner();
		QuestOWLConnection connection = reasoner.getConnection();
		QuestOWLStatement stmt = connection.createStatement();
		String query = "SELECT ?subject  WHERE { ?subject a <http://www.examaple.org/swrl/1#Driver> }";
		QuestOWLResultSet rs = stmt.executeTuple(query);
		while(rs.nextRow()){
			int columCount = rs.getColumCount();
			for(int i = 0; i < columCount; i++){
				System.out.print(rs.getOWLIndividual(i) + ", ");
			}
			System.out.println();
		}

	} 
	
	
}
