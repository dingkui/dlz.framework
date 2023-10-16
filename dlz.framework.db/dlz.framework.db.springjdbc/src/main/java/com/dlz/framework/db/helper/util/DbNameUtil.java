package com.dlz.framework.db.helper.util;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dlz.comm.exception.SystemException;
import com.dlz.comm.util.StringUtils;
import com.dlz.comm.util.VAL;
import com.dlz.comm.util.ValUtil;
import com.dlz.framework.db.modal.ResultMap;
import com.dlz.framework.util.system.MFunction;
import com.dlz.framework.util.system.Reflections;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            tName = getDbClumnName(clazz.getSimpleName());
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
        try {
            if (clazz.isAssignableFrom(Map.class)) {
                return (T) map;
            }
            return coverMap2Bean(map, clazz, null);
        } catch (Exception e) {
            throw new SystemException("转换失败", e);
        }
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
        List<T> list = new ArrayList<>();
        try {
            if (clazz.isAssignableFrom(Map.class)) {
                for (int i = 0; i < queryForList.size(); i++) {
                    list.add((T) queryForList.get(i));
                }
                return list;
            }

            Map<String, VAL<String, Field>> fieldsInfo = getFieldsInfos(clazz);
            for (ResultMap map : queryForList) {
                list.add(coverMap2Bean(map, clazz, fieldsInfo));
            }
        } catch (Exception e) {
            throw new SystemException("转换失败", e);
        }
        return list;
    }

    private static Map<String, VAL<String, Field>> getFieldsInfos(Class<?> clazz) {
        Field[] fields = Reflections.getFields(clazz);
        Map<String, VAL<String, Field>> fieldsInfo = new HashMap<>();
        for (int i = 0; i < fields.length; i++) {
            String dbClumnName = DbNameUtil.getDbClumnName(fields[i]);
            fieldsInfo.put(dbClumnName, new VAL(dbClumnName, fields[i]));
        }
        return fieldsInfo;
    }

    /**
     * Map转Bean
     *
     * @param <T>
     * @param map
     * @param clazz
     * @return
     */
    private static <T> T coverMap2Bean(ResultMap map, Class<T> clazz, Map<String, VAL<String, Field>> fieldsInfo) {
        try {
            if (fieldsInfo == null) {
                fieldsInfo = getFieldsInfos(clazz);
            }
            Object obj = clazz.getDeclaredConstructor().newInstance();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String mapKey = entry.getKey();
                VAL<String, Field> val = fieldsInfo.get(mapKey);
                if (val != null) {
                    Object mapValue = entry.getValue();
                    Reflections.setFieldValue(obj, val.v2, ValUtil.getObj(mapValue, val.v2.getType()));
                }
            }
            return (T) obj;
        } catch (Exception e) {
            throw new SystemException("转换失败", e);
        }
    }


}
