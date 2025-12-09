package com.dlz.framework.db.inf;

import com.dlz.framework.db.enums.DbOprateEnum;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * 添加and or条件
 * @param <T>
 */
public interface ICondAuto<T extends ICondAuto> extends ICondBase<T> {
    /**
     * 自动根据map的键值对添加查询条件
     *
     * @param req:{key:列名，value:值} key值为列名 可带$前缀，如$eq_key:表示 key=key DbOprateEnum=eq
     *                             value值为值
     * @return 返回当前条件对象，支持链式调用
     */
    default T auto(Map<String, Object> req) {
        return auto(req, (Function<String, Boolean>) null);
    }


    /**
     * 自动根据map的键值对添加查询条件
     *
     * @param req:{key:列名，value:值} key值为列名 可带$前缀，如$eq_key:表示 key=key DbOprateEnum=eq
     *                             value值为值
     * @return 返回当前条件对象，支持链式调用
     */
    default T auto(Map<String, Object> req, Function<String,Boolean> fillter)  {
        if (req != null) {
            for (String key : req.keySet()) {
                Object o = req.get(key);
                DbOprateEnum oprate = DbOprateEnum.eq;
                if (key.startsWith("_")) {
                    int keyIndex = key.substring(1).indexOf("_");
                    if (keyIndex == -1) {
                        continue;
                    }
                    String op = key.substring(1,keyIndex+1);
                    key = key.substring(op.length() + 2);
                    if (key.length() == 0) {
                        continue;
                    }
                    oprate = DbOprateEnum.getDbOprateEnum(op);
                }
                if(fillter!=null && !fillter.apply(key)){
                    continue;
                }
                addChildren(oprate.mk(key, o));
            }
        }
        return me();
    }
    /**
     * 自动根据map的键值对添加查询条件
     *
     * @param req:{key:列名，value:值} key值为列名 可带$前缀，如$eq_key:表示 key=key DbOprateEnum=eq
     *                             value值为值
     * @return 返回当前条件对象，支持链式调用
     */
    default T auto(Map<String, Object> req, Set<String> exclude) {
        return auto(req, (key)->exclude!=null && !exclude.contains(key));
    }
}
