package it.unibz.inf.ontop.sql;

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


import com.google.common.collect.ImmutableSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;

/**
 * Retrieves the database metadata (table schema and database constraints) 
 * 
 * @author Roman Kontchakov
 *
 */

/**
 * 
 * HSQLDB
 * 
 * http://www.hsqldb.org/doc/1.8/src/org/hsqldb/jdbc/jdbcDatabaseMetaData.html
 * 
 * HSQLDB treats unquoted identifiers as case insensitive in SQL but stores them in upper case; 
 * it treats quoted identifiers as case sensitive and stores them verbatim. All jdbcDatabaseMetaData 
 * methods perform case-sensitive comparison between name (pattern) arguments and the corresponding 
 * identifier values as they are stored in the database.
 *
 * HSQLDB uses the standard SQL identifier quote character (the double quote character); 
 * getIdentifierQuoteString() always returns ".
 * 
 * 
 * 
 * PostgreSQL
 * ----------
 * 
 * http://www.postgresql.org/docs/9.1/static/sql-syntax-lexical.html
 * 
 * Unquoted names are always folded to lower (!) case.
 * 
 * Quoted identifier is formed by enclosing an arbitrary sequence of characters in double-quotes ("). 
 * (To include a double quote, write two double quotes.) 
 * 
 * A variant of quoted identifiers allows including escaped Unicode characters identified by their code points. 
 * This variant starts with U& (upper or lower case U followed by ampersand) immediately before the opening 
 * double quote, without any spaces in between, for example U&"foo".
 * 
 * 
 * H2 
 * --
 * 
 * http://h2database.com/html/grammar.html
 * 
 * Names are not case sensitive (but it appears that the upper-case is the canonical form).
 * 
 * Quoted names are case sensitive, and can contain spaces. 
 * Two double quotes can be used to create a single double quote inside an identifier.
 * 
 * 
 * MS SQL Server
 * -------------
 * 
 * https://msdn.microsoft.com/en-us/library/ms378535(v=sql.110).aspx
 * 
 * When using the Microsoft JDBC Driver with a SQL Server database, 
 * getIdentifierQuoteString returns double quotation marks ("").
 * 
 * 
 * MySQL
 * -----
 * 
 * http://dev.mysql.com/doc/refman/5.0/en/identifier-case-sensitivity.html
 * 
 * How table and database names are stored on disk and used in MySQL is affected 
 * by the lower_case_table_names system variable, which you can set when starting mysqld.
 * 
 * Column, index, and stored routine names are not case sensitive on any platform, nor are column aliases. 
 * 
 */

public class DBMetadataExtractor {

	private static final boolean printouts = false;
	
	private static Logger log = LoggerFactory.getLogger(DBMetadataExtractor.class);

	
	public static DBMetadata createDummyMetadata() {
		return createDummyMetadata("dummy class");
	}
	
	public static DBMetadata createDummyMetadata(String driver_class) {
		return new DBMetadata(driver_class, null, null, "", new QuotedIDFactoryStandardSQL("\""));
	}
	
	
	/**
	 * Creates database metadata description (but does not load metadata) 
	 * 
	 * @return The database metadata object.
	 * @throws SQLException 
	 */

	public static DBMetadata createMetadata(Connection conn) throws SQLException  {
		
		final DatabaseMetaData md = conn.getMetaData();
		String productName = md.getDatabaseProductName();
		
		QuotedIDFactory idfac;
		// treat Exareme as a case-sensitive DB engine (like MS SQL Server)
		if (md.storesMixedCaseIdentifiers()) {
			 //  MySQL
			if (productName.contains("MySQL"))  {
				//System.out.println("getIdentifierQuoteString: " + md.getIdentifierQuoteString());		
				idfac = new QuotedIDFactoryMySQL("`"); 
			}
			else
				// "SQL Server" = MS SQL Server
				idfac = new QuotedIDFactoryIdentity("\"");
		}
		else {
			if (md.storesLowerCaseIdentifiers())
				// PostgreSQL treats unquoted identifiers as lower-case
				idfac = new QuotedIDFactoryLowerCase("\"");
			else if (md.storesUpperCaseIdentifiers())
				// Oracle, DB2, H2, HSQL 
				idfac = new QuotedIDFactoryStandardSQL("\"");
			else {
				log.warn("Unknown combination of identifier handling rules: " + md.getDatabaseProductName());
				log.warn("storesLowerCaseIdentifiers: " + md.storesLowerCaseIdentifiers());
				log.warn("storesUpperCaseIdentifiers: " + md.storesUpperCaseIdentifiers());
				log.warn("storesMixedCaseIdentifiers: " + md.storesMixedCaseIdentifiers());
				log.warn("supportsMixedCaseIdentifiers: " + md.supportsMixedCaseIdentifiers());
				log.warn("storesLowerCaseQuotedIdentifiers: " + md.storesLowerCaseQuotedIdentifiers());
				log.warn("storesUpperCaseQuotedIdentifiers: " + md.storesUpperCaseQuotedIdentifiers());
				log.warn("storesMixedCaseQuotedIdentifiers: " + md.storesMixedCaseQuotedIdentifiers());
				log.warn("supportsMixedCaseQuotedIdentifiers: " + md.supportsMixedCaseQuotedIdentifiers());
				log.warn("getIdentifierQuoteString: " + md.getIdentifierQuoteString());		
				
				idfac = new QuotedIDFactoryStandardSQL("\"");
			}
		}
		
		DBMetadata metadata = new DBMetadata(md.getDriverName(), md.getDriverVersion(), 
							productName, md.getDatabaseProductVersion(), idfac);
		
		return metadata;	
	}
	
	/**
	 * Retrieves the database metadata (table schema and database constraints) 
	 * 
	 * This method either uses the given list of tables or 
	 *    if it is null then it retrieves all the complete list of tables from 
	 *    the connection metadata
	 * 
	 * @return The database metadata object.
	 */

	public static void loadMetadata(DBMetadata metadata, Connection conn, Set<RelationID> realTables) throws SQLException {
		
		if (printouts)
			System.out.println("GETTING METADATA WITH " + conn + " ON " + realTables);
		
		final DatabaseMetaData md = conn.getMetaData();
		String productName = md.getDatabaseProductName();

		List<RelationID> seedRelationIds;
		QuotedIDFactory idfac =  metadata.getQuotedIDFactory();

		if (productName.contains("Oracle")) {
			String defaultSchema = getOracleDefaultOwner(conn);
			if (realTables == null || realTables.isEmpty())
				seedRelationIds = getTableList(conn, new OracleRelationListProvider(idfac, defaultSchema), idfac);
			else
				seedRelationIds = getTableList(defaultSchema, realTables, idfac);		
		} 
		else {
			if (realTables == null || realTables.isEmpty())  {
				if (productName.contains("DB2")) 
					// select CURRENT SCHEMA  from  SYSIBM.SYSDUMMY1
					seedRelationIds = getTableListDefault(md, 
							ImmutableSet.of("SYSTOOLS", "SYSCAT", "SYSIBM", "SYSIBMADM", "SYSSTAT"), idfac);
				else if (productName.contains("SQL Server"))  // MS SQL Server
					// SELECT SCHEMA_NAME() would give default schema name
					// https://msdn.microsoft.com/en-us/library/ms175068.aspx
					seedRelationIds = getTableListDefault(md, 
							ImmutableSet.of("sys", "INFORMATION_SCHEMA"), idfac);
				else 
					// for other database engines, including H2, HSQL, PostgreSQL and MySQL
					seedRelationIds = getTableListDefault(md, ImmutableSet.<String>of(), idfac);
			}
			else 
				seedRelationIds = getTableList(null, realTables, idfac);
		}
		
		DatatypeNormalizer dt = DefaultTypeFixer;
		if (productName.contains("Oracle"))
			dt = OracleTypeFixer;
		else if (productName.contains("MySQL"))
			dt = MySQLTypeFixer;

		List<DatabaseRelationDefinition> extractedRelations = new LinkedList<>();
		
		for (RelationID seedId : seedRelationIds) {
			// the same seedId can be mapped to many tables (if the seedId has no schema)
			// we collect attributes from all of them
			DatabaseRelationDefinition currentRelation = null;
			
			// catalog is ignored for now (rs.getString("TABLE_CAT"))
			try (ResultSet rs = md.getColumns(null, seedId.getSchemaName(), seedId.getTableName(), null)) {
				while (rs.next()) {
					RelationID relationId = RelationID.createRelationIdFromDatabaseRecord(idfac, rs.getString("TABLE_SCHEM"),
							rs.getString("TABLE_NAME"));
					QuotedID attributeId = QuotedID.createIdFromDatabaseRecord(idfac, rs.getString("COLUMN_NAME"));
					if (printouts)
						System.out.println("         " + relationId + "." + attributeId);

					if (currentRelation == null || !currentRelation.getID().equals(relationId)) {
						// switch to the next database relation
						currentRelation = metadata.createDatabaseRelation(relationId);
						extractedRelations.add(currentRelation);
					}

					// columnNoNulls, columnNullable, columnNullableUnknown 
					boolean isNullable = rs.getInt("NULLABLE") != DatabaseMetaData.columnNoNulls;
					String typeName = rs.getString("TYPE_NAME");
					int dataType = dt.getCorrectedDatatype(rs.getInt("DATA_TYPE"), typeName);

					currentRelation.addAttribute(attributeId, dataType, typeName, isNullable);
				}
				if (productName.contains("Oracle")){
					//Adding ROWID to database columns
					QuotedID attributeId = QuotedID.createIdFromDatabaseRecord(idfac, "ROWID");

					// columnNoNulls, columnNullable, columnNullableUnknown
					boolean isNullable = false;
					String typeName =  "VARCHAR2";
					//I made this rs.getInt("DATA_TYPE") with fixed value 12
					int dataType = dt.getCorrectedDatatype(12, typeName);

					currentRelation.addAttribute(attributeId, dataType, typeName, isNullable);
				}
			}
		}
			
		for (DatabaseRelationDefinition relation : extractedRelations)	{
			getPrimaryKey(md, relation, metadata.getQuotedIDFactory());
			getUniqueAttributes(md, relation, metadata.getQuotedIDFactory());
			getForeignKeys(md, relation, metadata);
			if (printouts) {
				System.out.println(relation + ";");
				for (UniqueConstraint uc : relation.getUniqueConstraints())				
					System.out.println(uc + ";");
				for (ForeignKeyConstraint fk : relation.getForeignKeys())
					System.out.println(fk +  ";");
				System.out.println("");
			}
		}	
	}
	
	
	/**
	 * Retrieve the normalized list of tables from a given list of RelationIDs
	 */

	private static List<RelationID> getTableList(String defaultTableSchema, Set<RelationID> realTables, QuotedIDFactory idfac) throws SQLException {

		List<RelationID> fks = new LinkedList<>();
		for (RelationID table : realTables) {
			// defaultTableSchema is non-empty only for Oracle and DUAL is a special Oracle table 
			if (table.hasSchema() || (defaultTableSchema == null) || table.getTableName().equals("DUAL"))
				fks.add(table);
			else {
				RelationID qualifiedTableId = idfac.createRelationID(defaultTableSchema, table.getTableNameSQLRendering());
				fks.add(qualifiedTableId);
			}
		}
		return fks;
	}
	
	
	
	/**
	 * Retrieve the table and view list from the JDBC driver (works for most database engines, e.g., MySQL and PostgreSQL)
	 */
	private static List<RelationID> getTableListDefault(DatabaseMetaData md, ImmutableSet<String> ignoredSchemas, QuotedIDFactory idfac) throws SQLException {
		List<RelationID> relationIds = new LinkedList<>();
		try (ResultSet rs = md.getTables(null, null, null, new String[] { "TABLE", "VIEW" })) {	
			while (rs.next()) {
				// String catalog = rs.getString("TABLE_CAT"); // not used
				String schema = rs.getString("TABLE_SCHEM");
				String table = rs.getString("TABLE_NAME");
				if (ignoredSchemas.contains(schema)) {
					continue;
				}
				RelationID id = RelationID.createRelationIdFromDatabaseRecord(idfac, schema, table);
				relationIds.add(id);
			}
		} 
		return relationIds;
	}
	
	/**
	 * Retrieve metadata for a specific database engine
	 */
	private static List<RelationID> getTableList(Connection conn, RelationListProvider relationListProvider, QuotedIDFactory idfac) throws SQLException {
		
		// Obtain the relational objects (i.e., tables and views) 
		List<RelationID> relationIds = new LinkedList<>();
		try (Statement stmt = conn.createStatement(); 
				ResultSet rs = stmt.executeQuery(relationListProvider.getQuery())) {
			while (rs.next()) 
				relationIds.add(relationListProvider.getTableID(rs));
		}
		return relationIds; 
	}
	
	
	
	
	
	private static String getOracleDefaultOwner(Connection conn) throws SQLException {
		// Obtain the table owner (i.e., schema name) 
		String loggedUser = "SYSTEM"; // default value
		try (Statement stmt = conn.createStatement()) {
			try (ResultSet resultSet = stmt.executeQuery("SELECT user FROM dual")) {
				if (resultSet.next()) {
					loggedUser = resultSet.getString("user");
				}
			}
		}
		return loggedUser.toUpperCase();
	}
	

	
	private interface RelationListProvider {
		String getQuery();
		RelationID getTableID(ResultSet rs) throws SQLException;
	}
	
	
	/**
	 * Table list for Oracle
	 */
	
	private static final class OracleRelationListProvider implements RelationListProvider {
		
		private final String defaultTableOwner; 
		private final QuotedIDFactory idfac;
		
		public OracleRelationListProvider(QuotedIDFactory idfac, String defaultTableOwner) {
			this.defaultTableOwner = defaultTableOwner;
			this.idfac = idfac;
		}
		
		@Override
		public String getQuery() {
			// filter out all irrelevant table and view names
			return "SELECT table_name as object_name FROM user_tables WHERE " +
			       "   NOT table_name LIKE 'MVIEW$_%' AND " +
			       "   NOT table_name LIKE 'LOGMNR_%' AND " +
			       "   NOT table_name LIKE 'AQ$_%' AND " +
			       "   NOT table_name LIKE 'DEF$_%' AND " +
			       "   NOT table_name LIKE 'REPCAT$_%' AND " +
			       "   NOT table_name LIKE 'LOGSTDBY$%' AND " +
			       "   NOT table_name LIKE 'OL$%' " +
			       "UNION ALL " +
			       "SELECT view_name as object_name FROM user_views WHERE " +
			       "   NOT view_name LIKE 'MVIEW_%' AND " +
			       "   NOT view_name LIKE 'LOGMNR_%' AND " +
			       "   NOT view_name LIKE 'AQ$_%'";
		}

		@Override
		public RelationID getTableID(ResultSet rs) throws SQLException {
			return RelationID.createRelationIdFromDatabaseRecord(idfac, defaultTableOwner, rs.getString("object_name"));
		}
	};
	
	/**
	 * Table list for DB2 database engine (not needed now -- use JDBC metadata instead)
	 */
/*	
	private static final RelationListProvider DB2RelationListProvider = new RelationListProvider() {
		@Override
		public String getQuery() {
			return "SELECT TABSCHEMA, TABNAME " +
			       "FROM SYSCAT.TABLES " +
			       "WHERE OWNERTYPE='U' AND (TYPE='T' OR TYPE='V') " +
			       "     AND TBSPACEID IN (SELECT TBSPACEID FROM SYSCAT.TABLESPACES WHERE TBSPACE LIKE 'USERSPACE%')";
		}

		@Override
		public RelationID getTableID(ResultSet rs) throws SQLException {
			return RelationID.createRelationIdFromDatabaseRecord(rs.getString("TABSCHEMA"), rs.getString("TABNAME"));
		}
	};
*/
	
	/**
	 * Table list for MS SQL Server database engine (not needed now -- use JDBC metadata instead)
	 */
/*
	private static final RelationListProvider MSSQLServerRelationListProvider = new RelationListProvider() {
		@Override
		public String getQuery() {
			return "SELECT TABLE_CATALOG, TABLE_SCHEMA, TABLE_NAME " +
					"FROM INFORMATION_SCHEMA.TABLES " +
					"WHERE TABLE_TYPE='BASE TABLE' OR TABLE_TYPE='VIEW'";
		}

		@Override
		public RelationID getTableID(ResultSet rs) throws SQLException {
			//String tblCatalog = rs.getString("TABLE_CATALOG");
			return RelationID.createRelationIdFromDatabaseRecord(rs.getString("TABLE_SCHEMA"), rs.getString("TABLE_NAME"));
		}
	};
*/	
	
	
	/**
	 * A method of fixing discrepancies in datatype correspondence
	 */
	
	private interface DatatypeNormalizer {
		int getCorrectedDatatype(int dataType, String typeName);
	}
	
	private static final DatatypeNormalizer DefaultTypeFixer = new DatatypeNormalizer() {
		@Override
		public int getCorrectedDatatype(int dataType, String typeName) {					
			return dataType;
		}};
		
	private static final DatatypeNormalizer MySQLTypeFixer = new DatatypeNormalizer() {
		@Override
		public int getCorrectedDatatype(int dataType, String typeName) {					
			// Fix for MySQL YEAR (see Table 5.2 at 
			//        http://dev.mysql.com/doc/connector-j/en/connector-j-reference-type-conversions.html)
			if (dataType ==  Types.DATE && typeName.equals("YEAR")) 
				return -10000;
			return dataType;
		}};	
			
	private static final DatatypeNormalizer OracleTypeFixer = new DatatypeNormalizer() {
		@Override
		public int getCorrectedDatatype(int dataType, String typeName) {					
			
			//TODO 
			// Oracle bug here - wrong automatic typing - Date vs DATETIME - driver ojdbc16-11.2.0.3
			// Oracle returns 93 for DATE SQL types, but this corresponds to 
			// TIMESTAMP. This causes a wrong typing to xsd:dateTime and later
			// parsing errors. To avoid this bug manually type the column in the
			// mapping. This may be a problem of the driver, try with other version
			// I tried oracle thin driver ojdbc16-11.2.0.3
			//
			// ROMAN (19 Sep 2015): see 
			//    http://www.oracle.com/technetwork/database/enterprise-edition/jdbc-faq-090281.html#08_01
			
			if (dataType == Types.TIMESTAMP && typeName.equals("DATE")) 
				dataType = Types.DATE;
			return dataType;
		}};
	
	
	
	
	/**
	 * Prints column names of a given table.
     *
	 */
/*		
	private static void displayColumnNames(DatabaseMetaData dbMetadata, 
			Connection connection, ResultSet rsColumns, 
			String tableSchema, String tableName) throws SQLException {

		log.debug("=============== COLUMN METADATA ========================");
		
		if (dbMetadata.getDatabaseProductName().contains("DB2")) {
			 // Alternative solution for DB2 to print column names
		     // Queries directly the system table SysCat.Columns
			//  ROMAN (20 Sep 2015): use PreparedStatement instead?
			try (Statement st = connection.createStatement()) {
		        String sqlQuery = String.format("SELECT colname, typename \n FROM SysCat.Columns \n" + 
		        								"WHERE tabname = '%s' AND tabschema = '%s'", tableName, tableSchema);
		        
		        try (ResultSet results = st.executeQuery(sqlQuery)) {
			        while (results.next()) {
			            log.debug("Column={} Type={}", results.getString("colname"), results.getString("typename"));
			        }
		        }
			}
		}
		else {
			 // Generic procedure based on JDBC
			ResultSetMetaData columnMetadata = rsColumns.getMetaData();
			int count = columnMetadata.getColumnCount();
			for (int j = 0; j < count; j++) {
			    String columnName = columnMetadata.getColumnName(j + 1);
			    String value = rsColumns.getString(columnName);
			    log.debug("Column={} Type={}", columnName, value);
			}				
		}
	}
*/	
	
	

	/** 
	 * Retrieves the primary key for the table 
	 * 
	 */
	private static void getPrimaryKey(DatabaseMetaData md, DatabaseRelationDefinition relation, QuotedIDFactory idfac) throws SQLException {
		RelationID id = relation.getID();
		// Retrieves a description of the given table's primary key columns. They are ordered by COLUMN_NAME (sic!)
		try (ResultSet rs = md.getPrimaryKeys(null, id.getSchemaName(), id.getTableName())) {
			Map<Integer, String> primaryKeyAttributes = new HashMap<>();
			String currentName = null;
			while (rs.next()) {
				// TABLE_CAT is ignored for now; assume here that relation has a fully specified name
				RelationID id2 = RelationID.createRelationIdFromDatabaseRecord(idfac, 
									rs.getString("TABLE_SCHEM"), rs.getString("TABLE_NAME"));		
				if (id2.equals(id)) {
					currentName = rs.getString("PK_NAME"); // may be null			
					String attr = rs.getString("COLUMN_NAME");
					int seq = rs.getShort("KEY_SEQ");
					primaryKeyAttributes.put(seq, attr);
				}
			}
			if (!primaryKeyAttributes.isEmpty()) {
				// use the KEY_SEQ values to restore the correct order of attributes in the PK
				UniqueConstraint.Builder builder = UniqueConstraint.builder(relation);
				for (int i = 1; i <= primaryKeyAttributes.size(); i++) {
					QuotedID attrId = QuotedID.createIdFromDatabaseRecord(idfac, primaryKeyAttributes.get(i));
					builder.add(relation.getAttribute(attrId));
				}
				relation.addUniqueConstraint(builder.build(currentName, true));
			}
		} 
	}
	
	/**
	 * Retrieves the unique attributes(s) 
	 * @param md
	 * @return
	 * @throws SQLException 
	 */
	private static void getUniqueAttributes(DatabaseMetaData md, DatabaseRelationDefinition relation, QuotedIDFactory idfac) throws SQLException {
		
		RelationID id = relation.getID();
		// extracting unique 
		try (ResultSet rs = md.getIndexInfo(null, id.getSchemaName(), id.getTableName(), true, true)) {
			UniqueConstraint.Builder builder = null;
			String currentName = null;
			while (rs.next()) {
				// TYPE: tableIndexStatistic - this identifies table statistics that are returned in conjunction with a table's index descriptions
				//       tableIndexClustered - this is a clustered index
				//       tableIndexHashed - this is a hashed index
				//       tableIndexOther (all are static final int in DatabaseMetaData)
				if (rs.getShort("TYPE") == DatabaseMetaData.tableIndexStatistic) {
					if (builder != null)
						relation.addUniqueConstraint(builder.build(currentName, false));
					
					builder = null;
					continue;
				}
				if (rs.getShort("ORDINAL_POSITION") == 1) {
					if (builder != null)
						relation.addUniqueConstraint(builder.build(currentName, false));

					// TABLE_CAT is ignored for now; assume here that relation has a fully specified name
					// and so, no need to check whether TABLE_SCHEM and TABLE_NAME match
					
					if (!rs.getBoolean("NON_UNIQUE")) {
						builder = UniqueConstraint.builder(relation);
						currentName = rs.getString("INDEX_NAME"); 
					}
					else 
						builder = null;
				}
				
				if (builder != null) {
					QuotedID attrId = QuotedID.createIdFromDatabaseRecord(idfac, rs.getString("COLUMN_NAME"));
					// ASC_OR_DESC String => column sort sequence, "A" => ascending, "D" => descending,
					//        may be null if sort sequence is not supported; null when TYPE is tableIndexStatistic
					// CARDINALITY int => When TYPE is tableIndexStatistic, then this is the number of rows in the table; 
					//                      otherwise, it is the number of unique values in the index.
					// PAGES int => When TYPE is tableIndexStatisic then this is the number of pages used for the table, 
					//                    otherwise it is the number of pages used for the current index.
					// FILTER_CONDITION String => Filter condition, if any. (may be null)
					Attribute attr = relation.getAttribute(attrId);
					if (attr == null) { // Compensate for the bug in PostgreSQL JBDC driver that
						// strips off the quatation marks
						attrId = QuotedID.createIdFromDatabaseRecord(idfac, "\"" + rs.getString("COLUMN_NAME") + "\"");
						attr = relation.getAttribute(attrId);
					}
					builder.add(attr);
				}
			}
			if (builder != null)
				relation.addUniqueConstraint(builder.build(currentName, false));
		}
	}
	
	/** 
	 * Retrieves the foreign keys for the table 
	 * 
	 */
	private static void getForeignKeys(DatabaseMetaData md, DatabaseRelationDefinition relation, DBMetadata metadata) throws SQLException {
		
		QuotedIDFactory idfac = metadata.getQuotedIDFactory();
		
		RelationID relationId = relation.getID();	
		try (ResultSet rs = md.getImportedKeys(null, relationId.getSchemaName(), relationId.getTableName())) {
			ForeignKeyConstraint.Builder builder = null;
			String currentName = null;
			while (rs.next()) {
				RelationID refId = RelationID.createRelationIdFromDatabaseRecord(idfac,
										rs.getString("PKTABLE_SCHEM"), rs.getString("PKTABLE_NAME"));
				DatabaseRelationDefinition ref = metadata.getDatabaseRelation(refId);
				// FKTABLE_SCHEM and FKTABLE_NAME are ignored for now  
				int seq = rs.getShort("KEY_SEQ");
				if (seq == 1) {
					if (builder != null) 
						relation.addForeignKeyConstraint(builder.build(currentName));
					
					currentName = rs.getString("FK_NAME"); // String => foreign key name (may be null)
					
					if (ref != null) {
						builder = new ForeignKeyConstraint.Builder(relation, ref);
					}
					else {
						builder = null; // do not add this foreign key 
						                // because there is no table it refers to
						System.err.println("Cannot find table: " + refId + " for FK " + currentName);
					}
				}
				if (builder != null) {
					QuotedID attrId = QuotedID.createIdFromDatabaseRecord(idfac, rs.getString("FKCOLUMN_NAME"));
					QuotedID refAttrId = QuotedID.createIdFromDatabaseRecord(idfac, rs.getString("PKCOLUMN_NAME"));
					builder.add(relation.getAttribute(attrId), ref.getAttribute(refAttrId));
				}
			}
			if (builder != null)
				relation.addForeignKeyConstraint(builder.build(currentName));
		} 
	}
}
