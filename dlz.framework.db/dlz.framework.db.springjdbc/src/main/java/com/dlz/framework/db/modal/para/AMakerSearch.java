package com.dlz.framework.db.modal.para;

import com.dlz.framework.db.inf.ISqlMakerSearch;
import com.dlz.framework.db.modal.condition.Condition;


/**
 * 构造单表的更新操作sql
 *
 * @author dingkui
 */
public abstract class AMakerSearch<T extends AMakerSearch> extends AMaker<T> implements ISqlMakerSearch<T> {
    private Condition whereCond = Condition.where();
    private boolean allowFullQuery = false;//是否允许全表查询，默认不允许
    protected AMakerSearch(String tableName) {
        super(tableName);
    }

    public Condition where() {
        return whereCond;
    }
    public T where(Condition cond) {
        this.whereCond = cond.clone();
        return me();
    }

    public T setAllowFullQuery(boolean allowFullQuery) {
        this.allowFullQuery = allowFullQuery;
        return me();
    }
    public boolean isAllowFullQuery() {
        return this.allowFullQuery;
    }
}
