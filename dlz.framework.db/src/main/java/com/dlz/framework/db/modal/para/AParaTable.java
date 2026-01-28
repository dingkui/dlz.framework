package com.dlz.framework.db.modal.para;

import com.dlz.framework.db.inf.ISqlPara;

/**
 * 构造单表的增删改查操作sql
 *
 * @author dingkui
 */
public abstract class AParaTable<T extends AParaTable> extends ParaMap<T> implements ISqlPara {
    private static final long serialVersionUID = 8374167270612933157L;
    private final String tableName;
    protected AParaTable(String tableName) {
        this.tableName = tableName;
    }
    public String getTableName() {
        return tableName;
    }
    public abstract String getSql();
}
