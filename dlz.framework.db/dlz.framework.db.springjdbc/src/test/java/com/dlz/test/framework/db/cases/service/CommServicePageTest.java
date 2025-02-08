package com.dlz.test.framework.db.cases.service;

import com.dlz.framework.db.holder.ServiceHolder;
import com.dlz.framework.db.modal.map.ParaMap;
import com.dlz.framework.db.modal.result.Order;
import com.dlz.framework.db.modal.result.Page;
import com.dlz.framework.db.modal.result.ResultMap;
import com.dlz.test.framework.db.config.SpingDbBaseTest;
import org.junit.Test;

import java.util.List;

public class CommServicePageTest extends SpingDbBaseTest {
    @Test
    public void PageTest() {
        ParaMap pm = new ParaMap("select t.* from Goods t where t.goods_id=310");
        pm.setPage(Page.build(1, 2, Order.asc("id")));
        Page<ResultMap> page = ServiceHolder.getService().getPage(pm);
    }

    @Test
    public void PageTest2() {
        ParaMap ump2 = new ParaMap("select t.* from PTN_GOODS_PRICE t where t.goods_id=#{goodsId}");
        ump2.setPage(Page.build(1, 2, Order.asc("id"),Order.desc("xx2")));
        ump2.addPara("goodsId", 123);
        ServiceHolder.getService().getMap(ump2);
    }
}
