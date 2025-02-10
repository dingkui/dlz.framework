package com.dlz.framework.db.service;

import com.dlz.comm.exception.DbException;
import com.dlz.comm.util.VAL;
import com.dlz.framework.db.convertor.ConvertUtil;
import com.dlz.framework.db.dao.IDlzDao;
import com.dlz.framework.db.helper.util.DbNameUtil;
import com.dlz.framework.db.modal.map.ParaMapBase;
import com.dlz.framework.db.modal.result.Page;
import com.dlz.framework.db.modal.result.ResultMap;
import com.dlz.framework.db.modal.wrapper.QueryWrapper;
import com.dlz.framework.executor.Executor;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.Callable;


/**
 * 从数据库中取得单条map类型数据：{adEnddate=2015-04-08 13:47:12.0}
 * sql语句，可以带参数如：select AD_ENDDATE from JOB_AD t where ad_id=#{ad_id}
 * paraMap ：Map<String,Object> m=new HashMap<String,Object>();m.put("ad_id", "47");
 *
 * @return
 * @throws Exception
 */
public interface IDbQwService {
    IDlzDao getDao();
    default <T,R> R doDb(QueryWrapper<T> wrapper, Executor<VAL<String, Object[]>, R> executor, boolean page) {
        try {
            return executor.excute(page?wrapper.jdbcSql():wrapper.jdbcCnt());
        } catch (Exception e) {
            if (e instanceof DbException) {
                throw e;
            }
            throw new DbException(e.getMessage() + " bean:"  + wrapper.getBeanClass() , 1005, e);
        }
    }
    default <T> List<T> getColumnList(QueryWrapper<T> wrapper, Class<T> tClass) {
        return doDb(wrapper, jdbcSql ->ConvertUtil.getColumnList(getDao().getList(jdbcSql.v1, jdbcSql.v2), tClass),true);
    }

    default <T> T getFistColumn(QueryWrapper<T> wrapper, Class<T> tClass) {
        return doDb(wrapper, jdbcSql -> getDao().getFistColumn(jdbcSql.v1, tClass, jdbcSql.v2),true);
    }

    default <T> List<T> getBeanList(QueryWrapper<T> wrapper) {
        return doDb(wrapper, jdbcSql -> ConvertUtil.conver(getDao().getList(jdbcSql.v1, jdbcSql.v2),wrapper.getBeanClass()),true);
    }

    default <T> T getBean(QueryWrapper<T> wrapper, boolean throwEx) {
        return doDb(wrapper, jdbcSql -> ConvertUtil.conver(getDao().getOne(jdbcSql.v1, throwEx, jdbcSql.v2),wrapper.getBeanClass()),true);
    }

    default List<ResultMap> getMapList(QueryWrapper<?> wrapper) {
        return doDb(wrapper, jdbcSql -> getDao().getList(jdbcSql.v1, jdbcSql.v2),true);
    }

    default ResultMap getMap(QueryWrapper<?> wrapper, Boolean throwEx) {
        return doDb(wrapper, jdbcSql -> getDao().getOne(jdbcSql.v1,throwEx, jdbcSql.v2),true);
    }

    default ResultMap getMap(QueryWrapper wrapper) {
        return getMap(wrapper, true);
    }

    default String getStr(QueryWrapper wrapper) {
        return getFistColumn(wrapper, String.class);
    }

    default BigDecimal getBigDecimal(QueryWrapper wrapper) {
        return getFistColumn(wrapper, BigDecimal.class);
    }

    default Float getFloat(QueryWrapper wrapper) {
        return getFistColumn(wrapper, Float.class);
    }

    default Integer getInt(QueryWrapper wrapper) {
        return getFistColumn(wrapper, Integer.class);
    }

    default Long getLong(QueryWrapper wrapper) {
        return getFistColumn(wrapper, Long.class);
    }

    default Double getDouble(QueryWrapper wrapper) {
        return getFistColumn(wrapper, Double.class);
    }

    default List<String> getStrList(QueryWrapper wrapper) {
        return getColumnList(wrapper, String.class);
    }

    default List<BigDecimal> getBigDecimalList(QueryWrapper wrapper) {
        return getColumnList(wrapper, BigDecimal.class);
    }

    default List<Float> getFloatList(QueryWrapper wrapper) {
        return getColumnList(wrapper, Float.class);
    }

    default List<Integer> getIntList(QueryWrapper wrapper) {
        return getColumnList(wrapper, Integer.class);
    }

    default List<Long> getLongList(QueryWrapper wrapper) {
        return getColumnList(wrapper, Long.class);
    }

    default List<Double> getDoubleList(QueryWrapper wrapper) {
        return getColumnList(wrapper, Double.class);
    }

    default <T> int getCnt(QueryWrapper<T> wrapper) {
        return doDb(wrapper,jdbcSql -> getDao().getFistColumn(jdbcSql.v1, Integer.class, jdbcSql.v2),false);
    }

    default <T> Page<T> getPage(QueryWrapper<T> wrapper) {
        Page<T> page = wrapper.getPage();
        if (page == null) {
            page = Page.build();
            wrapper.page(page);
        }
        return page.doPage(() -> getCnt(wrapper), () -> getBeanList(wrapper));
    }
}
