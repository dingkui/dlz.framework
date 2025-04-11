package com.dlz.framework.db.service;

import com.dlz.comm.exception.ValidateException;
import com.dlz.comm.util.StringUtils;
import com.dlz.comm.util.system.FieldReflections;
import com.dlz.framework.db.inf.IOperatorExec;
import com.dlz.framework.db.inf.IOperatorInsert;
import com.dlz.framework.db.modal.para.WrapperDelete;
import com.dlz.framework.db.modal.para.WrapperInsert;
import com.dlz.framework.db.modal.para.WrapperUpdate;

/**
 * 从数据库中取得单条map类型数据：{adEnddate=2015-04-08 13:47:12.0}
 * sql语句，可以带参数如：select AD_ENDDATE from JOB_AD t where ad_id=#{ad_id}
 * paraMap ：Map<String,Object> m=new HashMap<String,Object>();m.put("ad_id", "47");
 *
 * @return
 * @throws Exception
 */
public interface IDbExcuteService extends IDbBaseService{

    /**
     * 插入数据库
     * sql语句，可以带参数如：update JOB_AD set AD_text=#{adText} where ad_id in (${ad_id})
     *
     * @param paraMap ：Map<String,Object> m=new HashMap<String,Object>();m.put("ad_id", "47");
     * @return
     * @throws Exception
     */
    default Long insertWithAutoKey(IOperatorInsert paraMap) {
        return doDb(paraMap, jdbcSql -> getDao().updateForId(jdbcSql.sql, jdbcSql.paras));
    }

    /**
     * 更新或插入数据库
     * sql语句，可以带参数如：update JOB_AD set AD_text=#{adText} where ad_id in (${ad_id})
     *
     * @param paraMap ：Map<String,Object> m=new HashMap<String,Object>();m.put("ad_id", "47");
     * @return
     * @throws Exception
     */
    default int excute(IOperatorExec paraMap) {
        return doDb(paraMap, jdbcSql -> getDao().update(jdbcSql.sql, jdbcSql.paras));
    }

    default <T> long insert(T bean){
        return excute(WrapperInsert.wrapper(bean));
    }

    default <T> long insertWithAutoKey(T bean){
        return insertWithAutoKey(WrapperInsert.wrapper(bean));
    }

    default <T> long delete(T bean){
        return delete(WrapperDelete.wrapper(bean));
    }


    default <T> int update(T condition,T bean){
        return excute(WrapperUpdate.wrapper(condition).set(bean));
    }
    default <T> int updateByIdOrInsert(T bean){
        Object id = FieldReflections.getValue(bean, "id",false);
        if(StringUtils.isEmpty(id)){
            excute(WrapperInsert.wrapper(bean));
            return 1;
        }
        return excute(WrapperUpdate.wrapper((Class<T>)bean.getClass()).eq("id",id).set(bean));
    }
    default <T> int updateById(T bean){
        Object id = FieldReflections.getValue(bean, "id",false);
        if(StringUtils.isEmpty(id)){
            throw new ValidateException("id不能为空");
        }
        return excute(WrapperUpdate.wrapper((Class<T>)bean.getClass()).eq("id",id).set(bean));
    }
    default <T> int deleteById(String id,Class<T> clazz){
        if(StringUtils.isEmpty(id)){
            throw new ValidateException("id不能为空");
        }
        return excute(WrapperDelete.wrapper(clazz).eq("id",id));
    }
    default <T> int deleteByIds(String id,Class<T> clazz){
        if(StringUtils.isEmpty(id)){
            throw new ValidateException("id不能为空");
        }
        return excute(WrapperDelete.wrapper(clazz).in("id",id));
    }
}
