package com.dlz.framework.db.service;

import com.dlz.comm.exception.DbException;
import com.dlz.comm.util.JacksonUtil;
import com.dlz.framework.db.convertor.ConvertUtil;
import com.dlz.framework.db.modal.ResultMap;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


/**
 * 从数据库中取得单条map类型数据：{adEnddate=2015-04-08 13:47:12.0}
 * sql语句，可以带参数如：select AD_ENDDATE from JOB_AD t where ad_id=#{ad_id}
 * paraMap ：Map<String,Object> m=new HashMap<String,Object>();m.put("ad_id", "47");
 * @return
 * @throws Exception
 */
public interface IDbJdbcService extends IBaseDbService{
	default List<ResultMap> getMapList(String sql, Object... para) {
		return getDao().getList(sql, para);
	}

	/**
	 * 新的一套操作api,用于比较简单的sql,直接用问号传参
	 * @param sql sql语句，可以用问号传参数如：update JOB_AD set AD_text=? where ad_id = ?
	 * @param para ：参数数组
	 */
	default int excuteSql(String sql, Object... para) {
		return getDao().update(sql, para);
	}


	default ResultMap getMap(String sql, Boolean throwEx, Object... para){
		List<ResultMap> list = getMapList(sql, para);
		if(list.size()==0){
			return null;
		}else if(list.size()>1 && throwEx){
			throw new DbException("查询结果为多条",1004);
		}else{
			return list.get(0);
		}
	}

	default String getStr(String sql, Object... para){
		return ConvertUtil.getColum(getMapList(sql, para),String.class);
	}
	default BigDecimal getBigDecimal(String sql, Object... para){
		return ConvertUtil.getColum(getMapList(sql, para),BigDecimal.class);
	}
	default Float getFloat(String sql, Object... para){
		return ConvertUtil.getColum(getMapList(sql, para),Float.class);
	}
	default Integer getInt(String sql, Object... para){
		return ConvertUtil.getColum(getMapList(sql, para),Integer.class);
	}
	default Long getLong(String sql, Object... para){
		return ConvertUtil.getColum(getMapList(sql, para),Long.class);
	}
	default Double getDouble(String sql, Object... para){
		return ConvertUtil.getColum(getMapList(sql, para),Double.class);
	}

	default List<String> getStrList(String sql, Object... para){
		return ConvertUtil.getColumList(getMapList(sql, para),String.class);
	}
	default List<BigDecimal> getBigDecimalList(String sql, Object... para){
		return ConvertUtil.getColumList(getMapList(sql, para),BigDecimal.class);
	}
	default List<Float> getFloatList(String sql, Object... para){
		return ConvertUtil.getColumList(getMapList(sql, para),Float.class);
	}
	default List<Integer> getIntList(String sql, Object... para){
		return ConvertUtil.getColumList(getMapList(sql, para),Integer.class);
	}
	default List<Long> getLongList(String sql, Object... para){
		return ConvertUtil.getColumList(getMapList(sql, para),Long.class);
	}
	default List<Double> getDoubleList(String sql, Object... para){
		return ConvertUtil.getColumList(getMapList(sql, para),Double.class);
	}

	default ResultMap getMap(String sql, Object... para){
		return getMap(sql, true ,para);
	}

	default <T> T getBean(String sql, Class<T> t, Object... para){
		try {
			return JacksonUtil.coverObj(getMap(sql, para), t);
		} catch (Exception e) {
			if (e instanceof DbException) {
				throw e;
			}
			throw new DbException(e.getMessage(), 1005, e);
		}
	}
	default <T> List<T> getBeanList(String sql, Class<T> t, Object... para){
		List<ResultMap> list = getMapList(sql, para);
		List<T> l = new ArrayList<>();
		for (ResultMap r : list) {
			try {
				l.add(JacksonUtil.coverObj(r, t));
			} catch (Exception e) {
				if (e instanceof DbException) {
					throw e;
				}
				throw new DbException(e.getMessage(), 1005, e);
			}
		}
		return l;
	}
}
