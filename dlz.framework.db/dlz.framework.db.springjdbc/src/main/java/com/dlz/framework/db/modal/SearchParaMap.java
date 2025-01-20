package com.dlz.framework.db.modal;

import com.dlz.framework.db.convertor.ConvertUtil;
import com.dlz.framework.db.warpper.Condition;
import lombok.extern.slf4j.Slf4j;

/**
 * 构造单表的查询操作sql
 *
 * @author dingkui
 */
@Slf4j
@SuppressWarnings("rawtypes")
public class SearchParaMap extends CreateSqlParaMap {
    private static final long serialVersionUID = 8374167270612933157L;
    private static final String SQL = "key.comm.searchTable";
    private static final String STR_COLUMS = "colums";
    public SearchParaMap(String colums, String tableName) {
        super(SQL, tableName);
        addPara(STR_COLUMS, ConvertUtil.str2Clumn(colums));
    }
    public SearchParaMap(String tableName) {
        this("*", tableName);
    }

    public SearchParaMap page(Page page) {
        if (page != null) {
            this.setPage(page);
        }
        return this;
    }

	public SearchParaMap where(Condition cond){
		where(cond.getRunsql(this));
		return this;
	}

}
