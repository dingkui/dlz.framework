package com.dlz.framework.db.inf;

import com.dlz.framework.db.modal.items.JdbcItem;
import com.dlz.framework.db.modal.items.SqlItem;

/**
 * 添加and or条件
 */
public interface ISqlPara {
    JdbcItem jdbcSql();

    JdbcItem jdbcCnt();

    SqlItem getSqlItem();
}
