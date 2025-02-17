package com.dlz.framework.util.bean;

import com.dlz.comm.exception.SystemException;
import com.dlz.comm.json.JSONMap;
import com.dlz.framework.util.system.FieldReflections;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;
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
    public static <T> T cover2Bean(JSONMap map, Class<T> clazz) {
        if (clazz == null) {
            return (T) map;
        }
        if (Map.class.isAssignableFrom(clazz)) {
            return cover2Map(map, clazz);
        }
        return cover2Bean(map, clazz, FieldReflections.getFieldsMap(clazz));
    }

    /**
     * Map转Bean
     *
     * @param <T>
     * @param queryForList
     * @param clazz
     * @return
     */
    public static <T> List<T> cover2Bean(Collection<JSONMap> queryForList, Class<T> clazz) {
        if (Map.class.isAssignableFrom(clazz)) {
            return queryForList.stream().map(map -> cover2Map(map, clazz)).collect(Collectors.toList());
        }

        Map<String, Field> fieldsInfo = FieldReflections.getFieldsMap(clazz);
        return queryForList.stream().map(map -> cover2Bean(map, clazz, fieldsInfo)).collect(Collectors.toList());
    }

    private final static <T> T cover2Map(final JSONMap map, final Class<T> clazz) {
        try {
            if (clazz.isAssignableFrom(JSONMap.class)) {
                return (T) map;
            }
            if (clazz == Map.class) {
                return (T) map;
            }
            T t = clazz.newInstance();
            ((Map) t).putAll(map);
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
    private final static <T> T cover2Bean(JSONMap map, final Class<T> clazz, final Map<String, Field> fieldsInfo) {
        try {
            Object obj = clazz.getDeclaredConstructor().newInstance();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String mapKey = entry.getKey();
                Field field = fieldsInfo.get(mapKey);
                if (field != null) {
                    FieldReflections.setValue(obj, field, entry.getValue());
//                  Reflections.invokeSetter(obj, field.v2.getName(), ValUtil.toObj(mapValue, field.v2.getType()));
                }
            }
            return (T) obj;
        } catch (Exception e) {
            throw new SystemException("转换失败", e);
        }
    }
}