package com.dlz.framework.db.convertor.clumnname;

public interface IColumnNameConvertor {
	/**
	 * 数据库字段名转为bean字段名
	 * @param dbKey
	 * @return
	 */
	String clumn2Str(String dbKey);
	/**
	 * bean字段名转为数据库字段名
	 * @param beanKey
	 * @return
	 */
	String str2Clumn(String beanKey);
}
