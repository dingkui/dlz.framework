package com.dlz.framework.db.inf;

/**
 * sqlWrapper查询接口
 * 删除，更新，查询需要实现
 *
 * @param <ME>
 */
public interface ISqlWrapperSearch<ME extends ISqlWrapperSearch, T> extends ISqlSearch<ME>,ICondAddByLamda<ME, T> {
}
