package com.dlz.framework.redis.util;

import com.dlz.comm.exception.SystemException;
import com.dlz.comm.util.JacksonUtil;
import com.dlz.comm.util.ValUtil;
import com.dlz.framework.holder.SpringHolder;
import com.dlz.framework.redis.RedisKeyMaker;
import com.fasterxml.jackson.databind.JavaType;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Map;
import java.util.stream.Stream;

public class JedisKeyUtils {
    private static final String SPL = "$CLASS$";
    private static Map<String,JavaType> javaType_map=new Hashtable<>();
    public static JavaType getJavaType(String clazz) {
        JavaType javaType = javaType_map.get(clazz);
        if(javaType == null){
            try {
                javaType = JacksonUtil.constructType(Class.forName(clazz));
                javaType_map.put(clazz,javaType);
            } catch (ClassNotFoundException e) {
                throw new SystemException(e.getMessage(), e);
            }
        }
        return javaType;
    }
    public static String getValueStr(Serializable value) {
        if (value instanceof CharSequence) {
            return value.toString();
        }
        return value.getClass().getCanonicalName() + SPL + ValUtil.getStr(value);
    }

    public static <T> T getResult(String value, JavaType javaType) {
        if (value == null || value.length() == 0) {
            return null;
        }
        String str = value;
        int i = value.indexOf(SPL);
        if (i > -1) {
            String clazz = value.substring(0, i);
            str = value.substring(i + SPL.length());
            if (javaType == null) {
                javaType = getJavaType(clazz);
            }
        }
        if (javaType == null) {
            return (T)str;
        }
        return ValUtil.getObj(str, javaType);
    }

    public static byte[] getBytes(String key) {
        return key.getBytes();
    }

    private static RedisKeyMaker keyMaker;
    public static void init(RedisKeyMaker km) {
        if (km != null) {
            keyMaker = km;
        }
    }
    public static <T extends RedisKeyMaker> void init(Class<T> kmc) {
        if (kmc != null) {
            keyMaker = SpringHolder.createBean(kmc);
        }
    }
    /**
     * 获取缓存对象
     *
     * @return Cache
     */
    public static RedisKeyMaker getKeyMaker() {
        if (keyMaker == null) {
            keyMaker = SpringHolder.getBean(RedisKeyMaker.class);
        }
        return keyMaker;
    }
    public static String getClientKey(String key) {
        return getKeyMaker().getClientKey(key);
    }
    public static String getRedisKey(String key, Serializable... othrer) {
        return getKeyMaker().getRedisKey(key,othrer);
    }
    public static String getKey(String key, Serializable... othrer) {
        return getKeyMaker().getKey(key,othrer);
    }
    public static String[] getRedisKeyArray(String... keys) {
        String[] newkeys=new String[keys.length];
        for (int i = 0; i < keys.length; i++) {
            newkeys[i] = getRedisKey(keys[i]);
        }
        return newkeys;
    }
    public static void main(String[] args) {
        init(new RedisKeyMaker());
//        System.out.println(getRedisKey(":xxx:xxx::"));
//        System.out.println(getRedisKey(":xxx::xxx::"));
//        System.out.println(getRedisKey(":xxx::xxx::","aa"));
//        System.out.println(getRedisKey(":xxx::xxx::",":aa:"));
//        System.out.println(getRedisKey(":xxx::xxx::","aa:"));
//        System.out.println(getRedisKey(":xxx::xxx::", "*:"));
//        System.out.println(getClientKey("auto::xxx:xxx::"));
        System.out.println(getRedisKey("auto::xxx:xxx::","ss","xxx"));
    }
}
