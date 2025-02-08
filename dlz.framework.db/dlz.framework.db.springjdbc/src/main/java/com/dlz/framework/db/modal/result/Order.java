package com.dlz.framework.db.modal.result;

import com.dlz.framework.util.system.MFunction;
import com.dlz.framework.util.system.Reflections;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    public static Order asc(String column) {
        return build(column, true);
    }

    public static Order desc(String column) {
        return build(column, false);
    }

    public static List<Order> ascs(String... columns) {
        return Arrays.stream(columns).map(Order::asc).collect(Collectors.toList());
    }
    public static <T1> List<Order> ascs(MFunction<T1, ?>... columns) {
        return Arrays.stream(columns).map(item->Order.asc(Reflections.getFieldName(item))).collect(Collectors.toList());
    }
    public static <T1> List<Order> descs(MFunction<T1, ?>... columns) {
        return Arrays.stream(columns).map(item->Order.desc(Reflections.getFieldName(item))).collect(Collectors.toList());
    }

    public static List<Order> descs(String... columns) {
        return Arrays.stream(columns).map(Order::desc).collect(Collectors.toList());
    }

    public static Order build(String column, boolean asc) {
        return new Order(column, asc);
    }
    public static Order build(String column, String sort) {
        return new Order(column, "desc".equalsIgnoreCase(sort));
    }
}
