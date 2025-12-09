package com.dlz.test.framework.db.cases.service;

import com.dlz.framework.db.holder.DBHolder;
import com.dlz.framework.db.modal.DB;
import com.dlz.framework.db.modal.result.Order;
import com.dlz.framework.db.modal.result.Page;
import com.dlz.test.framework.db.config.SpingDbBaseTest;
import org.junit.Test;

public class CommServicePageTest extends SpingDbBaseTest {
    @Test
    public void PageTest() {
        DB.sqlSelect("select t.* from Goods t where t.goods_id=310")
                .page(Page.build(1, 2, Order.asc("id")))
                .queryPage();
    }

    @Test
    public void PageTest2() {
        DB.sqlSelect("select t.* from PTN_GOODS_PRICE t where t.goods_id=#{goodsId}")
                .page(Page.build(1, 2, Order.asc("id"), Order.desc("xx2")))
                .addPara("goodsId", 123).query();
    }

    @Test
    public void SeqTest2() {
        System.out.println(DBHolder.sequence("sys_sql_copy1", 1));
        System.out.println(DBHolder.sequence("sys_sql", 1));
    }
}
