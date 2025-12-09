package com.dlz.framework.db.modal.para;

import com.dlz.framework.db.inf.ISqlMaker;

/**
 * 构造单表的增删改查操作sql
 *
 * @author dingkui
 */
public abstract class AMaker<T extends AMaker> extends ParaMap<T> implements ISqlMaker {
    private static final long serialVersionUID = 8374167270612933157L;
    private final String tableName;
    protected AMaker(String tableName) {
        this.tableName = tableName;
    }
    @Override
    public String getTableName() {
        return tableName;
    }
}
