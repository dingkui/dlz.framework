package com.dlz.framework.db.modal.map;

import com.dlz.comm.json.JSONMap;
import com.dlz.comm.util.VAL;
import com.dlz.framework.db.SqlUtil;
import com.dlz.framework.db.convertor.result.Convert;
import com.dlz.framework.db.convertor.result.impl.DateConverter;
import com.dlz.framework.db.enums.DateFormatEnum;
import com.dlz.framework.db.enums.ParaTypeEnum;
import com.dlz.framework.db.modal.items.SqlItem;
import com.dlz.framework.db.modal.result.Page;
import com.dlz.framework.util.system.MFunction;
import com.dlz.framework.util.system.Reflections;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class ParaMapBase implements Serializable {

    private static final long serialVersionUID = 8374167270612933157L;
    @JsonIgnore
    @Getter
    private Convert convert = new Convert();
    @Getter
    private SqlItem sqlItem = new SqlItem();
    @Getter
    @Setter
    private Page page;

    @Getter
    private JSONMap para = new JSONMap();
    private void addDefualtConverter() {
        convert.addClassConvert(new DateConverter(DateFormatEnum.DateTimeStr));
    }

    public ParaMapBase(String sqlKey) {
        sqlItem.setSqlKey(sqlKey);
        this.addDefualtConverter();
    }

    public ParaMapBase(String sqlKey, Page page) {
        sqlItem.setSqlKey(sqlKey);
        this.page=page;
        this.addDefualtConverter();
    }

    public ParaMapBase addParas(Map<String, Object> map) {
        for (String key : map.keySet()) {
            Object v = map.get(key);
            if (v instanceof String[]) {
                String[] paras = (String[]) map.get(key);
                if (paras.length == 1) {
                    para.put(key, paras[0]);
                } else {
                    para.put(key, paras);
                }
            } else {
                para.put(key, v);
            }
        }
        return this;
    }

    /**
     * 添加参数
     *
     * @param key
     * @param value
     * @return
     */
    public ParaMapBase addPara(String key, Object value) {
		para.put(key, value == null ? "" : value);
        return this;
    }
    public <T> ParaMapBase addPara(MFunction<T, ?> column, Object value){
        return addPara(Reflections.getFieldName(column),value);
    }

    /**
     * 添加指定类型的参数（根据类型自动转换）
     *
     * @param key
     * @param value
     * @param pte
     * @return
     */
    public ParaMapBase addPara(String key, String value, ParaTypeEnum pte) {
		para.put(key, SqlUtil.coverString2Object(value, pte));
        return this;
    }

    public VAL<String,Object[]> jdbcSql() {
        SqlUtil.dealParm(this,1,true);
        return new VAL(sqlItem.getSqlJdbc(),sqlItem.getSqlJdbcPara());
    }
    public VAL<String,Object[]> jdbcCnt() {
        SqlUtil.dealParm(this,2,true);
        return new VAL(sqlItem.getSqlJdbc(),sqlItem.getSqlJdbcPara());
    }
    public VAL<String,Object[]> jdbcPage() {
        SqlUtil.dealParm(this,3,true);
        return new VAL(sqlItem.getSqlJdbc(),sqlItem.getSqlJdbcPara());
    }

}
