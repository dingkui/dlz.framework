package com.dlz.framework.db.modal.para;

import com.dlz.comm.fn.DlzFn;
import com.dlz.comm.json.JSONMap;
import com.dlz.framework.db.enums.ParaTypeEnum;
import com.dlz.framework.db.holder.BeanInfoHolder;
import com.dlz.framework.db.inf.IParaMap;
import com.dlz.framework.db.inf.ISqlPara;
import com.dlz.framework.db.modal.items.JdbcItem;
import com.dlz.framework.db.modal.items.SqlItem;
import com.dlz.framework.db.modal.result.Page;
import com.dlz.framework.db.util.SqlUtil;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class ParaMap<ME extends ParaMap> implements Serializable , ISqlPara, IParaMap<ME> {
    private static final long serialVersionUID = 8374167270612933157L;
//    @JsonIgnore
//    @Getter
//    private Convert convert = new Convert();
    @Getter
    private SqlItem sqlItem = new SqlItem();
    @Getter
    @Setter
    private Page page;

    @Getter
    private JSONMap para = new JSONMap();
//    private void addDefualtConverter() {
//        convert.addClassConvert(new DateConverter(DateFormatEnum.DateTimeStr));
//    }
    public ParaMap() {
    }
    public ParaMap(String sqlKey) {
        sqlItem.setSqlKey(sqlKey);
//        this.addDefualtConverter();
    }

    public ParaMap(String sqlKey, Page page) {
        sqlItem.setSqlKey(sqlKey);
        this.page=page;
//        this.addDefualtConverter();
    }

    public ME addParas(Map<String, Object> map) {
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
        return me();
    }

    /**
     * 添加参数
     *
     * @param key
     * @param value
     * @return
     */
    public ME addPara(String key, Object value) {
		para.put(key, value == null ? "" : value);
        return (ME)me();
    }
    public <T> ME addPara(DlzFn<T, ?> column, Object value){
        return addPara(BeanInfoHolder.fnName(column),value);
    }

    /**
     * 添加指定类型的参数（根据类型自动转换）
     *
     * @param key
     * @param value
     * @param pte
     * @return
     */
    public ME addPara(String key, String value, ParaTypeEnum pte) {
		para.put(key, SqlUtil.coverString2Object(value, pte));
        return me();
    }

    public JdbcItem jdbcSql() {
        if (this.getPage() == null) {
            SqlUtil.dealParm(this,1);
        }else{
            SqlUtil.dealParm(this,3);
        }
        return SqlUtil.dealParmToJdbc(this);
    }
    public JdbcItem jdbcCnt() {
        SqlUtil.dealParm(this,2);
        return SqlUtil.dealParmToJdbc(this);
    }

    @Override
    public ME me() {
        return (ME)this;
    }
}
