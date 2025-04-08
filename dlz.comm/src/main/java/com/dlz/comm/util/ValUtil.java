package com.dlz.comm.util;

import com.dlz.comm.exception.SystemException;
import com.dlz.comm.json.JSONList;
import com.dlz.comm.util.system.FieldReflections;
import com.fasterxml.jackson.databind.JavaType;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;

/**
 * 对象转换工具类
 *
 * @author dk 2017-11-03
 */
@Slf4j
public class ValUtil {
    public final static Integer ZERO_INT = 0;
    public final static Long ZERO_LONG = 0l;
    public final static Float ZERO_FLOAT = 0f;
    public final static Double ZERO_DOUBLE = 0.0;
    public final static String STR_BLANK = "";
    public final static String STR_NULL = "null";

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

    public static BigDecimal toBigDecimalZero(Object input) {
        return toBigDecimal(input, BigDecimal.ZERO);
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

    public static Double toDoubleZero(Object input) {
        return toDouble(input, ZERO_DOUBLE);
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

    public static Float toFloatZero(Object input) {
        return toFloat(input, ZERO_FLOAT);
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

    public static Integer toIntZero(Object input) {
        return toInt(input, ZERO_INT);
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

    public static Long toLongZero(Object input) {
        return toLong(input, ZERO_LONG);
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
        return toStr(input, STR_BLANK);
    }

    public static String toStrWithEmpty(Object input, String defaultValue) {
        if (null == input || input.equals(STR_NULL) || input.equals(STR_BLANK)) {
            return defaultValue;
        }
        return toStr(input, defaultValue);
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

    public static JSONList toListEmputy(Object input) {
        return toList(input, new ArrayList());
    }


    public static Object[] toArray(Object input) {
        return toArray(input, (Object[]) null);
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

    public static <T> T[] toArray(Collection input, Class<T> clazz) {
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

    public static <T> T[] toArray(Object[] input, Class<T> clazz) {
        if (input == null) {
            return null;
        }
        T[] re = (T[]) Array.newInstance(clazz, input.length);
        for (int i = 0; i < input.length; i++) {
            re[i] = toObj(input[i], clazz);
        }
        return re;
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


    public static Date toDate(Object input) {
        return toDate(input, null, null);
    }

    public static Date toDate(Object input, String format) {
        return toDate(input, format, null);
    }

    public static Date toDate(Object input, String format, Date defaultV) {
        if (input == null) {
            return defaultV;
        }
        if (Date.class.isAssignableFrom(input.getClass())) {
            return (Date) input;
        }
        if (Number.class.isAssignableFrom(input.getClass())) {
            return new Date(((Number) input).longValue());
        }
        if (LocalDateTime.class.isAssignableFrom(input.getClass())) {
            return DateUtil.getDate((LocalDateTime) input);
        }
        if (LocalDate.class.isAssignableFrom(input.getClass())) {
            return DateUtil.getDate((LocalDate) input);
        }
        return DateUtil.getDate(toStr(input), format);
    }

    public static Date toDateNow(Object input) {
        return toDate(input, null, new Date());
    }

    public static String toDateStr(Object input) {
        return toDateStr(input, null, null);
    }

    public static String toDateStr(Object input, String format) {
        return toDateStr(input, format, null);
    }

    public static String toDateStr(Object input, String format, Date defaultV) {
        Date date = toDate(input, null, defaultV);
        if (date == null) {
            return "";
        }
        if (format == null) {
            return DateUtil.getDateTimeStr(date);
        }
        return DateUtil.format(date, format);
    }

    public static String toDateStrNow(Object input) {
        return toDateStr(input, null, new Date());
    }

    private static Map<Class<?>, Function<?,?>> natveConverts= new HashMap<>();
    static {
        natveConverts.put(String.class, ValUtil::toStr);
        natveConverts.put(Integer.class, ValUtil::toInt);
        natveConverts.put(Long.class, ValUtil::toLong);
        natveConverts.put(Date.class, ValUtil::toDate);
        natveConverts.put(BigDecimal.class, ValUtil::toBigDecimal);
        natveConverts.put(Float.class, ValUtil::toFloat);
        natveConverts.put(Double.class, ValUtil::toDouble);
        natveConverts.put(Boolean.class, ValUtil::toBoolean);
        natveConverts.put(null, input -> input);
    }

    public static <T> T toNativeObj(Object input, Class<T> clazz) {
        if (input == null) {
            return null;
        }
        if (clazz.isAssignableFrom(input.getClass())) {
            return (T) input;
        }
        final Function function = natveConverts.get(clazz);
        if(function!=null){
            return (T) function.apply(input);
        }
        return null;
    }

    public static <T> T toObj(Object input, Class<T> classs) {
        if (input == null || classs == null) {
            return (T) input;
        }
        T re = (T)toNativeObj(input, classs);
        return re != null ? re : JacksonUtil.coverObj(input, classs);
    }

    public static <T> T toObj(Object input, Type type) {
        if (input == null || type == null) {
            return (T) input;
        }
        if (type instanceof Class) {
            return toObj(input, (Class<? extends T>) type);
        } else if (type instanceof ParameterizedType) {
            return JacksonUtil.coverObj(input, JacksonUtil.mkJavaType(type));
        } else if (type instanceof JavaType) {
            return JacksonUtil.coverObj(input, (JavaType) type);
        }
        throw new SystemException(type + "未识别泛型参数");
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

    /**
     * 获取对象属性值,无视private/protected修饰符, 不经过getter函数，支持多级取值
     * @param obj  支持pojo对象,map,数组和list
     * @param fieldNames 支持多级取值
     *                   如： {a:1}  取值1： fieldNames="a"
     *                       {a:{b:"xx"}}  取值xx： fieldNames="a.b"
     *                       {a:{b:[1,2]}}  取值2： fieldNames="a.b.1"
     * @param ignore 忽略空值或错误的属性
     * @param <T>
     * @return
     */
    public static <T> T getValue(final Object obj, final String fieldNames, final boolean ignore) {
        Object object = obj;
        String[] names = StringUtils.split(fieldNames, ".");
        for (int i = 0; i < names.length; i++){
            if (object == null) {
                if(ignore){
                    return null;
                }
                throw new IllegalArgumentException("can't getValue [" + fieldNames + "] from [" + obj + "]");
            }
            if(object instanceof Map){
                object = ((Map)object).get(names[i]);
            }else if(object instanceof List || object.getClass().isArray()){
                if(!StringUtils.isNumber(names[i])){
                    if(ignore){
                        return null;
                    }
                    throw new IllegalArgumentException("can't getValue [" + fieldNames + "] from [" + obj + "]");
                }
                final int i1 = Integer.parseInt(names[i]);
                if(object instanceof List){
                    object = ((List)object).get(i1);
                }else if(object.getClass().isArray()){
                    object = ((Object[])object)[i1];
                }
            }else{
                object = FieldReflections.getValue(object, names[i],ignore);
            }
        }
        return (T) object;
    }
    /**
     * 直接设置对象属性值, 无视private/protected修饰符, 不经过setter函数.
     */
    /**
     * 直接设置对象属性值, 无视private/protected修饰符, 不经过setter函数.支持多级属性
     * @param obj 支持pojo对象,map,数组和list
     * @param fieldNames 支持多级属性
     * @param value 属性值
     * @param ignore 忽略空值或错误的属性
     */
    public static boolean setValue(final Object obj, final String fieldNames, final Object value, final boolean ignore) {
        Object object = obj;
        String[] names = StringUtils.split(fieldNames, ".");
        for (int i = 0; i < names.length; i++){
            if (object == null) {
                if(ignore){
                    return false;
                }
                throw new IllegalArgumentException("can't setValue [" + fieldNames + "] from [" + obj + "]");
            }
            if (i < names.length - 1){
                object = getValue(object, names[i], true);
            }else{
                if(object instanceof Map){
                    ((Map)object).put(names[i],value);
                }else if(object instanceof List || object.getClass().isArray()){
                    if(!StringUtils.isNumber(names[i])){
                        if(ignore){
                            return false;
                        }
                        throw new IllegalArgumentException("can't setValue [" + fieldNames + "] from [" + obj + "]");
                    }
                    final int i1 = Integer.parseInt(names[i]);
                    if(object instanceof List){
                        final List list = (List) object;
                        if(list.size()<=i1){
                            if(ignore){
                                return false;
                            }
                            throw new IllegalArgumentException("can't setValue [" + fieldNames + "] from [" + obj + "]");
                        }
                        list.set(i1,value);
                    }else if(object.getClass().isArray()){
                        final Object[] array = (Object[]) object;
                        if(array.length<=i1){
                            if(ignore){
                                return false;
                            }
                            throw new IllegalArgumentException("can't setValue [" + fieldNames + "] from [" + obj + "]");
                        }
                        array[i1]=value;
                    }
                }else{
                    return FieldReflections.setValue(object, names[i],value,ignore);
                }
            }
        }
        return true;
    }

//    public static void main(String[] args) {
//        System.out.println(toDate("2018-21-24 19:23:39.583"));
//        System.out.println(toDate("2018-1-24 19:23"));
//        System.out.println(toDate("19:23"));
//        System.out.println(toDate("2018-1-24"));
//    }
}