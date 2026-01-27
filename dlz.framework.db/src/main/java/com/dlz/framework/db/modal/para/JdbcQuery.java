package com.dlz.framework.db.modal.para;

import com.dlz.framework.db.inf.IOperatorQuery;
import com.dlz.framework.db.inf.ISqlMakerPage;
import com.dlz.framework.db.inf.ISqlPara;
import com.dlz.framework.db.modal.result.Page;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * 构造单表的查询操作sql
 *
 * @author dingkui
 */
@Slf4j
public class JdbcQuery extends ParaJdbc implements Serializable, ISqlPara, ISqlMakerPage<JdbcQuery>, IOperatorQuery {
    private static final long serialVersionUID = 8374167270612933157L;
    public JdbcQuery(String sql, Object... paras) {
        super(sql, paras);
    }
    @Override
    public JdbcQuery page(Page page) {
        if (page != null) {
            this.setPage(page);
        }
        return this;
    }
}
