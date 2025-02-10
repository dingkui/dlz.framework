package com.dlz.framework.db.modal.condition;

import com.dlz.framework.db.modal.result.Order;
import com.dlz.framework.db.modal.result.Page;
import com.dlz.framework.util.system.MFunction;

import java.util.Arrays;
import java.util.List;

/**
 * 添加and or条件
 *
 * @param <T>
 */
public interface IQueryPage<T extends IQueryPage> {
    T mine();

    Page getPage();

    T page(Page page);

    default <T1> T orderByAsc(MFunction<T1, ?>... column) {
        return sort(Order.ascs(column));
    }

    default T orderByAsc(String... column) {
        return sort(Order.ascs(column));
    }

    default <T1> T orderByDesc(MFunction<T1, ?>... column) {
        return sort(Order.descs(column));
    }

    default T orderByDesc(String... column) {
        return sort(Order.descs(column));
    }


    default T page(int pageIndex,int size, Order... orders) {
        return page(pageIndex,size, Arrays.asList(orders));
    }

    /**
     * 分页
     * @param current 页号，从1开始
     * @param size 每页大小 最大10000,0则默认每页20条
     * @param orders
     * @return
     */
    default T page(long current,long size, List<Order> orders) {
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
        return page(0,0, orders);
    }
}
