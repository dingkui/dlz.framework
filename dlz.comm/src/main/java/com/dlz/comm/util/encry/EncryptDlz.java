package com.dlz.comm.util.encry;

import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Random;

/**
 * 加密工具
 * @author dk 2017-06-20
 */
@Slf4j
public class EncryptDlz {
	/**
	 * 使用XOR加密字符串
	 * @return 加密后的字节数组
	 */
	private static byte[] xorByKey(byte[] input,byte[] KEY) {
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
	public static String decryAsString(String input, String key) {
		byte[] decry = decry(input, key);
		if(decry != null){
			return ByteUtil.getStr(decry);
		}
		return null;
	}

	public static byte[] decry(String input, String key) {
		int l=input.length()-1,i=l;
		int e=Integer.parseInt(input.substring(0,1));
		char[] output = new char[l];
		int ind=1+i*e;
		while(i-->0 ) {
			ind-=e;
			int index=ind;
			while(index>=l){
				index-=l;
			}
			output[index]=input.charAt(l-i);
		}
		String result = new String(output).replaceAll("\\.$","").replaceAll("_","=");
		String base64str= result;

		int hasPass = result.indexOf("|");
		if(hasPass>-1){
			if(key==null){
				log.warn("解密失败，未输入key");
				return null;
			}
			String KEY=Md5Util.md5(key+result.substring(0,4));
			String checkPass = Md5Util.md5(KEY).substring(0, 8);
			if(!checkPass.equals(result.substring(4,12))){
				log.warn("解密失败，key错误");
				return null;
			}
			String expiryTimeStr= result.substring(12,hasPass);
			if(expiryTimeStr.length()>0){
				ByteBuffer buffer = ByteBuffer.allocate(8);
				buffer.put(xorByKey(Base64.decode(expiryTimeStr),checkPass.getBytes(StandardCharsets.UTF_8)));
				buffer.flip(); // 切换到读模式
				long expiry = buffer.getLong();
				if(expiry-System.currentTimeMillis()/1000<0){
					log.warn("解密失败，过期失效");
					return null;
				}
			}

			base64str= result.substring(hasPass+1);
			byte[] bytes = Base64.decode(base64str);
			return xorByKey(bytes,KEY.getBytes(StandardCharsets.UTF_8));
		}

		return Base64.decode(base64str);
	}

	/**
	 * 取得加密质数
	 * @param l
	 * @return
	 */
	private static int getZs(int l){
		int e=1;
		if(l>14){
			e=7;
		}else if(l>10){
			e=5;
		}else if(l>6){
			e=3;
		}else if(l>=2){
			e=2;
		}
		return e;
	}

	/**
	 * 使用XOR解密字符串
	 * @return 解密后的字符串
	 */
	public static String encry(byte[] inputBytes, String key,int expiry) {
		//取得随机数,每次加密时，随机数的长度为1-5位
		String[] prifixs = new String[]{"","",""};
		if(key!=null){
			prifixs[0] = (1000+new Random().nextInt(1000))+"";
			String KEY=Md5Util.md5(key+prifixs[0]);
			prifixs[1] = Md5Util.md5(KEY).substring(0,8);
			inputBytes = xorByKey(inputBytes, KEY.getBytes(StandardCharsets.UTF_8));
			if(expiry>0){
				long value = System.currentTimeMillis()/1000+expiry;
				ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES).putLong(value);
				prifixs[2] = Base64.encode2Str(xorByKey(buffer.array(),prifixs[1].getBytes(StandardCharsets.UTF_8)));
			}
		}
		String base64str = new String(Base64.encode(inputBytes));


		int prifixsLength=prifixs[0].length()+prifixs[1].length()+prifixs[2].length();

		int len=base64str.length();
		if(prifixsLength>0){
			len+=prifixsLength+1;
		}
		int e=getZs(len);
		if((len)%e==0){
			base64str+=".";
			len++;
		}
		if(prifixsLength>0){
			base64str=prifixs[0]+prifixs[1]+prifixs[2]+"|"+base64str;
		}

		int i=len;
		char[] output = new char[len];
		int ind=1+i*e;
		while(i-->0 ) {
			ind-=e;
			int index=ind;
			while(index>=len){
				index-=len;
			}
			output[len-i-1]=base64str.charAt(index);
		}
		return e+new String(output).replaceAll("=","_");
//		return base64str;
	}
	/**
	 * 使用XOR加字符串
	 * @return 解密后的字符串
	 */
	public static String encry(String input, String key) {
		return encry(input.getBytes(StandardCharsets.UTF_8),key,0);
	}
	/**
	 * 使用XOR加字符串
	 * @return 解密后的字符串
	 */
	public static String encry(String input, String key,int expiry) {
		return encry(input.getBytes(StandardCharsets.UTF_8),key,expiry);
	}

	public static void main(String[] args) throws InterruptedException {
		String str="1";
		String key="null";
		String encry = encry(str,key,0);
//		Thread.sleep(2000);
		System.out.println(System.currentTimeMillis()+" "+encry);
		String encry2 = encry(str,key,0);
		System.out.println(System.currentTimeMillis()+" "+encry2);
	}
}
