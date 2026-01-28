package com.dlz.framework.db.service;

import com.dlz.framework.db.inf.IExecutorQuery;
import com.dlz.framework.db.util.DbConvertUtil;

import java.math.BigDecimal;
import java.util.List;

/**
 * 从数据库中取得单条map类型数据：{adEnddate=2015-04-08 13:47:12.0}
 * sql语句，可以带参数如：select AD_ENDDATE from JOB_AD t where ad_id=#{ad_id}
 * paraMap ：Map<String,Object> m=new HashMap<String,Object>();m.put("ad_id", "47");
 *
  * @throws Exception
 */
public interface IDbClumnService extends IDbBaseService {

    default String getStr(IExecutorQuery paraMap) {
        return getFistColumn(paraMap, String.class);
    }

    default <T> List<T> getColumnList(IExecutorQuery paraMap, Class<T> tClass) {
        return doDb(paraMap, jdbcSql -> DbConvertUtil.getColumnList(getDao().getList(jdbcSql.sql, jdbcSql.paras), tClass));
    }

    default <T> T getFistColumn(IExecutorQuery paraMap, Class<T> tClass) {
        return doDb(paraMap, jdbcSql -> getDao().getFistColumn(jdbcSql.sql, tClass, jdbcSql.paras));
    }

    default BigDecimal getBigDecimal(IExecutorQuery paraMap) {
        return getFistColumn(paraMap, BigDecimal.class);
    }

    default Float getFloat(IExecutorQuery paraMap) {
        return getFistColumn(paraMap, Float.class);
    }

    default Integer getInt(IExecutorQuery paraMap) {
        return getFistColumn(paraMap, Integer.class);
    }

    default Long getLong(IExecutorQuery paraMap) {
        return getFistColumn(paraMap, Long.class);
    }

    default Double getDouble(IExecutorQuery paraMap) {
        return getFistColumn(paraMap, Double.class);
    }

    default List<String> getStrList(IExecutorQuery paraMap) {
        return getColumnList(paraMap, String.class);
    }

    default List<BigDecimal> getBigDecimalList(IExecutorQuery paraMap) {
        return getColumnList(paraMap, BigDecimal.class);
    }

    default List<Float> getFloatList(IExecutorQuery paraMap) {
        return getColumnList(paraMap, Float.class);
    }

    default List<Integer> getIntList(IExecutorQuery paraMap) {
        return getColumnList(paraMap, Integer.class);
    }

    default List<Long> getLongList(IExecutorQuery paraMap) {
        return getColumnList(paraMap, Long.class);
    }

    default List<Double> getDoubleList(IExecutorQuery paraMap) {
        return getColumnList(paraMap, Double.class);
    }
}
