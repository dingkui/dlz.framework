package com.dlz.framework.db.modal.para;

import com.dlz.framework.db.inf.ITableSql;

/**
 * 构造单表的增删改查操作sql
 *
 * @author dingkui
 */
public abstract class ATableMaker<T extends ATableMaker> extends ParaMap<T> implements ITableSql {
    private static final long serialVersionUID = 8374167270612933157L;
    private final String tableName;
    protected ATableMaker(String tableName) {
        this.tableName = tableName;
    }
    @Override
    public String getTableName() {
        return tableName;
    }
}
