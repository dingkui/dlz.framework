package com.dlz.framework.db.modal;

import com.dlz.comm.json.JSONMap;
import com.dlz.comm.util.VAL;
import com.dlz.framework.db.convertor.result.Convert;
import com.dlz.framework.db.convertor.result.impl.DateConverter;
import com.dlz.framework.db.enums.DateFormatEnum;
import com.dlz.framework.db.enums.ParaTypeEnum;
import com.dlz.framework.db.modal.items.SqlItem;
import com.dlz.framework.db.SqlUtil;
import com.dlz.framework.util.system.MFunction;
import com.dlz.framework.util.system.Reflections;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class BaseParaMap implements Serializable {

    private static final long serialVersionUID = 8374167270612933157L;
    @JsonIgnore
    private Convert convert = new Convert();
    private SqlItem sqlItem = new SqlItem();
    private Page page;

    private JSONMap para = new JSONMap();
    private void addDefualtConverter() {
        convert.addClassConvert(new DateConverter(DateFormatEnum.DateTimeStr));
    }

    public BaseParaMap(String sqlKey) {
        sqlItem.setSqlKey(sqlKey);
        this.addDefualtConverter();
    }

    public BaseParaMap(String sqlKey, Page page) {
        sqlItem.setSqlKey(sqlKey);
        this.page=page;
        this.addDefualtConverter();
    }

    public BaseParaMap addParas(Map<String, Object> map) {
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
    public BaseParaMap addPara(String key, Object value) {
		para.put(key, value == null ? "" : value);
        return this;
    }
    public <T> BaseParaMap addPara(MFunction<T, ?> column, Object value){
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
    public BaseParaMap addPara(String key, String value, ParaTypeEnum pte) {
		para.put(key, SqlUtil.coverString2Object(value, pte));
        return this;
    }

    /**
     * 获取convert
     *
     * @return convert convert
     */
    public Convert getConvert() {
        return convert;
    }

    /**
     * 获取page
     *
     * @return page page
     */
    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public SqlItem getSqlItem() {
        return sqlItem;
    }
    public VAL<String,Object[]> getJdbcSql() {
        SqlUtil.dealParm(this,1,true);
        return new VAL(sqlItem.getSqlJdbc(),sqlItem.getSqlJdbcPara());
    }
    public VAL<String,Object[]> getCntJdbc() {
        SqlUtil.dealParm(this,2,true);
        return new VAL(sqlItem.getSqlJdbc(),sqlItem.getSqlJdbcPara());
    }
    public VAL<String,Object[]> getPageJdbc() {
        SqlUtil.dealParm(this,3,true);
        return new VAL(sqlItem.getSqlJdbc(),sqlItem.getSqlJdbcPara());
    }

    public JSONMap getPara() {
        return para;
    }
}
