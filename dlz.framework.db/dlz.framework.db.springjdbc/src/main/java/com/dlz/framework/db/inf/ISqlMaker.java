package com.dlz.framework.db.inf;

/**
 * 添加and or条件
 */
public interface ISqlMaker extends ISqlPara{
     String getTableName();
     String getSql();
}
