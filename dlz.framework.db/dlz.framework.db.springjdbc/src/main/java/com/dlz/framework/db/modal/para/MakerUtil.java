package com.dlz.framework.db.modal.para;

import com.dlz.comm.util.StringUtils;
import com.dlz.comm.util.system.FieldReflections;
import com.dlz.framework.db.convertor.DbConvertUtil;
import com.dlz.framework.db.holder.BeanInfoHolder;
import com.dlz.framework.db.holder.DBHolder;
import com.dlz.framework.db.holder.SqlHolder;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


/**
 * sql操作Util
 *
 * @author ding_kui 2010-12-14
 */
@Slf4j
public class MakerUtil {
    public static final String MAKER_SQL_INSERT = "insert into ${tableName}(${colums}) values(${values})";
    public static final String MAKER_SQL_DELETE = "delete from ${tableName} t ${where} ${otherwhere}";
    public static final String MAKER_SQL_UPDATE = "update ${tableName} t set ${sets} ${where} ${otherwhere}";
    public static final String MAKER_SQL_SEARCHE = "select ${colums} from ${tableName} t ${where} ${otherwhere}";

    private static final String MAKER_TABLENAME = "tableName";
    private static final String MAKER_COLUMS = "colums";
    private static final String MAKER_VALUES = "values";
    private static final String MAKER_STR_SETS = "sets";
    private static final String MAKER_WHERE = "where";
    /**
     * 生成查询条件sql
     *
     * @return
     */
    public static void buildSql(AMaker maker) {
        maker.getSqlItem().setSqlKey(maker.getSql());
        maker.addPara(MAKER_TABLENAME, maker.getTableName());
        if(maker instanceof AMakerSearch){
            buildWhere((AMakerSearch)maker);
        }
        if(maker instanceof MakerQuery){
            buildWhereColums((MakerQuery)maker);
        }
        if(maker instanceof MakerUpdate){
            buildUpdateSql((MakerUpdate)maker);
        }
        if(maker instanceof MakerInsert){
            buildInsertSql((MakerInsert)maker);
        }
    }

    /**
     * 生成查询条件sql
     *
     * @return
     */
    public static void buildWhereColums(MakerQuery maker) {
        maker.addPara(MAKER_COLUMS, maker.colums);
    }
    /**
     * 生成查询条件sql
     *
     * @return
     */
    public static void buildWhere(AMakerSearch maker) {
        final String logicDeleteField = SqlHolder.properties.getLogicDeleteField();
        if(BeanInfoHolder.isColumnExists(maker.getTableName(), logicDeleteField)){
            if(!maker.where().isContainCondition(logicDeleteField)){
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
     * @return
     */
    public static void buildInsertSql(MakerInsert maker) {
        StringBuilder sbColums = new StringBuilder();
        StringBuilder sbValues = new StringBuilder();
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
                    sbValues.append(DbConvertUtil.str2Clumn(v.substring(4)));
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
     * @return
     */
    public static void buildUpdateSql(MakerUpdate maker) {
        StringBuilder sbSets = new StringBuilder();
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
                    sbSets.append(DbConvertUtil.str2Clumn(v.substring(4)));
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
                fieldsPart.add("`" + dbClumnName + "`");
                placeHolder.add("?");
            }
        }
        return  "INSERT INTO `" + dbName + "` (" + StringUtils.join(",", fieldsPart) + ") VALUES (" + StringUtils.join(",", placeHolder) + ")";
    }
    public static Object[] buildInsertParams(Object object, List<Field> fields) {
        List<Object> params = new ArrayList<>();
        for (Field field : fields) {
            String dbClumnName = BeanInfoHolder.getColumnName(field);
            if (!dbClumnName.equals("")) {
                params.add(FieldReflections.getValue(object, field));
            }
        }
        return params.toArray();
    }
    public static String buildUpdateSql(String dbName, List<Field> fields) {
        List<String> fieldsPart = new ArrayList<String>();
        for (Field field : fields) {
            String dbClumnName = BeanInfoHolder.getColumnName(field);
            if (!dbClumnName.equals("id") && !dbClumnName.equals("")) {
                fieldsPart.add("`" + dbClumnName + "`=?");
            }
        }
        return  "UPDATE `" + dbName + "` SET " + StringUtils.join(",", fieldsPart) + " WHERE id = ?";
    }
    public static Object[] buildUpdateParams(Object object, List<Field> fields) {
        List<Object> params = new ArrayList<Object>();
        for (Field field : fields) {
            String dbClumnName = BeanInfoHolder.getColumnName(field);
            if (!dbClumnName.equals("id") && !dbClumnName.equals("")) {
                params.add(FieldReflections.getValue(object, field));
            }
        }
        params.add(FieldReflections.getValue(object, "id",true));
        return params.toArray();
    }
}
