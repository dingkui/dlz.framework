package com.dlz.framework.db.modal.wrapper;

import com.dlz.comm.util.VAL;
import com.dlz.framework.db.enums.DbBuildEnum;
import com.dlz.framework.db.helper.util.DbNameUtil;
import com.dlz.framework.db.holder.ServiceHolder;
import com.dlz.framework.db.modal.condition.Condition;
import com.dlz.framework.db.modal.condition.ICondAuto;
import com.dlz.framework.db.modal.condition.ICondition;
import com.dlz.framework.db.modal.map.ParaMapDelete;
import com.dlz.framework.db.modal.map.ParaMapUpdate;
import com.dlz.framework.util.system.MFunction;
import com.dlz.framework.util.system.Reflections;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * 删除语句生成器
 * 
 * @author dk
 *
 */
public class DeleteWrapper<T> extends AWrapper<T> implements ICondition<DeleteWrapper<T>>, ICondAuto<DeleteWrapper<T>> {
	private T condtion;
	ParaMapDelete pm;
	Condition condition = DbBuildEnum.where.build();

	public static <T> DeleteWrapper<T> wrapper(T condtion) {
		return new DeleteWrapper(condtion);
	}
	public static <T> DeleteWrapper<T> wrapper(Class<T> beanClass) {
		return new DeleteWrapper(beanClass);
	}

	public DeleteWrapper(Class<T> beanClass) {
		super(beanClass);
		pm = new ParaMapDelete(getTableName());
	}
	public DeleteWrapper(T condtion) {
		super((Class<T>) condtion.getClass());
		pm = new ParaMapDelete(getTableName());
		this.condtion = condtion;
	}

	@Override
	protected void wrapValue(String columnName, Object value) {
		condition.eq(columnName, value);
	}
	@Override
	public VAL<String,Object[]> jdbcSql(boolean cnt) {
		generatWithBean(condtion);
		pm.where(condition);
		if(pm.getPage()!=null && pm.getPage().getCurrent()==0){
			return pm.jdbcSql();
		}
		return pm.jdbcPage();
	}

	@Override
	public DeleteWrapper<T> mine() {
		return this;
	}

	@Override
	public void addChildren(Condition child) {
		condition.addChildren(child);
	}

	/**
	 * 执行插入，返回主键
	 * @return
	 */
	public int excute() {
		return ServiceHolder.doDb(s->s.excute(this));
	}
}
