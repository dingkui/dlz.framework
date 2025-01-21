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


    public <T> Condition bt(MFunction<T, ?> column, Object value1, Object value2) {
        addChildren(bt.mk(Reflections.getFieldName(column), new Object[]{value1, value2}));
        return this;
    }
    public <T> Condition bt(MFunction<T, ?> column, Object value) {
        addChildren(bt.mk(Reflections.getFieldName(column), value));
        return this;
    }

    public <T> Condition nb(MFunction<T, ?> column, Object value1, Object value2) {
        addChildren(nb.mk(Reflections.getFieldName(column), new Object[]{value1, value2}));
        return this;
    }
    public <T> Condition nb(MFunction<T, ?> column, Object value) {
        addChildren(nb.mk(Reflections.getFieldName(column), value));
        return this;
    }

    public <T> Condition isnn(MFunction<T, ?> column) {
        addChildren(isnn.mk(Reflections.getFieldName(column), null));
        return this;
    }

    public <T> Condition isn(MFunction<T, ?> column) {
        addChildren(isn.mk(Reflections.getFieldName(column), null));
        return this;
    }

    public <T> Condition eq(MFunction<T, ?> column, Object value) {
        addChildren(eq.mk(Reflections.getFieldName(column), value));
        return this;
    }

    public <T> Condition lt(MFunction<T, ?> column, Object value) {
        addChildren(lt.mk(Reflections.getFieldName(column), value));
        return this;
    }

    public <T> Condition nl(MFunction<T, ?> column, Object value) {
        addChildren(nl.mk(Reflections.getFieldName(column), value));
        return this;
    }

    public <T> Condition lr(MFunction<T, ?> column, Object value) {
        addChildren(lr.mk(Reflections.getFieldName(column), value));
        return this;
    }

    public <T> Condition ll(MFunction<T, ?> column, Object value) {
        addChildren(ll.mk(Reflections.getFieldName(column), value));
        return this;
    }

    public <T> Condition lk(MFunction<T, ?> column, Object value) {
        addChildren(lk.mk(Reflections.getFieldName(column), value));
        return this;
    }

    public <T> Condition in(MFunction<T, ?> column, Object value) {
        addChildren(in.mk(Reflections.getFieldName(column), value));
        return this;
    }

    public <T> Condition ne(MFunction<T, ?> column, Object value) {
        addChildren(ne.mk(Reflections.getFieldName(column), value));
        return this;
    }

    public <T> Condition ge(MFunction<T, ?> column, Object value) {
        addChildren(ge.mk(Reflections.getFieldName(column), value));
        return this;
    }

    public <T> Condition gt(MFunction<T, ?> column, Object value) {
        addChildren(gt.mk(Reflections.getFieldName(column), value));
        return this;
    }

    public <T> Condition le(MFunction<T, ?> column, Object value) {
        addChildren(le.mk(Reflections.getFieldName(column), value));
        return this;
    }

    public Condition bt(String clumnName, Object value1, Object value2) {
        addChildren(bt.mk(clumnName, new Object[]{value1, value2}));
        return this;
    }
    public Condition bt(String clumnName, Object value) {
        addChildren(bt.mk(clumnName, value));
        return this;
    }

    public Condition nb(String clumnName, Object value) {
        addChildren(nb.mk(clumnName, value));
        return this;
    }

    public Condition nb(String clumnName, Object value1, Object value2) {
        addChildren(nb.mk(clumnName, new Object[]{value1, value2}));
        return this;
    }

    public Condition eq(String clumnName, Object value) {
        addChildren(eq.mk(clumnName, value));
        return this;
    }

    public Condition isnn(String clumnName) {
        addChildren(isnn.mk(clumnName, null));
        return this;
    }

    public Condition isn(String clumnName) {
        addChildren(isn.mk(clumnName, null));
        return this;
    }

    public Condition lt(String clumnName, Object value) {
        addChildren(lt.mk(clumnName, value));
        return this;
    }

    public Condition nl(String clumnName, Object value) {
        addChildren(nl.mk(clumnName, value));
        return this;
    }

    public Condition lr(String clumnName, Object value) {
        addChildren(lr.mk(clumnName, value));
        return this;
    }

    public Condition ll(String clumnName, Object value) {
        addChildren(ll.mk(clumnName, value));
        return this;
    }

    public Condition lk(String clumnName, Object value) {
        addChildren(lk.mk(clumnName, value));
        return this;
    }

    public Condition in(String clumnName, Object value) {
        addChildren(in.mk(clumnName, value));
        return this;
    }

    public Condition ne(String clumnName, Object value) {
        addChildren(ne.mk(clumnName, value));
        return this;
    }

    public Condition ge(String clumnName, Object value) {
        addChildren(ge.mk(clumnName, value));
        return this;
    }

    public Condition gt(String clumnName, Object value) {
        addChildren(gt.mk(clumnName, value));
        return this;
    }

    public Condition le(String clumnName, Object value) {
        addChildren(le.mk(clumnName, value));
        return this;
    }

    public Condition add(String clumnName, DbOprateEnum option, Object value) {
        addChildren(option.mk(clumnName, value));
        return this;
    }

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

    public Condition sql(String _sql, JSONMap paras) {
        String sql = _sql.replaceAll("\\$", "\\\\\\$");
        sql = SqlUtil.getConditionStr(sql, paras);
        sql = SqlUtil.replaceSql(sql, paras, 0);
        addChildren(DbBuildEnum.sql.build(sql, paras));
        return this;
    }

    public Condition and(Condition children) {
        Condition and = addChildren(DbBuildEnum.and.build());
        and.addChildren(children);
//        if (children.children.size() == 0 && children.builder == null) {
//            and.addChildren(children);
//        } else {
//            children.children.forEach(c -> and.addChildren(c));
//        }
        return this;
    }

    public Condition or(Condition children) {
        Condition or = addChildren(DbBuildEnum.or.build());
        or.addChildren(children);
//        if (children.children.size() == 0 && children.builder == null) {
//            or.addChildren(children);
//        } else {
//            children.children.forEach(c -> or.addChildren(c));
//        }

        return this;
    }

}
