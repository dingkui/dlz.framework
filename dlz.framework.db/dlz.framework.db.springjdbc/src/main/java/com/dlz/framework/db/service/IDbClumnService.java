package com.dlz.framework.db.service;

import com.dlz.framework.db.util.DbConvertUtil;
import com.dlz.framework.db.inf.IOperatorQuery;

import java.math.BigDecimal;
import java.util.List;

/**
 * 从数据库中取得单条map类型数据：{adEnddate=2015-04-08 13:47:12.0}
 * sql语句，可以带参数如：select AD_ENDDATE from JOB_AD t where ad_id=#{ad_id}
 * paraMap ：Map<String,Object> m=new HashMap<String,Object>();m.put("ad_id", "47");
 *
 * @return
 * @throws Exception
 */
public interface IDbClumnService extends IDbBaseService {

    default String getStr(IOperatorQuery paraMap) {
        return getFistColumn(paraMap, String.class);
    }

    default <T> List<T> getColumnList(IOperatorQuery paraMap, Class<T> tClass) {
        return doDb(paraMap, jdbcSql -> DbConvertUtil.getColumnList(getDao().getList(jdbcSql.sql, jdbcSql.paras), tClass));
    }

    default <T> T getFistColumn(IOperatorQuery paraMap, Class<T> tClass) {
        return doDb(paraMap, jdbcSql -> getDao().getFistColumn(jdbcSql.sql, tClass, jdbcSql.paras));
    }

    default BigDecimal getBigDecimal(IOperatorQuery paraMap) {
        return getFistColumn(paraMap, BigDecimal.class);
    }

    default Float getFloat(IOperatorQuery paraMap) {
        return getFistColumn(paraMap, Float.class);
    }

    default Integer getInt(IOperatorQuery paraMap) {
        return getFistColumn(paraMap, Integer.class);
    }

    default Long getLong(IOperatorQuery paraMap) {
        return getFistColumn(paraMap, Long.class);
    }

    default Double getDouble(IOperatorQuery paraMap) {
        return getFistColumn(paraMap, Double.class);
    }

    default List<String> getStrList(IOperatorQuery paraMap) {
        return getColumnList(paraMap, String.class);
    }

    default List<BigDecimal> getBigDecimalList(IOperatorQuery paraMap) {
        return getColumnList(paraMap, BigDecimal.class);
    }

    default List<Float> getFloatList(IOperatorQuery paraMap) {
        return getColumnList(paraMap, Float.class);
    }

    default List<Integer> getIntList(IOperatorQuery paraMap) {
        return getColumnList(paraMap, Integer.class);
    }

    default List<Long> getLongList(IOperatorQuery paraMap) {
        return getColumnList(paraMap, Long.class);
    }

    default List<Double> getDoubleList(IOperatorQuery paraMap) {
        return getColumnList(paraMap, Double.class);
    }
}
