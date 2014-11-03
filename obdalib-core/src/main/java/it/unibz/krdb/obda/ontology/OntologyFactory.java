package it.unibz.krdb.obda.ontology;

/*
 * #%L
 * ontop-obdalib-core
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

import it.unibz.krdb.obda.model.Constant;
import it.unibz.krdb.obda.model.ObjectConstant;
import it.unibz.krdb.obda.model.Predicate;
import it.unibz.krdb.obda.model.ValueConstant;

public interface OntologyFactory {

	public OClass createClass(String uri);

	public Datatype createDataType(Predicate p);
	
	public ObjectPropertyExpression createObjectProperty(String uri);
	
	public DataPropertyExpression createDataProperty(String uri);

	
	
	public Ontology createOntology();

	@Deprecated 
	public SomeValuesFrom createPropertySomeRestriction(PropertyExpression role);

	public SomeValuesFrom createPropertySomeRestriction(ObjectPropertyExpression role);

	public SomeValuesFrom createPropertySomeRestriction(DataPropertyExpression role);
	
	public DataPropertyRangeExpression createDataPropertyRange(DataPropertyExpression role);
		
	
	public ObjectPropertyAssertion createObjectPropertyAssertion(ObjectPropertyExpression attribute, ObjectConstant o1, ObjectConstant o2);

	public DataPropertyAssertion createDataPropertyAssertion(DataPropertyExpression attribute, ObjectConstant o1, ValueConstant o2);
	
	public ClassAssertion createClassAssertion(OClass concept, ObjectConstant object);

}
