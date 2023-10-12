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
    public static BigDecimal getBigDecimal(Object input, BigDecimal defaultV) {
        Number o = getNumber(input, null);
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

    public static BigDecimal getBigDecimal(Object input) {
        return getBigDecimal(input, null);
    }

    public static Double getDouble(Object input) {
        return getDouble(input, null);
    }

    public static Double getDouble(Object input, Double defaultV) {
        Number o = getNumber(input, null);
        if (o == null) {
            return defaultV;
        }
        return o.doubleValue();
    }

    public static Float getFloat(Object input) {
        return getFloat(input, null);
    }

    public static Float getFloat(Object input, Float defaultV) {
        Number o = getNumber(input, null);
        if (o == null) {
            return defaultV;
        }
        return o.floatValue();
    }

    public static Integer getInt(Object input) {
        return getInt(input, null);
    }

    public static Integer getInt(Object input, Integer defaultV) {
        Number o = getNumber(input, null);
        if (o == null) {
            return defaultV;
        }
        return o.intValue();
    }

    public static Long getLong(Object input) {
        return getLong(input, null);
    }

    public static Long getLong(Object input, Long defaultV) {
        Number o = getNumber(input, null);
        if (o == null) {
            return defaultV;
        }
        return o.longValue();
    }


    public static Object[] getArray(Object input) {
        return getArray(input, (Object[]) null);
    }

    public static JSONList getList(Object input) {
        return getList(input, null);
    }

    public static String getStr(Object input) {
        return getStr(input, null);
    }

    public static Boolean getBoolean(Object input) {
        return getBoolean(input, false);
    }


    public static String getStr(Object input, String defaultV) {
        if (input == null) {
            return defaultV;
        }
        if (input instanceof CharSequence || input instanceof Number) {
            return input.toString();
        }
        return JacksonUtil.getJson(input);
    }

    public static Boolean getBoolean(Object input, Boolean defaultV) {
        if (input == null) {
            return defaultV;
        }
        if (input instanceof Boolean) {
            return (Boolean) input;
        }
        String r = input.toString();

        return !"false".equalsIgnoreCase(r) && !"0".equals(r) && !"".equals(r);
    }

    private static Number getNumber(Object input, Number defaultV) {
        if (input == null || "".equals(input)) {
            return defaultV;
        }
        if (input instanceof Number) {
            return (Number) input;
        }
        return new BigDecimal(input.toString());
    }

    public static JSONList getList(Object input, List defaultV) {
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

    public static <T> List<T> getListObj(Object input, Class<T> clazz) {
        if (input == null) {
            return null;
        }
        if (input instanceof List) {
            boolean isClass = true;
            final Iterator input2 = ((Collection) input).iterator();
            while (input2.hasNext()) {
                final Object next = input2.next();
                if (!clazz.isAssignableFrom(next.getClass())) {
                    isClass = false;
                    break;
                }
            }
            if (isClass) {
                return (List) input;
            }
        }
        try {
            return (List) new JSONList(input, clazz);
        } catch (RuntimeException e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
        return null;
    }

    public static Date getDate(Object input) {
        return getDate(input, null);
    }

    public static Date getDate(Object input, String format) {
        if (input == null) {
            return null;
        }
        if (Date.class.isAssignableFrom(input.getClass())) {
            return (Date) input;
        }
        if (Number.class.isAssignableFrom(input.getClass())) {
            return new Date(((Number) input).longValue());
        }
        return DateUtil.getDateStr(getStr(input), format);
    }

    public static void main(String[] args) {
        System.out.println(getDate("2018-21-24 19:23:39.583"));
        System.out.println(getDate("2018-1-24 19:23"));
        System.out.println(getDate("19:23"));
        System.out.println(getDate("2018-1-24"));
    }

    public static String getDateStr(Object input, String format) {
        if (input == null) {
            return null;
        }
        input = getDate(input);
        if (input == null) {
            return "";
        }
        if (format == null) {
            return DateUtil.getDateTimeStr((Date) input);
        }
        return DateUtil.format((Date) input, format);
    }

    public static String getDateStr(Object input) {
        return getDateStr(input, null);
    }

    public static <T> T[] getArrayObj(Object input, Class<T> clazz, Class<? extends T[]> clazzs) {
        if (input == null) {
            return null;
        }
        if (input instanceof Collection) {
            final List listObj = getListObj(input, clazz);
            return Arrays.copyOf(listObj.toArray(), listObj.size(), clazzs);
        }

        if (input instanceof Object[]) {
            boolean isClass = true;

            Object[] g = (Object[]) input;
            for (int i = 0; i < g.length; i++) {
                if (!clazz.isAssignableFrom(g[i].getClass())) {
                    isClass = false;
                    break;
                }
            }
            if (isClass) {
                return Arrays.copyOf(g, g.length, clazzs);
            }
        }
        try {
            String string = getStr(input);
            if (JacksonUtil.isJsonArray(string)) {
                final List<T> readListValue = JacksonUtil.readListValue(string, clazz);
                return Arrays.copyOf(readListValue.toArray(), readListValue.size(), clazzs);
            } else {
                throw new RuntimeException("参数不能转换成List:" + string);
            }
        } catch (RuntimeException e) {
            log.warn(e.getMessage());
        }
        return null;
    }

    private static <T> T[] arraycopy(Object[] input, Class<T> clazz){
        T[] copy = (T[])Array.newInstance(clazz, input.length);
        System.arraycopy(input, 0, copy, 0, input.length);
        return copy;
    }

    public static <T> T[] getArray(Object input, Class<T> clazz) {
        if (input == null) {
            return null;
        }
        if (input instanceof Collection) {
            return arraycopy(getListObj(input, clazz).toArray(),clazz);
        }
        if (input instanceof Object[]) {
            Object[] g = (Object[]) input;
            boolean anyMatch = Arrays.stream(g).anyMatch(o -> !clazz.isAssignableFrom(o.getClass()));
            if (!anyMatch) {
                return arraycopy(g,clazz);
            }
        }
        try {
            String string = getStr(input);
            if (JacksonUtil.isJsonArray(string)) {
                return arraycopy(JacksonUtil.readListValue(string, clazz).toArray(),  clazz);
            } else {
                throw new RuntimeException("参数不能转换成List:" + string);
            }
        } catch (RuntimeException e) {
            log.warn(e.getMessage());
        }
        return null;
    }

    public static Object[] getArray(Object input, Object[] defaultV) {
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

    public static <T> T getObj(Object input, Class<T> classs) {
        if (input == null) {
            return null;
        }
        return getObj(input, JacksonUtil.constructType(classs));
    }

    public static <T> T getObj(Object input, JavaType javaType) {
        if (input == null) {
            return null;
        }
        if (javaType == null) {
            return (T)input;
        }
        Class classs = javaType.getRawClass();

        if (classs.isAssignableFrom(input.getClass())) {
            return (T) input;
        } else if (classs == String.class) {
            return (T) getStr(input);
        } else if (classs == Integer.class) {
            return (T) getInt(input);
        } else if (classs == Long.class) {
            return (T) getLong(input);
        } else if (classs == Date.class) {
            return (T) getDate(input);
        } else if (classs == BigDecimal.class) {
            return (T) getBigDecimal(input);
        } else if (classs == Float.class) {
            return (T) getFloat(input);
        } else if (classs == Double.class) {
            return (T) getDouble(input);
        } else if (classs == Boolean.class) {
            return (T) getBoolean(input);
        }
        return JacksonUtil.coverObj(input, javaType);
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
//        } else if (cs instanceof Date) {
//            return false;
//        } else if (cs instanceof Number) {
//            return false;
        } else {
            return false;
            // throw new IllegalArgumentException("检验空参数有误：" + cs.getClass());
        }
    }
}