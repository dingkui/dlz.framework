package com.dlz.framework.db.inf;

import com.dlz.framework.db.holder.DBHolder;

/**
 * 添加and or条件
 */
public interface IOperatorInsert extends IOperatorExec {
    default Long insertWithAutoKey() {
        return DBHolder.doDb(s -> s.insertWithAutoKey(this));
    }
}
