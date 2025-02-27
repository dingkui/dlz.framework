package com.dlz.comm.util.encry;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.security.SecureRandom;

/**
 * 生成的随机数类型
 *
 * @author dk
 */
@Getter
@RequiredArgsConstructor
public enum RandomEnum {
	/**
	 * INT STRING ALL
	 */
	INT(RandomEnum.INT_STR),
	STRING(RandomEnum.STR_STR),
	ALL(RandomEnum.INT_STR + RandomEnum.STR_STR);

	private final String factor;

	/**
	 * 随机字符串因子
	 */
	private static final String INT_STR = "0123456789";
	private static final String STR_STR = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	/**
	 * SECURE_RANDOM
	 */
	public final static SecureRandom SECURE_RANDOM = new SecureRandom();
	/**
	 * 随机数生成
	 * @param count      字符长度
	 * @return 随机数
	 */
	public String random(int count) {
		if (count <= 0) {
			return  "";
		}
		char[] buffer = new char[count];
		for (int i = 0; i < count; i++) {
			buffer[i] = factor.charAt(SECURE_RANDOM.nextInt(factor.length()));
		}
		return new String(buffer);
	}
}
