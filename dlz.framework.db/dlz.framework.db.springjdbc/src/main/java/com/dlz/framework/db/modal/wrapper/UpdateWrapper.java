package com.dlz.framework.db.modal.wrapper;

import com.dlz.comm.util.VAL;
import com.dlz.framework.db.helper.util.DbNameUtil;
import com.dlz.framework.db.holder.DBHolder;
import com.dlz.framework.db.modal.condition.Condition;
import com.dlz.framework.db.modal.condition.ICondAddByLamda;
import com.dlz.framework.db.modal.condition.ICondAuto;
import com.dlz.framework.db.modal.condition.ICondition;
import com.dlz.framework.db.modal.map.ParaMapUpdate;
import com.dlz.framework.util.system.MFunction;
import com.dlz.framework.util.system.FieldReflections;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * 更新语句生成器
 * 
 * @author dk
 *
 */
public class UpdateWrapper<T> extends AWrapper<T> implements ICondition<UpdateWrapper<T>>, ICondAuto<UpdateWrapper<T>> , ICondAddByLamda<UpdateWrapper<T>,T> {
	private T conditionBean;
	private final ParaMapUpdate pm;

	public static <T> UpdateWrapper<T> wrapper(T conditionBean) {
		return new UpdateWrapper(conditionBean);
	}
	public static <T> UpdateWrapper<T> wrapper(Class<T> beanClass) {
		return new UpdateWrapper(beanClass);
	}

	public UpdateWrapper(Class<T> beanClass) {
		super(beanClass);
		pm = new ParaMapUpdate(getTableName());
	}
	public UpdateWrapper(T conditionBean) {
		super((Class<T>) conditionBean.getClass());
		pm = new ParaMapUpdate(getTableName());
		this.conditionBean = conditionBean;
	}

	public UpdateWrapper<T> set(MFunction<T, ?> column, Object value) {
		pm.set(column,value);
		return this;
	}
	public UpdateWrapper<T> set(String column, Object value) {
		pm.set(column,value);
		return this;
	}
	public UpdateWrapper<T> set(Map<String,Object> setValues) {
		pm.set(setValues);
		return this;
	}
	public UpdateWrapper<T> set(T bean) {
		List<Field> fields = FieldReflections.getFields(bean.getClass());
		for (Field field : fields) {
			Object fieldValue = FieldReflections.getValue(bean, field);
			if(fieldValue!=null){
				pm.set(DbNameUtil.getDbClumnName(field),fieldValue);
			}
		}
		return this;
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
	public UpdateWrapper<T> me() {
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
		return DBHolder.doDb(s->s.excute(this));
	}
}
