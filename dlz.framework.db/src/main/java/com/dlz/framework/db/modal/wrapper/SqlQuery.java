package com.dlz.framework.db.modal.wrapper;

import com.dlz.framework.db.inf.ISqlPage;
import com.dlz.framework.db.modal.para.ParaMap;
import com.dlz.framework.db.modal.result.Page;
import com.dlz.framework.db.inf.IExecutorQuery;
import lombok.extern.slf4j.Slf4j;

/**
 * 构造单表的查询操作sql
 *
 * @author dingkui
 */
@Slf4j
@SuppressWarnings("rawtypes")
public class SqlQuery extends ParaMap<SqlQuery> implements ISqlPage<SqlQuery>, IExecutorQuery {
    private static final long serialVersionUID = 8374167270612933157L;
    public SqlQuery(String sql) {
        super(sql);
    }
    public SqlQuery(String sql, Page page) {
        super(sql);
        this.setPage(page);
    }
    @Override
    public SqlQuery me() {
        return this;
    }
    @Override
    public SqlQuery page(Page page) {
        if (page != null) {
            this.setPage(page);
        }
        return this;
    }
}
