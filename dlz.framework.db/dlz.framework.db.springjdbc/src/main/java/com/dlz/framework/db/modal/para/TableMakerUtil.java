package com.dlz.framework.db.modal.para;

import com.dlz.framework.db.annotation.IdType;
import com.dlz.framework.db.annotation.TableId;
import com.dlz.comm.exception.SystemException;
import com.dlz.comm.util.StringUtils;
import com.dlz.comm.util.system.FieldReflections;
import com.dlz.framework.db.annotation.proxy.AnnoProxys;
import com.dlz.framework.db.helper.support.SnowFlake;
import com.dlz.framework.db.holder.BeanInfoHolder;
import com.dlz.framework.db.holder.DBHolder;
import com.dlz.framework.db.holder.SqlHolder;
import com.dlz.framework.db.util.DbConvertUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * sql操作Util
 *
 * @author ding_kui 2010-12-14
 */
@Slf4j
public class TableMakerUtil {
    public static final String MAKER_SQL_INSERT = "insert into ${tableName}(${colums}) values(${values})";
    public static final String MAKER_SQL_DELETE = "delete from ${tableName} ${where}";
    public static final String MAKER_SQL_UPDATE = "update ${tableName} t set ${sets} ${where}";
    public static final String MAKER_SQL_SEARCHE = "select ${colums} from ${tableName} t ${where} ${otherwhere}";

    private static final String MAKER_TABLENAME = "tableName";
    private static final String MAKER_COLUMS = "colums";
    private static final String MAKER_VALUES = "values";
    private static final String MAKER_STR_SETS = "sets";
    private static final String MAKER_WHERE = "where";

    /**
     * 生成查询条件sql
     *
          */
    public static void buildSql(ATableMaker maker) {
        maker.getSqlItem().setSqlKey(maker.getSql());
        maker.addPara(MAKER_TABLENAME, maker.getTableName());
        if (maker instanceof AQuery) {
            buildWhere((AQuery) maker);
        }
        if (maker instanceof TableQuery) {
            buildWhereColums((TableQuery) maker);
        }
        if (maker instanceof TableUpdate) {
            buildUpdateSql((TableUpdate) maker);
        }
        if (maker instanceof TableInsert) {
            buildInsertSql((TableInsert) maker);
        }
    }

    /**
     * 生成查询条件sql
     *
          */
    public static void buildWhereColums(TableQuery maker) {
        maker.addPara(MAKER_COLUMS, maker.colums);
    }

    /**
     * 生成查询条件sql
     *
          */
    public static void buildWhere(AQuery maker) {
        final String logicDeleteField = SqlHolder.properties.getLogicDeleteField();
        if (BeanInfoHolder.isColumnExists(maker.getTableName(), logicDeleteField)) {
            if (!maker.where().isContainCondition(logicDeleteField)) {
                maker.where().eq(logicDeleteField, 0);
            }
        }
        String where = maker.where().getRunsql(maker);
        if (!maker.isAllowFullQuery() && StringUtils.isEmpty(where)) {
            where = "where false";
        }
        maker.addPara(MAKER_WHERE, where);
    }

    /**
     * 生成掺入sql
     *
          */
    public static void buildInsertSql(TableInsert maker) {
        StringBuilder sbColums = new StringBuilder();
        StringBuilder sbValues = new StringBuilder();
        if(maker.insertValues.isEmpty()){
            throw new SystemException("插入字段信息未设置");
        }
        maker.insertValues.entrySet().forEach(e -> {
            String paraName = e.getKey();
            Object value = e.getValue();
            String clumnName = paraName.replaceAll("`", "");

            if (sbColums.length() > 0) {
                sbColums.append(',');
                sbValues.append(',');
            }
            sbColums.append(paraName);
            if (value instanceof String) {
                String v = ((String) value);
                if (v.startsWith("sql:")) {
                    sbValues.append(DbConvertUtil.toDbColumnName(v.substring(4)));
                    return;
                }
            }
            sbValues.append("#{").append(clumnName).append("}");
            if (value == null)
                value = "";
            maker.addPara(clumnName, DbConvertUtil.getVal4Db(maker.getTableName(), clumnName, value));
        });
        maker.addPara(MAKER_COLUMS, sbColums.toString());
        maker.addPara(MAKER_VALUES, sbValues.toString());
        maker.addPara(MAKER_TABLENAME, maker.getTableName());
    }

    /**
     * 生成更新信息
     *
          */
    public static void buildUpdateSql(TableUpdate maker) {
        StringBuilder sbSets = new StringBuilder();
        if(maker.updateSets.isEmpty()){
            throw new SystemException("更新字段信息未设置");
        }
        maker.updateSets.entrySet().forEach(e -> {
            String paraName = e.getKey();
            Object value = e.getValue();
            String clumnName = paraName.replaceAll("`", "");

            if (sbSets.length() > 0) {
                sbSets.append(",");
            }
            sbSets.append(paraName);
            sbSets.append('=');
            if (value instanceof String) {
                String v = ((String) value);
                if (v.startsWith("sql:")) {
                    sbSets.append(DbConvertUtil.toDbColumnName(v.substring(4)));
                    return;
                }
            }
            sbSets.append("#{").append(clumnName).append("}");
            maker.addPara(clumnName, DbConvertUtil.getVal4Db(maker.getTableName(), clumnName, value));
        });
        maker.addPara(MAKER_STR_SETS, sbSets.toString());
    }


    public static String buildInsertSql(String dbName, List<Field> fields) {
        List<String> fieldsPart = new ArrayList<>();
        List<String> placeHolder = new ArrayList<>();
        for (Field field : fields) {
            String dbClumnName = BeanInfoHolder.getColumnName(field);
            if (!dbClumnName.equals("")) {
                fieldsPart.add(dbClumnName);
                placeHolder.add("?");
            }
        }
        return "INSERT INTO " + dbName + " (" + StringUtils.join(",", fieldsPart) + ") VALUES (" + StringUtils.join(",", placeHolder) + ")";
    }

    public static Object[] buildInsertParams(String dbName,Object object, List<Field> fields) {
        List<Object> params = new ArrayList<>();
        for (Field field : fields) {
            String dbClumnName = BeanInfoHolder.getColumnName(field);
            if (!dbClumnName.equals("")) {
                Object value = FieldReflections.getValue(object, field);
                if(value==null){
                    value = TableMakerUtil.getIdValue(field, dbName);
                }
                params.add(value);
            }
        }
        return params.toArray();
    }

    public static String buildUpdateSql(String dbName, List<Field> fields) {
        List<String> fieldsPart = new ArrayList<String>();
        for (Field field : fields) {
            String dbClumnName = BeanInfoHolder.getColumnName(field);
            if (!dbClumnName.equals("id") && !dbClumnName.equals("")) {
                fieldsPart.add(dbClumnName + "=?");
            }
        }
        return "UPDATE " + dbName + " SET " + StringUtils.join(",", fieldsPart) + " WHERE id = ?";
    }

    public static Object[] buildUpdateParams(Object object, List<Field> fields) {
        List<Object> params = new ArrayList<Object>();
        for (Field field : fields) {
            String dbClumnName = BeanInfoHolder.getColumnName(field);
            if (!dbClumnName.equals("id") && !dbClumnName.equals("")) {
                params.add(FieldReflections.getValue(object, field));
            }
        }
        params.add(FieldReflections.getValue(object, "id", true));
        return params.toArray();
    }

    public static Object getIdValue(Field field, String tableName) {
        final TableId annotation = field.getAnnotation(TableId.class);
        IdType type;
        if (annotation != null) {
            type = annotation.type();
        }else{
            type = AnnoProxys.MybatisPlusIdType.type(field);
        }
        if (type==null || type == IdType.AUTO||type == IdType.INPUT) {
            return null;
        } else {
            final String columnName = BeanInfoHolder.getColumnName(field);
            if (type == IdType.ASSIGN_ID) {
                return SnowFlake.id();
            } else if (type == IdType.ASSIGN_UUID) {
                return UUID.randomUUID().toString().replace("-", "");
            } else if (type == IdType.SEQ) {
                return DBHolder.sequence(tableName, 1l);
            } else {
                throw new SystemException(columnName + " idType is " + type + " but null");
            }
        }
    }

}
