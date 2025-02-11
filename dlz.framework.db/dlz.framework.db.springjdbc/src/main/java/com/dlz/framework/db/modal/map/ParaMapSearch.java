package com.dlz.framework.db.modal.map;

import com.dlz.framework.db.convertor.ConvertUtil;
import com.dlz.framework.db.holder.ServiceHolder;
import com.dlz.framework.db.modal.condition.IQueryPage;
import com.dlz.framework.db.modal.result.Page;
import com.dlz.framework.db.modal.result.ResultMap;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 构造单表的查询操作sql
 *
 * @author dingkui
 */
@Slf4j
@SuppressWarnings("rawtypes")
public class ParaMapSearch extends AParaMapSearch<ParaMapSearch> implements IQueryPage<ParaMapSearch> {
    private static final long serialVersionUID = 8374167270612933157L;
    private static final String SQL = "key.comm.searchTable";
    private static final String STR_COLUMS = "colums";
    private String colums;
    public ParaMapSearch(String colums, String tableName) {
        super(SQL, tableName);
        this.colums = colums;
    }
    public ParaMapSearch(String tableName) {
        this("*", tableName);
    }
    public void buildSql() {
        super.buildSql();
        addPara(STR_COLUMS, ConvertUtil.str2DbClumn(colums));
    }
    public List<ResultMap> queryMapList() {
        return ServiceHolder.doDb(s->s.getMapList(this));
    }
    public Page<ResultMap> queryPageData() {
        return ServiceHolder.doDb(s->s.getPage(this));
    }
    public ResultMap queryMap() {
        return ServiceHolder.doDb(s->s.getMap(this));
    }

    @Override
    public ParaMapSearch mine() {
        return this;
    }
    @Override
    public boolean isAllowEmptyWhere() {
        return true;
    }
    @Override
    public ParaMapSearch page(Page page) {
        if (page != null) {
            this.setPage(page);
        }
        return this;
    }
}
