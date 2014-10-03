/**
 * 
 */
package org.semanticweb.ontop.sql;

import org.semanticweb.ontop.model.OBDADataFactory;
import org.semanticweb.ontop.model.Predicate;
import org.semanticweb.ontop.sql.api.Attribute;
import org.semanticweb.ontop.sql.api.RelationJSQL;
import org.semanticweb.ontop.sql.api.TableJSQL;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sf.jsqlparser.schema.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 * Used for reading user-provided information about concepts and properties that are complete w.r.t
 * ontological reasoning 
 * 
 *  @author mrezk
 *
 */
public class CompletePredicatesWRTReasoning {
	
	
	private static Logger log = LoggerFactory.getLogger(CompletePredicatesWRTReasoning.class);
	
	
	//Complete predicates
	private HashSet<String> completePredicates;
	private OBDADataFactory fac ;
	// File with the user-supplied constraints
	private File file;
	
	/**
	 * Reads colon separated  predicates
	 * @param The name of the plain-text file with the fake keys
	 * @throws IOException 
	 */
	public CompletePredicatesWRTReasoning(String filename) {
		this(new File(filename));
	}
		
		
	/**
	 * Reads colon separated pairs of view name and primary key
	 * @param The plain-text file with the fake keys
	 * @throws IOException 
	 */
	public CompletePredicatesWRTReasoning(File file) {
		if(!file.exists()){
			throw new IllegalArgumentException("File " + file + " does not exist");
		}
		log.debug("Reading Complete Predicates.");
		this.file = file;
		this.completePredicates = new HashSet<String>();
		
		this.parseConstraints();
	}


	private void parseConstraints( ){
		BufferedReader reader = null;
	
		try {
			reader = new BufferedReader(new FileReader(file));
			String line = null;
			while ((line = reader.readLine()) != null) {
				
				line = line.trim();
				this.completePredicates.add(line);
				
			}

		} catch (FileNotFoundException e) {
			log.warn("Could not find file " + file + " in directory " + System.getenv().get("PWD"));
			String currentDir = System.getProperty("user.dir");
			log.warn("Current dir using System:" +currentDir);
		} catch (IOException e) {
			log.warn("Problem reading concepts from  file " + file);
			log.warn(e.getMessage());
		} 
		
	}
	

	
}
