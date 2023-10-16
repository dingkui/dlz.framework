package com.dlz.framework.db.helper.support.dbs;

import com.dlz.comm.util.ValUtil;
import com.dlz.framework.db.dao.IDlzDao;
import com.dlz.framework.db.helper.support.SqlHelper;
import com.dlz.framework.db.modal.ResultMap;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
}
