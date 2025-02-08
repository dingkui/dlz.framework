package com.dlz.framework.db.modal.wrapper;

import com.dlz.comm.util.VAL;
import com.dlz.framework.db.enums.DbBuildEnum;
import com.dlz.framework.db.helper.util.DbNameUtil;
import com.dlz.framework.db.holder.ServiceHolder;
import com.dlz.framework.db.modal.condition.Condition;
import com.dlz.framework.db.modal.condition.ICondAuto;
import com.dlz.framework.db.modal.condition.ICondition;
import com.dlz.framework.db.modal.condition.IQueryPage;
import com.dlz.framework.db.modal.map.ParaMapSearch;
import com.dlz.framework.db.modal.result.Page;
import com.dlz.framework.util.system.Reflections;

import java.lang.reflect.Field;
import java.util.List;

/**
 * 查询语句生成器
 * 
 * @author dk
 *
 */
public class QueryWrapper<T> implements ICondition<QueryWrapper<T>>, ICondAuto<QueryWrapper<T>>, IQueryPage<QueryWrapper<T>> {
	private Class<T> beanClass;
	private boolean isGenerator = false;
	private T bean;
	ParaMapSearch pm;
	Condition condition = DbBuildEnum.where.build();

	public static <T> QueryWrapper<T> wrapper(T bean) {
		return new QueryWrapper(bean);
	}
	public static <T> QueryWrapper<T> wrapper(Class<T> beanClass) {
		return new QueryWrapper(beanClass);
	}

	public QueryWrapper(Class<T> beanClass) {
		this.beanClass = beanClass;
		pm = new ParaMapSearch(DbNameUtil.getDbTableName(beanClass));
	}
	public QueryWrapper(T bean) {
		this.beanClass = (Class<T>) bean.getClass();
		pm = new ParaMapSearch(DbNameUtil.getDbTableName(beanClass));
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


	public Class<T> getBeanClass() {
		return beanClass;
	}

	@Override
	public QueryWrapper<T> mine() {
		return this;
	}

	@Override
	public Page getPage() {
		return pm.getPage();
	}
	@Override
	public QueryWrapper<T> page(Page page) {
		pm.setPage(page);
		return this;
	}
	@Override
	public void addChildren(Condition child) {
		condition.addChildren(child);
	}

	public T queryBean() {
		return ServiceHolder.getService().getBean(this,true);
	}
	public List<T> queryBeanList() {
		return ServiceHolder.getService().getBeanList(this);
	}
	public Page<T> queryPage() {
		return ServiceHolder.getService().getPage(this);
	}
}
