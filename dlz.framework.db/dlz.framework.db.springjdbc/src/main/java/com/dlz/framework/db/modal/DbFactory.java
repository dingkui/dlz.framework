package com.dlz.framework.db.modal;

import com.dlz.comm.exception.SystemException;
import com.dlz.framework.db.helper.util.DbNameUtil;
import com.dlz.framework.db.modal.map.*;
import com.dlz.framework.db.modal.result.Page;
import com.dlz.framework.util.system.MFunction;
import com.dlz.framework.util.system.Reflections;

import java.lang.reflect.Field;

public class DbFactory {
    public static ParaMap para(String sql) {
        return new ParaMap(sql);
    }
    public static ParaMap para(String sql, Page page) {
        return new ParaMap(sql,page);
    }

    public static ParaMapInsert insert(String tableName) {
        return new ParaMapInsert(tableName);
    }
    public static ParaMapDelete delete(String tableName) {
        return new ParaMapDelete(tableName);
    }

    public static ParaMapUpdate update(String tableName) {
        return new ParaMapUpdate(tableName);
    }
    public static ParaMapSearch select(String colums, String tableName) {
        return new ParaMapSearch(colums,tableName);
    }
    public static ParaMapSearch select(String tableName) {
        return new ParaMapSearch(tableName);
    }
    public static <T> ParaMapSearchColumn select(MFunction<T, ?> column) {
        Field field = Reflections.getField(column);
        if(field==null){
            throw new SystemException("字段无效");
        }
        String fieldName = field.getName();
        String tableName = DbNameUtil.getDbTableName(field.getDeclaringClass());
        return new ParaMapSearchColumn(fieldName,tableName);
    }
}
