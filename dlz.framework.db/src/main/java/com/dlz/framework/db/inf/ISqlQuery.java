package com.dlz.framework.db.inf;

import com.dlz.framework.db.modal.condition.Condition;

/**
 * sqlMaker查询接口
 * 删除，更新，查询需要实现
 *
 * @param <T>
 */
public interface ISqlQuery<T extends ISqlQuery> extends
        ICondAndOr<T>,
        ICondAddByKey<T>,
        ICondAuto<T> {
    Condition where();

    default void addChildren(Condition child) {
        where().addChildren(child);
    }

    T where(Condition cond);

    T setAllowFullQuery(boolean allowFullQuery);
    String getTableName();
    boolean isAllowFullQuery();
}
