package com.dlz.framework.db.enums;

import com.dlz.comm.exception.SystemException;
import com.dlz.comm.json.JSONMap;
import com.dlz.comm.util.ValUtil;
import com.dlz.comm.util.encry.TraceUtil;
import com.dlz.framework.db.SqlUtil;
import com.dlz.framework.db.convertor.ConvertUtil;
import com.dlz.framework.db.modal.BaseParaMap;
import com.dlz.framework.db.warpper.Condition;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.function.Supplier;

public enum DbOprateEnum {
    isn("dbn is null"),//为空
    isnn("dbn is not null"),//不为空
    eq("dbn = #{key}"),
    lt("dbn < #{key}"),//小于
    le("dbn <= #{key}"),//小于等于
    gt("dbn < #{key}"),//大于
    ge("dbn >= #{key}"),//大于等于
    ne("dbn <> #{key}"),//不等于
    in("dbn in (${key})"),
    lk("dbn like #{key}"),//like:%xxx%
    ll("dbn like #{key}"),//左like:xxx%
    lr("dbn like #{key}"),//右like：%xxx
    nl("dbn not like #{key}"),//不like
    bt("dbn between #{key1} and #{key2}"),//BETWEEN 值1 AND 值2
    nb("dbn not between #{key1} and #{key2}"),//BETWEEN 值1 AND 值2,
    and("and (sql)"),//and多条件语句,
    or("or (sql)"),//or多条件语句,
    sql("(sql)"),//自定义sql,
    where("where sql");//自定义sql,
    public final String condition;

    DbOprateEnum(String condition) {
        this.condition = condition;
    }

    public Condition mk(String sql, JSONMap paras) {
        Condition condition = new Condition();
        switch (this) {
            case sql:
                condition.setRunsql(this.condition.replaceAll("sql", sql));
                if(paras!=null && !paras.isEmpty()){
                    condition.addParas(paras);
                }
                return condition;
        }
        throw new SystemException("匹配符有误：" + this);
    }

    public Condition mk() {
        Condition condition = new Condition();
        switch (this) {
            case where:
            case and:
            case or:
                condition.setRunsql(this.condition);
                return condition;
        }
        throw new SystemException("匹配符有误：" + this);
    }

    public Condition mk(String dbn) {
        Condition condition = new Condition();
        switch (this) {
            case isn:
            case isnn:
                condition.setRunsql(this.condition.replaceAll("dbn", ConvertUtil.str2Clumn(dbn)));
                return condition;
        }
        throw new SystemException("匹配符有误：" + this);
    }
    public Condition mkin(String dbn, Object value) {
        Condition condition = new Condition();
        String key = this + "_" + TraceUtil.generateShortUuid();
        switch (this) {
            case in:
                condition.setRunsql(this.condition.replaceAll("dbn", ConvertUtil.str2Clumn(dbn)).replaceAll("key", key));
                if(value instanceof String) {
                    String v = ((String) value);
                    if (v.startsWith("sql:")) {
                        condition.addPara(key, ConvertUtil.str2Clumn(v.substring(4)));
                        return condition;
                    }
                }
                condition.addPara(key, SqlUtil.getSqlInStr(value));
                return condition;
        }
        throw new SystemException("匹配符有误：" + this);
    }

    public Condition mk(String dbn, Object value1, Object value2) {
        Condition condition = new Condition();
        String key = this + "_" + TraceUtil.generateShortUuid();
        switch (this) {
            case bt:
            case nb:
                String key1 = key + "1";
                String key2 = key + "2";
                condition.addPara(key1, value1);
                condition.addPara(key2, value2);
                condition.setRunsql(this.condition
                        .replaceAll("dbn", ConvertUtil.str2Clumn(dbn))
                        .replaceAll("key1", key1)
                        .replaceAll("key2", key2));
                return condition;
        }
        throw new SystemException("匹配符有误：" + this);
    }

    public Condition mk(String dbn, Object value) {
        Condition condition = new Condition();
        String key = this + "_" + TraceUtil.generateShortUuid();
        switch (this) {
            case lk:
            case nl:
                value = "%" + value + "%";
                break;
            case ll:
                value = value + "%";
                break;
            case lr:
                value = "%" + value;
                break;
            case eq:
            case ne:
            case gt:
            case ge:
            case lt:
            case le:
                break;
            default:
                throw new SystemException("匹配符有误：" + this);
        }
        condition.addPara(key, value);
        condition.setRunsql(this.condition
                .replaceAll("dbn", ConvertUtil.str2Clumn(dbn))
                .replaceAll("key", key));
        return condition;
    }

    public String getCondition(String dbn, Object value, BaseParaMap para) {
        try {
            String result = condition;
            switch (this) {
                case bt:
                case nb:
                    Object[] array = ValUtil.getArray(value);
                    para.addPara(this.toString() + "1_" + dbn, array[0]);
                    para.addPara(this.toString() + "2_" + dbn, array[1]);
                    result = result.replace("key1", this.toString() + "1_" + dbn);
                    result = result.replace("key2", this.toString() + "2_" + dbn);
                    result = result.replaceAll("^dbn", ConvertUtil.str2Clumn(dbn));
                    return result;
                case lk:
                case nl:
                    value = "%" + value + "%";
                    break;
                case ll:
                    value = value + "%";
                    break;
                case lr:
                    value = "%" + value;
                    break;
            }
            para.addPara(this.toString() + "_" + dbn, value);
            result = result.replace("key", this.toString() + "_" + dbn);
            result = result.replaceAll("^dbn", ConvertUtil.str2Clumn(dbn));
            return result;
        } catch (Exception e) {
            SystemException.isTrue(true, "匹配符有误：" + this.toString());
        }
        return null;
    }
}
