package com.dlz.framework.db.modal.para;

import com.dlz.comm.util.StringUtils;
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
public class MakerQuery extends AMakerSearch<MakerQuery> implements ISqlMakerPage<MakerQuery>, IOperatorQuery {
    private static final long serialVersionUID = 8374167270612933157L;
    final String colums;


    public MakerQuery(String colums, String tableName) {
        super(tableName);
        this.colums = StringUtils.isEmpty(colums) ? "*" : colums;
    }

    public MakerQuery(String tableName) {
        this(null, tableName);
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
