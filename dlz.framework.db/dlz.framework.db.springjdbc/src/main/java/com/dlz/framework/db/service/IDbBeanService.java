package com.dlz.framework.db.service;

import com.dlz.framework.db.helper.bean.Sort;
import com.dlz.framework.db.modal.Page;

import java.util.List;


/**
 * 从数据库中取得单条map类型数据：{adEnddate=2015-04-08 13:47:12.0}
 * sql语句，可以带参数如：select AD_ENDDATE from JOB_AD t where ad_id=#{ad_id}
 * paraMap ：Map<String,Object> m=new HashMap<String,Object>();m.put("ad_id", "47");
 * @return
 * @throws Exception
 */
public interface IDbBeanService extends IBaseDbService{
	<T> List<T> getBeanList(T bean);
	<T> T getBean(T bean);
	<T> T getById(String id,Class<T> clazz);
	<T> T getAll(Sort sort, Class<T> clazz);
	<T> T insert(T bean);
	<T> T delete(T bean);
	<T> T update(T bean);
	<T> T updateOrInsert(T bean);
	<T> T updateById(T bean);
	<T> T deleteById(String id,Class<T> clazz);
	<T> T deleteByIds(String id,Class<T> clazz);
	<T> void insertAll(List<T> list);
	<T> Page<T> page(Page<T> page);
}
