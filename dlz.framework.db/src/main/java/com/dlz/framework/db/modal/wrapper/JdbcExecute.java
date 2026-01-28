package com.dlz.framework.db.modal.wrapper;

import com.dlz.framework.db.modal.para.ParaJdbc;
import com.dlz.framework.db.inf.IExecutorUDI;
import lombok.extern.slf4j.Slf4j;

/**
 * 构造单表的查询操作sql
 *
 * @author dingkui
 */
@Slf4j
public class JdbcExecute extends ParaJdbc implements IExecutorUDI {
    private static final long serialVersionUID = 8374167270612933157L;
    public JdbcExecute(String sql, Object... paras) {
        super(sql,paras);
    }
}
