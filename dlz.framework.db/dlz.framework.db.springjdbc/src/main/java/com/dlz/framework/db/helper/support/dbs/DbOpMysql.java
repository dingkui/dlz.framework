package com.dlz.framework.db.helper.support.dbs;

import com.dlz.comm.util.StringUtils;
import com.dlz.comm.util.ValUtil;
import com.dlz.framework.db.dao.IDlzDao;
import com.dlz.framework.db.helper.support.SqlHelper;
import com.dlz.framework.db.helper.util.DbNameUtil;
import com.dlz.framework.db.modal.ResultMap;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DbOpMysql extends SqlHelper {
    public DbOpMysql(IDlzDao jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public void createTable(String tableName, Class<?> clazz) {
        String sql = "CREATE TABLE IF NOT EXISTS `" + tableName + "` (id VARCHAR(32) NOT NULL PRIMARY KEY)";
        String clumnCommont = DbNameUtil.getTableCommont(clazz);
        if (StringUtils.isNotEmpty(clumnCommont)) {
            sql += " COMMENT = '" + clumnCommont + "'";
        }
        dao.execute(sql);
    }

    /**
     * @param currPage 从1开始
     * @param pageSize
     * @return
     */
    @Override
    public String getLimitSql(int currPage, int pageSize) {
        return " LIMIT " + (currPage - 1) * pageSize + "," + pageSize;
    }

    @Override
    public Set<String> getTableColumnNames(String tableName) {
        // 获取表所有字段
        String sql = "SHOW COLUMNS FROM `" + tableName + "`";
        List<ResultMap> maps = dao.getList(sql);
        Set<String> re = new HashSet();
        maps.forEach(item -> {
            re.add(ValUtil.getStr(item.get("Field"), "").toUpperCase());
            re.add(ValUtil.getStr(item.get("field"), "").toUpperCase());
        });
        return re;
    }

    @Override
    public List<ResultMap> getTableIndexs(String tableName) {
        // 获取表所有索引
        String sql = "SHOW INDEX FROM `" + tableName + "`";
        return dao.getList(sql);
    }

    @Override
    public void createColumn(String tableName, String name, Field field) {
        String sql = "ALTER TABLE `" + tableName + "` ADD COLUMN `" + name + "` " + getDbClumnType(field);
        String clumnCommont = DbNameUtil.getClumnCommont(field);
        if (StringUtils.isNotEmpty(clumnCommont)) {
            sql += " COMMENT '" + clumnCommont + "'";
        }
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

    @Override
    public String getDbClumnType(Field field) {
        Class<?> classs = field.getType();
        if (classs == String.class) {
            return "varchar(255)";
        } else if (classs == Integer.class || "int".equals(classs.getCanonicalName())) {
            return "integer(10)";
        } else if (classs == Boolean.class || "boolean".equals(classs.getCanonicalName())) {
            return "integer(1)";
        } else if (classs == Long.class || "long".equals(classs.getCanonicalName())) {
            return "integer(12)";
        } else if (Number.class.isAssignableFrom(classs)) {
            return "numeric(12, 1)";
        } else if (Date.class.isAssignableFrom(classs)||classs== LocalDateTime.class||classs== LocalDate.class) {
            return "date";
        }
        return "text";
    }
}
