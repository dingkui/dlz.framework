package com.dlz.framework.db.modal.wrapper;

import com.dlz.comm.util.VAL;
import com.dlz.framework.db.holder.ServiceHolder;
import com.dlz.framework.db.modal.condition.Condition;
import com.dlz.framework.db.modal.condition.ICondAddByLamda;
import com.dlz.framework.db.modal.condition.ICondAuto;
import com.dlz.framework.db.modal.condition.ICondition;
import com.dlz.framework.db.modal.map.ParaMapDelete;

/**
 * 删除语句生成器
 * 
 * @author dk
 *
 */
public class DeleteWrapper<T> extends AWrapper<T> implements ICondition<DeleteWrapper<T>>, ICondAuto<DeleteWrapper<T>>, ICondAddByLamda<DeleteWrapper<T>,T>{
	private T conditionBean;
	private final ParaMapDelete pm;

	public static <T> DeleteWrapper<T> wrapper(T conditionBean) {
		return new DeleteWrapper(conditionBean);
	}
	public static <T> DeleteWrapper<T> wrapper(Class<T> beanClass) {
		return new DeleteWrapper(beanClass);
	}

	public DeleteWrapper(Class<T> beanClass) {
		super(beanClass);
		pm = new ParaMapDelete(getTableName());
	}
	public DeleteWrapper(T conditionBean) {
		super((Class<T>) conditionBean.getClass());
		pm = new ParaMapDelete(getTableName());
		this.conditionBean = conditionBean;
	}

	@Override
	protected void wrapValue(String columnName, Object value) {
		pm.eq(columnName, value);
	}
	@Override
	public VAL<String,Object[]> buildSql(boolean cnt) {
		generatWithBean(conditionBean);
		return pm.jdbcSql();
	}

	@Override
	public DeleteWrapper<T> mine() {
		return this;
	}

	@Override
	public void addChildren(Condition child) {
		pm.addChildren(child);
	}

	/**
	 * 执行插入，返回主键
	 * @return
	 */
	public int excute() {
		return ServiceHolder.doDb(s->s.excute(this));
	}
}
