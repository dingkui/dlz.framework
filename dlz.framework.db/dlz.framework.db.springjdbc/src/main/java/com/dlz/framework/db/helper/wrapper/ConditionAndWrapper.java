package com.dlz.framework.db.helper.wrapper;

import com.dlz.comm.fn.DlzFn;
import com.dlz.comm.util.system.FieldReflections;

import java.util.Arrays;
import java.util.Collection;

/**
 * 查询语句生成器 AND连接
 *
 */
public class ConditionAndWrapper extends ConditionWrapper {

	public ConditionAndWrapper() {
		andLink = true;
	}

	public ConditionAndWrapper and(ConditionWrapper conditionWrapper) {
		list.add(conditionWrapper);
		return this;
	}

	/**
	 * 等于
	 * 
	 * @param column 字段
	 * @param params 参数
	 * @return ConditionWrapper
	 */
	public ConditionAndWrapper eq(String column, Object params) {
		super.eq(column, params);
		return this;
	}

	/**
	 * 等于
	 * 
	 * @param column 字段
	 * @param params 参数
	 * @return ConditionWrapper
	 */
	public <T, R> ConditionAndWrapper eq(DlzFn<T, R> column, Object params) {
		super.eq(FieldReflections.getFieldName(column), params);
		return this;
	}

	/**
	 * 不等于
	 * 
	 * @param column 字段
	 * @param params 参数
	 * @return ConditionAndWrapper
	 */
	public ConditionAndWrapper ne(String column, Object params) {
		super.ne(column, params);
		return this;
	}

	/**
	 * 不等于
	 * 
	 * @param column 字段
	 * @param params 参数
	 * @return ConditionAndWrapper
	 */
	public <T, R> ConditionAndWrapper ne(DlzFn<T, R> column, Object params) {
		super.ne(FieldReflections.getFieldName(column), params);
		return this;
	}

	/**
	 * 小于
	 * 
	 * @param column 字段
	 * @param params 参数
	 * @return ConditionAndWrapper
	 */
	public ConditionAndWrapper lt(String column, Object params) {
		super.lt(column, params);
		return this;
	}

	/**
	 * 小于
	 * 
	 * @param column 字段
	 * @param params 参数
	 * @return ConditionAndWrapper
	 */
	public <T, R> ConditionAndWrapper lt(DlzFn<T, R> column, Object params) {
		super.lt(FieldReflections.getFieldName(column), params);
		return this;
	}

	/**
	 * 小于或等于
	 * 
	 * @param column 字段
	 * @param params 参数
	 * @return ConditionAndWrapper
	 */
	public ConditionAndWrapper lte(String column, Object params) {
		super.lte(column, params);
		return this;
	}

	/**
	 * 小于或等于
	 * 
	 * @param column 字段
	 * @param params 参数
	 * @return ConditionAndWrapper
	 */
	public <T, R> ConditionAndWrapper lte(DlzFn<T, R> column, Object params) {
		super.lte(FieldReflections.getFieldName(column), params);
		return this;
	}

	/**
	 * 大于
	 * 
	 * @param column 字段
	 * @param params 参数
	 * @return ConditionAndWrapper
	 */
	public ConditionAndWrapper gt(String column, Object params) {
		super.gt(column, params);
		return this;
	}

	/**
	 * 大于
	 * 
	 * @param column 字段
	 * @param params 参数
	 * @return ConditionAndWrapper
	 */
	public <T, R> ConditionAndWrapper gt(DlzFn<T, R> column, Object params) {
		super.gt(FieldReflections.getFieldName(column), params);
		return this;
	}

	/**
	 * 大于或等于
	 * 
	 * @param column 字段
	 * @param params 参数
	 * @return ConditionAndWrapper
	 */
	public ConditionAndWrapper gte(String column, Object params) {
		super.gte(column, params);
		return this;
	}

	/**
	 * 大于或等于
	 * 
	 * @param column 字段
	 * @param params 参数
	 * @return ConditionAndWrapper
	 */
	public <T, R> ConditionAndWrapper gte(DlzFn<T, R> column, Object params) {
		super.gte(FieldReflections.getFieldName(column), params);
		return this;
	}

	/**
	 * 相似于
	 * 
	 * @param column 字段
	 * @param params 参数
	 * @return ConditionAndWrapper
	 */
	public ConditionAndWrapper like(String column, String params) {
		super.like(column, params);
		return this;
	}

	/**
	 * 相似于
	 * 
	 * @param column 字段
	 * @param params 参数
	 * @return ConditionAndWrapper
	 */
	public <T, R> ConditionAndWrapper like(DlzFn<T, R> column, String params) {
		super.like(FieldReflections.getFieldName(column), params);
		return this;
	}

	/**
	 * 在其中
	 * 
	 * @param column 字段
	 * @param params 参数
	 * @return ConditionAndWrapper
	 */
	public ConditionAndWrapper in(String column, Collection<?> params) {
		super.in(column, params);
		return this;
	}

	/**
	 * 在其中
	 * 
	 * @param column 字段
	 * @param params 参数
	 * @return ConditionAndWrapper
	 */
	public <T, R> ConditionAndWrapper in(DlzFn<T, R> column, Collection<?> params) {
		super.in(FieldReflections.getFieldName(column), params);
		return this;
	}

	/**
	 * 在其中
	 * 
	 * @param column 字段
	 * @param params 参数
	 * @return ConditionAndWrapper
	 */
	public <T, R> ConditionAndWrapper in(String column, Object[] params) {
		super.in(column, Arrays.asList(params));
		return this;
	}

	/**
	 * 在其中
	 * 
	 * @param column 字段
	 * @param params 参数
	 * @return ConditionAndWrapper
	 */
	public <T, R> ConditionAndWrapper in(DlzFn<T, R> column, Object[] params) {
		super.in(FieldReflections.getFieldName(column), Arrays.asList(params));
		return this;
	}

	/**
	 * 不在其中
	 * 
	 * @param column 字段
	 * @param params 参数
	 * @return ConditionAndWrapper
	 */
	public ConditionAndWrapper nin(String column, Collection<?> params) {
		super.nin(column, params);
		return this;
	}

	/**
	 * 不在其中
	 * 
	 * @param column 字段
	 * @param params 参数
	 * @return ConditionAndWrapper
	 */
	public <T, R> ConditionAndWrapper nin(DlzFn<T, R> column, Collection<?> params) {
		super.nin(FieldReflections.getFieldName(column), params);
		return this;
	}

	/**
	 * 不在其中
	 * 
	 * @param column 字段
	 * @param params 参数
	 * @return ConditionAndWrapper
	 */
	public ConditionAndWrapper nin(String column, Object[] params) {
		super.nin(column, Arrays.asList(params));
		return this;
	}

	/**
	 * 不在其中
	 * 
	 * @param column 字段
	 * @param params 参数
	 * @return ConditionAndWrapper
	 */
	public <T, R> ConditionAndWrapper nin(DlzFn<T, R> column, Object[] params) {
		super.nin(FieldReflections.getFieldName(column), Arrays.asList(params));
		return this;
	}

	/**
	 * 为空
	 * 
	 * @param column 字段
	 * @return ConditionAndWrapper
	 */
	public ConditionAndWrapper isNull(String column) {
		super.isNull(column);
		return this;
	}

	/**
	 * 为空
	 * 
	 * @param <T>
	 * 
	 * @param column 字段
	 * @return ConditionAndWrapper
	 */
	public <T, R> ConditionAndWrapper isNull(DlzFn<T, R> column) {
		super.isNull(FieldReflections.getFieldName(column));
		return this;
	}

	/**
	 * 不为空
	 * 
	 * @param column 字段
	 * @return ConditionAndWrapper
	 */
	public ConditionAndWrapper isNotNull(String column) {
		super.isNotNull(column);
		return this;
	}

	/**
	 * 不为空
	 * 
	 * @param <T>
	 * 
	 * @param column 字段
	 * @return ConditionAndWrapper
	 */
	public <T, R> ConditionAndWrapper isNotNull(DlzFn<T, R> column) {
		super.isNotNull(FieldReflections.getFieldName(column));
		return this;
	}
}
