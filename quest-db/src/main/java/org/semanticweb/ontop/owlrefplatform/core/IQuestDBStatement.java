package org.semanticweb.ontop.owlrefplatform.core;

import org.semanticweb.ontop.model.OBDAException;
import org.semanticweb.ontop.model.OBDAStatement;
import org.semanticweb.ontop.ontology.Assertion;

import java.net.URI;
import java.util.Iterator;

/**
 * TODO: describe
 *
 * TODO: Rename it (not now) QuestDBStatement.
 */
public interface IQuestDBStatement extends OBDAStatement {

    /**
     * Deprecated. See getTargetQuery() instead.
     */
    @Deprecated
    String getSQL(String query) throws OBDAException;

    /**
     * Gets the target query.
     */
    TargetQuery getTargetQuery(String query) throws OBDAException;

    /**
     * TODO: explain
     */
    String getRewriting(String query) throws OBDAException;

    /**
     * May not be supported (if read-only)
     */
    int add(Assertion data) throws OBDAException;

    /**
     * May not be supported (if read-only)
     */
    int add(Iterator<Assertion> data) throws OBDAException;

    /**
     * May not be supported (if read-only)
     */
    int add(Iterator<Assertion> data, int commit, int batch) throws OBDAException;

    /**
     * May not be supported (if read-only)
     */
    int add(URI rdffile) throws OBDAException;

    /**
     * May not be supported (if read-only)
     */
    int add(URI rdffile, int commit, int batch) throws OBDAException;

    /**
     * May not be supported (if read-only)
     */
    int addWithTempFile(URI rdffile) throws OBDAException;

    /**
     * May not be supported (if read-only)
     */
    int addFromOBDA(URI obdaFile) throws OBDAException;

    /**
     * May not be supported (if read-only)
     */
    int addFromOBDA(URI obdaFile, int commitrate, int batchinserts) throws OBDAException;

    /**
     * May not be supported (if read-only)
     */
    int addFromOBDAWithTempFile(URI obdaFile) throws OBDAException;
}
