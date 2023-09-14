package com.dlz.comm.util.encry;


import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.File;

/**
 * @version 2.0
 */
@Slf4j
public class Base64 {
	public static byte[] encode(byte[] data){
		return java.util.Base64.getEncoder().encode(data);
	}
	// 编码文件对象所指的文件
	public static byte[] encode(File file) {
		if (!file.exists()) {
			log.error("错误:文件不存在！{}",file.getAbsoluteFile());
			return null;
		}
		return encode(ByteUtil.readBytes(file));
	}
	public static byte[] encode(String str,String charset){
		return encode(ByteUtil.getBytes(str,charset));
	}
	public static byte[] encode(String str){
		return encode(ByteUtil.getBytes(str));
	}
	public static String encode2Str(byte[] data){
		return new String(encode(data));
	}	
	public static String encode2Str(String str) {
		return new String(encode(str));
	}
	public static String encode2Str(String str,String charset) {
		return new String(encode(str,charset));
	}
	public static String encode2Str(File file) {
		return new String(encode(file));
	}

	public static byte[] decode(String str){
		return java.util.Base64.getDecoder().decode(str);
	}
	public static byte[] decode(byte[] bytes){
		return java.util.Base64.getDecoder().decode(bytes);
	}
	public static String decode2Str(String str,String charset ){
		return ByteUtil.getStr(decode(str),charset);
	}
	public static String decode2Str(String str){
		return ByteUtil.getStr(decode(str));
	}

	static public ByteArrayInputStream decodeAsInputSream(String base64Str) {
		return new ByteArrayInputStream(decode(base64Str));
	}
}