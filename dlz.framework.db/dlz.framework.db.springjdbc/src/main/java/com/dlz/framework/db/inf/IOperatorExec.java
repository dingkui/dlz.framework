package com.dlz.framework.db.inf;

import com.dlz.framework.db.holder.DBHolder;

/**
 * 添加and or条件
 */
public interface IOperatorExec extends ISqlPara {
    default int execute() {
        return DBHolder.doDb(s -> s.execute(this));
    }
}
