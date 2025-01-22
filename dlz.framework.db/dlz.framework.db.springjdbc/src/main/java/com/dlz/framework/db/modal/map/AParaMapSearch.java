package com.dlz.framework.db.modal.map;

import com.dlz.comm.util.StringUtils;
import com.dlz.framework.db.modal.condition.Condition;
import com.dlz.framework.db.modal.condition.ICondAuto;
import com.dlz.framework.db.modal.condition.ICondition;


/**
 * 构造单表的更新操作sql
 *
 * @author dingkui
 */
public abstract class AParaMapSearch<T extends AParaMapSearch> extends ParaMapMaker implements ICondition<T>, ICondAuto<T> {

    protected static final String STR_WHERE = "where";

    Condition whereCond = Condition.where();

    protected AParaMapSearch(String Sql, String tableName) {
        super(Sql, tableName);
    }

    public void addChildren(Condition child) {
        whereCond.addChildren(child);
    }

    public T where(Condition cond) {
        this.whereCond = cond;
        return mine();
    }

    public void addWhere() {
        String where = whereCond.getRunsql(mine());
        if (!isAllowEmptyWhere() && StringUtils.isEmpty(where)) {
            where = "where false";
        }
        addPara(STR_WHERE, where);
    }

    public boolean isAllowEmptyWhere() {
        return false;
    }

}
