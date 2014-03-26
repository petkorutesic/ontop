package it.unibz.krdb.obda.owlrefplatform.core.sql;

import it.unibz.krdb.obda.model.OBDAException;

@SuppressWarnings("serial")
public class SQLGenerationException extends RuntimeException {

	public SQLGenerationException(String cause) {
		super(cause);
	}

}
