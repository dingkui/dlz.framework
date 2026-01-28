package com.dlz.framework.db.modal.wrapper;

import com.dlz.framework.db.modal.para.ParaJdbc;
import com.dlz.framework.db.inf.IExecutorInsert;
import lombok.extern.slf4j.Slf4j;

/**
 * 构造单表的查询操作sql
 *
 * @author dingkui
 */
@Slf4j
public class JdbcInsert extends ParaJdbc implements IExecutorInsert {
    private static final long serialVersionUID = 8374167270612933157L;
    public JdbcInsert(String sql, Object... paras) {
        super(sql,paras);
    }
}
