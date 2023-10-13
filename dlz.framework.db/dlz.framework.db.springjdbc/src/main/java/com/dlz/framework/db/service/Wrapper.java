package com.dlz.framework.db.service;

import com.dlz.framework.db.helper.bean.Sort;
import com.dlz.framework.db.warpper.Condition;

import static com.dlz.framework.db.enums.DbOprateEnum.where;

/**
 * 查询语句生成器
 * 
 * @author CYM
 *
 */
public class Wrapper<T> {
	boolean andLink;
	String sqlJdbc;
	Object[] sqlJdbcPara;

	private Class<T> beanClass;
	private T bean;

	//查询字段
	String clumns="*";
	//查询条件
	private Condition condition=where.mk();
	//排序条件
	Sort sortList = new Sort();

	public Wrapper(Class<T> beanClass) {
		this.beanClass = beanClass;
	}
	public Wrapper(T bean) {
		this.beanClass = (Class<T>) bean.getClass();
		this.bean = bean;
	}
	public Wrapper() {
	}

	public String getSqlJdbc() {
		return sqlJdbc;
	}

	public Object[] getSqlJdbcPara() {
		return sqlJdbcPara;
	}

	public Class<T> getBeanClass() {
		return beanClass;
	}

}
