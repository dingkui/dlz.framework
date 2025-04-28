package com.dlz.framework.db.inf;

import com.dlz.framework.db.holder.BeanInfoHolder;
import com.dlz.framework.db.holder.DBHolder;
import com.dlz.framework.db.holder.SqlHolder;
import com.dlz.framework.db.modal.para.MakerDelete;
import com.dlz.framework.db.modal.para.MakerUpdate;
import com.dlz.framework.db.modal.para.ParaMap;
import com.dlz.framework.db.modal.para.WrapperUpdate;

/**
 * 添加and or条件
 */
public interface IOperatorDelete<T extends IOperatorDelete> extends IOperatorExec,ISqlSearch<T> {
    String getTableName();
    default int excute() {
        final String logicDeleteField = SqlHolder.properties.getLogicDeleteField();
        if(!BeanInfoHolder.isColumnExists(getTableName(), logicDeleteField)){
            return DBHolder.doDb(s -> s.excute(this));
        }
        final MakerUpdate update = new MakerUpdate(getTableName())
                .set(logicDeleteField, 1)
                .where(this.where());
        if(this instanceof MakerDelete){
            update.getPara().putAll(((MakerDelete) this).getPara());
        }else if(this instanceof WrapperUpdate){
            final ParaMap pm = ((WrapperUpdate) this).getPm();
            update.getPara().putAll(pm.getPara());
        }
        return DBHolder.doDb(s -> s.excute(update));
    }
}
