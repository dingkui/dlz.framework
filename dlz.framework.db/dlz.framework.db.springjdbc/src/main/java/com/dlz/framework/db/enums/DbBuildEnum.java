package com.dlz.framework.db.enums;

import com.dlz.comm.exception.SystemException;
import com.dlz.comm.json.JSONMap;
import com.dlz.framework.db.warpper.Condition;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum DbBuildEnum {
    and("and sql"),//and多条件语句,
    or("or sql"),//or多条件语句,
    muOr("sql"),//多条件语句 or 拼接
    muAnd("sql"),//多条件语句 and 拼接,
    sql("(sql)"),//自定义sql,
    where("where sql");//自定义sql,
    public final String _sql;

    public Condition build(String sql, JSONMap paras) {
        switch (this) {
            case sql:
                Condition condition = new Condition(this);
                condition.setRunsql(this._sql.replaceAll("sql", sql));
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
                return new Condition(this).setRunsql(this._sql);
        }
        throw new SystemException("匹配符有误：" + this);
    }

}
