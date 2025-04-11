package com.dlz.framework.db.modal;

import com.dlz.comm.exception.SystemException;
import com.dlz.comm.fn.DlzFn;
import com.dlz.comm.util.system.FieldReflections;
import com.dlz.framework.db.helper.util.DbNameUtil;
import com.dlz.framework.db.modal.para.*;
import com.dlz.framework.db.modal.result.Page;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

public class DB {
    public static SqlKeyQuery sqlSelect(String sql, Page page) {
        return new SqlKeyQuery(sql, page);
    }

    public static SqlKeyQuery sqlSelect(String sql) {
        return new SqlKeyQuery(sql, null);
    }

    public static SqlKeyInsert sqlInsert(String sql) {
        return new SqlKeyInsert(sql);
    }

    public static SqlKeyExcute sqlExuter(String sql) {
        return new SqlKeyExcute(sql);
    }

    public static MakerInsert insert(String tableName) {
        return new MakerInsert(tableName);
    }

    public static MakerDelete delete(String tableName) {
        return new MakerDelete(tableName);
    }

    public static MakerUpdate update(String tableName) {
        return new MakerUpdate(tableName);
    }

    public static MakerQuery select(String colums, String tableName) {
        return new MakerQuery(colums, tableName);
    }

    public static MakerQuery select(String tableName) {
        return new MakerQuery(tableName);
    }

    public static <T> MakerQuery select(DlzFn<T, ?> column) {
        Field field = FieldReflections.getField(column);
        if (field == null) {
            throw new SystemException("字段无效");
        }
        String fieldName = field.getName();
        String tableName = DbNameUtil.getDbTableName(field.getDeclaringClass());
        return new MakerQuery(fieldName, tableName);
    }

    public static <T> WrapperQuery<T> query(Class<T> re, Map<String, Object> query, Set<String> exclude, Page page) {
        WrapperQuery<T> qw = WrapperQuery.wrapper(re).auto(query, exclude);
        return page == null ? qw : qw.page(page);
    }

    public static <T> WrapperQuery<T> query(Class<T> re) {
        return WrapperQuery.wrapper(re);
    }

    public static <T> WrapperQuery<T> query(Map<String, Object> query, Class<T> re) {
        return WrapperQuery.wrapper(re).auto(query);
    }
    public static <T> WrapperQuery<T> query(T contion) {
        return WrapperQuery.wrapper(contion);
    }

    public static <T> WrapperDelete<T> delete(Class<T> beanClass) {
        return WrapperDelete.wrapper(beanClass);
    }

    public static <T> WrapperDelete<T> delete(T condition) {
        return WrapperDelete.wrapper(condition);
    }

    public static <T> WrapperInsert<T> insert(T bean) {
        return new WrapperInsert(bean);
    }

    public static <T> WrapperUpdate<T> update(Class<T> beanClass) {
        return WrapperUpdate.wrapper(beanClass);
    }

    public static <T> WrapperUpdate<T> update(T condition) {
        return WrapperUpdate.wrapper(condition);
    }
    public static <T> WrapperUpdate<T> update(T condition, T value) {
        return WrapperUpdate.wrapper(condition).set(value);
    }

}
