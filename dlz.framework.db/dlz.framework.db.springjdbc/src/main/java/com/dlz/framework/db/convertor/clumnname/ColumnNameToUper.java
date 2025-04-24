package com.dlz.framework.db.convertor.clumnname;

public class ColumnNameToUper implements IColumnNameConvertor {


	@Override
	public String clumn2Str(String dbKey) {
		return dbKey.toUpperCase();
	}

	@Override
	public String str2Clumn(String beanKey) {
		return beanKey;
	}
}
