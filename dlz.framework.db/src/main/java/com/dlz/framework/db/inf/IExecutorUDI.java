package com.dlz.framework.db.inf;

import com.dlz.framework.db.holder.DBHolder;

/**
 * 执行器：增删改执行器
 */
public interface IExecutorUDI extends ISqlPara {
    default int execute() {
        return DBHolder.doDb(s -> s.execute(this));
    }
}
