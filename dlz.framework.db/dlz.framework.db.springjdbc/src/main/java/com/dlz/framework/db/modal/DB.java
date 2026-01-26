package com.dlz.framework.db.modal;

import com.dlz.framework.db.modal.para.*;

import java.util.List;
import java.util.function.Function;

public class DB {
    public final static DbJdbc Jdbc = new DbJdbc();
    public final static DbTable Table = new DbTable();
    public final static DbSql Sql = new DbSql();
    public final static DbWrapper Wrapper = new DbWrapper();
    public final static DbBatch Batch = new DbBatch();

    /// JDBC 快捷操作
    public static JdbcQuery jdbcSelect(String sql, Object... para) {
        return Jdbc.select(sql,para);
    }

    public static JdbcExecute jdbcExute(String sql, Object... para) {
        return Jdbc.executer(sql, para);
    }

    /// SQL 快捷操作
    public static SqlKeyQuery sqlSelect(String sql) {
        return Sql.select(sql);
    }

    public static SqlKeyExecute sqlExuter(String sql) {
        return Sql.executer(sql);
    }

    /// Table 快捷操作
    public static TableInsert tableInsert(String tableName) {
        return Table.insert(tableName);
    }

    public static TableDelete tableDelete(String tableName) {
        return Table.delete(tableName);
    }

    public static TableUpdate tableUpdate(String tableName) {
        return Table.update(tableName);
    }

    public static TableQuery tableSelect(String tableName) {
        return Table.select(tableName);
    }

    /// Wrapper 快捷操作
    public static <T> WrapperQuery<T> query(Class<T> re) {
        return Wrapper.select(re);
    }
    public static <T> WrapperQuery<T> query(T contion) {
        return Wrapper.select(contion);
    }

    public static <T> WrapperDelete<T> delete(Class<T> beanClass) {
        return Wrapper.delete(beanClass);
    }

    public static <T> WrapperDelete<T> delete(T condition) {
        return Wrapper.delete(condition);
    }

    public static <T> WrapperInsert<T> insert(T bean) {
        return Wrapper.insert(bean);
    }
    public static <T> WrapperUpdate<T> update(Class<T> beanClass) {
        return Wrapper.update(beanClass);
    }

    public static <T> WrapperUpdate<T> update(T value, Function<String,Boolean> ignore) {
        return Wrapper.update(value,ignore);
    }

    public static <T> WrapperUpdate<T> update(T value) {
        return Wrapper.update( value);
    }

    /// Wrapper 执行快捷操作
    public static <T> int save(T bean) {
        return Wrapper.save(bean);
    }
    public static <T> int insertOrUpdate(T obj,String idName){
        return Wrapper.insertOrUpdate(obj,idName);
    }
    public static <T> int insertOrUpdate(T obj){
        return Wrapper.insertOrUpdate(obj);
    }
    public static <T> int updateById(T obj){
        return Wrapper.updateById(obj);
    }
    public static <T> int updateById(T obj,String idName){
        return Wrapper.updateById(obj,idName);
    }
    public static <T> T getById(Class<T> c,Object id,String idName){
        return Wrapper.getById(c,id,idName);
    }
    public static <T> T getById(Class<T> c,Object id){
        return Wrapper.getById(c,id);
    }
    public static <T> int removeByIds(Class<T> c,String ids){
        return Wrapper.removeByIds(c,ids);
    }
    public static <T> int removeByIds(Class<T> c,String ids,String idName){
        return Wrapper.removeByIds(c,ids,idName);
    }

    /// Batch 快捷操作
    public static <T> boolean saveBatch(List<T> bean) {
        return Batch.insert(bean, 1000);
    }
    public static <T> boolean saveBatch(List<T> bean, int batchSize) {
        return Batch.insert(bean,batchSize);
    }
    public static boolean batchUpdate(String sql, List<Object[]> valueBeans) {
        return Batch.update(sql, valueBeans, 1000);
    }
    public static boolean batchUpdate(String sql, List<Object[]> valueBeans, int batchSize) {
        return Batch.update(sql, valueBeans, batchSize);
    }
}
