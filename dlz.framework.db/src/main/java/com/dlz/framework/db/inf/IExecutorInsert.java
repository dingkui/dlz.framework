package com.dlz.framework.db.inf;

import com.dlz.framework.db.holder.DBHolder;

/**
 * 插入执行器=执行器+自己的插入方法
 */
public interface IExecutorInsert extends IExecutorUDI {
    default Long insertWithAutoKey() {
        return DBHolder.doDb(s -> s.insertWithAutoKey(this));
    }
}
