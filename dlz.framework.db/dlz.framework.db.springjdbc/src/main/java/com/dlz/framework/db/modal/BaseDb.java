package com.dlz.framework.db.modal;

import com.dlz.comm.exception.SystemException;
import com.dlz.comm.util.system.Reflections;
import com.dlz.framework.db.modal.para.WrapperQuery;

import java.io.Serializable;
import java.util.function.Consumer;
import java.util.function.Supplier;


/**
 * 数据库bean实现类
 *
 * @author dingkui
 */
public abstract class BaseDb<ME extends BaseDb> implements Serializable {
    public abstract Long getId();
    public abstract void setId(Long id);
    public void read(){
        Long id = getId();
        if(id==null){
            throw new SystemException("id不能为空");
        }
        final BaseDb me = DB.query(getClass()).eq("id", id).queryBean();
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
            final Long aLong = DB.insert(getClass()).insertWithAutoKey();
            if(aLong>0){
                this.setId(aLong);
            }
            return aLong >0;
        }
        return DB.update(this).eq("id", id).excute()>0;
    }
    public boolean insertOrUpdate(Supplier<Long> idMaker, Consumer<ME> update){
        Long id = getId();
        if(id !=null) {
            BaseDb one = DB.query(getClass()).eq("id",id).queryBean();
            if (one != null) {
                if(update!=null){
                    update.accept((ME)this);
                }
                return DB.update(this).eq("id", id).excute()>0;
            } else {
                return DB.insert(getClass()).excute()>0;
            }
        } else {
            if(idMaker!=null){
                setId(idMaker.get());
                return DB.insert(getClass()).excute()>0;
            }else{
                final Long aLong = DB.insert(getClass()).insertWithAutoKey();
                if(aLong>0){
                    this.setId(aLong);
                }
                return aLong >0;
            }
        }
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
