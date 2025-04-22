package com.dlz.framework.db.modal;

import com.dlz.comm.exception.SystemException;
import com.dlz.comm.fn.DlzFn;
import com.dlz.comm.util.StringUtils;
import com.dlz.comm.util.VAL;
import com.dlz.comm.util.system.FieldReflections;
import com.dlz.framework.db.holder.BeanInfoHolder;
import com.dlz.framework.db.modal.para.*;
import com.dlz.framework.db.modal.result.Page;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DB {
    public static JdbcQuery jdbcSelect(String sql, Object... para) {
        return new JdbcQuery(sql,para);
    }

    public static JdbcInsert jdbcInsert(String sql, Object... para) {
        return new JdbcInsert(sql, para);
    }

    public static JdbcExcute jdbcExute(String sql, Object... para) {
        return new JdbcExcute(sql, para);
    }

    public static SqlKeyQuery sqlSelect(String sql) {
        return new SqlKeyQuery(sql);
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

    public static MakerQuery select(String tableName) {
        return new MakerQuery(tableName);
    }

    public static <T> MakerQuery select(DlzFn<T, ?> column) {
        Field field = FieldReflections.getField(column);
        if (field == null) {
            throw new SystemException("字段无效");
        }
        final VAL<String, String> val = BeanInfoHolder.fnInfo(column);
        return new MakerQuery(val.v2).select(val.v1);
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
    public static <T> boolean saveBatch(List<T> bean) {
        if(bean.size()>0){
            return new WrapperInsert<>(bean.get(0)).batch(bean);
        }
        return false;
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

    public static <T> int insertOrUpdate(T obj,String idName){
        final Object id = FieldReflections.getValue(obj, idName, true);
        if(StringUtils.isEmpty(id)){
            return DB.insert(obj).excute();
        }
        return DB.update(obj).eq(idName,id).excute();
    }
    public static <T> int insertOrUpdate(T obj){
        return insertOrUpdate(obj,"id");
    }
    public static <T> int updateById(T obj){
        return updateById(obj,"id");
    }
    public static <T> int updateById(T obj,String idName){
        final Object id = FieldReflections.getValue(obj, idName, true);
        if(StringUtils.isEmpty(id)){
            throw new SystemException(idName+"不能为空");
        }
        return DB.update(obj).eq(idName,id).excute();
    }
    public static <T> T getById(Class<T> c,Object id,String idName){
        if(StringUtils.isEmpty(id)){
            throw new SystemException(idName+"不能为空");
        }
        return DB.query(c).eq(idName,id).queryBean();
    }
    public static <T> T getById(Class<T> c,Object id){
        return getById(c,id,"id");
    }
    public static <T> int removeByIds(Class<T> c,String ids){
        return removeByIds(c,ids,"id");
    }
    public static <T> int removeByIds(Class<T> c,String ids,String idName){
        if(StringUtils.isEmpty(ids)){
            throw new SystemException(idName+"不能为空");
        }
        return DB.delete(c).in(idName,ids).excute();
    }

}
