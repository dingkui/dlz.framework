package com.dlz.framework.db.helper.support.dbs;

import com.dlz.comm.util.StringUtils;
import com.dlz.comm.util.system.FieldReflections;
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

public class DbOpMysql extends SqlHelper {
    /*
    CREATE TABLE `ds_info`  (
        `id` varchar(32) NOT NULL PRIMARY KEY,
        `PLUGIN_ID` varchar(255) COMMENT '插件id',
        `NAME` varchar(255) COMMENT '数据源名称',
        `URL` varchar(255) COMMENT '数据源连接地址',
        `USERNAME` varchar(255) COMMENT '数据源用户名',
        `PASSWORD` varchar(255) COMMENT '数据源密码',
        `JSON` varchar(255) COMMENT '其他定义',
        `DESCRIPTION` varchar(255) COMMENT '数据源描述',
        `TYPE` varchar(255) COMMENT '数据源类型',
        `STATUS` varchar(255) COMMENT '数据源状态'
    )COMMENT = 'xxxx';
*/
    @Override
    public void createTable(String tableName, Class<?> clazz) {
        String createSql = "CREATE TABLE IF NOT EXISTS `{}` ({}){}";
        final String columns = FieldReflections.getFields(clazz).stream().map(field -> {
            String columnName = BeanInfoHolder.getColumnName(field);
            String column = null;
            if (columnName.equals("")) {
                return column;
            }
            column = " `" + columnName + "` " + getDbClumnType(field);
            String clumnCommont = BeanInfoHolder.getColumnComment(field);
            if (StringUtils.isNotEmpty(clumnCommont)) {
                column += " COMMENT '" + clumnCommont + "'";
            }
            if (BeanInfoHolder.isColumnPk(field)) {
                column += " PRIMARY KEY";
            }
            return column;
        }).filter(column -> column != null)
                .collect(Collectors.joining(","));

        String tableCommont = BeanInfoHolder.getTableComment(clazz);
        if (StringUtils.isNotEmpty(tableCommont)) {
            tableCommont = " COMMENT = '" + tableCommont  + "'";
        }else{
            tableCommont = "";
        }

        String sql = StringUtils.formatMsg(createSql, tableName,columns,tableCommont);

        DBHolder.getDao().execute(sql);
    }

    /**
     * @param currPage 从1开始
     * @param pageSize
          */
    @Override
    public String getLimitSql(long currPage, long pageSize) {
        return " LIMIT " + (currPage - 1) * pageSize + "," + pageSize;
    }

    @Override
    public Set<String> getTableColumnNames(String tableName) {
//        // 获取表所有字段
//        String sql = "SHOW COLUMNS FROM `" + tableName + "`";
//        List<ResultMap> maps = DBHolder.getDao().getList(sql);
//        Set<String> re = new HashSet();
//        maps.forEach(item -> {
//            String field = ValUtil.toStr(item.get("Field"), "");
//            if(field.length()==0){
//                field = ValUtil.toStr(item.get("field"), "");
//            }
//            if(field.length()>0){
//                re.add(field.toUpperCase());
//            }
//        });
//        return re;

        // 构建查询字段信息的SQL语句
        String sql = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = ?";
        // 执行查询并获取结果
        return DBHolder.getDao().getList(sql, tableName).stream().map(item -> item.getStr("columnName")).collect(Collectors.toSet());
    }

    @Override
    public TableInfo getTableInfo(String tableName) {
        // 构建查询表注释的SQL语句
        String sql = "SELECT TABLE_COMMENT FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = ?";
        // 执行查询并获取结果
        TableInfo tableInfo = new TableInfo();
        tableInfo.setTableName(tableName);
        tableInfo.setTableComment(DBHolder.getDao().getFistColumn(sql, String.class, tableName));

        // 获取主键信息
        // 构建查询主键的SQL语句
        sql = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = ? AND CONSTRAINT_NAME = 'PRIMARY'";
        // 执行查询并获取结果
        List<String> primaryKeys = DBHolder.getDao().getList(sql, tableName).stream().map(map -> map.getStr("columnName", "")).collect(Collectors.toList());
        tableInfo.setPrimaryKeys(primaryKeys);

        // 构建查询字段信息的SQL语句
        sql = "SELECT COLUMN_NAME, COLUMN_TYPE, COLUMN_COMMENT FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = ?";
        // 执行查询并获取结果
        List<ColumnInfo> columnInfos = DBHolder.getDao().getList(sql, tableName).stream().map(map -> {
            ColumnInfo columnInfo = new ColumnInfo();
            columnInfo.setColumnName(map.getStr("columnName", ""));
            columnInfo.setColumnType(map.getStr("columnType", ""));
            columnInfo.setColumnComment(map.getStr("columnComment", ""));
            // 转换字段类型为Java类型
            columnInfo.setJavaType(getJavaType(columnInfo.getColumnType()));
            return columnInfo;
        }).collect(Collectors.toList());
        tableInfo.setColumnInfos(columnInfos);
        return tableInfo;
    }



    @Override
    public List<ResultMap> getTableIndexs(String tableName) {
        // 获取表所有索引
        String sql = "SHOW INDEX FROM `" + tableName + "`";
        return DBHolder.getDao().getList(sql);
    }

    @Override
    public void createColumn(String tableName, String name, Field field) {
        String sql = "ALTER TABLE `" + tableName + "` ADD COLUMN `" + name + "` " + getDbClumnType(field);
        String clumnCommont = BeanInfoHolder.getColumnComment(field);
        if (StringUtils.isNotEmpty(clumnCommont)) {
            sql += " COMMENT '" + clumnCommont + "'";
        }
        DBHolder.getDao().execute(sql);
    }

    @Override
    public void updateDefaultValue(String tableName, String columnName, String value) {
        String sql = "SELECT COUNT(*) FROM " + tableName + " WHERE `" + columnName + "` IS NULL";
        Long count = DBHolder.getDao().getFistColumn(sql, Long.class);
        if (count > 0) {
            sql = "UPDATE " + tableName + " SET `" + columnName + "` = ? WHERE `" + columnName + "` IS NULL";
            DBHolder.getDao().update(sql, value);
        }
    }

    @Override
    public String getDbClumnType(Field field) {
        Class<?> classs = field.getType();
        if (classs == String.class) {
            return "varchar(255)";
        } else if (classs == Integer.class || "int".equals(classs.getCanonicalName())) {
            return "int";
        } else if (classs == Boolean.class || "boolean".equals(classs.getCanonicalName())) {
            return "tinyint";
        } else if (classs == Long.class || "long".equals(classs.getCanonicalName())) {
            return "bigint";
        } else if (Number.class.isAssignableFrom(classs)) {
            return "numeric(12, 1)";
        } else if (Date.class.isAssignableFrom(classs)||classs== LocalDateTime.class||classs== LocalDate.class) {
            return "datetime";
        }
        return "text";
    }
    private Class<?> getJavaType(String columnType) {
        columnType = columnType.toLowerCase();
        if (columnType.startsWith("varchar") || columnType.startsWith("char")) {
            return String.class;
        } else if (columnType.startsWith("int")) {
            return Integer.class;
        } else if (columnType.startsWith("tinyint")) {
            return Boolean.class;
        } else if (columnType.startsWith("bigint")) {
            return Long.class;
        } else if (columnType.startsWith("decimal") || columnType.startsWith("numeric")) {
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
