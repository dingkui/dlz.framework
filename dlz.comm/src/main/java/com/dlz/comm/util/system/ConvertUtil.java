package com.dlz.comm.util.system;

import com.dlz.comm.exception.SystemException;
import com.dlz.comm.util.ValUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ConvertUtil {


    /**
     * 排序
     *
     * @return
     */
    public static <T> List<T> sort(List<T> list, Comparable<T> fn) {
        return list.stream().sorted((Comparator<? super T>) fn).collect(Collectors.toList());
    }

    public static <T> T convert(Object input, Class<T> clazz) {
        return convert(input, clazz, null);
    }

    public static <T> T convert(Object input, Class<T> tClass, Consumer<T> fn) {
        if (input == null) {
            return null;
        }

        List<Field> tfields = Map.class.isAssignableFrom(tClass) ? null : FieldReflections.getFields(tClass);
        List<Field> sfields = input instanceof Map ? null : FieldReflections.getFields(input.getClass());
        //如果目标是Map
        if (tfields == null) {
            //输入是Map
            if (sfields == null) {
                return convertMap2Map((Map<String, Object>) input, tClass, fn);
            }
            //输入是bean
            return convertBean2Map(input, tClass, sfields, fn);
        }
        //目标是bean
        //输入是Map
        if (sfields == null) {
            return convertMap2Bean((Map<String, Object>) input, tClass, tfields, fn);
        }
        //输入是bean
        Map<String, Field> sFieldsMap = sfields.stream().collect(Collectors.toMap(Field::getName, f -> f));
        return convertBean2Bean(input, tClass, tfields, sFieldsMap, fn);
    }

    /**
     * Map转Bean
     *
     * @param <T>
     * @param input
     * @param tClass
     * @return
     */
    public static <T, S> List<T> convertList(List<S> input, Class<S> sClass, Class<T> tClass, Consumer<T> fn) {
        if (ValUtil.isEmpty(input)) {
            return new ArrayList<>();
        }
        List<Field> tfields = Map.class.isAssignableFrom(tClass) ? null : FieldReflections.getFields(tClass);
        List<Field> sfields = Map.class.isAssignableFrom(sClass) ? null : FieldReflections.getFields(sClass);

        //如果目标是Map
        if (tfields == null) {
            //输入是Map
            if (sfields == null) {
                return input.stream().map(map -> convertMap2Map((Map<String, Object>) map, tClass, fn)).collect(Collectors.toList());
            }
            //输入是bean
            return input.stream().map(bean -> convertBean2Map(bean, tClass, sfields, fn)).collect(Collectors.toList());
        }
        //目标是bean
        //输入是Map
        if (sfields == null) {
            return input.stream().map(map -> convertMap2Bean((Map<String, Object>) map, tClass, tfields, fn)).collect(Collectors.toList());
        }
        Map<String, Field> sFields = sfields.stream().collect(Collectors.toMap(Field::getName, f -> f, (f1, f2) -> f1));
        return input.stream().map(bean -> convertBean2Bean(bean, tClass, tfields, sFields, fn)).collect(Collectors.toList());
    }

    /**
     * Map转Bean
     *
     * @param <T>
     * @param input
     * @param tClass
     * @return
     */
    public static <T, T1> List<T> convertList(List<T1> input, Class<T> tClass, Consumer<T> fn) {
        if (ValUtil.isEmpty(input)) {
            return new ArrayList<>();
        }
        Object inputObj = input.get(0);
        List<Field> tfields = Map.class.isAssignableFrom(tClass) ? null : FieldReflections.getFields(tClass);
        List<Field> sfields = inputObj instanceof Map ? null : FieldReflections.getFields(inputObj.getClass());

        //如果目标是Map
        if (tfields == null) {
            //输入是Map
            if (sfields == null) {
                return input.stream().map(map -> convertMap2Map((Map<String, Object>) map, tClass, fn)).collect(Collectors.toList());
            }
            //输入是bean
            return input.stream().map(bean -> convertBean2Map(bean, tClass, sfields, fn)).collect(Collectors.toList());
        }
        //目标是bean
        //输入是Map
        if (sfields == null) {
            return input.stream().map(map -> convertMap2Bean((Map<String, Object>) map, tClass, tfields, fn)).collect(Collectors.toList());
        }
        Map<String, Field> sFields = sfields.stream().collect(Collectors.toMap(Field::getName, f -> f, (f1, f2) -> f1));
        return input.stream().map(bean -> convertBean2Bean(bean, tClass, tfields, sFields, fn)).collect(Collectors.toList());
    }

    public static <T, T1> List<T> convertList(List<T1> input, Class<T> clazz) {
        return convertList(input, clazz, null);
    }

    /**
     * 将一个 Map 对象转化为一个 JavaBean
     *
     * @param tClazz 要转化的类型
     * @param input  包含属性值的 input
     * @return 转化出来的 JavaBean 对象
     */
    private static <T> T convertBean2Bean(Object input, Class<T> tClazz, List<Field> targetFields, Map<String, Field> sourceFields, Consumer<T> fn) {
        if (input == null) {
            return null;
        }
        if (tClazz.isAssignableFrom(Map.class) || input instanceof Map) {
            throw new SystemException("convertBean2Bean只支持bean之间转换");
        }
        T obj = Reflections.newInstance(tClazz); // 创建 JavaBean 对象
        for (Field field : targetFields) {
            Field sourceField = sourceFields.get(field.getName());
            if (sourceField != null) {
                Object value = FieldReflections.getValue(input, sourceField);
                if (value != null) {
                    FieldReflections.setValue(obj, field, ValUtil.toObj(value, field.getGenericType()));
                }
            }
        }
        if (fn != null) {
            fn.accept(obj);
        }
        return obj;
    }

    /**
     * 将一个 Map 对象转化为一个 JavaBean
     *
     * @param tClazz 要转化的类型
     * @param input  包含属性值的 input
     * @return 转化出来的 JavaBean 对象
     */
    private static <T> T convertMap2Bean(Map<String, Object> input, Class<T> tClazz, List<Field> targetFields, Consumer<T> fn) {
        if (input == null) {
            return null;
        }
        if (tClazz.isAssignableFrom(Map.class)) {
            throw new SystemException("convertMap2Bean只支持map 转 bean");
        }
        T obj = Reflections.newInstance(tClazz); // 创建 JavaBean 对象
        for (Field field : targetFields) {
            Object value = input.get(field.getName());
            if (value != null) {
                FieldReflections.setValue(obj, field, ValUtil.toObj(value, field.getGenericType()));
            }
        }
        if (fn != null) {
            fn.accept(obj);
        }
        return obj;
    }


    /**
     * map2Map
     *
     * @param map
     * @param clazz
     * @param fn
     * @param <T>
     * @return
     */
    private static <T> T convertMap2Map(final Map<String, Object> map, final Class<T> clazz, Consumer<T> fn) {
        if (clazz.isAssignableFrom(map.getClass())) {
            return (T) map;
        }
        if (!Map.class.isAssignableFrom(clazz)) {
            throw new SystemException("convertMap2Map只支持map之间转换");
        }
        Map obj = (Map) Reflections.newInstance(clazz); // 创建 Map 对象
        obj.putAll(map);
        if (fn != null) {
            fn.accept((T) obj);
        }
        return (T) obj;
    }

    /**
     * bean2Map
     *
     * @param tClazz 要转化的类型
     * @param input  包含属性值的 input
     * @return 转化出来的 JavaBean 对象
     */
    private static <T> T convertBean2Map(Object input, Class<T> tClazz, List<Field> sfields, Consumer<T> fn) {
        if (input == null) {
            return null;
        }
        if (!tClazz.isAssignableFrom(Map.class) || input instanceof Map) {
            throw new SystemException("convertBean2Map只支持 bean 转 map");
        }
        Map obj = (Map) Reflections.newInstance(tClazz); // 创建 Map 对象
        for (Field field : sfields) {
            Object value = FieldReflections.getValue(input, field);
            if (value != null) {
                obj.put(field.getName(), value);
            }
        }
        if (fn != null) {
            fn.accept((T) obj);
        }
        return (T) obj;
    }

}
