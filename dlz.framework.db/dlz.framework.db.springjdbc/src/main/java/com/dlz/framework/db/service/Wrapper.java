package com.dlz.framework.db.service;

import com.dlz.comm.util.VAL;
import com.dlz.framework.db.enums.DbBuildEnum;
import com.dlz.framework.db.helper.util.DbNameUtil;
import com.dlz.framework.db.modal.Page;
import com.dlz.framework.db.modal.SearchParaMap;
import com.dlz.framework.db.warpper.Condition;
import com.dlz.framework.util.system.MFunction;
import com.dlz.framework.util.system.Reflections;

import java.lang.reflect.Field;

/**
 * 查询语句生成器
 * 
 * @author dk
 *
 */
public class Wrapper<T> {
	private Class<T> beanClass;
	private boolean isGenerator = false;
	private T bean;
	SearchParaMap pm;
	Condition condition = DbBuildEnum.where.build();

	public static <T> Wrapper<T> wrapper(T bean) {
		return new Wrapper<>(bean);
	}

	public Wrapper(Class<T> beanClass) {
		this.beanClass = beanClass;
		pm = new SearchParaMap(DbNameUtil.getDbTableName(beanClass));
	}
	public Wrapper(T bean) {
		this.beanClass = (Class<T>) bean.getClass();
		pm = new SearchParaMap(DbNameUtil.getDbTableName(beanClass));
		this.bean = bean;
	}

	private void generatConditionWithBean(){
		if(!isGenerator && bean!=null){
			Field[] fields = Reflections.getFields(this.beanClass);
			for (Field field : fields) {
				Object fieldValue = Reflections.getFieldValue(bean, field.getName());
				if(fieldValue!=null){
					condition.eq(DbNameUtil.getDbClumnName(field), fieldValue);
				}
			}
			isGenerator=true;
		}
		pm.where(condition);
	}

	public VAL<String,Object[]> jdbcSql() {
		generatConditionWithBean();
		if(pm.getPage()!=null && pm.getPage().getPageIndex()==-1){
			return pm.jdbcSql();
		}
		return pm.jdbcPage();
	}
	public VAL<String,Object[]> jdbcCnt() {
		generatConditionWithBean();
		return pm.jdbcCnt();
	}

	public Page<T> page() {
		Page pmPage = pm.getPage();
		if(pmPage==null){
			pmPage=new Page<>();
			pm.setPage(pmPage);
		}
		return pmPage;
	}

	public Wrapper<T> setPage(Page<T> page) {
		pm.setPage(page);
		return this;
	}

	public Class<T> getBeanClass() {
		return beanClass;
	}

	public Wrapper<T> bt(MFunction<T, ?> column,Object value1, Object value2) {
		condition.bt(column, value1,value2);
		return this;
	}
	public Wrapper<T> bt(MFunction<T, ?> column, Object value) {
		condition.bt(column, value);
		return this;
	}

	public Wrapper<T> nb(MFunction<T, ?> column, Object value1, Object value2) {
		condition.nb(column, value1, value2);
		return this;
	}
	public Wrapper<T> nb(MFunction<T, ?> column, Object value) {
		condition.nb(column, value);
		return this;
	}

	public Wrapper<T> isnn(MFunction<T, ?> column) {
		condition.isnn(column);
		return this;
	}

	/**
	 * is null
	 * @param column
	 * @return
	 */
	public Wrapper<T> isn(MFunction<T, ?> column) {
		condition.isn(column);
		return this;
	}

	public Wrapper<T> eq(MFunction<T, ?> column, Object value) {
		condition.eq(column, value);
		return this;
	}

	public Wrapper<T> lt(MFunction<T, ?> column, Object value) {
		condition.lt(column, value);
		return this;
	}

	public Wrapper<T> nl(MFunction<T, ?> column, Object value) {
		condition.nl(column, value);
		return this;
	}

	public Wrapper<T> lr(MFunction<T, ?> column, Object value) {
		condition.lr(column, value);
		return this;
	}

	public Wrapper<T> ll(MFunction<T, ?> column, Object value) {
		condition.ll(column, value);
		return this;
	}

	public Wrapper<T> lk(MFunction<T, ?> column, Object value) {
		condition.lk(column, value);
		return this;
	}

	public Wrapper<T> in(MFunction<T, ?> column, Object value) {
		condition.in(column, value);
		return this;
	}

	public Wrapper<T> ne(MFunction<T, ?> column, Object value) {
		condition.ne(column, value);
		return this;
	}

	public Wrapper<T> ge(MFunction<T, ?> column, Object value) {
		condition.ge(column, value);
		return this;
	}

	public Wrapper<T> gt(MFunction<T, ?> column, Object value) {
		condition.gt(column, value);
		return this;
	}

	public Wrapper<T> le(MFunction<T, ?> column, Object value) {
		condition.le(column, value);
		return this;
	}

	public Wrapper<T> and(Condition children) {
		Condition and = condition.addChildren(DbBuildEnum.and.build());
		and.addChildren(children);
		return this;
	}

	public Wrapper<T> or(Condition children) {
		Condition or = condition.addChildren(DbBuildEnum.or.build());
		or.addChildren(children);
		return this;
	}

}
