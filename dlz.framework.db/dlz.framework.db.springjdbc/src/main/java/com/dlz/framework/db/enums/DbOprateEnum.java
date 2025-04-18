package com.dlz.framework.db.enums;

import com.dlz.comm.exception.SystemException;
import com.dlz.comm.fn.DlzFn;
import com.dlz.comm.util.ValUtil;
import com.dlz.framework.db.SqlUtil;
import com.dlz.framework.db.convertor.DbConvertUtil;
import com.dlz.framework.db.holder.BeanInfoHolder;
import com.dlz.framework.db.modal.condition.Condition;
import lombok.AllArgsConstructor;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

@AllArgsConstructor
public enum DbOprateEnum {
    isn("#n is null"),//为空
    isnn("#n is not null"),//不为空
    eq("#n = #{#k}"),
    lt("#n < #{#k}"),//小于
    le("#n <= #{#k}"),//小于等于
    gt("#n > #{#k}"),//大于
    ge("#n >= #{#k}"),//大于等于
    ne("#n <> #{#k}"),//不等于
    in("#n in (${#k})"),
    lk("#n like #{#k}"),//like:%xxx%
    ll("#n like #{#k}"),//左like:xxx%
    lr("#n like #{#k}"),//右like：%xxx
    nl("#n not like #{#k}"),//不like
    bt("#n between #{#k1} and #{#k2}"),//BETWEEN 值1 AND 值2
    nb("#n not between #{#k1} and #{#k2}");//BETWEEN 值1 AND 值2
    public final String _sql;
    private final static Pattern patternKey = Pattern.compile("#k");
    private final static Pattern patternColumnName = Pattern.compile("#n");
    private static final ThreadLocal<AtomicInteger> paraNameIndex = ThreadLocal.withInitial(AtomicInteger::new);

    /**
     * 增加当前线程的计数器值（默认增加1）
     *
     * @return 增加后的值
     */
    private static int getParaNameIndex() {
        return paraNameIndex.get().addAndGet(1);
    }

    private String mkSql(String dbn, String key) {
        final String dbnSql = patternColumnName.matcher(this._sql).replaceAll(DbConvertUtil.str2DbClumn(dbn));
        return key==null?dbnSql: patternKey.matcher(dbnSql).replaceAll(key);
    }

    private Condition paraZero(String dbn) {
        Condition condition = new Condition();
        condition.setRunsql(mkSql(dbn, null));
        return condition;
    }

    private Condition paraOne(String dbn, Object value) {
        String key = this + "_" + getParaNameIndex();
        Condition condition = new Condition();
        condition.addPara(key, value);
        condition.setRunsql(mkSql(dbn, key));
        return condition;
    }

    private Condition paraTwo(String dbn, Object value) {
        String key = this + "_" + getParaNameIndex();
        Object[] array = ValUtil.toArray(value);
        if (array.length < 2) {
            throw new SystemException("参数有误，需要有2个值：" + this);
        }
        Condition condition = new Condition();
        String key1 = key + "1";
        String key2 = key + "2";
        condition.addPara(key1, array[0]);
        condition.addPara(key2, array[1]);
        condition.setRunsql(mkSql(dbn, key));
        return condition;
    }

    private Condition paraIn(String dbn, Object value) {
        String key = this + "_" + getParaNameIndex();
        Condition condition = new Condition();
        condition.setRunsql(mkSql(dbn, key));
        if (value instanceof String) {
            String v = ((String) value);
            if (v.startsWith("sql:")) {
                condition.addPara(key, DbConvertUtil.str2Clumn(v.substring(4)));
                return condition;
            }
        }
        condition.addPara(key, SqlUtil.getSqlInStr(value));
        return condition;
    }


    public <T> Condition mk(DlzFn<T,?> dbn, Object value) {
        return mk(BeanInfoHolder.fnName(dbn), value);
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
