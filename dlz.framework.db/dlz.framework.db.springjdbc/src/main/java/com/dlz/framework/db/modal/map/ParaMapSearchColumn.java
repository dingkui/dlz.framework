package com.dlz.framework.db.modal.map;

import com.dlz.framework.db.convertor.DbConvertUtil;
import com.dlz.framework.db.holder.DBHolder;
import com.dlz.framework.db.modal.result.Page;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 构造单表的查询操作sql
 *
 * @author dingkui
 */
@Slf4j
@SuppressWarnings("rawtypes")
public class ParaMapSearchColumn  extends AParaMapSearch<ParaMapSearchColumn>{
    private static final long serialVersionUID = 8374167270612933157L;
    private static final String SQL = "key.comm.searchTable";
    private static final String STR_COLUMS = "colums";
    public ParaMapSearchColumn(String colums, String tableName) {
        super(SQL, tableName);
        addPara(STR_COLUMS, DbConvertUtil.str2DbClumn(colums));
    }

    public ParaMapSearchColumn page(Page page) {
        if (page != null) {
            this.setPage(page);
        }
        return this;
    }
    @Override
    public ParaMapSearchColumn me() {
        return this;
    }
    @Override
    public boolean isAllowEmptyWhere() {
        return true;
    }

    public List<String> getStrList() {
        return DBHolder.doDb(s->s.getStrList(this));
    }
    public String getStr() {
        return DBHolder.doDb(s->s.getStr(this));
    }
    public Long getLong() {
        return DBHolder.doDb(s->s.getLong(this));
    }
    public List<Long> getLongList() {
        return DBHolder.doDb(s->s.getLongList(this));
    }
    public int count() {
        return DBHolder.doDb(s->s.getCnt(this));
    }
}
