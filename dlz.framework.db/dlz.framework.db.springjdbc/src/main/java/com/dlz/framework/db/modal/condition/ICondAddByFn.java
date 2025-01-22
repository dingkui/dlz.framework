package com.dlz.framework.db.modal.condition;

import com.dlz.framework.util.system.MFunction;
import com.dlz.framework.util.system.Reflections;

import static com.dlz.framework.db.enums.DbOprateEnum.*;

public interface ICondAddByFn<T extends ICondAddByFn> {
    T mine();

    void addChildren(Condition child);

    /**
     * 添加一个"between"条件到当前条件对象
     * 此方法接受一个列名和两个值，用于构建"between"查询条件
     *
     * @param column 列的名称，通过反射获取
     * @param value1 第一个值，用于构建范围查询的起始点
     * @param value2 第二个值，用于构建范围查询的结束点
     * @return 返回当前条件对象，支持链式调用
     */
    default <T1> T bt(MFunction<T1, ?> column, Object value1, Object value2) {
        addChildren(bt.mk(Reflections.getFieldName(column), new Object[]{value1, value2}));
        return mine();
    }

    /**
     * 添加一个简化版的"between"条件到当前条件对象
     * 此方法接受一个列名和一个单一值，用于构建"between"查询条件
     *
     * @param column 列的名称，通过反射获取
     * @param value  用于构建范围查询的值，支持形式：字符串："a1,a2",json:[a1,a2],数组，List
     * @return 返回当前条件对象，支持链式调用
     */
    default <T1> T bt(MFunction<T1, ?> column, Object value) {
        addChildren(bt.mk(Reflections.getFieldName(column), value));
        return mine();
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
    default <T1> T nb(MFunction<T1, ?> column, Object value1, Object value2) {
        addChildren(nb.mk(Reflections.getFieldName(column), new Object[]{value1, value2}));
        return mine();
    }

    /**
     * 添加一个简化版的"not between"条件到当前条件对象
     * 此方法接受一个列名和一个单一值，用于构建"not between"查询条件
     *
     * @param column 列的名称，通过反射获取
     * @param value  用于构建范围查询的值，支持形式：字符串："a1,a2",json:[a1,a2],数组，List
     * @return 返回当前条件对象，支持链式调用
     */
    default <T1> T nb(MFunction<T1, ?> column, Object value) {
        addChildren(nb.mk(Reflections.getFieldName(column), value));
        return mine();
    }

    /**
     * 添加一个"is not null"条件到当前条件对象
     * 此方法接受一个列名，用于构建"is not null"查询条件
     *
     * @param column 列的名称，通过反射获取
     * @return 返回当前条件对象，支持链式调用
     */
    default <T1> T isnn(MFunction<T1, ?> column) {
        addChildren(isnn.mk(Reflections.getFieldName(column), null));
        return mine();
    }

    /**
     * 添加一个"is null"条件到当前条件对象
     * 此方法接受一个列名，用于构建"is null"查询条件
     *
     * @param column 列的名称，通过反射获取
     * @return 返回当前条件对象，支持链式调用
     */
    default <T1> T isn(MFunction<T1, ?> column) {
        addChildren(isn.mk(Reflections.getFieldName(column), null));
        return mine();
    }

    /**
     * 添加一个"equal"条件到当前条件对象
     * 此方法接受一个列名和一个值，用于构建"equal"查询条件
     *
     * @param column 列的名称，通过反射获取
     * @param value  用于比较的值
     * @return 返回当前条件对象，支持链式调用
     */
    default <T1> T eq(MFunction<T1, ?> column, Object value) {
        addChildren(eq.mk(Reflections.getFieldName(column), value));
        return mine();
    }

    /**
     * 添加一个"less than"条件到当前条件对象
     * 此方法接受一个列名和一个值，用于构建"less than"查询条件
     *
     * @param column 列的名称，通过反射获取
     * @param value  用于比较的值
     * @return 返回当前条件对象，支持链式调用
     */
    default <T1> T lt(MFunction<T1, ?> column, Object value) {
        addChildren(lt.mk(Reflections.getFieldName(column), value));
        return mine();
    }

    /**
     * 添加一个"not like"条件到当前条件对象
     * 此方法接受一个列名和一个值，用于构建"not like"查询条件
     *
     * @param column 列的名称，通过反射获取
     * @param value  用于比较的值，通常包含通配符
     * @return 返回当前条件对象，支持链式调用
     */
    default <T1> T nl(MFunction<T1, ?> column, Object value) {
        addChildren(nl.mk(Reflections.getFieldName(column), value));
        return mine();
    }

    /**
     * 添加一个"like right"条件到当前条件对象
     * 此方法接受一个列名和一个值，用于构建"like"查询条件，值的左侧包含通配符
     *
     * @param column 列的名称，通过反射获取
     * @param value  用于比较的值，右侧通常不包含通配符
     * @return 返回当前条件对象，支持链式调用
     */
    default <T1> T lr(MFunction<T1, ?> column, Object value) {
        addChildren(lr.mk(Reflections.getFieldName(column), value));
        return mine();
    }

    /**
     * 添加一个"like left"条件到当前条件对象
     * 此方法接受一个列名和一个值，用于构建"like"查询条件，值的右侧包含通配符
     *
     * @param column 列的名称，通过反射获取
     * @param value  用于比较的值，左侧通常不包含通配符
     * @return 返回当前条件对象，支持链式调用
     */
    default <T1> T ll(MFunction<T1, ?> column, Object value) {
        addChildren(ll.mk(Reflections.getFieldName(column), value));
        return mine();
    }

    /**
     * 添加一个"like"条件到当前条件对象
     * 此方法接受一个列名和一个值，用于构建"like"查询条件，通常用于字符串匹配
     *
     * @param column 列的名称，通过反射获取
     * @param value  用于比较的值，通常包含通配符
     * @return 返回当前条件对象，支持链式调用
     */
    default <T1> T lk(MFunction<T1, ?> column, Object value) {
        addChildren(lk.mk(Reflections.getFieldName(column), value));
        return mine();
    }

    /**
     * 添加一个"in"条件到当前条件对象
     * 此方法接受一个列名和一个值，用于构建"in"查询条件，值通常是一个集合
     *
     * @param column 列的名称，通过反射获取
     * @param value  用于比较的值，可以是数组或集合或逗号分隔的字符串
     * @return 返回当前条件对象，支持链式调用
     */
    default <T1> T in(MFunction<T1, ?> column, Object value) {
        addChildren(in.mk(Reflections.getFieldName(column), value));
        return mine();
    }

    /**
     * 添加一个"not equal"条件到当前条件对象
     * 此方法接受一个列名和一个值，用于构建"not equal"查询条件
     *
     * @param column 列的名称，通过反射获取
     * @param value  用于比较的值
     * @return 返回当前条件对象，支持链式调用
     */
    default <T1> T ne(MFunction<T1, ?> column, Object value) {
        addChildren(ne.mk(Reflections.getFieldName(column), value));
        return mine();
    }

    /**
     * 添加一个"greater than or equal to"条件到当前条件对象
     * 此方法接受一个列名和一个值，用于构建"greater than or equal to"查询条件
     *
     * @param column 列的名称，通过反射获取
     * @param value  用于比较的值
     * @return 返回当前条件对象，支持链式调用
     */
    default <T1> T ge(MFunction<T1, ?> column, Object value) {
        addChildren(ge.mk(Reflections.getFieldName(column), value));
        return mine();
    }

    /**
     * 添加一个"greater than"条件到当前条件对象
     * 此方法接受一个列名和一个值，用于构建"greater than"查询条件
     *
     * @param column 列的名称，通过反射获取
     * @param value  用于比较的值
     * @return 返回当前条件对象，支持链式调用
     */
    default <T1> T gt(MFunction<T1, ?> column, Object value) {
        addChildren(gt.mk(Reflections.getFieldName(column), value));
        return mine();
    }

    /**
     * 添加一个"less than or equal to"条件到当前条件对象
     * 此方法接受一个列名和一个值，用于构建"less than or equal to"查询条件
     *
     * @param column 列的名称，通过反射获取
     * @param value  用于比较的值
     * @return 返回当前条件对象，支持链式调用
     */
    default <T1> T le(MFunction<T1, ?> column, Object value) {
        addChildren(le.mk(Reflections.getFieldName(column), value));
        return mine();
    }
}
