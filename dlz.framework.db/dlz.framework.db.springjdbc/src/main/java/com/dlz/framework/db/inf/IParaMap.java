package com.dlz.framework.db.inf;

import com.dlz.comm.fn.DlzFn;
import com.dlz.comm.inf.IChained;
import com.dlz.comm.json.JSONMap;
import com.dlz.comm.util.system.FieldReflections;
import com.dlz.framework.db.util.SqlUtil;
import com.dlz.framework.db.enums.ParaTypeEnum;

import java.util.Map;


/**
 * 构造单表的更新操作sql
 *
 * @author dingkui
 */
public interface IParaMap<ME extends IParaMap> extends IChained<ME> {

    JSONMap getPara();

    default ME addParas(Map<String, Object> map) {
        for (String key : map.keySet()) {
            Object v = map.get(key);
            if (v instanceof String[]) {
                String[] paras = (String[]) map.get(key);
                if (paras.length == 1) {
                    getPara().put(key, paras[0]);
                } else {
                    getPara().put(key, paras);
                }
            } else {
                getPara().put(key, v);
            }
        }
        return me();
    }

    /**
     * 添加参数
     *
     * @param key
     * @param value
     * @return
     */
    default ME addPara(String key, Object value) {
        getPara().put(key, value == null ? "" : value);
        return me();
    }

    default <T> ME addPara(DlzFn<T, ?> column, Object value) {
        return addPara(FieldReflections.getFieldName(column), value);
    }

    /**
     * 添加指定类型的参数（根据类型自动转换）
     *
     * @param key
     * @param value
     * @param pte
     * @return
     */
    default ME addPara(String key, String value, ParaTypeEnum pte) {
        getPara().put(key, SqlUtil.coverString2Object(value, pte));
        return me();
    }
}
