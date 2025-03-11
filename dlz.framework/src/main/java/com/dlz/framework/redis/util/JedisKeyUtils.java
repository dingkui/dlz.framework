package com.dlz.framework.redis.util;

import com.dlz.comm.exception.SystemException;
import com.dlz.comm.util.JacksonUtil;
import com.dlz.comm.util.ValUtil;
import com.fasterxml.jackson.databind.JavaType;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Map;

public class JedisKeyUtils {
    private static final String SPL = "$CLASS$";
    private static Map<String, JavaType> javaType_map = new Hashtable<>();

    public static JavaType getJavaType(String clazz) {
        JavaType javaType = javaType_map.get(clazz);
        if (javaType == null) {
            try {
                javaType = JacksonUtil.mkJavaType(Class.forName(clazz));
                javaType_map.put(clazz, javaType);
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
        return value.getClass().getCanonicalName() + SPL + ValUtil.toStr(value);
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
            return (T) str;
        }
        return ValUtil.toObj(str, javaType);
    }

    public static byte[] getBytes(String key) {
        return key.getBytes();
    }

}
