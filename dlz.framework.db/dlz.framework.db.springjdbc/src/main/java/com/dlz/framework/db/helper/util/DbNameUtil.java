package com.dlz.framework.db.helper.util;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dlz.comm.exception.SystemException;
import com.dlz.comm.util.StringUtils;
import com.dlz.comm.util.ValUtil;
import com.dlz.framework.db.modal.ResultMap;
import com.dlz.framework.util.system.MFunction;
import com.dlz.framework.util.system.Reflections;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DbNameUtil {
    public static String getDbTableName(Class<?> clazz) {
        TableName name = clazz.getAnnotation(TableName.class);
        String tName = null;
        String schema = null;
        if (name != null) {
            if (name.value().length() > 0) {
                tName = name.value();
            }
        }
        if (tName == null) {
            tName = getDbClumnName(clazz.getSimpleName()).replaceAll("^_","");
        }
        return tName;
    }

    public static String getTableCommont(Class<?> clazz) {
        ApiModel name = clazz.getAnnotation(ApiModel.class);
        if (name != null && StringUtils.isNotEmpty(name.value())) {
            return name.value().replaceAll("[\\\"'`]", "");
        }
        return null;
    }

    public static String getDbClumnName(Field field) {
        TableField name = field.getAnnotation(TableField.class);
        if (name != null) {
            if (!name.exist()) {
                return null;
            }
            if (StringUtils.isNotEmpty(name.value())) {
                return name.value();
            }
        }
        return getDbClumnName(field.getName());
    }
    public static String getDbClumnName(MFunction property) {
        Field field = Reflections.getField(property);
        return field == null ? null : getDbClumnName(field);
    }

    public static String getDbClumnName(String field) {
        return StringUtils.camelToUnderScore(field).toUpperCase();
    }

    public static String getClumnCommont(Field field) {
        ApiModelProperty name = field.getAnnotation(ApiModelProperty.class);
        if (name != null && StringUtils.isNotEmpty(name.value())) {
            return name.value().replaceAll("[\\\"\\\n'`]", "");
        }
        return null;
    }

    /**
     * Map转Bean
     *
     * @param <T>
     * @param map
     * @param clazz
     * @return
     */
    public static <T> T coverResult2Bean(ResultMap map, Class<T> clazz) {
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
    public static <T> List<T> coverResult2Bean(List<ResultMap> queryForList, Class<T> clazz) {
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
//            String dbClumnName = DbNameUtil.getDbClumnName(fields[i]);
            String dbClumnName = fields[i].getName();
            fieldsInfo.put(dbClumnName, fields[i]);
        }
        return fieldsInfo;
    }

    private final static <T> T coverMap2Map(final ResultMap map,final Class<T> clazz) {
        try {
            if (clazz.isAssignableFrom(ResultMap.class)) {
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
    private final static <T> T coverMap2Bean(ResultMap map,final Class<T> clazz,final Map<String, Field> fieldsInfo) {
        try {
            Object obj = clazz.getDeclaredConstructor().newInstance();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String mapKey = entry.getKey();
                Field field = fieldsInfo.get(mapKey);
                if (field != null) {
                    Object mapValue = entry.getValue();
                    Reflections.makeAccessible(field);
                    field.set(obj, ValUtil.getObj(mapValue, field.getType()));
//                  Reflections.invokeSetter(obj, field.v2.getName(), ValUtil.getObj(mapValue, field.v2.getType()));
                }
            }
            return (T) obj;
        } catch (Exception e) {
            throw new SystemException("转换失败", e);
        }
    }


}
