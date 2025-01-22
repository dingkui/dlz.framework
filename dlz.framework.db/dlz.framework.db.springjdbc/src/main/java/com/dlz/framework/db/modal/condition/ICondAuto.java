package com.dlz.framework.db.modal.condition;

import com.dlz.framework.db.enums.DbOprateEnum;

import java.util.Map;

/**
 * 添加and or条件
 * @param <T>
 */
public interface ICondAuto<T extends ICondAuto> {
    T mine();
    void addChildren(Condition child);

    /**
     * 自动根据map的键值对添加查询条件
     *
     * @param req:{key:列名，value:值} key值为列名 可带$前缀，如$eq_key:表示 key=key DbOprateEnum=eq
     *                             value值为值
     * @return 返回当前条件对象，支持链式调用
     */
    default T auto(Map<String, Object> req) {
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
                addChildren(oprate.mk(key, o));
            }
        }
        return mine();
    }
}
