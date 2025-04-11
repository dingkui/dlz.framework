package com.dlz.framework.db.modal.para;

import com.dlz.comm.fn.DlzFn;
import com.dlz.comm.util.system.FieldReflections;
import com.dlz.framework.db.convertor.DbConvertUtil;
import com.dlz.framework.db.inf.IOperatorInsert;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * 构造单表的添加操作sql
 *
 * @author dingkui
 */
@Slf4j
public class MakerInsert extends AMaker implements IOperatorInsert {
    private static final long serialVersionUID = 8374167270612933157L;
    final Map<String, Object> insertValues = new HashMap<>();

    public MakerInsert(String tableName) {
        super(tableName);
    }

    @Override
    public String getSql() {
        return MakerUtil.MAKER_SQL_INSERT;
    }

    public <T> void value(DlzFn<T, ?> column, Object value) {
        value(FieldReflections.getFieldName(column), value);
    }

    public MakerInsert value(String key, Object value) {
        String paraName = DbConvertUtil.str2DbClumn(key);
        boolean isClumnExists = isClumnExists(paraName);
        if (!isClumnExists) {
            log.warn("column is not exists:" + getTableName() + "." + paraName);
            return this;
        }
        insertValues.put(paraName, value);
        return this;
    }

    public MakerInsert value(Map<String, Object> values) {
        for (String str : values.keySet()) {
            value(str, values.get(str));
        }
        return this;
    }
}
