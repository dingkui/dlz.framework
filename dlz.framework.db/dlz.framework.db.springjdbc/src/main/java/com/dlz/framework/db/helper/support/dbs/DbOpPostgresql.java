package com.dlz.framework.db.helper.support.dbs;

import com.dlz.comm.util.StringUtils;
import com.dlz.comm.util.ValUtil;
import com.dlz.framework.db.dao.IDlzDao;
import com.dlz.framework.db.helper.support.SqlHelper;
import com.dlz.framework.db.helper.util.DbNameUtil;
import com.dlz.framework.db.modal.ResultMap;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    public String getLimitSql(int currPage, int pageSize) {
        return " LIMIT " + pageSize + " OFFSET " + (currPage - 1) * pageSize;
    }

    @Override
    public Set<String> getTableColumnNames(String tableName) {
        // 获取表所有字段
        String sql = "SELECT column_name as name FROM information_schema.columns WHERE table_schema='public' AND table_name='" + tableName.toLowerCase() + "'";
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
        Long count = dao.getObj(sql, Long.class);
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


}
