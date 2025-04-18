package com.dlz.framework.db.modal.para;

import com.dlz.comm.fn.DlzFn;
import com.dlz.comm.util.system.FieldReflections;
import com.dlz.framework.db.holder.DBHolder;
import com.dlz.framework.db.inf.IOperatorExec;
import com.dlz.framework.db.inf.ISqlWrapperSearch;
import com.dlz.framework.db.holder.BeanInfoHolder;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 更新语句生成器
 * 
 * @author dk
 *
 */
public class WrapperUpdate<T> extends AWrapperSearch<WrapperUpdate<T>,T, MakerUpdate> implements
		ISqlWrapperSearch<WrapperUpdate<T>, T>,
		IOperatorExec {
	public static <T> WrapperUpdate<T> wrapper(T conditionBean) {
		return new WrapperUpdate(conditionBean);
	}
	public static <T> WrapperUpdate<T> wrapper(Class<T> beanClass) {
		return new WrapperUpdate(beanClass);
	}

	public WrapperUpdate(Class<T> beanClass) {
		super(beanClass);
		setPm(new MakerUpdate(getTableName()));
	}
	public WrapperUpdate(T conditionBean) {
		super(conditionBean);
		setPm(new MakerUpdate(getTableName()));
	}

	public WrapperUpdate<T> set(DlzFn<T, ?> column, Object value) {
		getPm().set(column,value);
		return this;
	}
	public WrapperUpdate<T> set(String column, Object value) {
		getPm().set(column,value);
		return this;
	}
	public WrapperUpdate<T> set(Map<String,Object> setValues) {
		getPm().set(setValues);
		return this;
	}
	public WrapperUpdate<T> set(T bean) {
		List<Field> fields = FieldReflections.getFields(bean.getClass());
		for (Field field : fields) {
			Object fieldValue = FieldReflections.getValue(bean, field);
			if(fieldValue!=null){
				getPm().set(BeanInfoHolder.getColumnName(field),fieldValue);
			}
		}
		return this;
	}
	@Override
	public WrapperUpdate<T> me() {
		return this;
	}

	public boolean batch(List<T> valueBeans){
		String dbName = BeanInfoHolder.getTableName(getBeanClass());
		final List<Field> fields = BeanInfoHolder.getBeanFields(getBeanClass());
		String sql = MakerUtil.buildUpdateSql(dbName, fields);
		List<Object[]> paramValues = valueBeans.stream()
				.map(v->MakerUtil.buildUpdateParams(v,fields))
				.collect(Collectors.toList());
		DBHolder.getService().getDao().batchUpdate(sql, paramValues);
		return true;
	}
}
