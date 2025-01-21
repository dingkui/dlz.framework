package com.dlz.framework.db.enums;

import com.dlz.comm.exception.SystemException;
import com.dlz.comm.json.JSONMap;
import com.dlz.comm.util.StringUtils;
import com.dlz.framework.db.warpper.Condition;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum DbBuildEnum {
    and("and _sql_"),//and多条件语句,
    or("or _sql_"),//or多条件语句,
    muOr("_sql_"),//多条件语句 or 拼接
    muAnd("_sql_"),//多条件语句 and 拼接,
    sql("(_sql_)"),//自定义sql,
    where("where _sql_");//自定义sql,
    private final String _sql;

    public Condition build(String sql, JSONMap paras) {
        switch (this) {
            case sql:
                if(StringUtils.isEmpty(sql)){
                    return null;
                }
                Condition condition = new Condition(this);
                condition.setRunsql(buildSql(sql));
                if (paras != null && !paras.isEmpty()) {
                    condition.addParas(paras);
                }
                return condition;
        }
        throw new SystemException("匹配符有误：" + this);
    }

    public Condition build() {
        switch (this) {
            case where:
            case and:
            case or:
            case muOr:
            case muAnd:
                return new Condition(this).setRunsql(_sql);
        }
        throw new SystemException("匹配符有误：" + this);
    }
    public String buildSql(String sql) {
        if(sql.length()==0){
            return "";
        }
        return _sql.replace("_sql_", sql);
    }

}
