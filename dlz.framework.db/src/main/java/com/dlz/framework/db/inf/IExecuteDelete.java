package com.dlz.framework.db.inf;

import com.dlz.framework.db.holder.BeanInfoHolder;
import com.dlz.framework.db.holder.DBHolder;
import com.dlz.framework.db.holder.SqlHolder;
import com.dlz.framework.db.modal.wrapper.TableDelete;
import com.dlz.framework.db.modal.wrapper.TableUpdate;
import com.dlz.framework.db.modal.para.ParaMap;
import com.dlz.framework.db.modal.wrapper.PojoUpdate;

/**
 * 删除执行器=执行器+查询构造器
 */
public interface IExecuteDelete<T extends IExecuteDelete>
        extends IExecutorUDI, ISqlQuery<T> {
    String getTableName();
    default int execute() {
        final String logicDeleteField = SqlHolder.properties.getLogicDeleteField();
        if(!BeanInfoHolder.isColumnExists(getTableName(), logicDeleteField)){
            return DBHolder.doDb(s -> s.execute(this));
        }
        final TableUpdate update = new TableUpdate(getTableName())
                .set(logicDeleteField, 1)
                .where(this.where());
        if(this instanceof TableDelete){
            update.getPara().putAll(((TableDelete) this).getPara());
        }else if(this instanceof PojoUpdate){
            final ParaMap pm = ((PojoUpdate) this).getPm();
            update.getPara().putAll(pm.getPara());
        }
        return DBHolder.doDb(s -> s.execute(update));
    }

//    default T setPhysicallyDelete(boolean physicallyDelete) {
//        this.getTableName() ;
//        return me();
//    }
}
