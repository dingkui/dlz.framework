package com.dlz.framework.db.modal.wrapper;

import com.dlz.comm.util.VAL;
import com.dlz.comm.util.encry.TraceUtil;
import com.dlz.framework.db.enums.DbBuildEnum;
import com.dlz.framework.db.helper.util.DbNameUtil;
import com.dlz.framework.db.holder.ServiceHolder;
import com.dlz.framework.db.holder.SqlHolder;
import com.dlz.framework.db.modal.condition.*;
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
public class QueryWrapper<T> extends AWrapper<T>
		implements ICondition<QueryWrapper<T>>, ICondAuto<QueryWrapper<T>>, IQueryPageLamda<QueryWrapper<T>,T> {
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
		super(beanClass);
		pm = new ParaMapSearch(getTableName());
	}
	public QueryWrapper(T bean) {
		super((Class<T>) bean.getClass());
		pm = new ParaMapSearch(getTableName());
		this.bean = bean;
	}
	@Override
	protected void wrapValue(String columnName, Object value) {
		condition.eq(columnName, value);
	}
	@Override
	public VAL<String,Object[]> jdbcSql(boolean cnt) {
		generatWithBean(bean);
		pm.where(condition);
		if(cnt){
			return pm.jdbcCnt();
		}
		if(pm.getPage()!=null && pm.getPage().getCurrent()==0){
			return pm.jdbcSql();
		}
		return pm.jdbcPage();
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
		return ServiceHolder.doDb(s->s.getBean(this,true));
	}
	public List<T> queryBeanList() {
		return ServiceHolder.doDb(s->s.getBeanList(this));
	}
	public Page<T> queryPage() {
		return ServiceHolder.doDb(s->s.getPage(this));
	}
}
