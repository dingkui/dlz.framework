package com.dlz.framework.db.convertor.clumnname;

public interface IColumnNameConvertor {
	/**
	 * 数据库字段名转为bean字段名
	 * @param dbKey
	 	 */
	String toFieldName(String dbKey);
	/**
	 * bean字段名转为数据库字段名
	 * @param beanKey
	 	 */
	String toDbColumnName(String beanKey);
}
