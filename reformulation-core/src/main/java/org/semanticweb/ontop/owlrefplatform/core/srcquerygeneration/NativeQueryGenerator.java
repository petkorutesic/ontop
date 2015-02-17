package org.semanticweb.ontop.owlrefplatform.core.srcquerygeneration;

/*
 * #%L
 * ontop-reformulation-core
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

import java.io.Serializable;
import java.util.List;

import org.semanticweb.ontop.model.DatalogProgram;
import org.semanticweb.ontop.model.OBDAException;

/**
 * Generates a source query in a given native query language.
 *
 */
public interface NativeQueryGenerator extends Serializable {

	/**
	 * Translates the given datalog program into a source query, which can later
	 * be evaluated by a evaluation engine.
	 * 
	 * @param query
	 *            the datalog program
	 * @return the source query
	 * @throws Exception
	 */
	public String generateSourceQuery(DatalogProgram query, List<String> signature) throws OBDAException;

    /**
     * If the generator is immutable, the generator
     * can return itself instead of a clone.
     */
    public NativeQueryGenerator cloneIfNecessary();
}