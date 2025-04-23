package com.dlz.framework.db.inf;

import com.dlz.comm.json.JSONMap;
import com.dlz.framework.db.SqlUtil;
import com.dlz.framework.db.enums.DbBuildEnum;
import com.dlz.framework.db.modal.condition.Condition;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 添加and or条件
 * @param <T>
 */
public interface ICondAndOr<T extends ICondAndOr> extends ICondBase<T> {
    /**
     * 通过自定义SQL和参数构建条件对象
     * 此方法允许使用自定义的SQL语句和参数来构建数据库查询条件，特别适用于需要执行复杂查询的情况
     *
     * @param sql  自定义的SQL语句模板，其中的参数使用$,#标记${id}表示全文替换，#{id}表示占位符，中括号表示条件语句
     * @param paras 包含SQL参数的JSONMap，用于替换SQL模板中的参数标记
     * @return 返回Condition对象本身，支持链式调用
     */
    default T sql(String sql, JSONMap paras) {
        Condition sqlCond = DbBuildEnum.sql.build(sql, paras);
        if(sqlCond != null){
            addChildren(sqlCond);
        }
        return me();
    }
    /**
     * 通过自定义SQL和参数构建条件对象
     * 此方法允许使用自定义的SQL语句和参数来构建数据库查询条件，特别适用于需要执行复杂查询的情况
     *
     * @param sql  自定义的SQL语句模板，其中的参数使用$,#标记${id}表示全文替换，#{id}表示占位符，中括号表示条件语句
     * @param paras 包含SQL参数的JSONMap，用于替换SQL模板中的参数标记
     * @return 返回Condition对象本身，支持链式调用
     */
    default T apply(String sql, Object... paras) {
        Condition sqlCond = DbBuildEnum.apply.build(sql, paras);
        if(sqlCond != null){
            addChildren(sqlCond);
        }
        return me();
    }

//    /**
//     * 添加一个and语句到当前条件对象中，子条件有多个条件时用括号包装
//     * 内部连接方式根据子第一级子条件来判断，如children=and 括号内用and连接，如children=or 括号内用or连接
//     *
//     * @param children
//     * @return 返回Condition对象本身，支持链式调用
//     */
//    default T and(Condition children) {
//        Condition and = DbBuildEnum.and.build();
//        and.addChildren(children);
//        addChildren(and);
//        return me();
//    }
    /**
     * 添加一个and语句到当前条件对象中，子条件有多个条件时用括号包装
     * 内部连接方式根据子第一级子条件来判断，如children=and 括号内用and连接，如children=or 括号内用or连接
     *
     * @param ors
     * @return 返回Condition对象本身，支持链式调用
     */
    default T and(Consumer<Condition> ors) {
        Condition and = DbBuildEnum.muAnd.build();
        addChildren(and);
        ors.accept(and);
        return me();
    }

    /**
     * 添加一个or语句到当前条件对象中，子条件有多个条件时用括号包装
     * 内部连接方式根据第一级子条件来判断，如children=and 括号内用and连接，如children=or 括号内用or连接
     *
     * @param ands
     * @return 返回Condition对象本身，支持链式调用
     */
    default T or(Consumer<Condition> ands) {
        Condition and = DbBuildEnum.muOr.build();
        addChildren(and);
        ands.accept(and);
        return me();
    }
//    /**
//     * 添加一个or语句到当前条件对象中，子条件有多个条件时用括号包装
//     * 内部连接方式根据第一级子条件来判断，如children=and 括号内用and连接，如children=or 括号内用or连接
//     *
//     * @param children
//     * @return 返回Condition对象本身，支持链式调用
//     */
//    default T or(Condition children) {
//        Condition or = DbBuildEnum.or.build();
//        addChildren(or);
//        or.addChildren(children);
//        return me();
//    }
}
