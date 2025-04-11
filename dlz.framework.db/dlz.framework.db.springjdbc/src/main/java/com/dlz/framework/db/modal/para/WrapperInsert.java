package com.dlz.framework.db.modal.para;

import com.dlz.framework.db.inf.IOperatorInsert;

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
}
