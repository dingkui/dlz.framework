package com.dlz.framework.db.modal;

import com.dlz.comm.exception.SystemException;
import com.dlz.comm.fn.DlzFn;
import com.dlz.comm.util.StringUtils;
import com.dlz.comm.util.system.FieldReflections;
import com.dlz.framework.db.holder.DBHolder;
import com.dlz.framework.db.modal.para.*;
import com.dlz.framework.db.util.DbConvertUtil;

import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Function;

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

//    public static <T> WrapperQuery<T> select(DlzFn<T, ?> column) {
//        VAL<Class<?>, Field> infos = FieldReflections.getFn(column);
//        return WrapperQuery.wrapper((Class<T>)infos.v1).select(BeanInfoHolder.getColumnName(infos.v2));
//    }
    public static <T> WrapperQuery<T> select(Class<T> re, DlzFn<T, ?>... column) {
        return WrapperQuery.wrapper(re).select(column);
    }
    public static <T> WrapperQuery<T> query(Class<T> re) {
        return WrapperQuery.wrapper(re);
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
        return WrapperInsert.wrapper(bean);
    }
    public static <T> int save(T bean) {
        return insert(bean).excute();
    }
    public static <T> boolean saveBatch(List<T> bean) {
        return saveBatch(bean,1000);
    }
    public static <T> boolean saveBatch(List<T> bean, int batchSize) {
        if(bean.size()>0){
            return WrapperInsert.wrapper(bean.get(0)).batch(bean,batchSize);
        }
        return false;
    }

    public static <T> WrapperUpdate<T> update(Class<T> beanClass) {
        return WrapperUpdate.wrapper(beanClass);
    }

    public static <T> WrapperUpdate<T> update(T value, Function<String,Boolean> ignore) {
        return WrapperUpdate.wrapper((Class<T>)value.getClass()).set(value,ignore);
    }

    public static <T> WrapperUpdate<T> update(T value) {
        return WrapperUpdate.wrapper((Class<T>)value.getClass()).set(value);
    }

    public static <T> int insertOrUpdate(T obj,String idName){
        final Field field = FieldReflections.getField(obj, idName,false);
        final Object id = FieldReflections.getValue(obj, field);
        if(StringUtils.isEmpty(id)){
            return DB.insert(obj).excute();
        }
        return DB.update(obj, name->name.equalsIgnoreCase(idName)).eq(idName,id).excute();
    }
    public static <T> int insertOrUpdate(T obj){
        return insertOrUpdate(obj,"id");
    }
    public static <T> int updateById(T obj){
        return updateById(obj,"id");
    }
    public static <T> int updateById(T obj,String idName){
        final Object id = FieldReflections.getValue(obj, DbConvertUtil.toFieldName(idName), true);
        if(StringUtils.isEmpty(id)){
            throw new SystemException(idName+"不能为空");
        }
        return DB.update((Class<T>) obj.getClass()).set(obj,name->name.equalsIgnoreCase(idName)).eq(idName,id).excute();
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

    public static boolean batchUpdate(String sql, List<Object[]> valueBeans) {
        return batchUpdate(sql, valueBeans, 1000);
    }
    public static boolean batchUpdate(String sql, List<Object[]> valueBeans, int batchSize) {
        for (; valueBeans.size() > 0 && batchSize > 0; valueBeans = valueBeans.subList(batchSize, valueBeans.size())) {
            if (batchSize > valueBeans.size()) {
                batchSize = valueBeans.size();
            }
            List<Object[]> paramValues = valueBeans.subList(0, batchSize);
            DBHolder.getService().getDao().batchUpdate(sql, paramValues);
        }
        return true;
    }

}
