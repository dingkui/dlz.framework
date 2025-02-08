package com.dlz.framework.util.bean;

import com.dlz.comm.exception.SystemException;
import com.dlz.comm.json.JSONList;
import com.dlz.comm.json.JSONMap;
import com.dlz.comm.util.ValUtil;
import com.dlz.framework.util.system.Reflections;
import com.fasterxml.jackson.databind.JavaType;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 对象转换工具类
 *
 * @author dk 2017-11-03
 */
@Slf4j
public class BeanConvert {

    /**
     * Map转Bean
     *
     * @param <T>
     * @param map
     * @param clazz
     * @return
     */
    public static <T> T coverMap2Bean(JSONMap map, Class<T> clazz) {
        if(clazz==null){
            return (T) map;
        }
        if (Map.class.isAssignableFrom(clazz)) {
            return coverMap2Map(map, clazz);
        }
        return coverMap2Bean(map, clazz, getFieldsInfos(clazz));
    }

    /**
     * Map转Bean
     *
     * @param <T>
     * @param queryForList
     * @param clazz
     * @return
     */
    public static <T> List<T> coverMap2Bean(Collection<JSONMap> queryForList, Class<T> clazz) {
        if (Map.class.isAssignableFrom(clazz)) {
            return queryForList.stream().map(map -> coverMap2Map(map, clazz)).collect(Collectors.toList());
        }

        Map<String, Field> fieldsInfo = getFieldsInfos(clazz);
        return queryForList.stream().map(map -> coverMap2Bean(map, clazz, fieldsInfo)).collect(Collectors.toList());
    }

    private static Map<String, Field> getFieldsInfos(Class<?> clazz) {
        Field[] fields = Reflections.getFields(clazz);
        Map<String, Field> fieldsInfo = new HashMap<>();
        for (int i = 0; i < fields.length; i++) {
            String dbClumnName = fields[i].getName();
            fieldsInfo.put(dbClumnName, fields[i]);
        }
        return fieldsInfo;
    }

    private final static <T> T coverMap2Map(final JSONMap map,final Class<T> clazz) {
        try {
            if (clazz.isAssignableFrom(JSONMap.class)) {
                return (T) map;
            }
            if (clazz == Map.class) {
                return (T) map;
            }
            T t = clazz.newInstance();
            ((Map)t).putAll(map);
            return t;
        } catch (Exception e) {
            throw new SystemException("转换失败", e);
        }
    }
    /**
     * Map转Bean
     *
     * @param <T>
     * @param map
     * @param clazz
     * @return
     */
    private final static <T> T coverMap2Bean(JSONMap map,final Class<T> clazz,final Map<String, Field> fieldsInfo) {
        try {
            Object obj = clazz.getDeclaredConstructor().newInstance();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String mapKey = entry.getKey();
                Field field = fieldsInfo.get(mapKey);
                if (field != null) {
                    Object mapValue = entry.getValue();
                    Reflections.makeAccessible(field);
                    field.set(obj, ValUtil.toObj(mapValue, field.getType()));
//                  Reflections.invokeSetter(obj, field.v2.getName(), ValUtil.toObj(mapValue, field.v2.getType()));
                }
            }
            return (T) obj;
        } catch (Exception e) {
            throw new SystemException("转换失败", e);
        }
    }
}