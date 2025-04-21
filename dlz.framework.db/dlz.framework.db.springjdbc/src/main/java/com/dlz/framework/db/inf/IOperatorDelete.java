package com.dlz.framework.db.inf;

import com.dlz.framework.db.holder.BeanInfoHolder;
import com.dlz.framework.db.holder.DBHolder;
import com.dlz.framework.db.holder.SqlHolder;
import com.dlz.framework.db.modal.para.MakerUpdate;

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
        return DBHolder.doDb(s -> s.excute(new MakerUpdate(getTableName())
                .set(logicDeleteField, 1)
                .where(this.where())));
    }
}
