package com.dlz.framework.db.modal.para;

import com.dlz.framework.db.SqlUtil;
import com.dlz.framework.db.inf.ISqlPara;
import com.dlz.framework.db.modal.items.JdbcItem;
import com.dlz.framework.db.modal.items.SqlItem;
import com.dlz.framework.db.modal.result.Page;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

public class ParaJdbc implements Serializable , ISqlPara{
    private static final long serialVersionUID = 8374167270612933157L;
    @Getter
    private SqlItem sqlItem = new SqlItem();
    @Getter
    @Setter
    private Page page;
    @Getter
    private Object[] paras;
    public ParaJdbc(String sql,Object[] paras) {
        sqlItem.setSqlDeal(sql);
        this.paras = paras;
    }

    public JdbcItem jdbcSql() {
        if (this.getPage() == null) {
            return SqlUtil.dealJdbc(this,1);
        }
        return SqlUtil.dealJdbc(this,3);
    }
    public JdbcItem jdbcCnt() {
        return SqlUtil.dealJdbc(this,2);
    }
}
