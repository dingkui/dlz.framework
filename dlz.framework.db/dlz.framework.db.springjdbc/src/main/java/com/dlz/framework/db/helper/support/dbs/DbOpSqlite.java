package com.dlz.framework.db.helper.support.dbs;

import com.dlz.comm.util.ValUtil;
import com.dlz.framework.db.dao.IDlzDao;
import com.dlz.framework.db.helper.bean.ColumnInfo;
import com.dlz.framework.db.helper.bean.TableInfo;
import com.dlz.framework.db.helper.support.SqlHelper;
import com.dlz.framework.db.modal.ResultMap;

import java.lang.reflect.Field;
import java.util.*;

public class DbOpSqlite extends SqlHelper {
    public DbOpSqlite(IDlzDao jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public void createTable(String tableName, Class<?> clazz) {
        String sql = "CREATE TABLE IF NOT EXISTS `" + tableName + "` (id VARCHAR(32) NOT NULL PRIMARY KEY)";
        dao.execute(sql);
    }

    @Override
    public String getLimitSql(int currPage, int pageSize) {
        return " LIMIT " + (currPage - 1) * pageSize + "," + pageSize;
    }

    @Override
    public Set<String> getTableColumnNames(String tableName) {
        // 获取表所有字段
        String sql = "PRAGMA TABLE_INFO(`" + tableName + "`)";
        List<ResultMap> maps = dao.getList(sql);
        Set<String> re = new HashSet();
        maps.forEach(item -> {
            re.add(ValUtil.getStr(item.get("name"), "").toUpperCase());
        });
        return re;
    }

    @Override
    public TableInfo getTableInfo(String tableName) {
        // 获取表注释
        String sql = "SELECT table_comment FROM table_comments WHERE table_name = ?";
        String tableComment = dao.getClumn(sql, String.class, tableName);

        // 构建查询主键的SQL语句
        sql = "PRAGMA table_info(`" + tableName + "`)";
        // 执行查询并获取结果
        List<ResultMap> maps = dao.getList(sql);
        List<String> primaryKeys = new ArrayList<>();

        for (ResultMap map : maps) {
            int pk = ValUtil.getInt(map.get("pk"), 0);
            if (pk == 1) {
                primaryKeys.add(ValUtil.getStr(map.get("name"), ""));
            }
        }

        // 获取字段信息
        sql = "PRAGMA TABLE_INFO(`" + tableName + "`)";
        maps = dao.getList(sql);
        List<ColumnInfo> columnInfos = new ArrayList<>();

        for (ResultMap map : maps) {
            ColumnInfo columnInfo = new ColumnInfo();
            columnInfo.setColumnName(ValUtil.getStr(map.get("name"), ""));
            columnInfo.setColumnType(ValUtil.getStr(map.get("type"), ""));
            // SQLite doesn't support column comments directly, so set it to empty
            columnInfo.setColumnComment("");
            // 转换字段类型为Java类型
            columnInfo.setJavaType(getJavaType(columnInfo.getColumnType()));
            columnInfos.add(columnInfo);
        }

        TableInfo tableInfo = new TableInfo();
        tableInfo.setTableName(tableName);
        tableInfo.setTableComment(tableComment);
        tableInfo.setColumnInfos(columnInfos);
        tableInfo.setPrimaryKeys(primaryKeys);

        return tableInfo;
    }

    @Override
    public List<ResultMap> getTableIndexs(String tableName) {
        // 获取表所有索引
        String sql = "PRAGMA INDEX_LIST(`" + tableName + "`)";
        return dao.getList(sql);
    }

    @Override
    public void createColumn(String tableName, String name, Field field) {
        String sql = "ALTER TABLE `" + tableName + "` ADD COLUMN `" + name + "` " + getDbClumnType(field);
        dao.execute(sql);
    }

    @Override
    public void updateDefaultValue(String tableName, String columnName, String value) {
        String sql = "SELECT COUNT(*) FROM " + tableName + " WHERE `" + columnName + "` IS NULL";
        Long count = dao.getClumn(sql, Long.class);
        if (count > 0) {
            sql = "UPDATE " + tableName + " SET `" + columnName + "` = ? WHERE `" + columnName + "` IS NULL";
            dao.update(sql, value);
        }
    }

    //    1.NULL：空值。
    //    2.INTEGER：带符号的整型，具体取决有存入数字的范围大小。
    //    3.REAL：浮点数字，存储为8-byte IEEE浮点数。
    //    4.TEXT：字符串文本。
    //    5.BLOB：二进制对象。
    @Override
    public String getDbClumnType(Field field) {
        Class<?> classs = field.getType();
        if (classs == String.class) {
            return "TEXT";
        } else if (classs == Integer.class || "int".equals(classs.getCanonicalName())) {
            return "INTEGER";
        } else if (classs == Boolean.class || "boolean".equals(classs.getCanonicalName())) {
            return "TEXT";
        } else if (classs == Long.class || "long".equals(classs.getCanonicalName())) {
            return "INTEGER";
        } else if (Number.class.isAssignableFrom(classs)) {
            return "REAL";
        } else if (Date.class.isAssignableFrom(classs)) {
            return "TEXT";
        }
        return "TEXT";
    }
    private Class<?> getJavaType(String columnType) {
        if (columnType.equalsIgnoreCase("TEXT")) {
            return String.class;
        } else if (columnType.equalsIgnoreCase("INTEGER")) {
            return Integer.class;
        } else if (columnType.equalsIgnoreCase("REAL")) {
            return Double.class;
        } else if (columnType.equalsIgnoreCase("BLOB")) {
            return byte[].class;
        } else {
            return Object.class; // 默认类型
        }
    }
}
