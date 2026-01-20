package com.dlz.comm.util.encry;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5工具类
 * 
 * 提供MD5哈希值计算功能，支持多种输入类型
 * 
 * @author dingkui
 * @since 2023
 */
public class Md5Util {
	/**
	 * 计算字节数组的MD5哈希值
	 * 
	 * @param input 输入的字节数组
	 * @return MD5哈希值字符串
	 */
	public static String md5(byte[] input) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}	
		return byte2hex(md.digest(input));
	}
	
	/**
	 * 计算字符串的MD5哈希值（使用默认字符集）
	 * 
	 * @param input 输入的字符串
	 * @return MD5哈希值字符串
	 */
	public static String md5(String input) {
		return md5(ByteUtil.getBytes(input));
	}
	
	/**
	 * 计算字符串的MD5哈希值（指定字符集）
	 * 
	 * @param input 输入的字符串
	 * @param charset 字符集
	 * @return MD5哈希值字符串
	 */
	public static String md5(String input, String charset) {
		return md5(ByteUtil.getBytes(input, charset));
	}
	
	/**
	 * 计算文件的MD5哈希值
	 * 
	 * @param file 输入的文件
	 * @return MD5哈希值字符串
	 */
	public static String md5(File file) {
		if (!file.exists()) {
			throw new RuntimeException("文件" + file.getAbsolutePath() + "不存在！");
		}
		return md5(ByteUtil.readBytes(file));
	}
	
	/**
	 * 计算长整型数值的MD5哈希值
	 * 
	 * @param input 输入的长整型数值
	 * @return MD5哈希值字符串
	 */
	public static String md5(long input) {
		return md5(ByteUtil.getBytes(input));
	}
	
	/**
	 * 将字节数组转换为十六进制字符串
	 * 
	 * @param b 待转换的字节数组
	 * @return 十六进制字符串
	 */
	private static String byte2hex(byte[] b) {
		StringBuffer hs = new StringBuffer();
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs.append("0").append(stmp);
			else
				hs.append(stmp);
		}
		return hs.toString();
	}

}