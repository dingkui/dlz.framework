package com.dlz.framework.db.modal.para;

import com.dlz.framework.db.inf.IOperatorInsert;
import lombok.extern.slf4j.Slf4j;

/**
 * 构造单表的查询操作sql
 *
 * @author dingkui
 */
@Slf4j
@SuppressWarnings("rawtypes")
public class SqlKeyInsert extends ParaMap implements IOperatorInsert {
    private static final long serialVersionUID = 8374167270612933157L;

    public SqlKeyInsert(String sql) {
        super(sql);
    }

}
