package com.dlz.framework.db.helper.support;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IDbOp {
    void createTable(String tableName, Class<?> clazz);

    /**
     * 取得翻页sql后缀
     * @param currPage 从1开始
     * @param pageSize
     * @return
     */
    String getLimitSql(int currPage, int pageSize);
    /**
     * 获取表所有字段
     * @param tableName
     * @return
     */
    Set<String> getTableColumnNames(String tableName);

    /**
     * 获取表所有索引
     * @param tableName
     * @return
     */
    List<Map<String, Object>> getTableIndexs(String tableName);

    /**
     * 根据bean属性创建字段
     * @param tableName
     * @param name
     * @param field
     */
    void createColumn(String tableName, String name, Field field);

    /**
     * 更新默认值
     * @param tableName
     * @param columnName
     * @param value
     */
    void updateDefaultValue(String tableName, String columnName, String value);

    /**
     * 根据属性取得数据库字段属性
     * @param field
     * @return
     */
    String getDbClumnType(Field field);
}
