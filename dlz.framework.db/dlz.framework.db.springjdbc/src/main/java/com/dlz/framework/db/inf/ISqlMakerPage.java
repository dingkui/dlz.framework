package com.dlz.framework.db.inf;

import com.dlz.framework.db.modal.result.Order;
import com.dlz.framework.db.modal.result.Page;

import java.util.Arrays;
import java.util.List;

/**
 * sqlMaker 翻页查询 查询需要实现
 *
 * @param <T>
 */
public interface ISqlMakerPage<T extends ISqlMakerPage>{
    Page getPage();

    T page(Page page);

    default T orderByAsc(String... column) {
        return sort(Order.ascs(column));
    }

    default T orderByDesc(String... column) {
        return sort(Order.descs(column));
    }

    default T page(int pageIndex, int size, Order... orders) {
        return page(pageIndex, size, Arrays.asList(orders));
    }

    /**
     * 分页
     *
     * @param current 页号，从1开始
     * @param size    每页大小 最大10000,0则默认每页20条
     * @param orders
     * @return
     */
    default T page(long current, long size, List<Order> orders) {
        Page pmPage = getPage();
        if (pmPage == null) {
            pmPage = Page.build();
        }
        pmPage.addOrder(orders);
        if (size > 0) {
            pmPage.setSize(size);
        }
        if (current > 0) {
            pmPage.setCurrent(current);
        }
        return page(pmPage);
    }
    /**
     * 分页
     *
     * @param current 页号，从1开始
     * @param size    每页大小 最大10000,0则默认每页20条
     * @param orders
     * @return
     */
    default T page(long current, long size, Order... orders) {
        Page pmPage = getPage();
        if (pmPage == null) {
            pmPage = Page.build();
        }
        pmPage.addOrder(orders);
        if (size > 0) {
            pmPage.setSize(size);
        }
        if (current > 0) {
            pmPage.setCurrent(current);
        }
        return page(pmPage);
    }

    default T sort(Order... orders) {
        return sort(Arrays.asList(orders));
    }

    default T sort(List<Order> orders) {
        return page(0, 0, orders);
    }
}
