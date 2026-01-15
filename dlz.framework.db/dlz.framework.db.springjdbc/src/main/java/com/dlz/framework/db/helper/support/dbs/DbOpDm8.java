package com.dlz.framework.db.helper.support.dbs;

import com.dlz.comm.util.StringUtils;
import com.dlz.framework.db.dao.IDlzDao;
import com.dlz.framework.db.helper.bean.ColumnInfo;
import com.dlz.framework.db.helper.bean.TableInfo;
import com.dlz.framework.db.helper.support.SqlHelper;
import com.dlz.framework.db.holder.BeanInfoHolder;
import com.dlz.framework.db.holder.DBHolder;
import com.dlz.framework.db.modal.result.ResultMap;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DbOpDm8 extends SqlHelper {
    @Override
    public void createTable(String tableName, Class<?> clazz) {
        // 达梦数据库表名需大写
        String sql = "CREATE TABLE \"" + tableName.toUpperCase() + "\" (ID VARCHAR2(32) NOT NULL PRIMARY KEY)";
        String tableComment = BeanInfoHolder.getTableComment(clazz);
        if (StringUtils.isNotEmpty(tableComment)) {
            sql += " COMMENT ON TABLE \"" + tableName.toUpperCase() + "\" IS '" + tableComment + "'";
        }
        DBHolder.getService().getDao().execute(sql);
    }

    /**
     * 分页SQL（达梦8支持LIMIT OFFSET语法）
     */
    @Override
    public String getLimitSql(long currPage, long pageSize) {
        return " LIMIT " + ((currPage - 1) * pageSize) + " OFFSET " + pageSize;
    }

    @Override
    public Set<String> getTableColumnNames(String tableName) {
        // 达梦系统表查询字段信息
        String sql = "SELECT COLUMN_NAME FROM ALL_TAB_COLUMNS WHERE OWNER = USER AND TABLE_NAME = ?";
        return DBHolder.getService().getDao().getList(sql, tableName.toUpperCase())
                .stream()
                .map(item -> item.getStr("columnName"))
                .collect(Collectors.toSet());
    }

    @Override
    public TableInfo getTableInfo(String tableName) {
        TableInfo tableInfo = new TableInfo();
        tableInfo.setTableName(tableName);

        // 查询表注释
        String sql = "SELECT COMMENTS FROM ALL_TAB_COMMENTS WHERE OWNER = USER AND TABLE_NAME = ?";
        tableInfo.setTableComment(DBHolder.getService().getDao().getFistColumn(sql, String.class, tableName.toUpperCase()));

        // 查询主键信息
        sql = "SELECT COLUMN_NAME" +
                "  FROM ALL_CONSTRAINTS C" +
                "  JOIN ALL_CONS_COLUMNS CC" +
                "    ON C.CONSTRAINT_NAME = CC.CONSTRAINT_NAME" +
                " WHERE C.OWNER = USER" +
                "   AND C.TABLE_NAME = ?" +
                "   AND C.CONSTRAINT_TYPE = 'P'";
        List<String> primaryKeys = DBHolder.getService().getDao().getList(sql, tableName.toUpperCase())
                .stream()
                .map(map -> map.getStr("columnName", ""))
                .collect(Collectors.toList());
        tableInfo.setPrimaryKeys(primaryKeys);


        // 查询字段信息
        sql="   SELECT A.COLUMN_NAME, " +
                "          A.DATA_TYPE," +
                "          C.COMMENTS" +
                "     FROM USER_TAB_COLUMNS A" +
                " LEFT JOIN ALL_COL_COMMENTS C " +
                "       ON C.OWNER = USER" +
                "      AND a.TABLE_NAME = C.TABLE_NAME " +
                "      AND a.COLUMN_NAME = C.COLUMN_NAME" +
                "    WHERE A.TABLE_NAME = ?";
        List<ColumnInfo> columnInfos = DBHolder.getService().getDao().getList(sql, tableName.toUpperCase())
                .stream()
                .map(map -> {
                    ColumnInfo columnInfo = new ColumnInfo();
                    columnInfo.setColumnName(map.getStr("columnName", ""));
                    columnInfo.setColumnType(map.getStr("dataType", ""));
                    columnInfo.setColumnComment(map.getStr("comments", ""));
                    columnInfo.setJavaType(getJavaType(columnInfo.getColumnType()));
                    return columnInfo;
                })
                .collect(Collectors.toList());
        tableInfo.setColumnInfos(columnInfos);
        return tableInfo;
    }

    @Override
    public List<ResultMap> getTableIndexs(String tableName) {
        // 查询索引信息
        String sql = "SELECT INDEX_NAME, COLUMN_NAME FROM ALL_IND_COLUMNS WHERE TABLE_OWNER = USER AND TABLE_NAME = ?";
        return DBHolder.getService().getDao().getList(sql, tableName.toUpperCase());
    }

    @Override
    public void createColumn(String tableName, String name, Field field) {
        String sql = "ALTER TABLE \"" + tableName.toUpperCase() + "\" ADD \"" + name.toUpperCase() + "\" " + getDbClumnType(field);
        String columnComment = BeanInfoHolder.getColumnComment(field);
        if (StringUtils.isNotEmpty(columnComment)) {
            sql += "; COMMENT ON COLUMN \"" + tableName.toUpperCase() + "\".\"" + name.toUpperCase() + "\" IS '" + columnComment + "'";
        }
        DBHolder.getService().getDao().execute(sql);
    }

    @Override
    public void updateDefaultValue(String tableName, String columnName, String value) {
        String sql = "UPDATE \"" + tableName.toUpperCase() + "\" SET \"" + columnName.toUpperCase() + "\" = ? WHERE \"" + columnName.toUpperCase() + "\" IS NULL";
        DBHolder.getService().getDao().update(sql, value);
    }

    @Override
    public String getDbClumnType(Field field) {
        Class<?> clazz = field.getType();
        if (clazz == String.class) {
            return "varchar(255)"; // 达梦推荐使用VARCHAR2
        } else if (clazz == Integer.class || "int".equals(clazz.getCanonicalName())) {
            return "NUMBER(10)"; // 整数类型
        } else if (clazz == Boolean.class || "boolean".equals(clazz.getCanonicalName())) {
            return "NUMBER(1)"; // 布尔类型
        } else if (clazz == Long.class || "long".equals(clazz.getCanonicalName())) {
            return "NUMBER(19)"; // 长整型
        } else if (Number.class.isAssignableFrom(clazz)) {
            return "NUMBER(12, 2)"; // 数值类型
        } else if (Date.class.isAssignableFrom(clazz) || clazz == LocalDateTime.class || clazz == LocalDate.class) {
            return "TIMESTAMP"; // 时间类型
        }
        return "CLOB"; // 默认大文本类型
    }

    private Class<?> getJavaType(String columnType) {
        columnType = columnType.toLowerCase();
        if (columnType.indexOf("char") > -1 || columnType.startsWith("clob") || columnType.startsWith("text")) {
            return String.class;
        } else if (columnType.startsWith("int")) {
            return Integer.class;
        } else if (columnType.startsWith("tinyint")) {
            return Boolean.class;
        } else if (columnType.startsWith("bigint")) {
            return Long.class;
        } else if (columnType.startsWith("decimal") || columnType.startsWith("numeric") || columnType.startsWith("number")) {
            return Double.class;
        } else if (columnType.startsWith("date") || columnType.startsWith("datetime") || columnType.startsWith("timestamp")) {
            return Date.class;
//        } else if (columnType.startsWith("timestamp")) {
//            return LocalDateTime.class;
        } else {
            return Object.class; // 默认类型
        }

    }
}
