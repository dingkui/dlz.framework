package com.dlz.framework.db.modal.wrapper;

import com.dlz.comm.util.VAL;
import com.dlz.framework.db.enums.DbBuildEnum;
import com.dlz.framework.db.helper.util.DbNameUtil;
import com.dlz.framework.db.holder.ServiceHolder;
import com.dlz.framework.db.modal.condition.Condition;
import com.dlz.framework.db.modal.condition.ICondAddByLamda;
import com.dlz.framework.db.modal.condition.ICondAuto;
import com.dlz.framework.db.modal.condition.ICondition;
import com.dlz.framework.db.modal.map.ParaMapUpdate;
import com.dlz.framework.util.system.MFunction;
import com.dlz.framework.util.system.Reflections;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * 更新语句生成器
 * 
 * @author dk
 *
 */
public class UpdateWrapper<T> extends AWrapper<T> implements ICondition<UpdateWrapper<T>>, ICondAuto<UpdateWrapper<T>> , ICondAddByLamda<UpdateWrapper<T>,T> {
	private T condtion;
	ParaMapUpdate pm;
	Condition condition = DbBuildEnum.where.build();

	public static <T> UpdateWrapper<T> wrapper(T condtion) {
		return new UpdateWrapper(condtion);
	}
	public static <T> UpdateWrapper<T> wrapper(Class<T> beanClass) {
		return new UpdateWrapper(beanClass);
	}

	public UpdateWrapper(Class<T> beanClass) {
		super(beanClass);
		pm = new ParaMapUpdate(getTableName());
	}
	public UpdateWrapper(T condtion) {
		super((Class<T>) condtion.getClass());
		pm = new ParaMapUpdate(getTableName());
		this.condtion = condtion;
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
		Field[] fields = Reflections.getFields(bean.getClass());
		for (Field field : fields) {
			Object fieldValue = Reflections.getFieldValue(bean, field.getName());
			if(fieldValue!=null){
				pm.set(DbNameUtil.getDbClumnName(field),fieldValue);
			}
		}
		return this;
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
	public UpdateWrapper<T> mine() {
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
