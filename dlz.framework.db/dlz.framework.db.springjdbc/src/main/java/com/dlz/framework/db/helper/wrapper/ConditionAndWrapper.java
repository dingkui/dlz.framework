package com.dlz.framework.db.helper.wrapper;

import com.dlz.framework.util.system.Reflections;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;

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
	public <T, R> ConditionAndWrapper eq(Function<T, R> column, Object params) {
		super.eq(Reflections.getFieldName(column), params);
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
	public <T, R> ConditionAndWrapper ne(Function<T, R> column, Object params) {
		super.ne(Reflections.getFieldName(column), params);
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
	public <T, R> ConditionAndWrapper lt(Function<T, R> column, Object params) {
		super.lt(Reflections.getFieldName(column), params);
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
	public <T, R> ConditionAndWrapper lte(Function<T, R> column, Object params) {
		super.lte(Reflections.getFieldName(column), params);
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
	public <T, R> ConditionAndWrapper gt(Function<T, R> column, Object params) {
		super.gt(Reflections.getFieldName(column), params);
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
	public <T, R> ConditionAndWrapper gte(Function<T, R> column, Object params) {
		super.gte(Reflections.getFieldName(column), params);
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
	public <T, R> ConditionAndWrapper like(Function<T, R> column, String params) {
		super.like(Reflections.getFieldName(column), params);
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
	public <T, R> ConditionAndWrapper in(Function<T, R> column, Collection<?> params) {
		super.in(Reflections.getFieldName(column), params);
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
	public <T, R> ConditionAndWrapper in(Function<T, R> column, Object[] params) {
		super.in(Reflections.getFieldName(column), Arrays.asList(params));
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
	public <T, R> ConditionAndWrapper nin(Function<T, R> column, Collection<?> params) {
		super.nin(Reflections.getFieldName(column), params);
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
	public <T, R> ConditionAndWrapper nin(Function<T, R> column, Object[] params) {
		super.nin(Reflections.getFieldName(column), Arrays.asList(params));
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
	public <T, R> ConditionAndWrapper isNull(Function<T, R> column) {
		super.isNull(Reflections.getFieldName(column));
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
	public <T, R> ConditionAndWrapper isNotNull(Function<T, R> column) {
		super.isNotNull(Reflections.getFieldName(column));
		return this;
	}
}
