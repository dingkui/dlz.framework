package com.dlz.framework.db.modal.para;

import com.dlz.framework.db.holder.DBHolder;
import com.dlz.framework.db.inf.IOperatorInsert;
import com.dlz.framework.db.holder.BeanInfoHolder;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 插入语句生成器
 * 
 * @author dk
 *
 */
public class WrapperInsert<T> extends AWrapper<T, MakerInsert> implements IOperatorInsert {
	public static <T> WrapperInsert<T> wrapper(T valueBean) {
		return new WrapperInsert(valueBean);
	}

	public WrapperInsert(T valueBean) {
		super(valueBean);
		setPm(new MakerInsert(getTableName()));
	}

	@Override
	protected void wrapValue(String columnName, Object value) {
		getPm().value(columnName,value);
	}

	public boolean batch(List<T> valueBeans){
		String dbName = BeanInfoHolder.getTableName(getBeanClass());
		final List<Field> fields = BeanInfoHolder.getBeanFields(getBeanClass());
		String sql = MakerUtil.buildInsertSql(dbName, fields);
		List<Object[]> paramValues = valueBeans.stream()
				.map(v->MakerUtil.buildInsertParams(v,fields))
				.collect(Collectors.toList());
		DBHolder.getService().getDao().batchUpdate(sql, paramValues);
		return true;
	}
}
