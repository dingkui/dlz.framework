package com.dlz.test.framework.db.cases.service;

import com.dlz.framework.db.holder.DBHolder;
import com.dlz.framework.db.modal.map.ParaMap;
import com.dlz.framework.db.modal.result.Order;
import com.dlz.framework.db.modal.result.Page;
import com.dlz.test.framework.db.config.SpingDbBaseTest;
import org.junit.Test;

public class CommServicePageTest extends SpingDbBaseTest {
    @Test
    public void PageTest() {
        ParaMap pm = new ParaMap("select t.* from Goods t where t.goods_id=310");
        pm.setPage(Page.build(1, 2, Order.asc("id")));
        DBHolder.doDb(s -> s.getPage(pm));
    }

    @Test
    public void PageTest2() {
        ParaMap ump2 = new ParaMap("select t.* from PTN_GOODS_PRICE t where t.goods_id=#{goodsId}");
        ump2.setPage(Page.build(1, 2, Order.asc("id"), Order.desc("xx2")));
        ump2.addPara("goodsId", 123);
        DBHolder.doDb(s -> s.getMap(ump2));
    }

    @Test
    public void SeqTest2() {
        System.out.println(DBHolder.sequence("sys_sql_copy1"));
        System.out.println(DBHolder.sequence("sys_sql"));
    }
}
