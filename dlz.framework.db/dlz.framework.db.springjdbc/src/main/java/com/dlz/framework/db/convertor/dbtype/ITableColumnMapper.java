package com.dlz.framework.db.convertor.dbtype;

public interface ITableColumnMapper {
    /**
     * 取得字段对应的类型
     *
     * @param @param  tableName
     * @param @param  clumnName
     * @param @param  value
     * @param @return 设定文件
     * @return Object    返回类型
     * @throws
     * @Title: converObj4Db
     */
     Object converObj4Db(String tableName, String clumnName, Object value);
}
