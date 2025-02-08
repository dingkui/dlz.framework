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

    default T page(int size, String column, String order) {
        return page(size, Order.build(column, order));
    }

    default T page(int size, Order... orders) {
        return page(size, Arrays.asList(orders));
    }

    default T page(int size, List<Order> orders) {
        Page pmPage = getPage();
        if (pmPage == null) {
            pmPage = new Page<>();
        }
        pmPage.addOrder(orders);
        if (size > 0) {
            pmPage.setPageSize(size);
        }
        return page(pmPage);
    }

    default T sort(Order... orders) {
        return sort(Arrays.asList(orders));
    }

    default T sort(List<Order> orders) {
        return page(0, orders);
    }
}
