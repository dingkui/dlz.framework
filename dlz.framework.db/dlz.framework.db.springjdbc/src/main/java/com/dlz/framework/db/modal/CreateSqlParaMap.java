package com.dlz.framework.db.modal;

import com.dlz.framework.db.convertor.ConvertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 构造单表的增删改查操作sql
 *
 * @author dingkui
 */
public class CreateSqlParaMap extends BaseParaMap {

    private static final long serialVersionUID = 8374167270612933157L;
    protected static final String STR_TABLENAME = "tableName";
    protected static final String STR_WHERE = "where";
    private String tableName;
    protected CreateSqlParaMap(String Sql, String tableName) {
        super(Sql);
        table(tableName);
        where(" where false");
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
    public CreateSqlParaMap addClunmnValue(String key, Object value) {
        return addClunmnValue(key, key, value);
    }

    /**
     * 添加参数
     *
     * @param key
     * @param value
     * @return
     */
    public CreateSqlParaMap addClunmnValue(String key, String clumnName, Object value) {
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
