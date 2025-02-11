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
    protected String tableName;
    protected ParaMapMaker(String Sql, String tableName) {
        super(Sql);
        this.tableName = tableName;
    }
    public void buildSql(){
        addPara(STR_TABLENAME, tableName);
    }
    /**
     * 添加参数
     *
     * @param key
     * @param value
     * @return
     */
    public ParaMapMaker addClunmnValue(String key, Object value) {
        addPara(key, ConvertUtil.getVal4Db(tableName, key, value));
        return this;
    }

    /**
     * 判断字段是否存在
     *
     * @param clumnName
     * @return
     */
    public boolean isClumnExists(String clumnName) {
        return ConvertUtil.isClumnExists(tableName, clumnName.replaceAll("`", ""));
    }
}
