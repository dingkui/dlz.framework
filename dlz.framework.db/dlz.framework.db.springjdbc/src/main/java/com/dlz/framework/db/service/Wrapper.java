package com.dlz.framework.db.service;

import com.dlz.comm.util.VAL;
import com.dlz.framework.db.helper.bean.Sort;
import com.dlz.framework.db.helper.util.DbNameUtil;
import com.dlz.framework.db.modal.BaseParaMap;
import com.dlz.framework.db.modal.Page;
import com.dlz.framework.db.modal.SearchParaMap;
import com.dlz.framework.db.warpper.Condition;

import java.util.stream.Collectors;

import static com.dlz.framework.db.enums.DbOprateEnum.where;

/**
 * 查询语句生成器
 * 
 * @author CYM
 *
 */
public class Wrapper<T> {
	private Class<T> beanClass;
	private T bean;
	SearchParaMap pm;

	public Wrapper(Class<T> beanClass) {
		this.beanClass = beanClass;
		pm = new SearchParaMap(DbNameUtil.getDbTableName(beanClass));
	}
	public Wrapper(T bean) {
		this.beanClass = (Class<T>) bean.getClass();
		pm = new SearchParaMap(DbNameUtil.getDbTableName(beanClass));
		this.bean = bean;
	}

	public VAL<String,Object[]> getSqlJdbc() {
		if(pm.getPage().getPageIndex()==-1){
			return pm.getJdbcSql();
		}
		return pm.getPageJdbc();
	}
	public VAL<String,Object[]> getCntJdbc() {
		return pm.getCntJdbc();
	}

	public Condition condition() {
		return pm.condition();
	}
	public Sort sort() {
		return pm.sort();
	}
	public Page<T> page() {
		return pm.getPage();
	}

	public Class<T> getBeanClass() {
		return beanClass;
	}

}
