package com.dlz.comm.util.encry;

import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.util.Random;

/**
 * 加密工具
 * 加密含有效期校验，随机数密码，混淆等
 * 支持单独无秘钥混淆加密
 * @author dk 2024-03-14
 */
@Slf4j
public class EncryptDlz {
	/**
	 * 异或加密工具
	 * @author dk 2024-03-14
	 */
	public static class Xor {
		/**
		 * 使用XOR加密
		 * @return 加密后的字节数组
		 */
		public static byte[] xor(byte[] input,byte[] KEY) {
			if(KEY==null){
				return input;
			}
			byte[] encryptedBytes = new byte[input.length];
			for (int i = 0; i < input.length; i++) {
				if(KEY.length>input.length){
					int inputByte = input[i];
					for (int j = 0; j < KEY.length-input.length; j++) {
						inputByte = inputByte ^ KEY[i % KEY.length+j];
					}
					encryptedBytes[i] = (byte) inputByte;
				}else{
					encryptedBytes[i] = (byte) (input[i] ^ KEY[i % KEY.length]);
				}
			}
			return encryptedBytes;
		}
		public static byte[] xor(long input,byte[] KEY) {
			ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES).putLong(input);
			return xor(buffer.array(),KEY);
		}
		public static byte[] xor(long input,String KEY) {
			return xor(input,ByteUtil.getBytes(KEY));
		}
		public static byte[] xor(byte[] input,String KEY) {
			return xor(input,ByteUtil.getBytes(KEY));
		}
		public static byte[] xor(String input,byte[] KEY) {
			return xor(ByteUtil.getBytes(input),KEY);
		}
		public static byte[] xor(String input,String KEY) {
			return xor(ByteUtil.getBytes(input),ByteUtil.getBytes(KEY));
		}

		/**
		 * 使用XOR加密字符串
		 * @return 加密后的字节数组
		 */
		public static long xorAsLong(byte[] input,byte[] KEY) {
			ByteBuffer buffer = ByteBuffer.allocate(8);
			buffer.put(xor(input,KEY));
			buffer.flip(); // 切换到读模式
			return buffer.getLong();
		}
	}

	/**
	 * 加密工具(位置混淆)
	 *
	 * @author dk 2017-06-20
	 */
	public static class Mix {
		public static String mix(String input) {
			int len = input.length() + 1;
			int e = 7;
			char m = (char)97;
			if(len<16){
				int addLen=16-len+1;
				input+="0123456789abcdef".substring(0,16-len+1);
				len+=addLen;
				m = (char)(97+addLen);
			}

			if (len % e == 0) {
				m = (char)98;
				input += '0';
				len++;
			}
			input += m;
			int i = len;
			char[] output = new char[len];
			int ind = 1 + i * e;
			while (i-- > 0) {
				ind -= e;
				int index=ind;
				while(index>=len){
					index-=len;
				}
				output[len - i - 1] = input.charAt(index);
			}
			return new String(output);
		}

		public static String deMix(String input) {
			int len = input.length(), i = len;
			int e = 7;
			char[] output = new char[len];
			int ind = 1 + i * e;
			while (i-- > 0) {
				ind -= e;
				int index=ind;
				while(index>=len){
					index-=len;
				}
				output[index]=input.charAt(len-i-1);
			}
			String result = new String(output);
			return result.substring(0, result.length() - (int)output[len-1]+96);
		}
	}
	/**
	 * 使用XOR解密字符串
	 * @param inputBytes 要加密的信息
	 * @param key 解密秘钥 可为空
	 * @param expiry 解密有效期 单位秒
	 * @return 解密后的字符串
	 */
	public static String encry(byte[] inputBytes, String key,int expiry) {
		//取得随机数,每次加密时，随机数的长度为1-5位
		String[] prifixs = new String[]{(1000+new Random().nextInt(1000))+"","nkabcdef",""};
		if(key!=null){
			String KEY=Md5Util.md5(key+prifixs[0]);
			prifixs[1] = Md5Util.md5(KEY).substring(0,8);
			inputBytes = Xor.xor(inputBytes, KEY);
		}
		if(expiry>0){
			long value = System.currentTimeMillis()/1000+expiry;
			prifixs[2] = Base64.encode2Str(Xor.xor(String.valueOf(value),prifixs[0]+prifixs[1]));
		}
		String base64str = prifixs[0]+prifixs[1]+prifixs[2]+"|"+new String(Base64.encode(inputBytes));
		return Mix.mix(base64str).replaceAll("=","_");
	}

	/**
	 * 使用XOR加字符串
	 * @return 解密后的字符串
	 */
	public static String encry(String input, String key,int expiry) {
		return encry(ByteUtil.getBytes(input),key,expiry);
	}

	/**
	 * 使用XOR解密字符串
	 * @return 解密后的byte[]
	 */
	public static byte[] decry(String input, String key) {
		String result = Mix.deMix(input).replaceAll("_","=");

		int hasPass = result.indexOf("|");
		if(hasPass==-1){
			log.warn("解密失败，不是有效的加密信息");
			return null;
		}
		String check = result.substring(4, 12);
		String KEY=Md5Util.md5(key+result.substring(0,4));
		if(key!=null){
			String checkPass = Md5Util.md5(KEY).substring(0, 8);
			if(!checkPass.equals(check)){
				log.warn("解密失败，key错误");
				return null;
			}
		}else if(!check.equals("nkabcdef")){
			log.warn("解密失败，key错误");
			return null;
		}

		String expiryTimeStr= result.substring(12,hasPass);
		if(expiryTimeStr.length()>0){
			long expiryTime = Long.valueOf(new String(Xor.xor(Base64.decode(expiryTimeStr),result.substring(0,12))));
			long keepTime = expiryTime - System.currentTimeMillis() / 1000;
			if(keepTime<0|| keepTime>300){
				log.warn("解密失败，过期失效");
				return null;
			}
		}

		byte[] bytes = Base64.decode(result.substring(hasPass+1));
		return Xor.xor(bytes,KEY);
	}

	public static String decryAsString(String input, String key) {
		byte[] decry = decry(input, key);
		if(decry != null){
			return ByteUtil.getStr(decry);
		}
		return null;
	}
}
