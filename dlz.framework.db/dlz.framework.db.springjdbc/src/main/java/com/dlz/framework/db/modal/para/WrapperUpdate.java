package com.dlz.framework.db.modal.para;

import com.dlz.comm.fn.DlzFn;
import com.dlz.comm.util.system.FieldReflections;
import com.dlz.framework.db.holder.BeanInfoHolder;
import com.dlz.framework.db.holder.DBHolder;
import com.dlz.framework.db.inf.IOperatorExec;
import com.dlz.framework.db.inf.ISqlWrapperQuery;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 更新语句生成器
 * 
 * @author dk
 *
 */
public class WrapperUpdate<T> extends AWrapperQuery<WrapperUpdate<T>,T, TableUpdate> implements
        ISqlWrapperQuery<WrapperUpdate<T>, T>,
		IOperatorExec {
	public static <T> WrapperUpdate<T> wrapper(Class<T> beanClass) {
		return new WrapperUpdate(beanClass);
	}

	private WrapperUpdate(Class<T> beanClass) {
		super(beanClass);
		setPm(new TableUpdate(getTableName()));
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
	public WrapperUpdate<T> set(T bean, Function<String,Boolean> ignore) {
		List<Field> fields = FieldReflections.getFields(bean.getClass());
		for (Field field : fields) {
			Object fieldValue = FieldReflections.getValue(bean, field);
			if(fieldValue!=null){
				final String columnName = BeanInfoHolder.getColumnName(field);
				if(ignore!=null && ignore.apply(columnName)){
					continue;
				}
				getPm().set(columnName,fieldValue);
			}
		}
		return this;
	}
	public WrapperUpdate<T> set(T bean) {
		return set(bean, name->name.equalsIgnoreCase("ID"));
	}
	@Override
	public WrapperUpdate<T> me() {
		return this;
	}

	public boolean batch(List<T> valueBeans){
		return batch(valueBeans,1000);
	}
	public boolean batch(List<T> valueBeans,int batchSize){
		String dbName = BeanInfoHolder.getTableName(getBeanClass());
		final List<Field> fields = BeanInfoHolder.getBeanFields(getBeanClass());
		String sql = TableMakerUtil.buildUpdateSql(dbName, fields);
		while (valueBeans.size()>0 && batchSize>0){
			if(batchSize>valueBeans.size()){
				batchSize=valueBeans.size();
			}
			final List<T> ts = valueBeans.subList(0, batchSize);

			List<Object[]> paramValues = ts.stream()
					.map(v-> TableMakerUtil.buildUpdateParams(v,fields))
					.collect(Collectors.toList());
			DBHolder.getDao().batchUpdate(sql, paramValues);
			valueBeans = valueBeans.subList(batchSize, valueBeans.size());
		}
		return true;
	}
}
