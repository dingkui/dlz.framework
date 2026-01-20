package com.dlz.comm.exception;

import com.dlz.comm.util.StringUtils;

import java.util.function.Supplier;

/**
 * 验证异常类
 * 
 * 继承自BaseException，用于处理验证相关的异常
 * 
 * @author dingkui
 * @since 2023
 */
public class ValidateException extends BaseException {
	/**
	 * 序列化版本UID
	 */
	private static final long serialVersionUID = -5345825923487658213L;
	
	/**
	 * 默认错误码
	 */
	private static int DEFUALT_ERROR_CODE = 3003;

	/**
	 * 构造函数，使用指定消息
	 * 
	 * @param message 异常消息
	 */
	public ValidateException(String message) {
		super(DEFUALT_ERROR_CODE, message);
	}
	
	/**
	 * 构建验证异常实例
	 * 
	 * @param message 异常消息
	 * @return 验证异常实例
	 */
	public static ValidateException build(String message) {
		return new ValidateException(message);
	}

	/**
	 * 断言布尔表达式为真
	 * 
	 * 如果表达式为假则抛出异常
	 *
	 * @param expression 布尔表达式
	 * @param message 异常消息
	 */
	public static void isTrue(boolean expression, String message) {
		if (!expression) {
			throw new ValidateException(message);
		}
	}
	
	/**
	 * 断言布尔表达式为真
	 * 
	 * 如果表达式为假则抛出异常
	 *
	 * @param expression 布尔表达式
	 * @param c 异常消息提供者
	 */
	public static void isTrue(boolean expression, Supplier<String> c) {
		if (!expression) {
			throw new ValidateException(c.get());
		}
	}

	/**
	 * 断言对象不为null
	 * 
	 * 如果对象为null则抛出异常
	 *
	 * @param object 待检查的对象
	 * @param message 异常消息
	 */
	public static void notNull(Object object, String message) {
		isTrue(object != null, message);
	}

	/**
	 * 断言对象不为null
	 * 
	 * 如果对象为null则抛出异常
	 *
	 * @param object 待检查的对象
	 * @param c 异常消息提供者
	 */
	public static void notNull(Object object, Supplier<String> c) {
		isTrue(object != null, c);
	}

	/**
	 * 断言对象不为空
	 * 
	 * 如果对象为空则抛出异常
	 *
	 * @param value 待检查的对象
	 * @param message 异常消息
	 */
	public static void notEmpty(Object value, String message) {
		isTrue(!StringUtils.isEmpty(value), message);
	}
	
	/**
	 * 断言对象不为空
	 * 
	 * 如果对象为空则抛出异常
	 *
	 * @param value 待检查的对象
	 * @param c 异常消息提供者
	 */
	public static void notEmpty(Object value, Supplier<String> c) {
		isTrue(!StringUtils.isEmpty(value), c);
	}
}