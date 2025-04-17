package com.dlz.framework.db.modal.para;

import com.dlz.comm.util.system.FieldReflections;
import com.dlz.framework.db.holder.DBHolder;
import com.dlz.framework.db.inf.IOperatorInsert;
import com.dlz.framework.db.modal.DbInfoCache;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

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
		String dbName = DbInfoCache.getTableName(getBeanClass());
		final List<Field> fields = DbInfoCache.getTableFields(getBeanClass());
		String sql = MakerUtil.buildInsertSql(dbName, fields);

		List<Object[]> paramValues = new ArrayList<>();
		for (Object object : valueBeans) {
			List<Object> params = new ArrayList<Object>();
			for (Field field : fields) {
				params.add(FieldReflections.getValue(object, field));
			}
			paramValues.add(params.toArray());
		}
		DBHolder.getService().getDao().batchUpdate(sql, paramValues);
		return true;
	}
}
