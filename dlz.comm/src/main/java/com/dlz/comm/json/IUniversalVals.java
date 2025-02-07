package com.dlz.comm.json;

import com.dlz.comm.util.JacksonUtil;
import com.dlz.comm.util.ValUtil;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 万能取值器
 * @author dk 2017-06-15
 *
 */
@SuppressWarnings({ "rawtypes" })
public interface IUniversalVals {
	default BigDecimal getBigDecimal(String key){
		return  getBigDecimal(key,null);
	}
	default BigDecimal getBigDecimal(String key,BigDecimal defaultV){
		return ValUtil.toBigDecimal(getKeyVal(key),defaultV);
	}
	default Double getDouble(String key){
		return getDouble(key,null);
	}
	default Double getDouble(String key,Double defaultV){
		return ValUtil.toDouble(getKeyVal(key),defaultV);
	}
	default Float getFloat(String key){
		return  getFloat(key,null);
	}
	default Float getFloat(String key,Float defaultV){
		return ValUtil.toFloat(getKeyVal(key),defaultV);
	}
	default Integer getInt(String key){
		return  getInt(key,null);
	}
	default Integer getInt(String key,Integer defaultV){
		return ValUtil.toInt(getKeyVal(key),defaultV);
	}
	default Long getLong(String key){
		return  getLong(key,null);
	}
	default Long getLong(String key,Long defaultV){
		return ValUtil.toLong(getKeyVal(key),defaultV);
	}
	default Object[] getArray(String key){
		return  getArray(key, (Object[]) null);
	}
	default <T> T[] getArray(String key, Class<T> clazz){
		return ValUtil.toArray(getKeyVal(key), clazz);
	}
	default Object[] getArray(String key, Object[] defaultV){
		return ValUtil.toArray(getKeyVal(key),defaultV);
	}
	default JSONList getList(String key){
		return  getList(key,(List)null);
	}
	default JSONList getList(String key,List defaultV){
		return ValUtil.toList(getKeyVal(key),defaultV);
	}
	default JSONMap getMap(String key){
		return getObj(key,JSONMap.class);
	}
	default <T> List<T> getList(String key,Class<T> clazz){
		return ValUtil.toList(getKeyVal(key),clazz);
	}
	default String getStr(String key){
		return  getStr(key,null);
	}
	default String getStr(String key,String defaultV){
		return ValUtil.toStr(getKeyVal(key),defaultV);
	}
	default Boolean getBoolean(String key){
		return getBoolean(key,null);
	}
	default Boolean getBoolean(String key,Boolean defaultV){
		return ValUtil.toBoolean(getKeyVal(key),defaultV);
	}
	default Date getDate(String key){
		return ValUtil.toDate(getKeyVal(key));
	}
	default Date getDate(String key,String format){
		return ValUtil.toDate(getKeyVal(key),format);
	}
	default String getDateStr(String key){
		return ValUtil.toDateStr(getKeyVal(key));
	}
	default String getDateStr(String key,String format){
		return ValUtil.toDateStr(getKeyVal(key),format);
	}
	default <T> T getObj(String key,Class<T> classs){
		return ValUtil.toObj(getKeyVal(key),classs);
	}
	default <T> T at(String key,Class<T> classs){
		return ValUtil.toObj(getKeyVal(key),classs);
	}
	default <T> T as(Class<T> classs){
		return JacksonUtil.coverObj(getInfoObject(),classs);
	}
	default Object getKeyVal(String key){
		return JacksonUtil.at(getInfoObject(),key);
	}
	Object getInfoObject();
}