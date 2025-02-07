package com.dlz.comm.util;

import com.dlz.comm.json.JSONList;
import com.fasterxml.jackson.databind.JavaType;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.*;

/**
 * 对象转换工具类
 *
 * @author dk 2017-11-03
 */
@Slf4j
public class ValUtil {
    private static Number toNumber(Object input, Number defaultV) {
        if (input == null || "".equals(input)) {
            return defaultV;
        }
        if (input instanceof Number) {
            return (Number) input;
        }
        return new BigDecimal(input.toString());
    }
    public static BigDecimal toBigDecimal(Object input) {
        return toBigDecimal(input, null);
    }

    public static BigDecimal toBigDecimal(Object input, BigDecimal defaultV) {
        Number o = toNumber(input, null);
        if (o == null) {
            return defaultV;
        }
        if (o instanceof BigDecimal) {
            return (BigDecimal) o;
        } else if (o instanceof Float) {
            return new BigDecimal(o.toString());
        } else if (o instanceof Double) {
            return new BigDecimal(o.toString());
        } else if (o instanceof Integer) {
            return new BigDecimal(o.intValue());
        } else if (o instanceof Long) {
            return new BigDecimal(o.longValue());
        }
        return new BigDecimal(o.toString());
    }


    public static Double toDouble(Object input) {
        return toDouble(input, null);
    }

    public static Double toDouble(Object input, Double defaultV) {
        Number o = toNumber(input, null);
        if (o == null) {
            return defaultV;
        }
        return o.doubleValue();
    }

    public static Float toFloat(Object input) {
        return toFloat(input, null);
    }

    public static Float toFloat(Object input, Float defaultV) {
        Number o = toNumber(input, null);
        if (o == null) {
            return defaultV;
        }
        return o.floatValue();
    }

    public static Integer toInt(Object input) {
        return toInt(input, null);
    }

    public static Integer toInt(Object input, Integer defaultV) {
        Number o = toNumber(input, null);
        if (o == null) {
            return defaultV;
        }
        return o.intValue();
    }

    public static Long toLong(Object input) {
        return toLong(input, null);
    }

    public static Long toLong(Object input, Long defaultV) {
        Number o = toNumber(input, null);
        if (o == null) {
            return defaultV;
        }
        return o.longValue();
    }


    public static Boolean toBoolean(Object input) {
        return toBoolean(input, false);
    }

    public static Boolean toBoolean(Object input, Boolean defaultV) {
        if (input == null) {
            return defaultV;
        }
        if (input instanceof Boolean) {
            return (Boolean) input;
        }
        String r = input.toString();

        return !"false".equalsIgnoreCase(r) && !"0".equals(r) && !"".equals(r);
    }

    public static String toStrBlank(Object input) {
        return toStr(input, "");
    }

    public static String toStr(Object input) {
        return toStr(input, null);
    }

    public static String toStr(Object input, String defaultV) {
        if (input == null) {
            return defaultV;
        }
        if (input instanceof CharSequence || input instanceof Number) {
            return input.toString();
        }
        return JacksonUtil.getJson(input);
    }




    public static JSONList toList(Object input) {
        return toList(input, (List) null);
    }

    public static JSONList toList(Object input, Collection defaultV) {
        if (input == null) {
            return new JSONList(defaultV);
        }
        if (input instanceof JSONList) {
            return (JSONList) input;
        }
        try {
            return new JSONList(input);
        } catch (RuntimeException e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
        return new JSONList(defaultV);
    }

    public static <T> List<T> toList(Object input, Class<T> clazz) {
        T[] array = toArray(input, clazz);
        if (array == null) {
            return null;
        }
        return Arrays.asList(array);
    }

    public static Date toDate(Object input) {
        return toDate(input, null);
    }

    public static Date toDate(Object input, String format) {
        if (input == null) {
            return null;
        }
        if (Date.class.isAssignableFrom(input.getClass())) {
            return (Date) input;
        }
        if (Number.class.isAssignableFrom(input.getClass())) {
            return new Date(((Number) input).longValue());
        }
        return DateUtil.getDateStr(toStr(input), format);
    }


    public static String toDateStr(Object input, String format) {
        if (input == null) {
            return null;
        }
        input = toDate(input);
        if (input == null) {
            return "";
        }
        if (format == null) {
            return DateUtil.getDateTimeStr((Date) input);
        }
        return DateUtil.format((Date) input, format);
    }

    public static String toDateStr(Object input) {
        return toDateStr(input, null);
    }

    private static <T> T[] toArray(Collection input, Class<T> clazz) {
        if (input == null) {
            return null;
        }
        T[] re = (T[]) Array.newInstance(clazz, input.size());
        final Iterator it = input.iterator();
        int i = 0;
        while (it.hasNext()) {
            re[i++] = toObj(it.next(), clazz);
        }
        return re;
    }

    private static <T> T[] toArray(Object[] input, Class<T> clazz) {
        if (input == null) {
            return null;
        }
        T[] re = (T[]) Array.newInstance(clazz, input.length);
        for (int i = 0; i < input.length; i++) {
            re[i] = toObj(input[i], clazz);
        }
        return re;
    }

    public static Object[] toArray(Object input) {
        return toArray(input, (Object[]) null);
    }

    public static <T> T[] toArray(Object input, Class<T> clazz) {
        if (input == null) {
            return null;
        }
        if (input instanceof Collection) {
            return toArray((Collection) input, clazz);
        }
        if (input instanceof Object[]) {
            return toArray((Object[]) input, clazz);
        }
        if (input instanceof CharSequence) {
            return toArray(input.toString().split(","), clazz);
        }
        try {
            String string = toStr(input);
            if (JacksonUtil.isJsonArray(string)) {
                return toArray(JacksonUtil.readListValue(string, clazz), clazz);
            } else {
                throw new RuntimeException("参数不能转换成List:" + string);
            }
        } catch (RuntimeException e) {
            log.warn(e.getMessage());
        }
        return null;
    }

    public static Object[] toArray(Object input, Object[] defaultV) {
        if (input == null) {
            return defaultV;
        }
        if (input instanceof Object[]) {
            return (Object[]) input;
        } else if (input instanceof Collection) {
            return ((Collection) input).toArray();
        }
        try {
            return new JSONList(input).toArray();
        } catch (RuntimeException e) {
            log.warn(e.getMessage());
        }
        return defaultV;
    }

    private static <T> T toNativeObj(Object input, Class<T> classs) {
        if (classs.isAssignableFrom(input.getClass())) {
            return (T) input;
        } else if (classs == String.class) {
            return (T) toStr(input);
        } else if (classs == Integer.class) {
            return (T) toInt(input);
        } else if (classs == Long.class) {
            return (T) toLong(input);
        } else if (classs == Date.class) {
            return (T) toDate(input);
        } else if (classs == BigDecimal.class) {
            return (T) toBigDecimal(input);
        } else if (classs == Float.class) {
            return (T) toFloat(input);
        } else if (classs == Double.class) {
            return (T) toDouble(input);
        } else if (classs == Boolean.class) {
            return (T) toBoolean(input);
        }
        return null;
    }

    public static <T> T toObj(Object input, Class<T> classs) {
        if (input == null || classs == null) {
            return (T) input;
        }
        T re = toNativeObj(input, classs);
        if (re == null) {
            re = JacksonUtil.coverObj(input, classs);
        }

        if (re == null) {
            re = JacksonUtil.coverObj(input, classs);
        }
        return re != null ? re : JacksonUtil.coverObj(input, classs);
    }

    public static <T> T toObj(Object input, JavaType javaType) {
        if (input == null || javaType == null) {
            return (T) input;
        }
        T re = toNativeObj(input, (Class<T>) javaType.getRawClass());
        return re != null ? re : JacksonUtil.coverObj(input, javaType);
    }

    public static boolean isEmpty(Object cs) {
        if (cs == null) {
            return true;
        }
        if (cs instanceof Collection) {
            return ((Collection) cs).isEmpty();
        } else if (cs instanceof Map) {
            return ((Map) cs).isEmpty();
        } else if (cs.getClass().isArray()) {
            return ((Object[]) cs).length == 0;
        } else if (cs instanceof CharSequence) {
            return ((CharSequence) cs).length() == 0;
        } else {
            return false;
        }
    }


    public static void main(String[] args) {
        System.out.println(toDate("2018-21-24 19:23:39.583"));
        System.out.println(toDate("2018-1-24 19:23"));
        System.out.println(toDate("19:23"));
        System.out.println(toDate("2018-1-24"));
    }
}