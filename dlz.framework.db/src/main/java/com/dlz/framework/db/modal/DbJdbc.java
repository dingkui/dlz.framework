package com.dlz.framework.db.modal;

import com.dlz.framework.db.modal.wrapper.JdbcExecute;
import com.dlz.framework.db.modal.wrapper.JdbcQuery;

public class DbJdbc {
    public JdbcQuery select(String sql, Object... para) {
        return new JdbcQuery(sql, para);
    }

    public JdbcExecute executer(String sql, Object... para) {
        return new JdbcExecute(sql, para);
    }
}