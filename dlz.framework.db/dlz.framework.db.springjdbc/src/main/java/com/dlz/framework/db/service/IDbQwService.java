package com.dlz.framework.db.service;

import com.dlz.comm.exception.DbException;
import com.dlz.comm.util.VAL;
import com.dlz.framework.db.convertor.ConvertUtil;
import com.dlz.framework.db.dao.IDlzDao;
import com.dlz.framework.db.helper.util.DbNameUtil;
import com.dlz.framework.db.modal.result.Page;
import com.dlz.framework.db.modal.result.ResultMap;
import com.dlz.framework.db.modal.wrapper.QueryWrapper;

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
public interface IDbQwService{
	IDlzDao getDao();
	default <T> List<T> getBeanList(QueryWrapper<T> wrapper) {
		return DbNameUtil.coverResult2Bean(getMapList(wrapper),wrapper.getBeanClass());
	}
	default <T> T getBean(QueryWrapper<T> wrapper, boolean throwEx) {
		List<ResultMap> list = getMapList(wrapper);
		if(list.size()==0){
			return null;
		}else if(list.size()>1 && throwEx){
			throw new DbException("查询结果为多条",1004);
		}else{
			return DbNameUtil.coverResult2Bean(list.get(0),wrapper.getBeanClass());
		}
	}
	default List<ResultMap> getMapList(QueryWrapper wrapper) {
		VAL<String, Object[]> sqlJdbc = wrapper.jdbcSql();
		return getDao().getList(sqlJdbc.v1, sqlJdbc.v2);
	}

	default ResultMap getMap(QueryWrapper wrapper, Boolean throwEx){
		List<ResultMap> list = getMapList(wrapper);
		if(list.size()==0){
			return null;
		}else if(list.size()>1 && throwEx){
			throw new DbException("查询结果为多条",1004);
		}else{
			return list.get(0);
		}
	}

	default ResultMap getMap(QueryWrapper wrapper){
		return getMap(wrapper,true);
	}

	default String getStr(QueryWrapper wrapper){
		return ConvertUtil.getColum(getMapList(wrapper),String.class);
	}
	default BigDecimal getBigDecimal(QueryWrapper wrapper){
		return ConvertUtil.getColum(getMapList(wrapper),BigDecimal.class);
	}
	default Float getFloat(QueryWrapper wrapper){
		return ConvertUtil.getColum(getMapList(wrapper),Float.class);
	}
	default Integer getInt(QueryWrapper wrapper){
		return ConvertUtil.getColum(getMapList(wrapper),Integer.class);
	}
	default Long getLong(QueryWrapper wrapper){
		return ConvertUtil.getColum(getMapList(wrapper),Long.class);
	}
	default Double getDouble(QueryWrapper wrapper){
		return ConvertUtil.getColum(getMapList(wrapper),Double.class);
	}

	default List<String> getStrList(QueryWrapper wrapper){
		return ConvertUtil.getColumList(getMapList(wrapper),String.class);
	}
	default List<BigDecimal> getBigDecimalList(QueryWrapper wrapper){
		return ConvertUtil.getColumList(getMapList(wrapper),BigDecimal.class);
	}
	default List<Float> getFloatList(QueryWrapper wrapper){
		return ConvertUtil.getColumList(getMapList(wrapper),Float.class);
	}
	default List<Integer> getIntList(QueryWrapper wrapper){
		return ConvertUtil.getColumList(getMapList(wrapper),Integer.class);
	}
	default List<Long> getLongList(QueryWrapper wrapper){
		return ConvertUtil.getColumList(getMapList(wrapper),Long.class);
	}
	default List<Double> getDoubleList(QueryWrapper wrapper){
		return ConvertUtil.getColumList(getMapList(wrapper),Double.class);
	}

	default <T> int getCnt(QueryWrapper<T> wrapper){
		VAL<String, Object[]> sqlJdbc = wrapper.jdbcCnt();
		return getDao().getClumn(sqlJdbc.v1, Integer.class, sqlJdbc.v2);
	}
	default <T> Page<T> getPage(QueryWrapper<T> wrapper) {
		Page<T> page = wrapper.getPage();
		if(page==null){
			page = new Page<>();
			wrapper.page(page);
		}
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
