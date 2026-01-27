package com.dlz.framework.db.modal;

import com.dlz.framework.db.modal.para.SqlKeyExecute;
import com.dlz.framework.db.modal.para.SqlKeyQuery;

public class DbSql {
    public SqlKeyQuery select(String sql) {
        return new SqlKeyQuery(sql);
    }

    public SqlKeyExecute executer(String sql) {
        return new SqlKeyExecute(sql);
    }
}
