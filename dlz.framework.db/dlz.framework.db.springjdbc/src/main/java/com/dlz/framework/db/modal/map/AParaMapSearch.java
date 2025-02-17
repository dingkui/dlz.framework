package com.dlz.framework.db.modal.map;

import com.dlz.comm.util.StringUtils;
import com.dlz.framework.db.modal.condition.Condition;
import com.dlz.framework.db.modal.condition.ICondAddByFn;
import com.dlz.framework.db.modal.condition.ICondAuto;
import com.dlz.framework.db.modal.condition.ICondition;


/**
 * 构造单表的更新操作sql
 *
 * @author dingkui
 */
public abstract class AParaMapSearch<T extends AParaMapSearch> extends ParaMapMaker implements ICondition<T>, ICondAuto<T>,ICondAddByFn<T> {

    protected static final String STR_WHERE = "where";

    Condition whereCond = Condition.where();

    protected AParaMapSearch(String Sql, String tableName) {
        super(Sql, tableName);
    }

    public void addChildren(Condition child) {
        whereCond.addChildren(child);
    }
    public void buildSql() {
        super.buildSql();
        String where = whereCond.getRunsql(this);
        if (!isAllowEmptyWhere() && StringUtils.isEmpty(where)) {
            where = "where false";
        }
        addPara(STR_WHERE, where);
    }
    public T where(Condition cond) {
        this.whereCond = cond;
        return me();
    }

    public boolean isAllowEmptyWhere() {
        return false;
    }

}
