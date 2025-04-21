package com.dlz.framework.db.modal;

import com.dlz.comm.exception.SystemException;
import com.dlz.comm.inf.IChained;
import com.dlz.comm.util.system.Reflections;
import com.dlz.framework.db.modal.para.WrapperQuery;

import java.util.function.Consumer;


/**
 * 数据库bean实现类
 *
 * @author dingkui
 */
public abstract class BaseDb<ME extends BaseDb> implements IChained<ME> {
    abstract Long getId();
    abstract void setId(Long id);
    public void read(){
        Long id = getId();
        if(id==null){
            throw new SystemException("id不能为空");
        }
        final BaseDb me = DB.query(this).eq("id", id).queryBean();
        if(me==null){
            throw new SystemException("id不存在");
        }
        Reflections.copy(me,this);
    }
    public boolean update(){
        Long id = getId();
        if(id==null){
            throw new SystemException("id不能为空");
        }
        return DB.update(this).eq("id", id).excute()>0;
    }
    public boolean insertOrUpdate(){
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
    public boolean insert(){
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

    public ME selectOne(Consumer<WrapperQuery<ME>> ors){
        final WrapperQuery<ME> query = DB.query((Class<ME>)this.getClass());
        ors.accept(query);
        return query.queryBean();
    }
}
