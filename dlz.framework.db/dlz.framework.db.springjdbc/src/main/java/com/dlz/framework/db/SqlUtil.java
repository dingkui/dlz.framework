package com.dlz.framework.db;

import com.dlz.comm.exception.DbException;
import com.dlz.comm.exception.SystemException;
import com.dlz.comm.json.JSONMap;
import com.dlz.comm.util.*;
import com.dlz.framework.db.enums.ParaTypeEnum;
import com.dlz.framework.db.holder.SqlHolder;
import com.dlz.framework.db.modal.items.JdbcItem;
import com.dlz.framework.db.modal.items.SqlItem;
import com.dlz.framework.db.modal.para.ParaJdbc;
import com.dlz.framework.db.modal.para.ParaMap;
import com.dlz.framework.db.modal.para.AMaker;
import com.dlz.framework.db.modal.para.MakerUtil;
import com.dlz.framework.db.modal.result.Page;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * sql操作Util
 *
 * @author ding_kui 2010-12-14
 */
@Slf4j
public class SqlUtil {
    public final static String SU_STR_TABLE_NM = "SU_STR_TABLE_NM";
    public final static String SU_STR_UPDATE_KEYS = "SU_STR_UPDATE_KEYS";

    /**
     * 参数匹配符：如  ?
     */
    private static Pattern PATTERN_PARA = Pattern.compile("\\?");
    /**
     * 替换内容匹配符：如  ${bb}
     */
    private static Pattern PATTERN_REPLACE = Pattern.compile("\\$\\{(\\w[\\.\\w]*)\\}");
    /**
     * 预处理匹配符：如   #{aa}
     */
    private static Pattern PATTERN_PREPARE = Pattern.compile("#\\{(\\w+[\\.\\w]*)\\}");
    /**
     * 条件判断符号（只用作条件判断，不做输出）：如   ^#{cc}
     */
    private static Pattern PATTERN_NONE = Pattern.compile("\\^#\\{(\\w[\\.\\w]*)\\}");
    /**
     * 条件语句匹配 ：如   [xxx #{aa} ${bb} ^#{cc}]
     */
    private static Pattern PATTERN_CONDITION = Pattern.compile("\\[([^\\^][^\\[\\]]*)\\]");
    /**
     * 是否数字
     */
    private static Pattern PATTERN_ISNUM = Pattern.compile("^[\\d\\.-]+$");

    /**
     * 转换mybatisSQl为jdbcSql
     *
     * @return
     * @throws Exception
     * @author dk 2015-04-09
     */
    public static JdbcItem dealParmToJdbc(ParaMap paraMap) {
        return dealToJdbcSql(paraMap.getSqlItem().getSqlRun(), paraMap.getPara());
    }
    /**
     * 转换mybatisSQl为jdbcSql
     *
     * @return
     * @throws Exception
     * @author dk 2015-04-09
     */
    private static JdbcItem dealToJdbcSql(String sqlRun, JSONMap para) {
        List<Object> paraList = new ArrayList<Object>();
        StringBuffer sb = new StringBuffer();
        int beginIndex = 0;
        Matcher mat = PATTERN_PREPARE.matcher(sqlRun);
        while (mat.find()) {
            String _startStr = sqlRun.substring(beginIndex, mat.start());
            String group = mat.group(1);
            Object jdbcParaItem = JacksonUtil.at(para, group);
            beginIndex = mat.end();

            sb.append(_startStr);
            sb.append("?");
            paraList.add(jdbcParaItem);
        }
        sb.append(sqlRun.substring(beginIndex));
        return JdbcItem.of(sb.toString(),paraList.toArray());
    }



    /**
     * 取得可以直接运行的sql
     *
     * @param jdbcSql
     * @param paraList
     * @return
     * @throws Exception
     * @author dk 2015-04-09
     */
    public static String getRunSqlByJdbc(String jdbcSql,Object[] paraList) {
        if(jdbcSql == null){
            throw new DbException("jdbcSql不应该为空", 1002);
        }
        StringBuffer sbRunSql = new StringBuffer();
        int beginIndex = 0;
        Matcher mat = PATTERN_PARA.matcher(jdbcSql);
        int index=0;
        while (mat.find()) {
            String _startStr = jdbcSql.substring(beginIndex, mat.start());
            Object jdbcParaItem = paraList[index++];
            beginIndex = mat.end();
            sbRunSql.append(_startStr);
            if(jdbcParaItem instanceof Number){
                sbRunSql.append(jdbcParaItem);
            }else{
                sbRunSql.append("'"+ValUtil.toStr(jdbcParaItem)+"'");
            }
        }
        sbRunSql.append(jdbcSql.substring(beginIndex));
        return sbRunSql.toString();
    }

    /**
     * 转换参数
     *
     * @param paraMap
     * @param dealType 1:普通sql 2:查询条数 3：翻页
     * @return
     * @throws Exception
     * @author dk 2015-04-09
     */
    public static SqlItem dealParm(ParaMap paraMap, int dealType) {
        SqlItem sqlItem = paraMap.getSqlItem();
        if(sqlItem.getSqlKey() == null && paraMap instanceof AMaker){
             MakerUtil.buildSql((AMaker) paraMap);
        }
        if (sqlItem.getSqlKey() != null){
            String sql = sqlItem.getSqlDeal();
            String sqlInput = sqlItem.getSqlKey();
            if (sql == null && sqlInput != null) {
                sql = createSqlDeal(paraMap.getPara(), sqlInput);
                sqlItem.setSqlDeal(sql);
            }
            switch (dealType) {
                case 1:
                    sqlItem.setSqlRun(sqlItem.getSqlDeal());
                    break;
                case 2:
                    sqlItem.setSqlRun(SqlUtil.getCntSql(sqlItem.getSqlDeal()));
                    break;
                case 3:
                    sqlItem.setSqlRun(SqlUtil.getPageSql(paraMap));
                    break;
            }
        }
        return sqlItem;
    }
    /**
     * 转换参数
     *
     * @param paraMap
     * @param dealType 1:普通sql 2:查询条数 3：翻页
     * @return
     * @throws Exception
     * @author dk 2015-04-09
     */
    public static JdbcItem dealJdbc(ParaJdbc paraMap, int dealType) {
        String sql = paraMap.getSqlItem().getSqlDeal();
        Object[] paras = paraMap.getParas();
        switch (dealType) {
            case 1:
                return JdbcItem.of(sql,paras);
            case 2:
                return JdbcItem.of(SqlUtil.getCntSql(sql),paras);
            case 3:
                return SqlUtil.getPageSql(paraMap);
        }
        throw new DbException("dealType错误", 1002);
    }

//    /**
//     * 转换参数
//     *
//     * @param sql
//     * @param para
//     * @return
//     * @throws Exception
//     * @author dk 2015-04-09
//     */
//    public static ParaMapBase getParmMap(String sql, Object... para) {
//        ParaMapBase paraMap = new ParaMapBase(null);
//        SqlItem sqlItem = paraMap.getSqlItem();
//        sqlItem.setSqlJdbc(sql);
//        sqlItem.setSqlJdbcPara(para);
//        return paraMap;
//    }

    /**
     * 创建执行sql(带替换符)
     *
     * @param para
     * @param sql
     * @return
     * @throws Exception
     * @author dk 2015-04-09
     */
    private static String createSqlDeal(Map<String, Object> para, String sql) {
        sql = SqlHolder.getSql(sql);
        sql = getConditionStr(sql, para);
        sql = replaceSql(sql, para, 0);
        sql = sql.replaceAll("[\\s]+", " ");
        return sql;
    }

    /**
     * 转换成翻页sql
     *
     * @return
     * @throws Exception
     */
    public static String getPageSql(ParaMap paraMap) {
        SqlItem sqlItem = paraMap.getSqlItem();
        String sqlDeal = sqlItem.getSqlDeal();
        if(sqlDeal == null){
            throw new DbException("sqlDeal不应该为空", 1002);
        }
        Page page = paraMap.getPage();
        if (page == null) {
            sqlItem.setSqlRun(sqlDeal);
            return sqlDeal;
        }
        final VAL<String, JSONMap> pageSql = pageSql(sqlDeal, page);
        paraMap.addParas(pageSql.v2);
        return pageSql.v1;
    }
    /**
     * 转换成翻页sql
     *
     * @return
     * @throws Exception
     */
    private static VAL<String, JSONMap> pageSql(String sql, Page page) {
        String _orderBy = page.getSortSql();
        Long _begin;
        Long _end;
        Long _pageSize= page.getSize();

        if(page.getSize()==0){
            _begin = null;
            _end = null;
        }else{
            //current=0表示不分页
            if (page.getCurrent() == 0) {
                _begin = null;
                _pageSize = null;
                _end = null;
            } else {
                _begin = page.getCurrent() * page.getSize()-page.getSize();
                _end = _begin + page.getSize();
            }
        }
        JSONMap p = new JSONMap();
        p.put("_sql", sql);
        p.put("_begin", _begin);
        p.put("_end", _end);
        p.put("_pageSize", _pageSize);
        p.put("_orderBy", _orderBy);
        String sqlPage = createSqlDeal(p, "key.comm.pageSql");
        return VAL.of(sqlPage,p);
    }
    /**
     * 转换成翻页sql
     *
     * @return
     * @throws Exception
     */
    public static JdbcItem getPageSql(ParaJdbc paraMap) {
        String sqlDeal = paraMap.getSqlItem().getSqlDeal();
        if(sqlDeal == null){
            throw new DbException("sqlDeal不应该为空", 1002);
        }
        Page page = paraMap.getPage();
        final Object[] paras = paraMap.getParas();
        if (page == null) {
            return JdbcItem.of(sqlDeal, paras);
        }
        final VAL<String, JSONMap> pageSql = pageSql(sqlDeal, page);
        JdbcItem jdbcItem = dealToJdbcSql(pageSql.v1, pageSql.v2);
        Object[] jdbcPara = new Object[jdbcItem.paras.length + paras.length];
        for (int i = 0; i < paras.length; i++) {
            jdbcPara[i] = paras[i];
        }
        for (int i = 0; i < jdbcItem.paras.length; i++) {
            jdbcPara[i+paras.length] = jdbcItem.paras[i];
        }
        return JdbcItem.of(jdbcItem.sql, jdbcPara);
    }
    /**
     * 转换成查询条数sql
     *
     * @return
     * @throws Exception
     */
    public static String getCntSql(String sql) {
        int from = sql.toLowerCase().indexOf(" from ");
        if(from == -1){
            throw new DbException("sql语句无from：" + sql, 1002);
        }
        return "select count(1) from" + sql.substring(from + 5);
    }

    /**
     * sql 语句中 ${aa} 的内容进行文本替换
     *
     * @param sql
     * @param m
     * @param replaceTimes
     * @return
     */
    public static String replaceSql(String sql, Map<String, Object> m, int replaceTimes) {
        int length = sql.length();
        if (length > 10000 || replaceTimes++ > 3000) {
            throw new DbException("sql过长或出现引用死循环！", 1002);
        }
        Matcher mat = PATTERN_REPLACE.matcher(sql);
        int start = 0;
        StringBuffer sb = new StringBuffer();
        while (mat.find()) {
            String key = mat.group(1);
            Object o = JacksonUtil.at(m, key);
            if (o == null && key.startsWith("key.")) {
                o = getConditionStr(SqlHolder.getSql(key), m);
            }
            String matStr = "";
            if (o != null) {
                if (o instanceof Object[] || o instanceof Collection) {
                    matStr = getSqlInStr(o);
                } else {
                    matStr = String.valueOf(o == null ? "" : o);
                }
            }
            sb.append(sql, start, mat.start());
            sb.append(replaceSql(matStr, m, replaceTimes));
            start = mat.end();
        }
        if (start == 0) {
            return sql;
        }
        sb.append(sql, start, length);
        return replaceSql(sb.toString(), m, replaceTimes);
    }

    /**
     * sql 语句中 [] 提交内容进行处理
     *
     * @param sql
     * @param m
     * @return
     */
    public static String getConditionStr(String sql, Map<String, Object> m) {
        Matcher mat = PATTERN_CONDITION.matcher(sql);
        int start = 0;
        StringBuffer sb = new StringBuffer();
        while (mat.find()) {
            String conditionInfo = mat.group(1);
            boolean append = false;

            Matcher mat2 = PATTERN_PREPARE.matcher(conditionInfo);
            while (mat2.find()) {
                if (isNotEmpty(m, mat2.group(1))) {
                    append = true;
                    break;
                }
            }
            if (!append) {
                Matcher mat3 = PATTERN_REPLACE.matcher(conditionInfo);
                while (mat3.find()) {
                    if (isNotEmpty(m, mat3.group(1))) {
                        append = true;
                        break;
                    }
                }
            }

            sb.append(sql, start, mat.start());
            if (append) {
                sb.append(PATTERN_NONE.matcher(conditionInfo).replaceAll(""));
            }
            start = mat.end();
        }

        if (start == 0) {
            return sql;
        }
        sb.append(sql, start, sql.length());
        return getConditionStr(sb.toString(), m);
    }

    private static boolean isNotEmpty(Map<String, Object> m, String key) {
        Object o = JacksonUtil.at(m, key);
        return o != null && !"".equals(o);
    }

    /**
     * 将参数转换成对应的Object
     *
     * @param value
     * @param pte
     * @return
     * @author dk 2015-04-09
     */
    public static Object coverString2Object(String value, ParaTypeEnum pte) {
        try {
            switch (pte) {
                case Blob:
                    return value.getBytes(SqlHolder.properties.getBlob_charsetname());
                case Date:
                    return ValUtil.toDate(value);
                default:
                    return value;
            }
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e.getMessage(),e));
        }
        return value;
    }


    /**
     * 创建表插入语句
     *
     * @param p
     * @return
     * @throws SQLException
     */
    public static String createInsertSql(Map<String, Object> p) throws SQLException {
        String tableNm = (String) p.get(SU_STR_TABLE_NM);
        if (tableNm == null) {
            throw new SQLException("创建sql出错，参数中缺少表名：" + SU_STR_TABLE_NM);
        }
        p.remove(SU_STR_TABLE_NM);

        StringBuffer sb1 = new StringBuffer("insert into " + tableNm + "(");
        StringBuffer sb2 = new StringBuffer(" values (");
        Iterator<Entry<String, Object>> it = p.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, Object> entry = it.next();
            String columeNm = entry.getKey();
            String valueStr = "#{" + columeNm + "}";
            sb1.append((String) entry.getKey() + ",");
            sb2.append(valueStr + ",");
        }
        return sb1.substring(0, sb1.length() - 1) + ")" + sb2.substring(0, sb2.length() - 1) + ")";
    }

    /**
     * 创建表更新语句
     *
     * @param p
     * @param endSql
     * @return
     * @throws SQLException
     */
    public static String createUpdateSql(Map<String, Object> p, String endSql) throws SQLException {
        String tableNm = (String) p.get(SU_STR_TABLE_NM);
        if (tableNm == null) {
            throw new SQLException("创建sql出错，参数中缺少表名：" + SU_STR_TABLE_NM);
        }
        p.remove(SU_STR_TABLE_NM);

        String keys = (String) p.get(SU_STR_UPDATE_KEYS);
        if (keys == null) {
            throw new SQLException("创建sql出错，参数中缺少检索条件：" + SU_STR_UPDATE_KEYS);
        }
        p.remove(SU_STR_UPDATE_KEYS);
        // where语句
        StringBuffer sb2 = new StringBuffer(" where 1=1");
        for (String key : keys.split(",")) {
            if (p.containsKey(key)) {
                String valueStr = "#{" + key + "}";
                sb2.append(" and " + key + "=" + valueStr);
                p.remove(key);
            } else {
                throw new SQLException("参数中键值未设定：" + key);
            }
        }
        if (endSql != null) {
            sb2.append(endSql);
        }
        if (sb2.toString().equals(" where 1=1 ")) {
            throw new SQLException("更新条件为空：" + p);
        }
        // update语句
        StringBuffer sb1 = new StringBuffer("update " + tableNm + " set ");
        Iterator<Entry<String, Object>> it = p.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, Object> entry = it.next();
            String columeNm = String.valueOf(entry.getKey());
            String valueStr = "#{" + columeNm + "}";
            sb1.append((String) entry.getKey() + "=");
            sb1.append(valueStr + ",");
        }
        // sql语句拼接
        return sb1.substring(0, sb1.length() - 1) + sb2.toString();
    }


    /**
     * 创建单表查询语句
     *
     * @param p
     * @param endSql
     * @return list
     * @throws SQLException
     */
    public static String createSelectSql(Map<String, Object> p, String endSql) throws SQLException {
        String tableNm = (String) p.get(SU_STR_TABLE_NM);
        if (tableNm == null) {
            throw new SQLException("创建sql出错，参数中缺少表名：" + SU_STR_TABLE_NM);
        }
        tableNm = tableNm.toUpperCase();
        p.remove(SU_STR_TABLE_NM);

        StringBuilder sql = new StringBuilder("SELECT *");
        sql.append(",'" + tableNm + "' " + SU_STR_TABLE_NM);
        sql.append(" FROM " + tableNm);
        sql.append(" WHERE 1=1 ");

        Iterator<Entry<String, Object>> it = p.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, Object> entry = it.next();
            String columeNm = String.valueOf(entry.getKey());
            String value = String.valueOf(entry.getValue());
            String valueStr = "#{" + columeNm + "}";
            if (value == null) {
                sql.append(" and " + entry.getKey() + " is null");
            } else {
                sql.append(" and " + entry.getKey() + "=" + valueStr);
            }
        }
        if (endSql != null) {
            sql.append(endSql);
        }
        return sql.toString();
    }

    public static String getSqlInStr(Object o) {
        if (StringUtils.isEmpty(o)) {
            throw new SystemException("转换成in的参数不能为空！");
        }
        boolean isNum = true;
        if (o instanceof CharSequence) {
            String valueOf = String.valueOf(o);
            o = valueOf.replaceAll("\\s*,\\s*", ",").trim().split(",");
        } else if (o instanceof Collection) {
            o = StringUtils.listToArray((Collection<?>) o);
        } else if (!(o instanceof Object[])) {
            throw new SystemException("转换成in的参数只能是字符串或者列表，数组");
        }

        Object[] o2 = (Object[]) o;
        for (int i = 0; i < o2.length; i++) {
            if (o2[i] instanceof Number) {
                continue;
            }
            String valueOf = String.valueOf(o2[i]);
            if (!PATTERN_ISNUM.matcher(valueOf).find()) {
                isNum = false;
                if (valueOf.startsWith("'") && valueOf.endsWith("'")) {
                    valueOf = valueOf.substring(1, valueOf.length() - 1);
                }
                o2[i] = valueOf.replaceAll("'", "''");
            }
        }
        if (isNum) {
            return StringUtils.join(o2, ",");
        }
        return "'" + StringUtils.join(o2, "','") + "'";
    }
}
