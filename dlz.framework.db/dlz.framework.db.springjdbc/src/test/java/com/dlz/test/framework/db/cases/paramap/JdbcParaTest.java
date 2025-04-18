package com.dlz.test.framework.db.cases.paramap;

import com.dlz.framework.db.modal.DB;
import com.dlz.framework.db.modal.para.JdbcQuery;
import com.dlz.framework.db.modal.result.Order;
import com.dlz.framework.db.modal.result.Page;
import com.dlz.test.framework.db.config.SpingDbBaseTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class JdbcParaTest extends SpingDbBaseTest{

    @Test
    public void sqlTest1() {
        final JdbcQuery page = DB.jdbcSelect("select 1 from dual where ?=1", 1);
        showSql(page,"sqlTest1","select 1 from dual where 1=1");
        log.info(page.queryStr());
        log.info(""+page.queryStrList());
    }
    @Test
    public void pageSqlTest1() {
        final JdbcQuery page = DB.jdbcSelect("select 1 from dual where ?=1", 1)
                .page(Page.build(1, 2));
        showSql(page,"pageSqlTest1","select 1 from dual where 1=1 LIMIT 0,2");
    }
    @Test
    public void pageSqlTest2() {
        final JdbcQuery page = DB.jdbcSelect("select 1 from dual where ?=1", 1)
                .page(Page.build(1, 2, Order.descs("x1","x2")));
        showSql(page,"pageSqlTest2","select 1 from dual where 1=1 order by X1 desc,X2 desc LIMIT 0,2");
    }
    @Test
    public void pageSqlTest3() {
        final JdbcQuery page = DB.jdbcSelect("select 1 from dual where ?=1", 1)
                .page(Page.build(Order.descs("x1","x2")));
        showSql(page,"pageSqlTest3","select 1 from dual where 1=1 order by X1 desc,X2 desc");
    }
    @Test
    public void conditionSqlTest1() {
        final JdbcQuery page = DB.jdbcSelect("select 1 from dual where ?=1", 1)
                .page(Page.build(1, 2));
        showSql(page,"conditionSqlTest1","select 1 from dual where 1=1 LIMIT 0,2");
    }
    @Test
    public void conditionSqlTest2() {
        final JdbcQuery page = DB.jdbcSelect("select 1 from dual where ?=1", 1);
        showSql(page,"conditionSqlTest2","select 1 from dual where 1=1");
    }
}