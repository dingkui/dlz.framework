package com.dlz.framework.db.service;

import com.dlz.comm.util.VAL;
import com.dlz.framework.db.enums.DbBuildEnum;
import com.dlz.framework.db.helper.util.DbNameUtil;
import com.dlz.framework.db.modal.Page;
import com.dlz.framework.db.modal.SearchParaMap;
import com.dlz.framework.db.warpper.Condition;
import com.dlz.framework.util.system.Reflections;

import java.lang.reflect.Field;

/**
 * 查询语句生成器
 * 
 * @author CYM
 *
 */
public class Wrapper<T> {
	private Class<T> beanClass;
	private boolean isGenerator = false;
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
		if(isGenerator && bean!=null){
			Condition condition = DbBuildEnum.where.build();
			Field[] fields = Reflections.getFields(this.beanClass);
			for (Field field : fields) {
				Object fieldValue = Reflections.getFieldValue(bean, field.getName());
				if(fieldValue!=null){
					condition.eq(DbNameUtil.getDbClumnName(field), fieldValue);
				}
			}
			pm.where(condition);
		}
		if(pm.getPage().getPageIndex()==-1){
			return pm.getJdbcSql();
		}
		return pm.getPageJdbc();
	}
	public VAL<String,Object[]> getCntJdbc() {
		return pm.getCntJdbc();
	}

	public Page<T> page() {
		return pm.getPage();
	}

	public Class<T> getBeanClass() {
		return beanClass;
	}

}
