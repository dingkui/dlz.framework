package com.dlz.framework.db.inf;

import com.dlz.comm.exception.SystemException;
import com.dlz.comm.inf.IChained;
import com.dlz.comm.util.system.Reflections;
import com.dlz.framework.db.modal.DB;


/**
 * 数据库bean实现类
 *
 * @author dingkui
 */
public interface IDb<ME extends IDb> extends IChained<ME> {
    Long getId();
    void setId(Long id);
    default void read(){
        Long id = getId();
        if(id==null){
            throw new SystemException("id不能为空");
        }
        final IDb me = DB.query(this).eq("id", id).queryBean();
        if(me==null){
            throw new SystemException("id不存在");
        }
        Reflections.copy(me,this);
    }
    default boolean update(){
        Long id = getId();
        if(id==null){
            throw new SystemException("id不能为空");
        }
        return DB.update(this).eq("id", id).excute()>0;
    }
    default boolean updateOrInsert(){
        Long id = getId();
        if(id==null){
            final Long aLong = DB.insert(this).insertWithAutoKey();
            if(aLong>0){
                this.setId(aLong);
            }
            return aLong >0;
        }
        return DB.update(this).eq("id", id).excute()>0;
    }
    default boolean insert(){
        Long id = getId();
        if(id==null){
            final Long aLong = DB.insert(this).insertWithAutoKey();
            if(aLong>0){
                this.setId(aLong);
            }
            return aLong >0;
        }
        return DB.insert(this).excute()>0;
    }
}
