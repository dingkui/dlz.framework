package com.dlz.framework.db.service;

import com.dlz.comm.exception.DbException;
import com.dlz.comm.json.JSONMap;
import com.dlz.comm.util.JacksonUtil;
import com.dlz.comm.util.VAL;
import com.dlz.framework.db.convertor.ConvertUtil;
import com.dlz.framework.db.dao.IDlzDao;
import com.dlz.framework.db.helper.util.DbNameUtil;
import com.dlz.framework.db.modal.result.ResultMap;
import com.dlz.framework.executor.Executor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 从数据库中取得单条map类型数据：{adEnddate=2015-04-08 13:47:12.0}
 * sql语句，可以带参数如：select AD_ENDDATE from JOB_AD t where ad_id=#{ad_id}
 * paraMap ：Map<String,Object> m=new HashMap<String,Object>();m.put("ad_id", "47");
 *
 * @return
 * @throws Exception
 */
public interface IDbJdbcService {
    IDlzDao getDao();

    default List<ResultMap> getMapList(String sql, Object... para) {
		return doDb(VAL.of(sql, para), jdbcSql -> getDao().getList(sql, para));
    }

	default <T> T doDb(VAL<String, Object[]> para,Executor<VAL<String, Object[]>, T> executor) {
		try {
			return executor.excute(para);
		} catch (Exception e) {
			if (e instanceof DbException) {
				throw e;
			}
			throw new DbException(e.getMessage(), 1005, e);
		}
	}
	default <T> List<T> getColumnList(String sql, Class<T> tClass, Object... para) {
		return doDb(VAL.of(sql, para), jdbcSql -> ConvertUtil.getColumnList(getDao().getList(sql, para), tClass));
	}

	default <T> T getFistColumn(String sql, Class<T> tClass, Object... para) {
		return doDb(VAL.of(sql, para), jdbcSql -> getDao().getFistColumn(sql,tClass, para));
	}

    /**
     * 新的一套操作api,用于比较简单的sql,直接用问号传参
     *
     * @param sql  sql语句，可以用问号传参数如：update JOB_AD set AD_text=? where ad_id = ?
     * @param para ：参数数组
     */
    default int excuteSql(String sql, Object... para) {
		return doDb(VAL.of(sql, para), jdbcSql -> getDao().update(sql, para));
    }


    default ResultMap getMap(String sql, Boolean throwEx, Object... para) {
		return doDb(VAL.of(sql, para), jdbcSql -> getDao().getOne(sql,throwEx, para));
    }

    default String getStr(String sql, Object... para) {
        return this.getFistColumn(sql, String.class, para);
    }

    default BigDecimal getBigDecimal(String sql, Object... para) {
        return this.getFistColumn(sql, BigDecimal.class, para);
    }

    default Float getFloat(String sql, Object... para) {
        return this.getFistColumn(sql, Float.class, para);
    }

    default Integer getInt(String sql, Object... para) {
        return this.getFistColumn(sql, Integer.class, para);
    }

    default Long getLong(String sql, Object... para) {
        return this.getFistColumn(sql, Long.class, para);
    }

    default Double getDouble(String sql, Object... para) {
        return this.getFistColumn(sql, Double.class, para);
    }

    default List<String> getStrList(String sql, Object... para) {
        return this.getColumnList(sql, String.class, para);
    }

    default List<BigDecimal> getBigDecimalList(String sql, Object... para) {
        return this.getColumnList(sql, BigDecimal.class, para);
    }

    default List<Float> getFloatList(String sql, Object... para) {
        return this.getColumnList(sql, Float.class, para);
    }

    default List<Integer> getIntList(String sql, Object... para) {
        return this.getColumnList(sql, Integer.class, para);
    }

    default List<Long> getLongList(String sql, Object... para) {
        return this.getColumnList(sql, Long.class, para);
    }

    default List<Double> getDoubleList(String sql, Object... para) {
        return this.getColumnList(sql, Double.class, para);
    }

    default ResultMap getMap(String sql, Object... para) {
        return getMap(sql, true, para);
    }

    default <T> T getBean(String sql, Class<T> t, Object... para) {
		return doDb(VAL.of(sql, para), jdbcSql -> ConvertUtil.conver(getDao().getOne(sql, true,para),t));
    }

    default <T> List<T> getBeanList(String sql, Class<T> t, Object... para) {
        return doDb(VAL.of(sql, para), jdbcSql -> ConvertUtil.conver(getDao().getList(sql, para),t));
    }
}
