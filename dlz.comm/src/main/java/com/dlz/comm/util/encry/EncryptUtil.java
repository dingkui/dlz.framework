package com.dlz.comm.util.encry;

import lombok.extern.slf4j.Slf4j;

/**
 * 加密工具
 * @author dk 2017-06-20
 */
@Slf4j
public class EncryptUtil {
	/**
	 * all set GLOBAL_AUTH_KEY
	 */
	public static String GLOBAL_AUTH_KEY = "e317b362fafa0c96c20b8543d054b850";
	
	public static String simpleDecry(String codes) {
		return EncryptDlz.decryAsString(codes,null);
	}
	public static String simpleEncry(String input) {
		return EncryptDlz.encry(input,null);
	}


	public static String decode(String $string, String $key) {
		$key=($key==null||"".equals($key))?GLOBAL_AUTH_KEY:$key;
		return EncryptAuthcode.authcode($string, "DECODE", $key, 0);
	}
	public static String decode(String $string) {
		return decode($string, null);
	}
	public static String encode(String $string) {
		return encode($string,null, null);
	}
	public static String encode(String $string,String $key) {
		return encode($string, $key, null);
	}
	public static String encode(String $string,Integer $expiry) {
		return encode($string, null, $expiry);
	}
	public static String encode(String $string, String $key,Integer $expiry) {
		$key=($key==null||"".equals($key))?GLOBAL_AUTH_KEY:$key;
		$expiry=($expiry==null||$expiry==0)?0:$expiry;
		return EncryptAuthcode.authcode($string, "ENECODE", $key, $expiry);
	}
}
