package com.dlz.framework.db.warpper;

import com.dlz.comm.json.JSONMap;
import com.dlz.framework.db.SqlUtil;
import com.dlz.framework.db.enums.DbBuildEnum;
import com.dlz.framework.db.enums.DbOprateEnum;
import com.dlz.framework.db.modal.BaseParaMap;
import com.dlz.framework.util.system.MFunction;
import com.dlz.framework.util.system.Reflections;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.dlz.framework.db.enums.DbOprateEnum.*;

public class Condition{
    boolean isMake = false;
    String runsql;
    JSONMap paras = new JSONMap();
    List<Condition> children = new ArrayList<>();
    private Condition parent;
    private DbBuildEnum builder;

    public Condition(DbBuildEnum builder) {
        this.builder = builder;
    }

    public Condition() {
    }

    private void make(BaseParaMap pm) {
        if (isMake) {
            return;
        }
        isMake = true;

        if (builder != null) {
            if (builder == DbBuildEnum.sql) {
                pm.addParas(paras);
                return;
            }
            if (children.size() == 0) {
                runsql="";
//                runsql = runsql.replace("sql", "false");
                return;
            }
            String join = builder == DbBuildEnum.muOr ? "or" : "and";
            String sub = children.stream()
                    .map(item -> item.getRunsql(pm))
                    .filter(item -> item != null && !item.isEmpty())
                    .collect(Collectors.joining(" " + join + " "));
            sub = sub.replaceAll(join + " and", "and")
                    .replaceAll(join + " or", "or");
            if (children.size() > 1 && builder != DbBuildEnum.where) {
                sub = "(" + sub + ")";
            }
            runsql = builder.buildSql(sub);
            return;
        }
        pm.addParas(paras);
    }

    public static Condition where() {
        return DbBuildEnum.where.build();
    }

    public static Condition OR() {
        return DbBuildEnum.muOr.build();
    }

    public static Condition AND() {
        return DbBuildEnum.muAnd.build();
    }

    public String getRunsql(BaseParaMap pm) {
        make(pm);
        return runsql;
    }

    public Condition setRunsql(String runsql) {
        isMake = false;
        this.runsql = runsql;
        return this;
    }

    public Condition addPara(String key, Object value) {
        isMake = false;
        paras.put(key, value);
        return this;
    }

    public Condition addParas(JSONMap paras) {
        isMake = false;
        this.paras.putAll(paras);
        return this;
    }

    public Condition addChildren(Condition child) {
        children.add(child);
        child.parent = this;
        return child;
    }

    /**
     * 添加一个"between"条件到当前条件对象
     * 此方法接受一个列名和两个值，用于构建"between"查询条件
     * @param column 列的名称，通过反射获取
     * @param value1 第一个值，用于构建范围查询的起始点
     * @param value2 第二个值，用于构建范围查询的结束点
     * @return 返回当前条件对象，支持链式调用
     */
    public <T> Condition bt(MFunction<T, ?> column, Object value1, Object value2) {
        addChildren(bt.mk(Reflections.getFieldName(column), new Object[]{value1, value2}));
        return this;
    }

    /**
     * 添加一个简化版的"between"条件到当前条件对象
     * 此方法接受一个列名和一个单一值，用于构建"between"查询条件
     * @param column 列的名称，通过反射获取
     * @param value 用于构建范围查询的值，支持形式：字符串："a1,a2",json:[a1,a2],数组，List
     * @return 返回当前条件对象，支持链式调用
     */
    public <T> Condition bt(MFunction<T, ?> column, Object value) {
        addChildren(bt.mk(Reflections.getFieldName(column), value));
        return this;
    }

    /**
     * 添加一个"not between"条件到当前条件对象
     * 此方法接受一个列名和两个值，用于构建"not between"查询条件
     * @param column 列的名称，通过反射获取
     * @param value1 第一个值，用于构建范围查询的起始点
     * @param value2 第二个值，用于构建范围查询的结束点
     * @return 返回当前条件对象，支持链式调用
     */
    public <T> Condition nb(MFunction<T, ?> column, Object value1, Object value2) {
        addChildren(nb.mk(Reflections.getFieldName(column), new Object[]{value1, value2}));
        return this;
    }

    /**
     * 添加一个简化版的"not between"条件到当前条件对象
     * 此方法接受一个列名和一个单一值，用于构建"not between"查询条件
     * @param column 列的名称，通过反射获取
     * @param value 用于构建范围查询的值，支持形式：字符串："a1,a2",json:[a1,a2],数组，List
     * @return 返回当前条件对象，支持链式调用
     */
    public <T> Condition nb(MFunction<T, ?> column, Object value) {
        addChildren(nb.mk(Reflections.getFieldName(column), value));
        return this;
    }

    /**
     * 添加一个"is not null"条件到当前条件对象
     * 此方法接受一个列名，用于构建"is not null"查询条件
     * @param column 列的名称，通过反射获取
     * @return 返回当前条件对象，支持链式调用
     */
    public <T> Condition isnn(MFunction<T, ?> column) {
        addChildren(isnn.mk(Reflections.getFieldName(column), null));
        return this;
    }

    /**
     * 添加一个"is null"条件到当前条件对象
     * 此方法接受一个列名，用于构建"is null"查询条件
     * @param column 列的名称，通过反射获取
     * @return 返回当前条件对象，支持链式调用
     */
    public <T> Condition isn(MFunction<T, ?> column) {
        addChildren(isn.mk(Reflections.getFieldName(column), null));
        return this;
    }

    /**
     * 添加一个"equal"条件到当前条件对象
     * 此方法接受一个列名和一个值，用于构建"equal"查询条件
     * @param column 列的名称，通过反射获取
     * @param value 用于比较的值
     * @return 返回当前条件对象，支持链式调用
     */
    public <T> Condition eq(MFunction<T, ?> column, Object value) {
        addChildren(eq.mk(Reflections.getFieldName(column), value));
        return this;
    }

    /**
     * 添加一个"less than"条件到当前条件对象
     * 此方法接受一个列名和一个值，用于构建"less than"查询条件
     * @param column 列的名称，通过反射获取
     * @param value 用于比较的值
     * @return 返回当前条件对象，支持链式调用
     */
    public <T> Condition lt(MFunction<T, ?> column, Object value) {
        addChildren(lt.mk(Reflections.getFieldName(column), value));
        return this;
    }

    /**
     * 添加一个"not like"条件到当前条件对象
     * 此方法接受一个列名和一个值，用于构建"not like"查询条件
     * @param column 列的名称，通过反射获取
     * @param value 用于比较的值，通常包含通配符
     * @return 返回当前条件对象，支持链式调用
     */
    public <T> Condition nl(MFunction<T, ?> column, Object value) {
        addChildren(nl.mk(Reflections.getFieldName(column), value));
        return this;
    }

    /**
     * 添加一个"like right"条件到当前条件对象
     * 此方法接受一个列名和一个值，用于构建"like"查询条件，值的左侧包含通配符
     * @param column 列的名称，通过反射获取
     * @param value 用于比较的值，右侧通常不包含通配符
     * @return 返回当前条件对象，支持链式调用
     */
    public <T> Condition lr(MFunction<T, ?> column, Object value) {
        addChildren(lr.mk(Reflections.getFieldName(column), value));
        return this;
    }

    /**
     * 添加一个"like left"条件到当前条件对象
     * 此方法接受一个列名和一个值，用于构建"like"查询条件，值的右侧包含通配符
     * @param column 列的名称，通过反射获取
     * @param value 用于比较的值，左侧通常不包含通配符
     * @return 返回当前条件对象，支持链式调用
     */
    public <T> Condition ll(MFunction<T, ?> column, Object value) {
        addChildren(ll.mk(Reflections.getFieldName(column), value));
        return this;
    }

    /**
     * 添加一个"like"条件到当前条件对象
     * 此方法接受一个列名和一个值，用于构建"like"查询条件，通常用于字符串匹配
     * @param column 列的名称，通过反射获取
     * @param value 用于比较的值，通常包含通配符
     * @return 返回当前条件对象，支持链式调用
     */
    public <T> Condition lk(MFunction<T, ?> column, Object value) {
        addChildren(lk.mk(Reflections.getFieldName(column), value));
        return this;
    }

    /**
     * 添加一个"in"条件到当前条件对象
     * 此方法接受一个列名和一个值，用于构建"in"查询条件，值通常是一个集合
     * @param column 列的名称，通过反射获取
     * @param value 用于比较的值，可以是数组或集合或逗号分隔的字符串
     * @return 返回当前条件对象，支持链式调用
     */
    public <T> Condition in(MFunction<T, ?> column, Object value) {
        addChildren(in.mk(Reflections.getFieldName(column), value));
        return this;
    }

    /**
     * 添加一个"not equal"条件到当前条件对象
     * 此方法接受一个列名和一个值，用于构建"not equal"查询条件
     * @param column 列的名称，通过反射获取
     * @param value 用于比较的值
     * @return 返回当前条件对象，支持链式调用
     */
    public <T> Condition ne(MFunction<T, ?> column, Object value) {
        addChildren(ne.mk(Reflections.getFieldName(column), value));
        return this;
    }

    /**
     * 添加一个"greater than or equal to"条件到当前条件对象
     * 此方法接受一个列名和一个值，用于构建"greater than or equal to"查询条件
     * @param column 列的名称，通过反射获取
     * @param value 用于比较的值
     * @return 返回当前条件对象，支持链式调用
     */
    public <T> Condition ge(MFunction<T, ?> column, Object value) {
        addChildren(ge.mk(Reflections.getFieldName(column), value));
        return this;
    }

    /**
     * 添加一个"greater than"条件到当前条件对象
     * 此方法接受一个列名和一个值，用于构建"greater than"查询条件
     * @param column 列的名称，通过反射获取
     * @param value 用于比较的值
     * @return 返回当前条件对象，支持链式调用
     */
    public <T> Condition gt(MFunction<T, ?> column, Object value) {
        addChildren(gt.mk(Reflections.getFieldName(column), value));
        return this;
    }

    /**
     * 添加一个"less than or equal to"条件到当前条件对象
     * 此方法接受一个列名和一个值，用于构建"less than or equal to"查询条件
     * @param column 列的名称，通过反射获取
     * @param value 用于比较的值
     * @return 返回当前条件对象，支持链式调用
     */
    public <T> Condition le(MFunction<T, ?> column, Object value) {
        addChildren(le.mk(Reflections.getFieldName(column), value));
        return this;
    }
    /**
     * 添加一个"between"条件到当前条件对象
     * 此方法接受一个列名和两个值，用于构建"between"查询条件
     * @param clumnName 列的名称
     * @param value1 第一个值，用于构建范围查询的起始点
     * @param value2 第二个值，用于构建范围查询的结束点
     * @return 返回当前条件对象，支持链式调用
     */
    public Condition bt(String clumnName, Object value1, Object value2) {
        addChildren(bt.mk(clumnName, new Object[]{value1, value2}));
        return this;
    }
    /**
     * 添加一个简化版的"between"条件到当前条件对象
     * 此方法接受一个列名和一个单一值，用于构建"between"查询条件
     * @param clumnName 列的名称
     * @param value 用于构建范围查询的值，支持形式：字符串："a1,a2",json:[a1,a2],数组，List
     * @return 返回当前条件对象，支持链式调用
     */
    public Condition bt(String clumnName, Object value) {
        addChildren(bt.mk(clumnName, value));
        return this;
    }
    /**
     * 添加一个简化版的"not between"条件到当前条件对象
     * 此方法接受一个列名和一个单一值，用于构建"not between"查询条件
     * @param clumnName 列的名称
     * @param value 用于构建范围查询的值，支持形式：字符串："a1,a2",json:[a1,a2],数组，List
     * @return 返回当前条件对象，支持链式调用
     */
    public Condition nb(String clumnName, Object value) {
        addChildren(nb.mk(clumnName, value));
        return this;
    }
    /**
     * 添加一个"not between"条件到当前条件对象
     * 此方法接受一个列名和两个值，用于构建"not between"查询条件
     * @param clumnName 列的名称
     * @param value1 第一个值，用于构建范围查询的起始点
     * @param value2 第二个值，用于构建范围查询的结束点
     * @return 返回当前条件对象，支持链式调用
     */
    public Condition nb(String clumnName, Object value1, Object value2) {
        addChildren(nb.mk(clumnName, new Object[]{value1, value2}));
        return this;
    }
    /**
     * 添加一个"equal"条件到当前条件对象
     * 此方法接受一个列名和一个值，用于构建"equal"查询条件
     * @param clumnName 列的名称
     * @param value 用于比较的值
     * @return 返回当前条件对象，支持链式调用
     */
    public Condition eq(String clumnName, Object value) {
        addChildren(eq.mk(clumnName, value));
        return this;
    }
    /**
     * 添加一个"is not null"条件到当前条件对象
     * 此方法接受一个列名，用于构建"is not null"查询条件
     * @param clumnName 列的名称
     * @return 返回当前条件对象，支持链式调用
     */
    public Condition isnn(String clumnName) {
        addChildren(isnn.mk(clumnName, null));
        return this;
    }
    /**
     * 添加一个"is null"条件到当前条件对象
     * 此方法接受一个列名，用于构建"is null"查询条件
     * @param clumnName 列的名称
     * @return 返回当前条件对象，支持链式调用
     */
    public Condition isn(String clumnName) {
        addChildren(isn.mk(clumnName, null));
        return this;
    }
    /**
     * 添加一个"less than"条件到当前条件对象
     * 此方法接受一个列名和一个值，用于构建"less than"查询条件
     * @param clumnName 列的名称
     * @param value 用于比较的值
     * @return 返回当前条件对象，支持链式调用
     */
    public Condition lt(String clumnName, Object value) {
        addChildren(lt.mk(clumnName, value));
        return this;
    }
    /**
     * 添加一个"not like"条件到当前条件对象
     * 此方法接受一个列名和一个值，用于构建"not like"查询条件
     * @param clumnName 列的名称
     * @param value 用于比较的值，通常包含通配符
     * @return 返回当前条件对象，支持链式调用
     */
    public Condition nl(String clumnName, Object value) {
        addChildren(nl.mk(clumnName, value));
        return this;
    }
    /**
     * 添加一个"like right"条件到当前条件对象
     * 此方法接受一个列名和一个值，用于构建"like"查询条件，值的左侧包含通配符
     * @param clumnName 列的名称
     * @param value 用于比较的值，右侧通常不包含通配符
     * @return 返回当前条件对象，支持链式调用
     */
    public Condition lr(String clumnName, Object value) {
        addChildren(lr.mk(clumnName, value));
        return this;
    }
    /**
     * 添加一个"like left"条件到当前条件对象
     * 此方法接受一个列名和一个值，用于构建"like"查询条件，值的右侧包含通配符
     * @param clumnName 列的名称
     * @param value 用于比较的值，左侧通常不包含通配符
     * @return 返回当前条件对象，支持链式调用
     */
    public Condition ll(String clumnName, Object value) {
        addChildren(ll.mk(clumnName, value));
        return this;
    }
    /**
     * 添加一个"like"条件到当前条件对象
     * 此方法接受一个列名和一个值，用于构建"like"查询条件，通常用于字符串匹配
     * @param clumnName 列的名称
     * @param value 用于比较的值，通常包含通配符
     * @return 返回当前条件对象，支持链式调用
     */
    public Condition lk(String clumnName, Object value) {
        addChildren(lk.mk(clumnName, value));
        return this;
    }
    /**
     * 添加一个"in"条件到当前条件对象
     * 此方法接受一个列名和一个值，用于构建"in"查询条件，值通常是一个集合
     * @param clumnName 列的名称
     * @param value 用于比较的值，可以是数组或集合或逗号分隔的字符串
     * @return 返回当前条件对象，支持链式调用
     */
    public Condition in(String clumnName, Object value) {
        addChildren(in.mk(clumnName, value));
        return this;
    }
    /**
     * 添加一个"not equal"条件到当前条件对象
     * 此方法接受一个列名和一个值，用于构建"not equal"查询条件
     * @param clumnName 列的名称
     * @param value 用于比较的值
     * @return 返回当前条件对象，支持链式调用
     */
    public Condition ne(String clumnName, Object value) {
        addChildren(ne.mk(clumnName, value));
        return this;
    }
    /**
     * 添加一个"greater than or equal to"条件到当前条件对象
     * 此方法接受一个列名和一个值，用于构建"greater than or equal to"查询条件
     * @param clumnName 列的名称
     * @param value 用于比较的值
     * @return 返回当前条件对象，支持链式调用
     */
    public Condition ge(String clumnName, Object value) {
        addChildren(ge.mk(clumnName, value));
        return this;
    }
    /**
     * 添加一个"greater than"条件到当前条件对象
     * 此方法接受一个列名和一个值，用于构建"greater than"查询条件
     * @param clumnName 列的名称
     * @param value 用于比较的值
     * @return 返回当前条件对象，支持链式调用
     */
    public Condition gt(String clumnName, Object value) {
        addChildren(gt.mk(clumnName, value));
        return this;
    }
    /**
     * 添加一个"less than or equal to"条件到当前条件对象
     * 此方法接受一个列名和一个值，用于构建"less than or equal to"查询条件
     * @param clumnName 列的名称
     * @param value 用于比较的值
     * @return 返回当前条件对象，支持链式调用
     */
    public Condition le(String clumnName, Object value) {
        addChildren(le.mk(clumnName, value));
        return this;
    }
    /**
     * 添加一个查询条件
     * @param clumnName 列的名称
     * @param option 查询条件操作符
     * @param value 用于比较的值
     * @return 返回当前条件对象，支持链式调用
     */
    public Condition add(String clumnName, DbOprateEnum option, Object value) {
        addChildren(option.mk(clumnName, value));
        return this;
    }
    /**
     * 自动根据map的键值对添加查询条件
     * @param req:{key:列名，value:值}
     *           key值为列名 可带$前缀，如$eq_key:表示 key=key DbOprateEnum=eq
     *           value值为值
     * @return 返回当前条件对象，支持链式调用
     */
    public Condition auto(Map<String, Object> req) {
        if (req != null) {
            for (String key : req.keySet()) {
                Object o = req.get(key);
                DbOprateEnum oprate = DbOprateEnum.eq;
                if (key.startsWith("$")) {
                    String[] keys = key.split("_");
                    if (keys.length < 2) {
                        continue;
                    }
                    String op = keys[1].substring(1);
                    key = key.substring(op.length() + 2);
                    if (key.length() == 0) {
                        continue;
                    }
                    oprate = DbOprateEnum.getDbOprateEnum(op);
                }
                add(key, oprate, o);
            }
        }
        return this;
    }
    /**
     * 通过自定义SQL和参数构建条件对象
     * 此方法允许使用自定义的SQL语句和参数来构建数据库查询条件，特别适用于需要执行复杂查询的情况
     *
     * @param _sql 自定义的SQL语句模板，其中的参数使用$,#标记${id}表示全文替换，#{id}表示占位符，中括号表示条件语句
     * @param paras 包含SQL参数的JSONMap，用于替换SQL模板中的参数标记
     * @return 返回Condition对象本身，支持链式调用
     */
    public Condition sql(String _sql, JSONMap paras) {
        String sql = _sql.replaceAll("\\$", "\\\\\\$");
        sql = SqlUtil.getConditionStr(sql, paras);
        sql = SqlUtil.replaceSql(sql, paras, 0);
        addChildren(DbBuildEnum.sql.build(sql, paras));
        return this;
    }

    /**
     * 添加一个and语句到当前条件对象中，子条件有多个条件时用括号包装
     *  内部连接方式根据子第一级子条件来判断，如children=and 括号内用and连接，如children=or 括号内用or连接
     * @param children
     * @return 返回Condition对象本身，支持链式调用
     */
    public Condition and(Condition children) {
        Condition and = addChildren(DbBuildEnum.and.build());
        and.addChildren(children);
        return this;
    }
    /**
     * 添加一个or语句到当前条件对象中，子条件有多个条件时用括号包装
     *  内部连接方式根据第一级子条件来判断，如children=and 括号内用and连接，如children=or 括号内用or连接
     * @param children
     * @return 返回Condition对象本身，支持链式调用
     */
    public Condition or(Condition children) {
        Condition or = addChildren(DbBuildEnum.or.build());
        or.addChildren(children);
        return this;
    }

}
