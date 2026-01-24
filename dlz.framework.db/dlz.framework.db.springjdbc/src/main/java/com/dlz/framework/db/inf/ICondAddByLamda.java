package com.dlz.framework.db.inf;

import com.dlz.comm.fn.DlzFn;
import com.dlz.framework.db.enums.DbOprateEnum;

import static com.dlz.framework.db.enums.DbOprateEnum.*;

/**
 * wrapper中使用，约束Bean
 * @param <ME>
 * @param <T>
 */
public interface ICondAddByLamda<ME extends ICondAddByLamda, T> extends ICondBase<ME> {
    /**
     * 添加一个"between"条件到当前条件对象
     * 此方法接受一个列名和两个值，用于构建"between"查询条件
     *
     * @param column 列的名称，通过反射获取
     * @param value1 第一个值，用于构建范围查询的起始点
     * @param value2 第二个值，用于构建范围查询的结束点
     * @return 返回当前条件对象，支持链式调用
     */
    default ME bt(DlzFn<T, ?> column, Object value1, Object value2) {
        addChildren(bt.mk(column, new Object[]{value1, value2}));
        return me();
    }

    /**
     * 添加一个"between"条件到当前条件对象
     * 此方法接受一个布尔值判断、列名和两个值，用于构建"between"查询条件，仅在is为true时添加条件
     *
     * @param is     是否添加该条件
     * @param column 列的名称，通过反射获取
     * @param value1 第一个值，用于构建范围查询的起始点
     * @param value2 第二个值，用于构建范围查询的结束点
     * @return 返回当前条件对象，支持链式调用
     */
    default ME bt(boolean is, DlzFn<T, ?> column, Object value1, Object value2) {
        if (is) {
            addChildren(bt.mk(column, new Object[]{value1, value2}));
        }
        return me();
    }

    /**
     * 添加一个简化版的"between"条件到当前条件对象
     * 此方法接受一个列名和一个单一值，用于构建"between"查询条件
     *
     * @param column 列的名称，通过反射获取
     * @param value  用于构建范围查询的值，支持形式：字符串："a1,a2",json:[a1,a2],数组，List
     * @return 返回当前条件对象，支持链式调用
     */
    default ME bt(DlzFn<T, ?> column, Object value) {
        addChildren(bt.mk(column, value));
        return me();
    }
    /**
     * 添加一个"between"条件到当前条件对象
     * 此方法接受一个布尔值判断、列名和一个单一值，用于构建"between"查询条件，仅在is为true时添加条件
     *
     * @param is     是否添加该条件
     * @param column 列的名称，通过反射获取
     * @param value  用于构建范围查询的值，支持形式：字符串："a1,a2",json:[a1,a2],数组，List
     * @return 返回当前条件对象，支持链式调用
     */
    default ME bt(boolean is, DlzFn<T, ?> column, Object value) {
        if (is) {
            addChildren(bt.mk(column, value));
        }
        return me();
    }

    /**
     * 添加一个"not between"条件到当前条件对象
     * 此方法接受一个列名和两个值，用于构建"not between"查询条件
     *
     * @param column 列的名称，通过反射获取
     * @param value1 第一个值，用于构建范围查询的起始点
     * @param value2 第二个值，用于构建范围查询的结束点
     * @return 返回当前条件对象，支持链式调用
     */
    default ME nb(DlzFn<T, ?> column, Object value1, Object value2) {
        addChildren(nb.mk(column, new Object[]{value1, value2}));
        return me();
    }

    /**
     * 添加一个"not between"条件到当前条件对象
     * 此方法接受一个布尔值判断、列名和两个值，用于构建"not between"查询条件，仅在is为true时添加条件
     *
     * @param is     是否添加该条件
     * @param column 列的名称，通过反射获取
     * @param value1 第一个值，用于构建范围查询的起始点
     * @param value2 第二个值，用于构建范围查询的结束点
     * @return 返回当前条件对象，支持链式调用
     */
    default ME nb(boolean is, DlzFn<T, ?> column, Object value1, Object value2) {
        if (is) {
            addChildren(nb.mk(column, new Object[]{value1, value2}));
        }
        return me();
    }

    /**
     * 添加一个简化版的"not between"条件到当前条件对象
     * 此方法接受一个列名和一个单一值，用于构建"not between"查询条件
     *
     * @param column 列的名称，通过反射获取
     * @param value  用于构建范围查询的值，支持形式：字符串："a1,a2",json:[a1,a2],数组，List
     * @return 返回当前条件对象，支持链式调用
     */
    default ME nb(DlzFn<T, ?> column, Object value) {
        addChildren(nb.mk(column, value));
        return me();
    }
    /**
     * 添加一个"not between"条件到当前条件对象
     * 此方法接受一个布尔值判断、列名和一个单一值，用于构建"not between"查询条件，仅在is为true时添加条件
     *
     * @param is     是否添加该条件
     * @param column 列的名称，通过反射获取
     * @param value  用于构建范围查询的值，支持形式：字符串："a1,a2",json:[a1,a2],数组，List
     * @return 返回当前条件对象，支持链式调用
     */
    default ME nb(boolean is, DlzFn<T, ?> column, Object value) {
        if (is) {
            addChildren(nb.mk(column, value));
        }
        return me();
    }

    /**
     * 添加一个"is not null"条件到当前条件对象
     * 此方法接受一个列名，用于构建"is not null"查询条件
     *
     * @param column 列的名称，通过反射获取
     * @return 返回当前条件对象，支持链式调用
     */
    default ME isnn(DlzFn<T, ?> column) {
        addChildren(isnn.mk(column, null));
        return me();
    }

    /**
     * 添加一个"is not null"条件到当前条件对象
     * 此方法接受一个布尔值判断和列名，用于构建"is not null"查询条件，仅在is为true时添加条件
     *
     * @param is     是否添加该条件
     * @param column 列的名称，通过反射获取
     * @return 返回当前条件对象，支持链式调用
     */
    default ME isnn(boolean is, DlzFn<T, ?> column) {
        if (is) {
            addChildren(isnn.mk(column, null));
        }
        return me();
    }

    /**
     * 添加一个"is null"条件到当前条件对象
     * 此方法接受一个列名，用于构建"is null"查询条件
     *
     * @param column 列的名称，通过反射获取
     * @return 返回当前条件对象，支持链式调用
     */
    default ME isn(DlzFn<T, ?> column) {
        addChildren(isn.mk(column, null));
        return me();
    }

    /**
     * 添加一个"is null"条件到当前条件对象
     * 此方法接受一个布尔值判断和列名，用于构建"is null"查询条件，仅在is为true时添加条件
     *
     * @param is     是否添加该条件
     * @param column 列的名称，通过反射获取
     * @return 返回当前条件对象，支持链式调用
     */
    default ME isn(boolean is, DlzFn<T, ?> column) {
        if (is) {
            addChildren(isn.mk(column, null));
        }
        return me();
    }

    /**
     * 添加一个"equal"条件到当前条件对象
     * 此方法接受一个列名和一个值，用于构建"equal"查询条件
     *
     * @param column 列的名称，通过反射获取
     * @param value  用于比较的值
     * @return 返回当前条件对象，支持链式调用
     */
    default ME eq(DlzFn<T, ?> column, Object value) {
        addChildren(eq.mk(column, value));
        return me();
    }

    /**
     * 添加一个"equal"条件到当前条件对象
     * 此方法接受一个布尔值判断、列名和值，用于构建"equal"查询条件，仅在is为true时添加条件
     *
     * @param is     是否添加该条件
     * @param column 列的名称，通过反射获取
     * @param value  用于比较的值
     * @return 返回当前条件对象，支持链式调用
     */
    default ME eq(boolean is, DlzFn<T, ?> column, Object value) {
        if (is) {
            addChildren(eq.mk(column, value));
        }
        return me();
    }

    /**
     * 添加一个"less than"条件到当前条件对象
     * 此方法接受一个列名和一个值，用于构建"less than"查询条件
     *
     * @param column 列的名称，通过反射获取
     * @param value  用于比较的值
     * @return 返回当前条件对象，支持链式调用
     */
    default ME lt(DlzFn<T, ?> column, Object value) {
        addChildren(lt.mk(column, value));
        return me();
    }

    /**
     * 添加一个"less than"条件到当前条件对象
     * 此方法接受一个布尔值判断、列名和值，用于构建"less than"查询条件，仅在is为true时添加条件
     *
     * @param is     是否添加该条件
     * @param column 列的名称，通过反射获取
     * @param value  用于比较的值
     * @return 返回当前条件对象，支持链式调用
     */
    default ME lt(boolean is, DlzFn<T, ?> column, Object value) {
        if (is) {
            addChildren(lt.mk(column, value));
        }
        return me();
    }

    /**
     * 添加一个"not like"条件到当前条件对象
     * 此方法接受一个列名和一个值，用于构建"not like"查询条件
     *
     * @param column 列的名称，通过反射获取
     * @param value  用于比较的值，通常包含通配符
     * @return 返回当前条件对象，支持链式调用
     */
    default ME nl(DlzFn<T, ?> column, Object value) {
        addChildren(nl.mk(column, value));
        return me();
    }

    /**
     * 添加一个"not like"条件到当前条件对象
     * 此方法接受一个布尔值判断、列名和值，用于构建"not like"查询条件，仅在is为true时添加条件
     *
     * @param is     是否添加该条件
     * @param column 列的名称，通过反射获取
     * @param value  用于比较的值，通常包含通配符
     * @return 返回当前条件对象，支持链式调用
     */
    default ME nl(boolean is, DlzFn<T, ?> column, Object value) {
        if (is) {
            addChildren(nl.mk(column, value));
        }
        return me();
    }

    /**
     * 添加一个"like right"条件到当前条件对象
     * 此方法接受一个列名和一个值，用于构建"like"查询条件，值的左侧包含通配符
     *
     * @param column 列的名称，通过反射获取
     * @param value  用于比较的值，右侧通常不包含通配符
     * @return 返回当前条件对象，支持链式调用
     */
    default ME lr(DlzFn<T, ?> column, Object value) {
        addChildren(lr.mk(column, value));
        return me();
    }

    /**
     * 添加一个"like right"条件到当前条件对象
     * 此方法接受一个布尔值判断、列名和值，用于构建"like right"查询条件，仅在is为true时添加条件
     *
     * @param is     是否添加该条件
     * @param column 列的名称，通过反射获取
     * @param value  用于比较的值，右侧通常不包含通配符
     * @return 返回当前条件对象，支持链式调用
     */
    default ME lr(boolean is, DlzFn<T, ?> column, Object value) {
        if (is) {
            addChildren(lr.mk(column, value));
        }
        return me();
    }

    /**
     * 添加一个"like left"条件到当前条件对象
     * 此方法接受一个列名和一个值，用于构建"like"查询条件，值的右侧包含通配符
     *
     * @param column 列的名称，通过反射获取
     * @param value  用于比较的值，左侧通常不包含通配符
     * @return 返回当前条件对象，支持链式调用
     */
    default ME ll(DlzFn<T, ?> column, Object value) {
        addChildren(ll.mk(column, value));
        return me();
    }

    /**
     * 添加一个"like left"条件到当前条件对象
     * 此方法接受一个布尔值判断、列名和值，用于构建"like left"查询条件，仅在is为true时添加条件
     *
     * @param is     是否添加该条件
     * @param column 列的名称，通过反射获取
     * @param value  用于比较的值，左侧通常不包含通配符
     * @return 返回当前条件对象，支持链式调用
     */
    default ME ll(boolean is, DlzFn<T, ?> column, Object value) {
        if (is) {
            addChildren(ll.mk(column, value));
        }
        return me();
    }

    /**
     * 添加一个"like"条件到当前条件对象
     * 此方法接受一个列名和一个值，用于构建"like"查询条件，通常用于字符串匹配
     *
     * @param column 列的名称，通过反射获取
     * @param value  用于比较的值，通常包含通配符
     * @return 返回当前条件对象，支持链式调用
     */
    default ME lk(DlzFn<T, ?> column, Object value) {
        addChildren(lk.mk(column, value));
        return me();
    }

    /**
     * 添加一个"like"条件到当前条件对象
     * 此方法接受一个布尔值判断、列名和值，用于构建"like"查询条件，仅在is为true时添加条件
     *
     * @param is     是否添加该条件
     * @param column 列的名称，通过反射获取
     * @param value  用于比较的值，通常包含通配符
     * @return 返回当前条件对象，支持链式调用
     */
    default ME lk(boolean is, DlzFn<T, ?> column, Object value) {
        if (is) {
            addChildren(lk.mk(column, value));
        }
        return me();
    }

    /**
     * 添加一个"not in"条件到当前条件对象
     * 此方法接受一个列名和一个值，用于构建"in"查询条件，值通常是一个集合
     *
     * @param column 列的名称，通过反射获取
     * @param value  用于比较的值，可以是数组或集合或逗号分隔的字符串
     * @return 返回当前条件对象，支持链式调用
     */
    default ME ni(DlzFn<T, ?> column, Object value) {
        addChildren(ni.mk(column, value));
        return me();
    }

    /**
     * 添加一个"not in"条件到当前条件对象
     * 此方法接受一个布尔值判断、列名和值，用于构建"not in"查询条件，仅在is为true时添加条件
     *
     * @param is     是否添加该条件
     * @param column 列的名称，通过反射获取
     * @param value  用于比较的值，可以是数组或集合或逗号分隔的字符串
     * @return 返回当前条件对象，支持链式调用
     */
    default ME ni(boolean is, DlzFn<T, ?> column, Object value) {
        if (is) {
            addChildren(ni.mk(column, value));
        }
        return me();
    }

    /**
     * 添加一个"in"条件到当前条件对象
     * 此方法接受一个列名和一个值，用于构建"in"查询条件，值通常是一个集合
     *
     * @param column 列的名称，通过反射获取
     * @param value  用于比较的值，可以是数组或集合或逗号分隔的字符串
     * @return 返回当前条件对象，支持链式调用
     */
    default ME in(DlzFn<T, ?> column, Object value) {
        addChildren(in.mk(column, value));
        return me();
    }

    /**
     * 添加一个"in"条件到当前条件对象
     * 此方法接受一个布尔值判断、列名和值，用于构建"in"查询条件，仅在is为true时添加条件
     *
     * @param is     是否添加该条件
     * @param column 列的名称，通过反射获取
     * @param value  用于比较的值，可以是数组或集合或逗号分隔的字符串
     * @return 返回当前条件对象，支持链式调用
     */
    default ME in(boolean is, DlzFn<T, ?> column, Object value) {
        if (is) {
            addChildren(in.mk(column, value));
        }
        return me();
    }

    /**
     * 添加一个"not equal"条件到当前条件对象
     * 此方法接受一个列名和一个值，用于构建"not equal"查询条件
     *
     * @param column 列的名称，通过反射获取
     * @param value  用于比较的值
     * @return 返回当前条件对象，支持链式调用
     */
    default ME ne(DlzFn<T, ?> column, Object value) {
        addChildren(ne.mk(column, value));
        return me();
    }

    /**
     * 添加一个"not equal"条件到当前条件对象
     * 此方法接受一个布尔值判断、列名和值，用于构建"not equal"查询条件，仅在is为true时添加条件
     *
     * @param is     是否添加该条件
     * @param column 列的名称，通过反射获取
     * @param value  用于比较的值
     * @return 返回当前条件对象，支持链式调用
     */
    default ME ne(boolean is, DlzFn<T, ?> column, Object value) {
        if (is) {
            addChildren(ne.mk(column, value));
        }
        return me();
    }

    /**
     * 添加一个"greater than or equal to"条件到当前条件对象
     * 此方法接受一个列名和一个值，用于构建"greater than or equal to"查询条件
     *
     * @param column 列的名称，通过反射获取
     * @param value  用于比较的值
     * @return 返回当前条件对象，支持链式调用
     */
    default ME ge(DlzFn<T, ?> column, Object value) {
        addChildren(ge.mk(column, value));
        return me();
    }

    /**
     * 添加一个"greater than or equal to"条件到当前条件对象
     * 此方法接受一个布尔值判断、列名和值，用于构建"greater than or equal to"查询条件，仅在is为true时添加条件
     *
     * @param is     是否添加该条件
     * @param column 列的名称，通过反射获取
     * @param value  用于比较的值
     * @return 返回当前条件对象，支持链式调用
     */
    default ME ge(boolean is, DlzFn<T, ?> column, Object value) {
        if (is) {
            addChildren(ge.mk(column, value));
        }
        return me();
    }

    /**
     * 添加一个"greater than"条件到当前条件对象
     * 此方法接受一个列名和一个值，用于构建"greater than"查询条件
     *
     * @param column 列的名称，通过反射获取
     * @param value  用于比较的值
     * @return 返回当前条件对象，支持链式调用
     */
    default ME gt(DlzFn<T, ?> column, Object value) {
        addChildren(gt.mk(column, value));
        return me();
    }

    /**
     * 添加一个"greater than"条件到当前条件对象
     * 此方法接受一个布尔值判断、列名和值，用于构建"greater than"查询条件，仅在is为true时添加条件
     *
     * @param is     是否添加该条件
     * @param column 列的名称，通过反射获取
     * @param value  用于比较的值
     * @return 返回当前条件对象，支持链式调用
     */
    default ME gt(boolean is, DlzFn<T, ?> column, Object value) {
        if (is) {
            addChildren(gt.mk(column, value));
        }
        return me();
    }

    /**
     * 添加一个"less than or equal to"条件到当前条件对象
     * 此方法接受一个列名和一个值，用于构建"less than or equal to"查询条件
     *
     * @param column 列的名称，通过反射获取
     * @param value  用于比较的值
     * @return 返回当前条件对象，支持链式调用
     */
    default ME le(DlzFn<T, ?> column, Object value) {
        addChildren(le.mk(column, value));
        return me();
    }

    /**
     * 添加一个"less than or equal to"条件到当前条件对象
     * 此方法接受一个布尔值判断、列名和值，用于构建"less than or equal to"查询条件，仅在is为true时添加条件
     *
     * @param is     是否添加该条件
     * @param column 列的名称，通过反射获取
     * @param value  用于比较的值
     * @return 返回当前条件对象，支持链式调用
     */
    default ME le(boolean is, DlzFn<T, ?> column, Object value) {
        if (is) {
            addChildren(le.mk(column, value));
        }
        return me();
    }

    /**
     * 添加一个自定义条件
     *
     * @param column 列的名称
     * @param op 自定义
     * @param value     用于比较的值
     * @return 返回当前条件对象，支持链式调用
     */
    default ME op(DlzFn<T, ?> column, DbOprateEnum op, Object value) {
        addChildren(op.mk(column, value));
        return me();
    }
}
