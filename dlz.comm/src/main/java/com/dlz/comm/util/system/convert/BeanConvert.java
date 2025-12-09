package com.dlz.comm.util.system.convert;

import com.dlz.comm.util.system.FieldReflections;
import com.dlz.comm.util.system.Reflections;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class BeanConvert implements IConvert{
    public <T> T convert(Object input, Class<T> tClass, Consumer<T> fn) {
        if (input == null) {
            return null;
        }
        //输入值是Map List Array不支持
        if (input instanceof Map || input instanceof List || input.getClass().isArray()) {
            return null;
        }
        if (Map.class.isAssignableFrom(tClass)) {
            return toMap(input, tClass, fn);
        }
        return toBean(input, tClass, fn);
    }

    @Override
    public <S, T> List<T> convertList(List<S> input, Class<S> sClass, Class<T> tClass, Consumer<T> fn) {
        if (input == null) {
            return null;
        }
        if (input.isEmpty()) {
            return new ArrayList<>();
        }
        //输入值是Map List Array不支持
        if (Map.class.isAssignableFrom(sClass) || List.class.isAssignableFrom(sClass) || sClass.isArray()) {
            return null;
        }
        List<Field> sfields = FieldReflections.getFields(sClass);
        List<Field> tfields = Map.class.isAssignableFrom(tClass) ? null : FieldReflections.getFields(tClass);
        //如果目标是Map
        if (tfields != null) {
            //输入是bean
            return input.stream().map(bean -> toMap(bean, tClass, sfields, fn)).collect(Collectors.toList());
        }
        //目标是bean
        Map<String, Field> sFields = sfields.stream().collect(Collectors.toMap(Field::getName, f -> f, (f1, f2) -> f1));
        return input.stream().map(bean -> toBean(bean, tClass, tfields, sFields, fn)).collect(Collectors.toList());
    }

    /**
     * 将一个 Map 对象转化为一个 JavaBean
     *
     * @param tClazz 要转化的类型
     * @param input  包含属性值的 input
     * @return 转化出来的 JavaBean 对象
     */
    private static <T> T toBean(Object input, Class<T> tClazz, Consumer<T> fn) {
        List<Field> targetFields = FieldReflections.getFields(tClazz);
        Map<String, Field> sourceFields = FieldReflections.getFields(input.getClass()).stream().collect(Collectors.toMap(Field::getName, f -> f));
        return toBean(input, tClazz, targetFields, sourceFields, fn);
    }

    /**
     * 将一个 Map 对象转化为一个 JavaBean
     *
     * @param tClazz 要转化的类型
     * @param input  包含属性值的 input
     * @return 转化出来的 JavaBean 对象
     */
    private static <T> T toMap(Object input, Class<T> tClazz, Consumer<T> fn) {
        List<Field> sfields = FieldReflections.getFields(input.getClass());
        return toMap(input, tClazz, sfields, fn);
    }

    /**
     * 将一个 Map 对象转化为一个 JavaBean
     *
     * @param tClazz 要转化的类型
     * @param input  包含属性值的 input
     * @return 转化出来的 JavaBean 对象
     */
    private static <T> T toBean(Object input, Class<T> tClazz, List<Field> targetFields, Map<String, Field> sourceFields, Consumer<T> fn) {
        T obj = Reflections.newInstance(tClazz); // 创建 JavaBean 对象
        for (Field field : targetFields) {
            Field sourceField = sourceFields.get(field.getName());
            if (sourceField != null) {
                Object value = FieldReflections.getValue(input, sourceField);
                if (value != null) {
                    FieldReflections.setValue(obj, field, value);
                }
            }
        }
        if (fn != null) {
            fn.accept(obj);
        }
        return obj;
    }

    /**
     * bean2Map
     *
     * @param tClazz 要转化的类型
     * @param input  包含属性值的 input
     * @return 转化出来的 JavaBean 对象
     */
    private static <T> T toMap(Object input, Class<T> tClazz, List<Field> sfields, Consumer<T> fn) {
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
