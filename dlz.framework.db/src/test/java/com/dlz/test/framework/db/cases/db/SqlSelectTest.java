package com.dlz.test.framework.db.cases.db;

import com.dlz.framework.db.modal.DB;
import com.dlz.framework.db.modal.wrapper.SqlQuery;
import com.dlz.framework.db.modal.result.Order;
import com.dlz.framework.db.modal.result.Page;
import com.dlz.test.framework.db.config.SpingDbBaseTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class SqlSelectTest extends SpingDbBaseTest {
    @Test
    public void sqlSelectTest1() {
        String sql = "key.sqlTest.sqlUtil";
        SqlQuery ump2 = DB.Sql.select(sql);
        ump2.addPara("a", "a1");
        ump2.addPara("b", "b1");
        ump2.addPara("d", "d1");
        ump2.addPara("c", "c1");
        ump2.addPara("_sql", "_sql${a}");
        ump2.setPage(Page.build(1, 2, Order.asc("id")));

        showSql(ump2, "sqlSelectTest1", "select * from bb where 1=1 and a='a1' and b='b1' and c=2 and d=d1 and d=ddd ^d1 and d='d1' and d1='null' and d2='null' and c='c1' order by ID asc LIMIT 0,2");

    }

    @Test
    public void sqlSelectTest2() {
        String sql = "key.test";
        SqlQuery ump2 = DB.Sql.select(sql);
        ump2.addPara("a", "a1");
        ump2.addPara("b", "b1");
        ump2.addPara("d", "d1");
        ump2.addPara("c", "c1");
        ump2.addPara("_sql", "_sql${a}");
        ump2.setPage(Page.build(1, 2, Order.asc("id")));
        showSql(ump2, "sqlSelectTest2", "select * from from dual xxx order by ID asc LIMIT 0,2");
    }

    @Test
    public void sqlSelectTest3(){
//		ParaMap ump=new ParaMap("select 1 from dual");
//		ump.setPage(Page.build(1, 1));
//		cs.getMap(ump);
        DB.Sql.select("select t.* from PTN t where t.id=${key.comm.pageSql} and t.cc=${a} and c=${b} and ccc")
                .addPara("a", "a${b}")
                .addPara("b", "b${c}")
                .addPara("_sql", "_sql${a}").queryOne();
//		ump2.setPage(Page.build(1, 2,"id","asc"));
    }
}