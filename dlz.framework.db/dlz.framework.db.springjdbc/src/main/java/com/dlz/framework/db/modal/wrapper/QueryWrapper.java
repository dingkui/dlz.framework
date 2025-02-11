package com.dlz.framework.db.modal.wrapper;

import com.dlz.comm.util.VAL;
import com.dlz.framework.db.holder.ServiceHolder;
import com.dlz.framework.db.modal.condition.Condition;
import com.dlz.framework.db.modal.condition.ICondAuto;
import com.dlz.framework.db.modal.condition.ICondition;
import com.dlz.framework.db.modal.condition.IQueryPageLamda;
import com.dlz.framework.db.modal.map.ParaMapSearch;
import com.dlz.framework.db.modal.result.Page;

import java.util.List;

/**
 * 查询语句生成器
 * 
 * @author dk
 *
 */
public class QueryWrapper<T> extends AWrapper<T> implements ICondition<QueryWrapper<T>>, ICondAuto<QueryWrapper<T>>, IQueryPageLamda<QueryWrapper<T>,T> {
	private T conditionBean;
	private final ParaMapSearch pm;

	public static <T> QueryWrapper<T> wrapper(T conditionBean) {
		return new QueryWrapper(conditionBean);
	}
	public static <T> QueryWrapper<T> wrapper(Class<T> beanClass) {
		return new QueryWrapper(beanClass);
	}

	public QueryWrapper(Class<T> beanClass) {
		super(beanClass);
		pm = new ParaMapSearch(getTableName());
	}
	public QueryWrapper(T conditionBean) {
		super((Class<T>) conditionBean.getClass());
		pm = new ParaMapSearch(getTableName());
		this.conditionBean = conditionBean;
	}
	@Override
	protected void wrapValue(String columnName, Object value) {
		pm.eq(columnName, value);
	}
	@Override
	public VAL<String,Object[]> buildSql(boolean cnt) {
		generatWithBean(conditionBean);
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
		pm.addChildren(child);
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
