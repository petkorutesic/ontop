package it.unibz.krdb.obda.utils;

import it.unibz.krdb.obda.model.OBDADataFactory;
import it.unibz.krdb.obda.model.Predicate;
import it.unibz.krdb.obda.model.impl.OBDADataFactoryImpl;

import java.sql.Types;
import java.util.HashMap;

public class TypeMapper {

	private static TypeMapper typeMapper;

	private final HashMap<Integer, Predicate> sqlToQuest = new HashMap<Integer, Predicate>();

	private static OBDADataFactory dfac = OBDADataFactoryImpl.getInstance();

	static {
		typeMapper = new TypeMapper();
		typeMapper.put(Types.VARCHAR, dfac.getDataTypePredicateString());
		typeMapper.put(Types.INTEGER, dfac.getDataTypePredicateInteger());
		typeMapper.put(Types.BIGINT, dfac.getDataTypePredicateInteger());
		typeMapper.put(Types.SMALLINT, dfac.getDataTypePredicateInteger());
		typeMapper.put(Types.NUMERIC, dfac.getDataTypePredicateDecimal());
		typeMapper.put(Types.DECIMAL, dfac.getDataTypePredicateDecimal());
		typeMapper.put(Types.FLOAT, dfac.getDataTypePredicateDouble());
		typeMapper.put(Types.DOUBLE, dfac.getDataTypePredicateDouble());
		typeMapper.put(Types.REAL, dfac.getDataTypePredicateDouble());
		typeMapper.put(Types.DATE, dfac.getDataTypePredicateDateTime());
		typeMapper.put(Types.TIMESTAMP, dfac.getDataTypePredicateDateTime());
		typeMapper.put(Types.BOOLEAN, dfac.getDataTypePredicateBoolean());
		typeMapper.put(Types.BINARY, dfac.getDataTypePredicateBoolean());
		typeMapper.put(Types.BIT, dfac.getDataTypePredicateBoolean());
	}

	public static TypeMapper getInstance() {
		return typeMapper;
	}

	public void put(int sqlType, Predicate questType) {
		sqlToQuest.put(sqlType, questType);
	}
	
	public Predicate getPredicate(int sqlType) {
		Predicate result = sqlToQuest.get(sqlType);
		if (result == null) {
			result = dfac.getDataTypePredicateLiteral();
		}
		return result;
	}
}
