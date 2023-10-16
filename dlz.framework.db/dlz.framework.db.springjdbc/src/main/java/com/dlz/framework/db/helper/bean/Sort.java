package com.dlz.framework.db.helper.bean;

import com.dlz.comm.util.StringUtils;
import com.dlz.framework.db.helper.util.DbNameUtil;
import com.dlz.framework.util.system.MFunction;
import com.dlz.framework.util.system.Reflections;

import java.util.ArrayList;
import java.util.List;

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

    public <T, R> Sort(MFunction<T, R> column, Direction direction) {
        Order order = new Order();
        order.setColumn(Reflections.getFieldName(column));
        order.setDirection(direction);
        orderList.add(order);
    }
    public Sort asc(String column) {
        return this.add(column,Direction.ASC);
    }
    public <T> Sort asc(MFunction<T, ?>  column) {
        return this.add(column,Direction.ASC);
    }
    public Sort desc(String column) {
        return this.add(column,Direction.DESC);
    }
    public Sort desc(MFunction<?, ?>  column) {
        return this.add(column,Direction.DESC);
    }
    public Sort add(String column, Direction direction) {
        Order order = new Order();
        order.setColumn(column);
        order.setDirection(direction);
        orderList.add(order);
        return this;
    }

    public <T> Sort add(MFunction<T, ?> column, Direction direction) {
        Order order = new Order();
        order.setColumn(Reflections.getFieldName(column));
        order.setDirection(direction);
        orderList.add(order);
        return this;
    }

    public String toString() {
        if(orderList.size()== 0){
            return "";
        }
        List<String> sqlList = new ArrayList<>();
        for (Order order : orderList) {
            String sql = DbNameUtil.getDbClumnName(order.getColumn());
            sqlList.add(sql+" "+order.getDirection().toString());
        }

        return " ORDER BY " + StringUtils.join(",", sqlList);
    }
}
