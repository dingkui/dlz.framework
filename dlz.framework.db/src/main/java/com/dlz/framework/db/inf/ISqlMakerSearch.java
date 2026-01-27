package com.dlz.framework.db.inf;

/**
 * sqlMaker查询接口
 * 删除，更新，查询需要实现
 *
 * @param <T>
 */
public interface ISqlMakerSearch<T extends ISqlMakerSearch> extends
        ISqlQuery<T>,
        ICondAddByFn<T>{
}
