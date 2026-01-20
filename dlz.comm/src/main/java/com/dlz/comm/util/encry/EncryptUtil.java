package com.dlz.comm.util.encry;

import lombok.extern.slf4j.Slf4j;

/**
 * 加密工具类
 * 
 * 提供基于Authcode算法的加密解密功能
 * 
 * @author dk 2017-06-20
 */
@Slf4j
public class EncryptUtil {
	/**
	 * 全局认证密钥
	 */
	public static String GLOBAL_AUTH_KEY = "e317b362fafa0c96c20b8543d054b850";

	/**
	 * 解密字符串
	 * 
	 * @param $string 待解密的字符串
	 * @param $key 密钥，如果为null或空字符串则使用全局密钥
	 * @return 解密后的字符串
	 */
	public static String decode(String $string, String $key) {
		$key = ($key == null || "".equals($key)) ? GLOBAL_AUTH_KEY : $key;
		return EncryptAuthcode.authcode($string, "DECODE", $key, 0);
	}
	
	/**
	 * 解密字符串（使用全局密钥）
	 * 
	 * @param $string 待解密的字符串
	 * @return 解密后的字符串
	 */
	public static String decode(String $string) {
		return decode($string, null);
	}
	
	/**
	 * 加密字符串（使用全局密钥，无过期时间）
	 * 
	 * @param $string 待加密的字符串
	 * @return 加密后的字符串
	 */
	public static String encode(String $string) {
		return encode($string, null, null);
	}
	
	/**
	 * 加密字符串（指定密钥，无过期时间）
	 * 
	 * @param $string 待加密的字符串
	 * @param $key 密钥，如果为null或空字符串则使用全局密钥
	 * @return 加密后的字符串
	 */
	public static String encode(String $string, String $key) {
		return encode($string, $key, null);
	}
	
	/**
	 * 加密字符串（使用全局密钥，指定过期时间）
	 * 
	 * @param $string 待加密的字符串
	 * @param $expiry 过期时间（秒），0表示永不过期
	 * @return 加密后的字符串
	 */
	public static String encode(String $string, Integer $expiry) {
		return encode($string, null, $expiry);
	}
	
	/**
	 * 加密字符串
	 * 
	 * @param $string 待加密的字符串
	 * @param $key 密钥，如果为null或空字符串则使用全局密钥
	 * @param $expiry 过期时间（秒），0表示永不过期
	 * @return 加密后的字符串
	 */
	public static String encode(String $string, String $key, Integer $expiry) {
		$key = ($key == null || "".equals($key)) ? GLOBAL_AUTH_KEY : $key;
		$expiry = ($expiry == null || $expiry == 0) ? 0 : $expiry;
		return EncryptAuthcode.authcode($string, "ENECODE", $key, $expiry);
	}
}