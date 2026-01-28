package com.dlz.framework.db.modal.wrapper;

import com.dlz.comm.fn.DlzFn;
import com.dlz.comm.util.StringUtils;
import com.dlz.framework.db.holder.BeanInfoHolder;
import com.dlz.framework.db.inf.ISqlPage;
import com.dlz.framework.db.modal.para.AQuery;
import com.dlz.framework.db.modal.result.Page;
import com.dlz.framework.db.inf.IExecutorQuery;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * 构造单表的查询操作sql
 *
 * @author dingkui
 */
@Slf4j
@SuppressWarnings("rawtypes")
public class TableQuery extends AQuery<TableQuery> implements ISqlPage<TableQuery>, IExecutorQuery {
    private static final long serialVersionUID = 8374167270612933157L;
    String columns="*";

    public TableQuery(String tableName) {
        super(tableName);
    }

    public TableQuery columns(String... columns) {
        if (columns.length > 0) {
            this.columns = StringUtils.join(columns, ",");
        }
        return this;
    }

    public <T> TableQuery columns(DlzFn<T, ?>... columns) {
        if (columns.length > 0) {
            this.columns = Arrays.stream(columns).map(item -> BeanInfoHolder.fnName(item)).collect(Collectors.joining(","));
        }
        return this;
    }

    @Override
    public String getSql() {
        return WrapperBuildUtil.MAKER_SQL_SEARCHE;
    }

    @Override
    public TableQuery me() {
        return this;
    }

    @Override
    public TableQuery page(Page page) {
        if (page != null) {
            this.setPage(page);
        }
        return this;
    }
}
