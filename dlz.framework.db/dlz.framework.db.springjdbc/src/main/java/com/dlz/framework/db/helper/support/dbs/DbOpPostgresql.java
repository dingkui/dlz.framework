package com.dlz.framework.db.helper.support.dbs;

import com.dlz.comm.util.StringUtils;
import com.dlz.comm.util.ValUtil;
import com.dlz.framework.db.dao.IDlzDao;
import com.dlz.framework.db.helper.bean.ColumnInfo;
import com.dlz.framework.db.helper.bean.TableInfo;
import com.dlz.framework.db.helper.support.SqlHelper;
import com.dlz.framework.db.helper.util.DbNameUtil;
import com.dlz.framework.db.modal.result.ResultMap;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
public class DbOpPostgresql extends SqlHelper {
    public DbOpPostgresql(IDlzDao jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public void createTable(String tableName, Class<?> clazz) {
        String sql = "CREATE TABLE IF NOT EXISTS public.\"" + tableName + "\" (id VARCHAR(32) NOT NULL PRIMARY KEY)";
        dao.execute(sql);
        String clumnCommont = DbNameUtil.getTableCommont(clazz);
        if (StringUtils.isNotEmpty(clumnCommont)) {
            sql = "COMMENT ON TABLE \"public\".\"" + tableName + "\" IS '" + clumnCommont + "'";
            dao.execute(sql);
        }
    }

    /**
     * @param currPage 从1开始
     * @param pageSize
     * @return
     */
    @Override
    public String getLimitSql(long currPage, long pageSize) {
        return " LIMIT " + pageSize + " OFFSET " + (currPage - 1) * pageSize;
    }

    @Override
    public Set<String> getTableColumnNames(String tableName) {
        // 获取表所有字段
        String sql = "SELECT column_name as name FROM information_schema.columns WHERE table_schema='public' AND table_name='" + tableName.toLowerCase() + "'";
        List<ResultMap> maps = dao.getList(sql);
        Set<String> re = new HashSet();
        maps.forEach(item -> {
            re.add(ValUtil.toStr(item.get("name"), "").toUpperCase());
        });
        return re;
    }


    @Override
    public TableInfo getTableInfo(String tableName) {
        // 构建查询表注释的SQL语句
        String sql = "SELECT obj_description('public." + tableName + "'::regclass) AS TABLE_COMMENT";
        // 执行查询并获取结果
        TableInfo tableInfo = new TableInfo();
        tableInfo.setTableName(tableName);
        tableInfo.setTableComment(dao.getFistColumn(sql, String.class));

        // 构建查询主键的SQL语句
        sql = "SELECT kcu.column_name " +
                "FROM information_schema.table_constraints tc " +
                "JOIN information_schema.key_column_usage kcu " +
                "ON tc.constraint_name = kcu.constraint_name " +
                "AND tc.table_schema = kcu.table_schema " +
                "WHERE tc.table_schema = 'public' " +
                "AND tc.table_name = ? " +
                "AND tc.constraint_type = 'PRIMARY KEY'";
        // 执行查询并获取结果
        List<ResultMap> maps = dao.getList(sql, tableName);
        List<String> primaryKeys = new ArrayList<>();
        for (ResultMap map : maps) {
            primaryKeys.add(ValUtil.toStr(map.get("column_name"), ""));
        }
        tableInfo.setPrimaryKeys(primaryKeys);


        // 构建查询字段信息的SQL语句
        sql = "SELECT a.attname AS COLUMN_NAME, pg_catalog.format_type(a.atttypid, a.atttypmod) AS COLUMN_TYPE, pg_catalog.col_description(a.attrelid, a.attnum) AS COLUMN_COMMENT " +
                "FROM pg_catalog.pg_attribute a " +
                "JOIN pg_catalog.pg_class c ON a.attrelid = c.oid " +
                "JOIN pg_catalog.pg_namespace n ON c.relnamespace = n.oid " +
                "WHERE c.relname = ? AND n.nspname = 'public' AND a.attnum > 0 AND NOT a.attisdropped";
        // 执行查询并获取结果
        maps = dao.getList(sql, tableName);
        List<ColumnInfo> columnInfos = new ArrayList<>();

        for (ResultMap map : maps) {
            ColumnInfo columnInfo = new ColumnInfo();
            columnInfo.setColumnName(ValUtil.toStr(map.get("COLUMN_NAME"), ""));
            columnInfo.setColumnType(ValUtil.toStr(map.get("COLUMN_TYPE"), ""));
            columnInfo.setColumnComment(ValUtil.toStr(map.get("COLUMN_COMMENT"), ""));
            // 转换字段类型为Java类型
            columnInfo.setJavaType(getJavaType(columnInfo.getColumnType()));
            columnInfos.add(columnInfo);
        }
        tableInfo.setColumnInfos(columnInfos);
        return tableInfo;
    }

    @Override
    public List<ResultMap> getTableIndexs(String tableName) {
        // 获取表所有索引
        String sql = "SELECT " + //
                "A.INDEXNAME as name " + //
                "FROM PG_AM B " + //
                "LEFT JOIN PG_CLASS F ON B.OID = F.RELAM " + //
                "LEFT JOIN PG_STAT_ALL_INDEXES E ON F.OID = E.INDEXRELID " + //
                "LEFT JOIN PG_INDEX C ON E.INDEXRELID = C.INDEXRELID " + //
                "LEFT OUTER JOIN PG_DESCRIPTION D ON C.INDEXRELID = D.OBJOID, " + //
                "PG_INDEXES A " + //
                "WHERE " + //
                "A.SCHEMANAME = E.SCHEMANAME AND A.TABLENAME = E.RELNAME AND A.INDEXNAME = E.INDEXRELNAME " + //
                "AND E.SCHEMANAME = 'public' AND E.RELNAME = '" + tableName + "' ";//
        return dao.getList(sql);
    }

    @Override
    public void createColumn(String tableName, String name, Field field) {
        String sql = "ALTER TABLE public." + tableName + " ADD COLUMN " + name + " " + getDbClumnType(field);
        dao.execute(sql);
        String clumnCommont = DbNameUtil.getClumnCommont(field);
        if (StringUtils.isNotEmpty(clumnCommont)) {
            sql = "COMMENT ON COLUMN " + tableName + "." + name + " IS '" + clumnCommont + "'";
            dao.execute(sql);
        }
    }

    @Override
    public void updateDefaultValue(String tableName, String columnName, String value) {
        String sql = "SELECT COUNT(*) FROM " + tableName + " WHERE `" + columnName + "` IS NULL";
        Long count = dao.getFistColumn(sql, Long.class);
        if (count > 0) {
            sql = "UPDATE " + tableName + " SET `" + columnName + "` = ? WHERE `" + columnName + "` IS NULL";
            dao.update(sql, value);
        }
    }

    @Override
    public String getDbClumnType(Field field) {
        Class<?> classs = field.getType();
        if (classs == String.class) {
            return "varchar(255)";
        } else if (classs == Integer.class || "int".equals(classs.getCanonicalName())) {
            return "int2";
        } else if (classs == Boolean.class || "boolean".equals(classs.getCanonicalName())) {
            return "bool";
        } else if (classs == Long.class || "long".equals(classs.getCanonicalName())) {
            return "int4";
        } else if (Number.class.isAssignableFrom(classs)) {
            return "numeric(12, 1)";
        } else if (Date.class.isAssignableFrom(classs)||classs== LocalDateTime.class||classs== LocalDate.class) {
            return "date";
        }
        return "text";
    }

    private Class<?> getJavaType(String columnType) {
        if (columnType.toLowerCase().startsWith("varchar") || columnType.toLowerCase().startsWith("char")) {
            return String.class;
        } else if (columnType.toLowerCase().startsWith("int") || columnType.toLowerCase().startsWith("integer")) {
            return Integer.class;
        } else if (columnType.toLowerCase().startsWith("boolean")) {
            return Boolean.class;
        } else if (columnType.toLowerCase().startsWith("bigint")) {
            return Long.class;
        } else if (columnType.toLowerCase().startsWith("decimal") || columnType.toLowerCase().startsWith("numeric")) {
            return Double.class;
        } else if (columnType.toLowerCase().startsWith("date")) {
            return Date.class;
        } else if (columnType.toLowerCase().startsWith("timestamp")) {
            return LocalDateTime.class;
        } else {
            return Object.class; // 默认类型
        }
    }
}
