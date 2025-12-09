package com.dlz.framework.db.modal.result;

import com.dlz.comm.fn.DlzFn;
import com.dlz.framework.db.holder.BeanInfoHolder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Arrays;

/**
 * 排序元素载体
 *
 * @author HCL
 * Create at 2019/5/27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 需要进行排序的字段
     */
    private String column;
    /**
     * 是否正序排列，默认 true
     */
    private boolean asc = true;
    public static Order build(String column, boolean asc) {
        return new Order(column, asc);
    }
    public static Order build(String column, String sort) {
        return new Order(column, "asc".equalsIgnoreCase(sort));
    }
    public static Order buildWithSql(String sql) {
        String[] split = sql.split(" ");
        return build(split[0], split[1]);
    }
    public static Order asc(String column) {
        return build(column, true);
    }
    public static <T> Order asc(DlzFn<T, ?> column) {
        return build(BeanInfoHolder.fnName(column), true);
    }
    public static Order desc(String column) {
        return build(column, false);
    }
    public static <T> Order desc(DlzFn<T, ?> column) {
        return build(BeanInfoHolder.fnName(column), false);
    }
    public static Order[] ascs(String... columns) {
        return Arrays.stream(columns).map(Order::asc).toArray(Order[]::new);
    }
    public static <T> Order[] ascs(DlzFn<T, ?>... columns) {
        return Arrays.stream(columns).map(Order::asc).toArray(Order[]::new);
    }
    public static Order[] descs(String... columns) {
        return Arrays.stream(columns).map(Order::desc).toArray(Order[]::new);
    }
    public static <T1> Order[] descs(DlzFn<T1, ?>... columns) {
        return Arrays.stream(columns).map(Order::desc).toArray(Order[]::new);
    }
    public static Order[] buildWithSqls(String columns) {
        return Arrays.stream(columns.split(",")).map(Order::buildWithSql).toArray(Order[]::new);
    }
}
