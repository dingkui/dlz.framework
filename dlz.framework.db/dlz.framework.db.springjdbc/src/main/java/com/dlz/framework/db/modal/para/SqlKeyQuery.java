package com.dlz.framework.db.modal.para;

import com.dlz.framework.db.inf.IOperatorQuery;
import com.dlz.framework.db.inf.ISqlMakerPage;
import com.dlz.framework.db.modal.result.Page;
import lombok.extern.slf4j.Slf4j;

/**
 * 构造单表的查询操作sql
 *
 * @author dingkui
 */
@Slf4j
@SuppressWarnings("rawtypes")
public class SqlKeyQuery extends ParaMap<SqlKeyQuery> implements ISqlMakerPage<SqlKeyQuery>, IOperatorQuery {
    private static final long serialVersionUID = 8374167270612933157L;
    public SqlKeyQuery(String sql) {
        super(sql);
    }
    public SqlKeyQuery(String sql, Page page) {
        super(sql);
        this.setPage(page);
    }
    @Override
    public SqlKeyQuery me() {
        return this;
    }
    @Override
    public SqlKeyQuery page(Page page) {
        if (page != null) {
            this.setPage(page);
        }
        return this;
    }
}
