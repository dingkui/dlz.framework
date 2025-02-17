package com.dlz.framework.db.modal.condition;

import com.dlz.comm.json.JSONMap;
import com.dlz.framework.db.enums.DbBuildEnum;
import com.dlz.framework.db.modal.map.ParaMapBase;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Condition implements ICondition<Condition>,ICondAddByFn<Condition>{
    boolean isMake = false;
    String runsql;
    JSONMap paras = new JSONMap();
    List<Condition> children = new ArrayList<>();
    private DbBuildEnum builder;

    public Condition(DbBuildEnum builder) {
        this.builder = builder;
    }

    public Condition() {
    }

    private void make(ParaMapBase pm) {
        if (isMake) {
            pm.addParas(paras);
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

    public String getRunsql(ParaMapBase pm) {
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

    @Override
    public Condition me() {
        return this;
    }

    public void addChildren(Condition child) {
        children.add(child);
    }


}
