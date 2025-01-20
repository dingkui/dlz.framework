package com.dlz.framework.db.service;

import com.dlz.comm.exception.DbException;
import com.dlz.comm.util.VAL;
import com.dlz.framework.db.convertor.ConvertUtil;
import com.dlz.framework.db.helper.util.DbNameUtil;
import com.dlz.framework.db.modal.Page;
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
public interface IDbQwService extends IBaseDbService{
	default <T> List<T> getBeanList(Wrapper<T> wrapper) {
		return DbNameUtil.coverResult2Bean(getMapList(wrapper),wrapper.getBeanClass());
	}
	default <T> T getBean(Wrapper<T> wrapper,boolean throwEx) {
		List<ResultMap> list = getMapList(wrapper);
		if(list.size()==0){
			return null;
		}else if(list.size()>1 && throwEx){
			throw new DbException("查询结果为多条",1004);
		}else{
			return DbNameUtil.coverResult2Bean(list.get(0),wrapper.getBeanClass());
		}
	}
	default List<ResultMap> getMapList(Wrapper wrapper) {
		VAL<String, Object[]> sqlJdbc = wrapper.getSqlJdbc();
		return getDao().getList(sqlJdbc.v1, sqlJdbc.v2);
	}

	default ResultMap getMap(Wrapper wrapper, Boolean throwEx){
		List<ResultMap> list = getMapList(wrapper);
		if(list.size()==0){
			return null;
		}else if(list.size()>1 && throwEx){
			throw new DbException("查询结果为多条",1004);
		}else{
			return list.get(0);
		}
	}

	default ResultMap getMap(Wrapper wrapper){
		return getMap(wrapper,true);
	}

	default String getStr(Wrapper wrapper){
		return ConvertUtil.getColum(getMapList(wrapper),String.class);
	}
	default BigDecimal getBigDecimal(Wrapper wrapper){
		return ConvertUtil.getColum(getMapList(wrapper),BigDecimal.class);
	}
	default Float getFloat(Wrapper wrapper){
		return ConvertUtil.getColum(getMapList(wrapper),Float.class);
	}
	default Integer getInt(Wrapper wrapper){
		return ConvertUtil.getColum(getMapList(wrapper),Integer.class);
	}
	default Long getLong(Wrapper wrapper){
		return ConvertUtil.getColum(getMapList(wrapper),Long.class);
	}
	default Double getDouble(Wrapper wrapper){
		return ConvertUtil.getColum(getMapList(wrapper),Double.class);
	}

	default List<String> getStrList(Wrapper wrapper){
		return ConvertUtil.getColumList(getMapList(wrapper),String.class);
	}
	default List<BigDecimal> getBigDecimalList(Wrapper wrapper){
		return ConvertUtil.getColumList(getMapList(wrapper),BigDecimal.class);
	}
	default List<Float> getFloatList(Wrapper wrapper){
		return ConvertUtil.getColumList(getMapList(wrapper),Float.class);
	}
	default List<Integer> getIntList(Wrapper wrapper){
		return ConvertUtil.getColumList(getMapList(wrapper),Integer.class);
	}
	default List<Long> getLongList(Wrapper wrapper){
		return ConvertUtil.getColumList(getMapList(wrapper),Long.class);
	}
	default List<Double> getDoubleList(Wrapper wrapper){
		return ConvertUtil.getColumList(getMapList(wrapper),Double.class);
	}
	default <T> int getCnt(Wrapper<T> wrapper){
		VAL<String, Object[]> sqlJdbc = wrapper.getCntJdbc();
		return getDao().getClumn(sqlJdbc.v1, Integer.class, sqlJdbc.v2);
	}
	default <T> Page<T> getPage(Wrapper<T> wrapper) {
		Page<T> page = wrapper.page();
		//是否需要查询列表（需要统计条数并且条数是0的情况不查询，直接返回空列表）
		boolean needList = true;

		int cnt = getCnt(wrapper);
		page.setCount(cnt);
		if (page.getCount() == 0) {
			needList = false;
		}
		if(page.getPageIndex()==-1){
			page.setPageIndex(0);
		}
		if (needList) {
			page.setData(getBeanList(wrapper));
		} else {
			page.setData(new ArrayList<T>());
		}
		return page;
	}
}
