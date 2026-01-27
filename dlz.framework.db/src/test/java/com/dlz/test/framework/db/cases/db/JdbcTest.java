package com.dlz.test.framework.db.cases.db;

import com.dlz.framework.db.modal.DB;
import com.dlz.framework.db.modal.para.JdbcQuery;
import com.dlz.framework.db.modal.result.Order;
import com.dlz.framework.db.modal.result.Page;
import com.dlz.test.framework.db.config.SpingDbBaseTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class JdbcTest extends SpingDbBaseTest {
    @Test
    public void jdbcTest1() {
        final JdbcQuery page = DB.Jdbc.select("select 1 from dual where ?=1", 1);
        showSql(page, "jdbcTest1", "select 1 from dual where 1=1");
        log.info(page.queryStr());
        log.info(page.queryStrList().toString());
    }

    @Test
    public void jdbcPageTest1() {
        final JdbcQuery page = DB.Jdbc.select("select 1 from dual where ?=1", 1)
                .page(Page.build(1, 2));
        showSql(page, "jdbcPageTest1", "select 1 from dual where 1=1 LIMIT 0,2");
    }

    @Test
    public void jdbcPageTest2() {
        final JdbcQuery page = DB.Jdbc.select("select 1 from dual where ?=1", 1)
                .page(Page.build(1, 2, Order.descs("x1", "x2")));
        showSql(page, "jdbcPageTest2", "select 1 from dual where 1=1 order by X1 desc,X2 desc LIMIT 0,2");
    }

    @Test
    public void jdbcPageTest3() {
        final JdbcQuery page = DB.Jdbc.select("select 1 from dual where ?=1", 1)
                .page(1, 20, Order.descs("x1", "x2"))
                .page(Page.build(Order.descs("x1", "x2")));
        showSql(page, "jdbcPageTest3", "select 1 from dual where 1=1 order by X1 desc,X2 desc");
    }
}