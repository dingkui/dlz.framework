package com.dlz.framework.db.util;

import com.dlz.comm.exception.DbException;
import com.dlz.comm.exception.SystemException;
import com.dlz.comm.json.JSONMap;
import com.dlz.comm.util.*;
import com.dlz.framework.db.enums.ParaTypeEnum;
import com.dlz.framework.db.holder.SqlHolder;
import com.dlz.framework.db.modal.items.JdbcItem;
import com.dlz.framework.db.modal.items.SqlItem;
import com.dlz.framework.db.modal.para.ATableMaker;
import com.dlz.framework.db.modal.para.ParaJdbc;
import com.dlz.framework.db.modal.para.ParaMap;
import com.dlz.framework.db.modal.para.TableMakerUtil;
import com.dlz.framework.db.modal.result.Page;
import lombok.extern.slf4j.Slf4j;

import java.time.temporal.TemporalAccessor;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * sql操作Util
 *
 * @author ding_kui 2010-12-14
 */
@Slf4j
public class SqlUtil {
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
    private static Pattern PATTERN_ISNUM = Pattern.compile("^-?\\d+(\\.\\d+)?$");

    /**
     * 转换mybatisSQl为jdbcSql
     * @throws Exception
     * @author dk 2015-04-09
     */
    public static JdbcItem dealParmToJdbc(ParaMap paraMap) {
        return dealToJdbcSql(paraMap.getSqlItem().getSqlRun(), paraMap.getPara());
    }
    /**
     * 转换mybatisSQl为jdbcSql
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
            }else if(jdbcParaItem instanceof Date){
                sbRunSql.append("'"+DateUtil.DATETIME.format((Date)jdbcParaItem)+"'");
            }else if(jdbcParaItem instanceof TemporalAccessor){
                sbRunSql.append("'"+DateUtil.DATETIME.format((TemporalAccessor)jdbcParaItem)+"'");
            }else{
                try {
                    sbRunSql.append("'"+ValUtil.toStr(jdbcParaItem)+"'");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
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
     * @throws Exception
     * @author dk 2015-04-09
     */
    public static SqlItem dealParm(ParaMap paraMap, int dealType) {
        SqlItem sqlItem = paraMap.getSqlItem();
        if(sqlItem.getSqlKey() == null && paraMap instanceof ATableMaker){
             TableMakerUtil.buildSql((ATableMaker) paraMap);
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

    /**
     * 创建执行sql(带替换符)
     *
     * @param para
     * @param sql
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
