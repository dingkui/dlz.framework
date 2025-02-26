package com.dlz.framework.db.service;

import com.dlz.comm.exception.ValidateException;
import com.dlz.comm.util.StringUtils;
import com.dlz.framework.db.modal.wrapper.DeleteWrapper;
import com.dlz.framework.db.modal.wrapper.InsertWrapper;
import com.dlz.framework.db.modal.wrapper.QueryWrapper;
import com.dlz.framework.db.modal.wrapper.UpdateWrapper;
import com.dlz.comm.util.system.FieldReflections;

import java.util.List;


/**
 * 从数据库中取得单条map类型数据：{adEnddate=2015-04-08 13:47:12.0}
 * sql语句，可以带参数如：select AD_ENDDATE from JOB_AD t where ad_id=#{ad_id}
 * paraMap ：Map<String,Object> m=new HashMap<String,Object>();m.put("ad_id", "47");
 * @return
 * @throws Exception
 */
public interface IDbBeanService extends IDbQwService{
	default <T> List<T> getBeanList(T bean){
		return getBeanList(QueryWrapper.wrapper(bean));
	}
	default <T> T getBean(T bean){
		return getBean(QueryWrapper.wrapper(bean),true);
	}
	default <T> T getById(String id,Class<T> clazz){
		if(StringUtils.isEmpty(id)){
			throw new ValidateException("id不能为空");
		}
		return getBean(QueryWrapper.wrapper(clazz).eq("id",id),true);
	}
	default <T> long insertWithAutoKey(T bean){
		return insertWithAutoKey(InsertWrapper.wrapper(bean));
	}
	default <T> long insert(T bean){
		return excute(InsertWrapper.wrapper(bean));
	}
	default <T> long delete(T bean){
		return delete(DeleteWrapper.wrapper(bean));
	}
	default <T> int update(T condition,T bean){
		return excute(UpdateWrapper.wrapper(condition).set(bean));
	}
	default <T> int updateByIdOrInsert(T bean){
		Object id = FieldReflections.getValue(bean, "id",false);
		if(StringUtils.isEmpty(id)){
			excute(InsertWrapper.wrapper(bean));
			return 1;
		}
		return excute(UpdateWrapper.wrapper((Class<T>)bean.getClass()).eq("id",id).set(bean));
	}
	default <T> int updateById(T bean){
		Object id = FieldReflections.getValue(bean, "id",false);
		if(StringUtils.isEmpty(id)){
			throw new ValidateException("id不能为空");
		}
		return excute(UpdateWrapper.wrapper((Class<T>)bean.getClass()).eq("id",id).set(bean));
	}
	default <T> int deleteById(String id,Class<T> clazz){
		if(StringUtils.isEmpty(id)){
			throw new ValidateException("id不能为空");
		}
		return excute(DeleteWrapper.wrapper(clazz).eq("id",id));
	}
	default <T> int deleteByIds(String id,Class<T> clazz){
		if(StringUtils.isEmpty(id)){
			throw new ValidateException("id不能为空");
		}
		return excute(DeleteWrapper.wrapper(clazz).in("id",id));
	}
}
