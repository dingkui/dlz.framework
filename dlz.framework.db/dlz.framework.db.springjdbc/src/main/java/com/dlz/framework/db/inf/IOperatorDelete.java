package com.dlz.framework.db.inf;

import com.dlz.framework.db.holder.BeanInfoHolder;
import com.dlz.framework.db.holder.DBHolder;
import com.dlz.framework.db.holder.SqlHolder;
import com.dlz.framework.db.modal.para.TableDelete;
import com.dlz.framework.db.modal.para.TableUpdate;
import com.dlz.framework.db.modal.para.ParaMap;
import com.dlz.framework.db.modal.para.WrapperUpdate;

/**
 * 添加and or条件
 */
public interface IOperatorDelete<T extends IOperatorDelete> extends IOperatorExec, ISqlQuery<T> {
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
        }else if(this instanceof WrapperUpdate){
            final ParaMap pm = ((WrapperUpdate) this).getPm();
            update.getPara().putAll(pm.getPara());
        }
        return DBHolder.doDb(s -> s.execute(update));
    }
}
