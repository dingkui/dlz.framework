package com.dlz.framework.db.service;

import com.dlz.comm.exception.DbException;
import com.dlz.comm.util.system.ConvertUtil;
import com.dlz.framework.db.convertor.DbConvertUtil;
import com.dlz.framework.db.dao.IDlzDao;
import com.dlz.framework.db.modal.map.ParaJDBC;
import com.dlz.framework.db.modal.result.Page;
import com.dlz.framework.db.modal.result.ResultMap;
import com.dlz.framework.db.modal.wrapper.AWrapper;
import com.dlz.framework.db.modal.wrapper.InsertWrapper;
import com.dlz.framework.db.modal.wrapper.QueryWrapper;

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
public interface IDbQwService {
    IDlzDao getDao();
    default <T,R> R doDb(AWrapper<T> wrapper, Function<ParaJDBC, R> executor, boolean cnt) {
        try {
            return executor.apply(wrapper.buildSql(cnt));
        } catch (Exception e) {
            if (e instanceof DbException) {
                throw e;
            }
            throw new DbException(e.getMessage() + " bean:"  + wrapper.getBeanClass() , 1005, e);
        }
    }
    default <T> List<T> getColumnList(QueryWrapper<T> wrapper, Class<T> tClass) {
        return doDb(wrapper, jdbcSql -> DbConvertUtil.getColumnList(getDao().getList(jdbcSql.sql, jdbcSql.paras), tClass),false);
    }

    default <T> T getFistColumn(QueryWrapper<T> wrapper, Class<T> tClass) {
        return doDb(wrapper, jdbcSql -> getDao().getFistColumn(jdbcSql.sql, tClass, jdbcSql.paras),false);
    }

    default <T> List<T> getBeanList(QueryWrapper<T> wrapper) {
        return doDb(wrapper, jdbcSql -> ConvertUtil.convertList(getDao().getList(jdbcSql.sql, jdbcSql.paras),wrapper.getBeanClass()),false);
    }

    default <T> T getBean(QueryWrapper<T> wrapper, boolean throwEx) {
        return doDb(wrapper, jdbcSql -> ConvertUtil.convert(getDao().getOne(jdbcSql.sql, throwEx, jdbcSql.paras),wrapper.getBeanClass()),false);
    }

    default List<ResultMap> getMapList(QueryWrapper<?> wrapper) {
        return doDb(wrapper, jdbcSql -> getDao().getList(jdbcSql.sql, jdbcSql.paras),false);
    }

    default ResultMap getMap(QueryWrapper<?> wrapper, Boolean throwEx) {
        return doDb(wrapper, jdbcSql -> getDao().getOne(jdbcSql.sql,throwEx, jdbcSql.paras),false);
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
        return doDb(wrapper,jdbcSql -> getDao().getFistColumn(jdbcSql.sql, Integer.class, jdbcSql.paras),true);
    }

    default <T> Page<T> getPage(QueryWrapper<T> wrapper) {
        Page<T> page = wrapper.getPage();
        if (page == null) {
            page = Page.build();
            wrapper.page(page);
        }
        return page.doPage(() -> getCnt(wrapper), () -> getBeanList(wrapper));
    }
    default <T> Long insertWithAutoKey(InsertWrapper<T> wrapper) {
        return doDb(wrapper,jdbcSql -> getDao().updateForId(jdbcSql.sql, jdbcSql.paras),false);
    }
    default <T> int excute(AWrapper<T> wrapper) {
        return doDb(wrapper,jdbcSql -> getDao().update(jdbcSql.sql, jdbcSql.paras),false);
    }
}
