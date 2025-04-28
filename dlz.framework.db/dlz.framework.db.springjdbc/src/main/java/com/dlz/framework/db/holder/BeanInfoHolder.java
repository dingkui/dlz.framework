package com.dlz.framework.db.holder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dlz.comm.cache.CacheMap;
import com.dlz.comm.fn.DlzFn;
import com.dlz.comm.util.StringUtils;
import com.dlz.comm.util.system.FieldReflections;
import com.dlz.framework.db.util.DbConvertUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * bean与数据库表信息保持器
 *
 * @author dk
 */
@Slf4j
public class BeanInfoHolder {
    private static final CacheMap<Class<?>, String> tableNameCache = new CacheMap<>();
    private static final CacheMap<Field, String> columnNameCache = new CacheMap<>();
    private static final CacheMap<String, List<Field>> tableFieldCache = new CacheMap<>();
    private static final CacheMap<String, HashMap<String, Integer>> tableColumnsInfoCahe = new CacheMap<>();

    public static void clearAll() {
        tableColumnsInfoCahe.clear();
    }

    /**
     * （带缓存）取得字段对应的数据库字段名
     *
     * @param field
     * @return
     */
    public static String getColumnName(Field field) {
        return columnNameCache.getAndSet(field, () -> {
            TableField name = field.getAnnotation(TableField.class);
            if (name != null) {
                if (!name.exist()) {
                    return "";
                }
                if (StringUtils.isNotEmpty(name.value())) {
                    return name.value();
                }
            }
            final String columnName = getColumnName(field.getName());
            if (log.isDebugEnabled()) {
                log.debug("字段：{} 对应数据库字段：{}", field.getDeclaringClass().getName() + "." + field.getName(), columnName);
            }
            return columnName;
        });
    }

    /**
     * 判断字段是否存在
     *
     * @param tableName  表名
     * @param columnName 字段名：支持bean字段名，数据库字段名
     * @return
     * @author dk 2018-09-28
     */
    public static boolean isColumnExists(String tableName, String columnName) {
        Map<String, Integer> map = getTableColumnsInfo(tableName);
        if (map == null) {
            return false;
        }
        return map.containsKey(DbConvertUtil.str2DbClumn(columnName.replaceAll("`", "")).toUpperCase());
    }

    /**
     * bean字段名转换成数据库字段名
     *
     * @param field
     * @return
     */
    public static String getColumnName(String field) {
        return DbConvertUtil.str2Clumn(field);
    }

    /**
     * 根据bean取得表注释
     *
     * @param clazz
     * @return
     */
    public static String getTableComment(Class<?> clazz) {
        ApiModel name = clazz.getAnnotation(ApiModel.class);
        if (name != null && StringUtils.isNotEmpty(name.value())) {
            return name.value().replaceAll("[\\\"'`]", "");
        }
        return null;
    }

    /**
     * 根据bean字段取得字段注释
     *
     * @param field
     * @return
     */
    public static String getColumnComment(Field field) {
        ApiModelProperty name = field.getAnnotation(ApiModelProperty.class);
        if (name != null && StringUtils.isNotEmpty(name.value())) {
            return name.value().replaceAll("[\\\"\\\n'`]", "");
        }
        return null;
    }

    /**
     * （带缓存）根据bean取得表名
     *
     * @param clazz
     * @return
     */
    public static String getTableName(Class<?> clazz) {
        return tableNameCache.getAndSet(clazz, () -> {
            TableName name = clazz.getAnnotation(TableName.class);
            String tName = null;
            String schema = null;
            if (name != null) {
                if (name.value().length() > 0) {
                    tName = name.value();
                }
            }
            if (tName == null) {
                tName = getColumnName(clazz.getSimpleName()).replaceAll("^_", "");
            }
            return tName;
        });
    }

    /**
     * （带缓存）取得数据库表字段信息
     *
     * @param tableName
     * @return
     */
    public static HashMap<String, Integer> getTableColumnsInfo(String tableName) {
        return tableColumnsInfoCahe.getAndSet(tableName, () ->
                DBHolder.getService().getDao().getTableColumnsInfo(tableName)
        );
    }

    /**
     * （带缓存）根据bean取得数据库对应的字段信息，如果字段在表中不存在，则不返回
     *
     * @param beanClass
     * @return
     */
    public static List<Field> getBeanFields(Class<?> beanClass) {
        String tableName = getTableName(beanClass);
        return tableFieldCache.getAndSet(tableName, () -> {
            HashMap<String, Integer> tableColumnsInfo = getTableColumnsInfo(tableName);
            return FieldReflections.getFields(beanClass).stream()
                    .filter(field -> tableColumnsInfo.containsKey(getColumnName(field.getName())))
                    .collect(Collectors.toList());
        });
    }

    /**
     * 根据lamda 表达式取得数据库字段名
     *
     * @param column
     * @param <T>
     * @return
     */
    public static <T> String fnName(DlzFn<T, ?> column) {
        return getColumnName(FieldReflections.getFn(column).v2);
    }
}
