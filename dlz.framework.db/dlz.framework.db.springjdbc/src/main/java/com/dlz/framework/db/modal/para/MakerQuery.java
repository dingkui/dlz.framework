package com.dlz.framework.db.modal.para;

import com.dlz.comm.fn.DlzFn;
import com.dlz.comm.util.StringUtils;
import com.dlz.framework.db.holder.BeanInfoHolder;
import com.dlz.framework.db.inf.IOperatorQuery;
import com.dlz.framework.db.inf.ISqlMakerPage;
import com.dlz.framework.db.modal.result.Page;
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
public class MakerQuery extends AMakerSearch<MakerQuery> implements ISqlMakerPage<MakerQuery>, IOperatorQuery {
    private static final long serialVersionUID = 8374167270612933157L;
    String colums="*";

    public MakerQuery(String tableName) {
        super(tableName);
    }

    public MakerQuery select(String... colums) {
        if (colums.length > 0) {
            this.colums = StringUtils.join(colums, ",");
        }
        return this;
    }

    public <T> MakerQuery select(DlzFn<T, ?>... colums) {
        if (colums.length > 0) {
            this.colums = Arrays.stream(colums).map(item -> BeanInfoHolder.fnName(item)).collect(Collectors.joining(","));
        }
        return this;
    }

    @Override
    public String getSql() {
        return MakerUtil.MAKER_SQL_SEARCHE;
    }

    @Override
    public MakerQuery me() {
        return this;
    }

    @Override
    public MakerQuery page(Page page) {
        if (page != null) {
            this.setPage(page);
        }
        return this;
    }
}
