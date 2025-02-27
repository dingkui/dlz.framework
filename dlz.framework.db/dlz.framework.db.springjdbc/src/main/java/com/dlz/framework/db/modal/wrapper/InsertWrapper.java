package com.dlz.framework.db.modal.wrapper;

import com.dlz.framework.db.holder.DBHolder;
import com.dlz.framework.db.modal.map.ParaJDBC;
import com.dlz.framework.db.modal.map.ParaMapInsert;

/**
 * 插入语句生成器
 * 
 * @author dk
 *
 */
public class InsertWrapper<T> extends AWrapper<T>{
	private T valueBean;
	private final ParaMapInsert pm;

	public static <T> InsertWrapper<T> wrapper(T valueBean) {
		return new InsertWrapper(valueBean);
	}

	public InsertWrapper(T valueBean) {
		super((Class<T>) valueBean.getClass());
		pm = new ParaMapInsert(getTableName());
		this.valueBean = valueBean;
	}

	@Override
	protected void wrapValue(String columnName, Object value) {
		pm.value(columnName,value);
	}

	@Override
	public ParaJDBC buildSql(boolean cnt) {
		generatWithBean(valueBean);
		return pm.jdbcSql();
	}

	/**
	 * 执行插入，自动生成主键，返回生成的主键
	 * @return
	 */
	public Long insertWithAutoKey() {
		return DBHolder.doDb(s->s.insertWithAutoKey(this));
	}

	/**
	 * 执行插入
	 * @return
	 */
	public int excute() {
		return DBHolder.doDb(s->s.excute(this));
	}
}
