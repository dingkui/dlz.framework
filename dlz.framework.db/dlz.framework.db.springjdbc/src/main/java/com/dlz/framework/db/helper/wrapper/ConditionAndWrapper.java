package com.dlz.framework.db.helper.wrapper;

import com.dlz.comm.util.system.FieldReflections;
import com.dlz.comm.util.system.MFunction;

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
	public <T, R> ConditionAndWrapper eq(MFunction<T, R> column, Object params) {
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
	public <T, R> ConditionAndWrapper ne(MFunction<T, R> column, Object params) {
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
	public <T, R> ConditionAndWrapper lt(MFunction<T, R> column, Object params) {
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
	public <T, R> ConditionAndWrapper lte(MFunction<T, R> column, Object params) {
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
	public <T, R> ConditionAndWrapper gt(MFunction<T, R> column, Object params) {
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
	public <T, R> ConditionAndWrapper gte(MFunction<T, R> column, Object params) {
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
	public <T, R> ConditionAndWrapper like(MFunction<T, R> column, String params) {
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
	public <T, R> ConditionAndWrapper in(MFunction<T, R> column, Collection<?> params) {
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
	public <T, R> ConditionAndWrapper in(MFunction<T, R> column, Object[] params) {
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
	public <T, R> ConditionAndWrapper nin(MFunction<T, R> column, Collection<?> params) {
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
	public <T, R> ConditionAndWrapper nin(MFunction<T, R> column, Object[] params) {
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
	public <T, R> ConditionAndWrapper isNull(MFunction<T, R> column) {
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
	public <T, R> ConditionAndWrapper isNotNull(MFunction<T, R> column) {
		super.isNotNull(FieldReflections.getFieldName(column));
		return this;
	}
}
