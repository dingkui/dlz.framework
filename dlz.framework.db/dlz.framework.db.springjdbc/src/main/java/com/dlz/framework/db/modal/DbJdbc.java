package com.dlz.framework.db.modal;

import com.dlz.framework.db.modal.para.JdbcExecute;
import com.dlz.framework.db.modal.para.JdbcQuery;

public class DbJdbc {
    public JdbcQuery select(String sql, Object... para) {
        return new JdbcQuery(sql, para);
    }
    public JdbcExecute executer(String sql, Object... para) {
        return new JdbcExecute(sql, para);
    }
}
