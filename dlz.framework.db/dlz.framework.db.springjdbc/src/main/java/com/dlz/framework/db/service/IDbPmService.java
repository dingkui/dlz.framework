package com.dlz.framework.db.service;

import com.dlz.comm.exception.DbException;
import com.dlz.comm.util.system.ConvertUtil;
import com.dlz.framework.db.convertor.DbConvertUtil;
import com.dlz.framework.db.dao.IDlzDao;
import com.dlz.framework.db.modal.map.ParaJDBC;
import com.dlz.framework.db.modal.map.ParaMapBase;
import com.dlz.framework.db.modal.result.Page;
import com.dlz.framework.db.modal.result.ResultMap;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;

/**
 * 从数据库中取得单条map类型数据：{adEnddate=2015-04-08 13:47:12.0}
 * sql语句，可以带参数如：select AD_ENDDATE from JOB_AD t where ad_id=#{ad_id}
 * paraMap ：Map<String,Object> m=new HashMap<String,Object>();m.put("ad_id", "47");
 *
 * @return
 * @throws Exception
 */
public interface IDbPmService {
    IDlzDao getDao();

    default <T> T doDb(ParaMapBase paraMap, Function<ParaJDBC, T> executor, boolean page) {
        try {
            return executor.apply(page?paraMap.jdbcPage():paraMap.jdbcCnt());
        } catch (Exception e) {
            if (e instanceof DbException) {
                throw e;
            }
            throw new DbException(e.getMessage() + " sqkKey:" + paraMap.getSqlItem().getSqlKey(), 1005, e);
        }
    }

    /**
     * 插入数据库
     * sql语句，可以带参数如：update JOB_AD set AD_text=#{adText} where ad_id in (${ad_id})
     *
     * @param paraMap ：Map<String,Object> m=new HashMap<String,Object>();m.put("ad_id", "47");
     * @return
     * @throws Exception
     */
    default Long insertWithAutoKey(ParaMapBase paraMap) {
        return doDb(paraMap, jdbcSql -> getDao().updateForId(jdbcSql.sql, jdbcSql.paras), true);
    }

    /**
     * 更新或插入数据库
     * sql语句，可以带参数如：update JOB_AD set AD_text=#{adText} where ad_id in (${ad_id})
     *
     * @param paraMap ：Map<String,Object> m=new HashMap<String,Object>();m.put("ad_id", "47");
     * @return
     * @throws Exception
     */
    default int excuteSql(ParaMapBase paraMap) {
        return doDb(paraMap, jdbcSql -> getDao().update(jdbcSql.sql, jdbcSql.paras), true);
    }

    /**
     * 从数据库中取得map类型列表如：[{AD_ENDDATE=2015-04-08 13:47:12.0}]
     * sql语句，可以带参数如：select AD_ENDDATE from JOB_AD t where ad_id=#{ad_id}
     *
     * @param paraMap ：Map<String,Object> m=new HashMap<String,Object>();m.put("ad_id", "47");
     * @return
     * @throws Exception
     */
    default List<ResultMap> getMapList(ParaMapBase paraMap) {
        return doDb(paraMap, jdbcSql -> getDao().getList(jdbcSql.sql, jdbcSql.paras), true);
    }

    default int getCnt(ParaMapBase paraMap) {
        return doDb(paraMap, jdbcSql ->getDao().getFistColumn(jdbcSql.sql, Integer.class, jdbcSql.paras), false);
    }

    default String getStr(ParaMapBase paraMap) {
        return getFistColumn(paraMap, String.class);
    }

    default <T> List<T> getColumnList(ParaMapBase paraMap, Class<T> tClass) {
        return doDb(paraMap, jdbcSql -> DbConvertUtil.getColumnList(getDao().getList(jdbcSql.sql, jdbcSql.paras), tClass), true);
    }

    default <T> T getFistColumn(ParaMapBase paraMap, Class<T> tClass) {
        return doDb(paraMap, jdbcSql -> getDao().getFistColumn(jdbcSql.sql, tClass, jdbcSql.paras),  true);
    }

    default BigDecimal getBigDecimal(ParaMapBase paraMap) {
        return getFistColumn(paraMap, BigDecimal.class);
    }

    default Float getFloat(ParaMapBase paraMap) {
        return getFistColumn(paraMap, Float.class);
    }

    default Integer getInt(ParaMapBase paraMap) {
        return getFistColumn(paraMap, Integer.class);
    }

    default Long getLong(ParaMapBase paraMap) {
        return getFistColumn(paraMap, Long.class);
    }

    default Double getDouble(ParaMapBase paraMap) {
        return getFistColumn(paraMap, Double.class);
    }

    default List<String> getStrList(ParaMapBase paraMap) {
        return getColumnList(paraMap, String.class);
    }

    default List<BigDecimal> getBigDecimalList(ParaMapBase paraMap) {
        return getColumnList(paraMap, BigDecimal.class);
    }

    default List<Float> getFloatList(ParaMapBase paraMap) {
        return getColumnList(paraMap, Float.class);
    }

    default List<Integer> getIntList(ParaMapBase paraMap) {
        return getColumnList(paraMap, Integer.class);
    }

    default List<Long> getLongList(ParaMapBase paraMap) {
        return getColumnList(paraMap, Long.class);
    }

    default List<Double> getDoubleList(ParaMapBase paraMap) {
        return getColumnList(paraMap, Double.class);
    }

    /**
     * 从数据库中取得集合
     */
    default ResultMap getMap(ParaMapBase paraMap) {
        return getMap(paraMap, true);
    }

    default ResultMap getMap(ParaMapBase paraMap, boolean throwEx) {
        return doDb(paraMap, jdbcSql -> getDao().getOne(jdbcSql.sql, throwEx,jdbcSql.paras),  true);
    }

    default <T> T getBean(ParaMapBase paraMap, Class<T> t, boolean throwEx) {
        return doDb(paraMap, jdbcSql -> ConvertUtil.convert(getDao().getOne(jdbcSql.sql, throwEx,jdbcSql.paras),t),  true);
    }

    default <T> T getBean(ParaMapBase paraMap, Class<T> t) {
        return getBean(paraMap, t, true);
    }

    default <T> List<T> getBeanList(ParaMapBase paraMap, Class<T> t) {
        return ConvertUtil.convertList(getMapList(paraMap), t);
    }

    /**
     * 取得分页数据
     *
     * @return
     * @throws Exception
     */
    default Page<ResultMap> getPage(ParaMapBase paraMap) {
        return getPage(paraMap, ResultMap.class);
    }

    default <T> Page<T> getPage(ParaMapBase paraMap, Class<T> t) {
        Page<T> page = paraMap.getPage();
        if (page == null) {
            page = Page.build();
            paraMap.setPage(page);
        }
        return page.doPage(() -> getCnt(paraMap), () -> getBeanList(paraMap, t));
    }
}
