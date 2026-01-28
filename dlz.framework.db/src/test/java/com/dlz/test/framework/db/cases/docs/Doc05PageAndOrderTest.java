package com.dlz.test.framework.db.cases.docs;

import com.dlz.framework.db.modal.DB;
import com.dlz.framework.db.modal.result.Order;
import com.dlz.framework.db.modal.result.Page;
import com.dlz.test.framework.db.config.SpingDbBaseTest;
import com.dlz.test.framework.db.entity.User;
import org.junit.Test;

public class Doc05PageAndOrderTest extends SpingDbBaseTest {
    @Test
    public void pageAndOrderTest5_1_1() {
        // 基础分页
        Page page = Page.build(1, 10);  // 第1页，每页10条

        // 带排序的分页
        Page page2 = Page.build(1, 10, Order.desc("create_time"));

        // 多字段排序
        Page page3 = Page.build(1, 10, Order.desc("create_time"), Order.asc("id"));

        // 只排序，无分页
        Page page4 = Page.build(Order.desc("create_time"), Order.asc("id"));

        // 分页结果
        page.getRecords();    // List<User> 当前页数据
        page.getTotal();      // long 总条数
        page.getPages();     // int 总页数
        page.getCurrent();    // int 当前页码
        page.getSize();       // int 每页条数
        // 链式设置排序字段
        page.addOrder(Order.desc("createTime"), Order.asc("id"));

        //非常用方法
        page.getSortSql();       // 取得排序SQL
        page.getOrders() ;      // List<Order> 排序字段
    }

    @Test
    public void pageAndOrderTest5_1_2() {
        // 分页构造方式1：page单独构建
        final Page<?> page = Page.build(1, 10, Order.desc("create_time"));
        DB.Pojo.select(User.class)
                .eq(User::getStatus, 1)
                .page(page)
                .queryBeanPage();

        // 分页构造方式2：分页和排序链式构建
        DB.Pojo.select(User.class)
                .eq(User::getStatus, 1)
                .page(1,10)
                .orderByAsc(User::getId)
                .queryBeanPage();
    }

    @Test
    public void pageAndOrderTest5_1_3() {
       DB.Jdbc.select("SELECT * FROM user WHERE status = ?", 1)
               .page(Page.build(1, 10, Order.desc("id")))
               .queryPage();

        // 生成 SQL：
        // select count(1) from user WHERE status = 1 （自动生成count语句）
        // SELECT * FROM user WHERE status = 1 LIMIT 0, 10 （COUNT>0 时才执行）
    }

    @Test
    public void pageAndOrderTest5_1_4() {
        //方法1：预设模版
        /**
         <sql sqlId="key.pageAndOrderTest5_1_4"><![CDATA[
         SELECT * FROM user WHERE and status = #{status}
         ]]></sql>
         */
        DB.Sql.select("key.pageAndOrderTest5_1_4")
                .addPara("status", 1)
                .page(Page.build(1, 10, Order.desc("id")))
                .queryPage();

        //方法2：直接写SQL
        DB.Sql.select("SELECT * FROM user WHERE and status = #{status}")
                .addPara("status", 1)
                .page(Page.build(1, 10, Order.desc("id")))
                .queryPage();

        // 生成 SQL：
        // select count(1) from user WHERE status = 1 （自动生成count语句）
        // SELECT * FROM user WHERE and status = 1 order by ID desc LIMIT 0,10  （COUNT>0 时才执行）
    }

    @Test
    public void pageAndOrderTest5_1_5() {
        // 只需要排序，不需要分页
        //方法1：设置只有排序的分页
        DB.Pojo.select(User.class)
            .page(Page.build(Order.descs("create_time", "id")))  // 不传页码
            .queryList();
        //方法2：直接链式设置order
        DB.Pojo.select(User.class)
            .orderByDesc("create_time", "id")  // 不传页码
            .queryList();

        // 生成 SQL：
        // select * from USER t where IS_DELETED = 0 order by create_time desc,ID desc
        // （无 LIMIT）
    }

    @Test
    public void pageAndOrderTest5_2_1() {
        // 单字段升序
        Order.asc("create_time");

        // 单字段降序
        Order.desc("create_time");

        // 多字段升序
        Order.ascs("status", "create_time");

        // 多字段降序
        Order.descs("create_time", "id");

        // page 构造
        Page.build(1, 10, Order.desc("create_time"), Order.asc("id"));
    }

}
