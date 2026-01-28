package com.dlz.framework.db.modal.wrapper;

import com.dlz.framework.db.inf.IExecutorUDI;
import com.dlz.framework.db.modal.para.ParaMap;
import lombok.extern.slf4j.Slf4j;

/**
 * 构造单表的查询操作sql
 *
 * @author dingkui
 */
@Slf4j
@SuppressWarnings("rawtypes")
public class SqlExecute extends ParaMap implements IExecutorUDI {
    private static final long serialVersionUID = 8374167270612933157L;
    public SqlExecute(String sql) {
        super(sql);
    }

}
