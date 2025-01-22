package com.dlz.framework.db.modal.map;

import com.dlz.framework.db.convertor.ConvertUtil;

/**
 * 构造单表的增删改查操作sql
 *
 * @author dingkui
 */
public class ParaMapMaker extends ParaMapBase {

    private static final long serialVersionUID = 8374167270612933157L;
    protected static final String STR_TABLENAME = "tableName";
    protected static final String STR_WHERE = "where";
    private String tableName;
    protected ParaMapMaker(String Sql, String tableName) {
        super(Sql);
        table(tableName);
    }

    protected void table(String tableName) {
        this.tableName = tableName;
        addPara(STR_TABLENAME, tableName);
    }

    public void where(String where) {
        addPara(STR_WHERE, where);
    }

    /**
     * 添加参数
     *
     * @param key
     * @param value
     * @return
     */
    public ParaMapMaker addClunmnValue(String key, Object value) {
        return addClunmnValue(key, key, value);
    }

    /**
     * 添加参数
     *
     * @param key
     * @param value
     * @return
     */
    public ParaMapMaker addClunmnValue(String key, String clumnName, Object value) {
        addPara(key, ConvertUtil.getVal4Db(tableName, clumnName, value));
        return this;
    }


    /**
     * 判断字段是否存在
     *
     * @param clumnName
     * @return
     */
    public boolean isClumnExists(String clumnName) {
        return ConvertUtil.isClumnExists(tableName, clumnName);
    }
}
