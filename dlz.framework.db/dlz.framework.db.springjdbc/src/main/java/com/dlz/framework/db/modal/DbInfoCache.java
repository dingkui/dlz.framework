package com.dlz.framework.db.modal;

import com.dlz.comm.cache.CaheMap;
import com.dlz.comm.exception.SystemException;
import com.dlz.comm.util.VAL;
import com.dlz.framework.db.helper.util.DbNameUtil;
import com.dlz.framework.db.holder.DBHolder;
import com.dlz.framework.util.system.MFunction;
import com.dlz.framework.util.system.FieldReflections;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 构造器缓存
 *
 * @author dk
 */
public class DbInfoCache {
    private static final CaheMap<Class<?>, String> tableNameCahe = new CaheMap<>();
    private static final CaheMap<String, List<Field>> tableFieldCahe = new CaheMap<>();
    private static final CaheMap<MFunction, VAL<String, String>> fnCahe = new CaheMap<>();

    public static String getTableName(Class<?> beanClass) {
        return tableNameCahe.getAndSet(beanClass, () -> DbNameUtil.getDbTableName(beanClass));
    }

    public static HashMap<String, Integer> getTableComums(Class<?> beanClass) {
        return DBHolder.getService().getDao().getTableColumnsInfo(getTableName(beanClass));
    }
    public static List<Field> getTableFields(Class<?> beanClass) {
        String tableName = getTableName(beanClass);
        return tableFieldCahe.getAndSet(tableName, () -> {
            HashMap<String, Integer> tableColumnsInfo = DBHolder.getService().getDao().getTableColumnsInfo(tableName);
            return FieldReflections.getFields(beanClass).stream()
                    .filter(field -> tableColumnsInfo.containsKey(DbNameUtil.getDbClumnName(field.getName())))
                    .collect(Collectors.toList());
        });
    }

    public static <T> VAL<String, String> fnInfo(MFunction<T, ?> column) {
        return fnCahe.getAndSet(column, () -> {
            Field field = FieldReflections.getField(column);
            if (column == null) {
                throw new SystemException("字段无效");
            }
            return VAL.of(DbNameUtil.getDbClumnName(field), DbNameUtil.getDbTableName(field.getDeclaringClass()));
        });
    }

    public static <T> String fnName(MFunction<T, ?> column) {
        return fnInfo(column).v1;
    }

    public static <T> String fnTableName(MFunction<T, ?> column) {
        return fnInfo(column).v2;
    }
}
