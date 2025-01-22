package com.dlz.framework.db.enums;

import com.dlz.comm.exception.SystemException;
import com.dlz.comm.util.ValUtil;
import com.dlz.comm.util.encry.TraceUtil;
import com.dlz.framework.db.SqlUtil;
import com.dlz.framework.db.convertor.ConvertUtil;
import com.dlz.framework.db.modal.condition.Condition;
import lombok.AllArgsConstructor;

@AllArgsConstructor
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
    nb("dbn not between #{key1} and #{key2}");//BETWEEN 值1 AND 值2
    public final String _sql;

    private Condition paraZero(String dbn) {
        Condition condition = new Condition();
        condition.setRunsql(this._sql.replaceAll("dbn", ConvertUtil.str2Clumn(dbn)));
        return condition;
    }

    private Condition paraOne(String dbn, Object value) {
        String key = this + "_" + TraceUtil.generateShortUuid();
        Condition condition = new Condition();
        condition.addPara(key, value);
        condition.setRunsql(this._sql
                .replaceAll("dbn", ConvertUtil.str2Clumn(dbn))
                .replaceAll("key", key));
        return condition;
    }

    private Condition paraTwo(String dbn, Object value) {
        String key = this + "_" + TraceUtil.generateShortUuid();
        Object[] array = ValUtil.getArray(value);
        if (array.length < 2) {
            throw new SystemException("参数有误，需要有2个值：" + this);
        }
        Condition condition = new Condition();
        String key1 = key + "1";
        String key2 = key + "2";
        condition.addPara(key1, array[0]);
        condition.addPara(key2, array[1]);
        condition.setRunsql(this._sql
                .replaceAll("dbn", ConvertUtil.str2Clumn(dbn))
                .replaceAll("key1", key1)
                .replaceAll("key2", key2));
        return condition;
    }
    private Condition paraIn(String dbn, Object value) {
        String key = this + "_" + TraceUtil.generateShortUuid();
        Condition condition = new Condition();
        condition.setRunsql(this._sql
                .replaceAll("dbn", ConvertUtil.str2Clumn(dbn))
                .replaceAll("key", key));
        if (value instanceof String) {
            String v = ((String) value);
            if (v.startsWith("sql:")) {
                condition.addPara(key, ConvertUtil.str2Clumn(v.substring(4)));
                return condition;
            }
        }
        condition.addPara(key, SqlUtil.getSqlInStr(value));
        return condition;
    }

    public Condition mk(String dbn, Object value) {
        switch (this) {
            case lk:
            case nl:
                return paraOne(dbn, "%" + value + "%");
            case ll:
                return paraOne(dbn, value + "%");
            case lr:
                return paraOne(dbn, "%" + value);
            case eq:
            case ne:
            case gt:
            case ge:
            case lt:
            case le:
                return paraOne(dbn, value);
            case bt:
            case nb:
                return paraTwo(dbn, value);
            case isn:
            case isnn:
                return paraZero(dbn);
            case in:
                return paraIn(dbn, value);
            default:
                throw new SystemException("匹配符有误：" + this);
        }
    }

    public static DbOprateEnum getDbOprateEnum(String oprate) {
        for (DbOprateEnum dbOprateEnum : DbOprateEnum.values()) {
            if (dbOprateEnum.toString().equals(oprate)) {
                return dbOprateEnum;
            }
        }
        return null;
    }
}
