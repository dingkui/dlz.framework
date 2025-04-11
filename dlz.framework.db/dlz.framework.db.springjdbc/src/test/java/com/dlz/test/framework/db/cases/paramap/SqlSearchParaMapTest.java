package com.dlz.test.framework.db.cases.paramap;

import com.dlz.framework.db.modal.DB;
import com.dlz.framework.db.modal.para.SqlKeyQuery;
import com.dlz.framework.db.modal.result.Order;
import com.dlz.framework.db.modal.result.Page;
import com.dlz.test.framework.db.config.SpingDbBaseTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class SqlSearchParaMapTest extends SpingDbBaseTest{
    @Test
    public void SqlSearchParaTest1(){
        String sql = "key.sqlTest.sqlUtil";
        SqlKeyQuery ump2=DB.sqlSelect(sql);
        ump2.addPara("a", "a1");
        ump2.addPara("b", "b1");
        ump2.addPara("d", "d1");
        ump2.addPara("c", "c1");
        ump2.addPara("_sql", "_sql${a}");
        ump2.setPage(Page.build(1, 2, Order.asc("id")));
        showSql(ump2,"SqlSearchParaTest1","SqlSearchParaTest1");
    }    @Test
    public void SqlSearchKeyTest1(){
        String sql = "key.test";
        SqlKeyQuery ump2=DB.sqlSelect(sql);
        ump2.addPara("a", "a1");
        ump2.addPara("b", "b1");
        ump2.addPara("d", "d1");
        ump2.addPara("c", "c1");
        ump2.addPara("_sql", "_sql${a}");
        ump2.setPage(Page.build(1, 2, Order.asc("id")));
        showSql(ump2,"SqlSearchParaTest1","SqlSearchParaTest1");
    }
}