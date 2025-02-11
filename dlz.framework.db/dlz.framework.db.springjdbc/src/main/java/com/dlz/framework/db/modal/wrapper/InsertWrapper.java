package com.dlz.framework.db.modal.wrapper;

import com.dlz.comm.exception.DbException;
import com.dlz.comm.util.VAL;
import com.dlz.framework.db.enums.DbBuildEnum;
import com.dlz.framework.db.helper.util.DbNameUtil;
import com.dlz.framework.db.holder.ServiceHolder;
import com.dlz.framework.db.modal.condition.Condition;
import com.dlz.framework.db.modal.condition.ICondAuto;
import com.dlz.framework.db.modal.condition.ICondition;
import com.dlz.framework.db.modal.condition.IQueryPage;
import com.dlz.framework.db.modal.map.ParaMapInsert;
import com.dlz.framework.db.modal.map.ParaMapSearch;
import com.dlz.framework.db.modal.result.Page;
import com.dlz.framework.util.system.Reflections;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;

/**
 * 插入语句生成器
 * 
 * @author dk
 *
 */
public class InsertWrapper<T> extends AWrapper<T>{
	private T bean;
	ParaMapInsert pm;

	public static <T> InsertWrapper<T> wrapper(T bean) {
		return new InsertWrapper(bean);
	}

	public InsertWrapper(T bean) {
		super((Class<T>) bean.getClass());
		pm = new ParaMapInsert(getTableName());
		this.bean = bean;
	}

	@Override
	protected void wrapValue(String columnName, Object value) {
		pm.value(columnName,value);
	}

	@Override
	public VAL<String,Object[]> jdbcSql(boolean cnt) {
		generatWithBean(bean);
		return pm.jdbcSql();
	}

	/**
	 * 执行插入，返回主键
	 * @return
	 */
	public Long excute() {
		return ServiceHolder.doDb(s->s.insert(this));
	}
}
