package com.dlz.framework.db.helper.bean;

import com.dlz.comm.util.StringUtils;
import com.dlz.framework.util.system.Reflections;
import com.dlz.framework.db.helper.util.DbNameUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Sort {
    List<Order> orderList = new ArrayList<>();

    public Sort() {
    }

    public Sort(String column, Direction direction) {
        Order order = new Order();
        order.setColumn(column);
        order.setDirection(direction);

        orderList.add(order);
    }

    public Sort(List<Order> orderList) {
        this.orderList.addAll(orderList);
    }

    public <T, R> Sort(Function<T, R> column, Direction direction) {
        Order order = new Order();
        order.setColumn(Reflections.getFieldName(column));
        order.setDirection(direction);

        orderList.add(order);
    }

    public Sort add(String column, Direction direction) {
        Order order = new Order();
        order.setColumn(column);
        order.setDirection(direction);

        orderList.add(order);

        return this;
    }

    public <T, R> Sort add(Function<T, R> column, Direction direction) {
        Order order = new Order();
        order.setColumn(Reflections.getFieldName(column));
        order.setDirection(direction);

        orderList.add(order);

        return this;
    }

    public String toString() {
        List<String> sqlList = new ArrayList<>();
        for (Order order : orderList) {

            String sql = DbNameUtil.getDbClumnName(order.getColumn());

            if (order.getDirection() == Direction.ASC) {
                sql += " ASC";
            }
            if (order.getDirection() == Direction.DESC) {
                sql += " DESC";
            }

            sqlList.add(sql);
        }

        return " ORDER BY " + StringUtils.join(",", sqlList);
    }


}
