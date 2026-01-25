package com.dlz.framework.db.modal.para;

import com.dlz.comm.fn.DlzFn;
import com.dlz.framework.db.holder.BeanInfoHolder;
import com.dlz.framework.db.inf.IOperatorInsert;
import com.dlz.framework.db.util.DbConvertUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * 构造单表的添加操作sql
 *
 * @author dingkui
 */
@Slf4j
public class TableInsert extends ATableMaker implements IOperatorInsert {
    private static final long serialVersionUID = 8374167270612933157L;
    final Map<String, Object> insertValues = new HashMap<>();

    public TableInsert(String tableName) {
        super(tableName);
    }

    @Override
    public String getSql() {
        return TableMakerUtil.MAKER_SQL_INSERT;
    }

    public <T> void value(DlzFn<T, ?> column, Object value) {
        value(BeanInfoHolder.fnName(column), value);
    }

    public TableInsert value(String key, Object value) {
        String paraName = DbConvertUtil.toDbColumnNames(key);
        if (!BeanInfoHolder.isColumnExists(getTableName(),paraName)) {
            log.warn("column is not exists:" + getTableName() + "." + paraName);
            return this;
        }
        insertValues.put(paraName, value);
        return this;
    }

    public TableInsert value(Map<String, Object> values) {
        for (String str : values.keySet()) {
            value(str, values.get(str));
        }
        return this;
    }
}
