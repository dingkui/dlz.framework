package com.dlz.comm.util.system.convert;

import com.dlz.comm.exception.SystemException;
import com.dlz.comm.util.ValUtil;
import com.dlz.comm.util.system.FieldReflections;
import com.dlz.comm.util.system.Reflections;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class MapConvert implements IConvert{
    public <T> T convert(Object input, Class<T> tClass, Consumer<T> fn) {
        if (input==null || !(input instanceof Map)) {
            return null;
        }
        if(tClass.isArray()){
            throw new SystemException("不能将Map转化数组");
        }
        if(Collection.class.isAssignableFrom(tClass)){
            throw new SystemException("不能将Map转化list");
        }
        if (Map.class.isAssignableFrom(tClass)) {
            return toMap((Map) input, tClass, fn);
        }
        return toBean((Map) input, tClass, null, fn);
    }

    @Override
    public <S, T> List<T> convertList(List<S> input, Class<S> sClass, Class<T> tClass, Consumer<T> fn) {
        if (input == null) {
            return null;
        }
        if(input.size()==0){
            return new ArrayList<>();
        }
        //输入值不是Map不支持
        if (!(Map.class.isAssignableFrom(sClass))) {
            return null;
        }

        //如果目标是Map
        if (Map.class.isAssignableFrom(tClass)) {
            return input.stream().map(map -> toMap((Map<String, Object>) map, tClass, fn)).collect(Collectors.toList());
        }
        List<Field> tfields = Map.class.isAssignableFrom(tClass) ? null : FieldReflections.getFields(tClass);
        //目标是bean
        return input.stream().map(map -> toBean((Map<String, Object>) map, tClass, tfields, fn)).collect(Collectors.toList());
    }

    /**
     * 将一个 Map 对象转化为一个 JavaBean
     *
     * @param tClazz 要转化的类型
     * @param input  包含属性值的 input
     * @return 转化出来的 JavaBean 对象
     */
    private static <T> T toBean(Map input, Class<T> tClazz, List<Field> targetFields, Consumer<T> fn) {
        if (input == null) {
            return null;
        }
        T obj = Reflections.newInstance(tClazz); // 创建 JavaBean 对象
        if (targetFields == null) {
            targetFields = FieldReflections.getFields(tClazz);
        }
        for (Field field : targetFields) {
            Object value = input.get(field.getName());
            if (value != null) {
                FieldReflections.setValue(obj, field, value);
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
    private <T> T toMap(final Map map, final Class<T> clazz, Consumer<T> fn) {
        if (clazz.isAssignableFrom(map.getClass())) {
            return (T) map;
        }
        Map obj = (Map) Reflections.newInstance(clazz); // 创建 Map 对象
        obj.putAll(map);
        if (fn != null) {
            fn.accept((T) obj);
        }
        return (T) obj;
    }
}
