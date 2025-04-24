package com.dlz.framework.db.convertor.clumnname;

public class ColumnNameToLower implements IColumnNameConvertor {

	@Override
	public String clumn2Str(String dbKey) {
		return dbKey.toLowerCase();
	}

	@Override
	public String str2Clumn(String beanKey) {
		return beanKey;
	}
}
