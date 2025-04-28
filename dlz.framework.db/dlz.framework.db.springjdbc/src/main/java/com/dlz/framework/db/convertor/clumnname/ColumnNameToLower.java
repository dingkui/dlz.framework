package com.dlz.framework.db.convertor.clumnname;

public class ColumnNameToLower implements IColumnNameConvertor {

	@Override
	public String toFieldName(String dbKey) {
		return dbKey.toLowerCase();
	}

	@Override
	public String toDbColumnName(String beanKey) {
		return beanKey;
	}
}
