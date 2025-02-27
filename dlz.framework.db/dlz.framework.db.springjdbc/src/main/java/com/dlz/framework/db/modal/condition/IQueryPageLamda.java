package com.dlz.framework.db.modal.condition;

import com.dlz.framework.db.modal.result.Order;
import com.dlz.comm.fn.DlzFn;

/**
 * 添加and or条件
 *
 * @param <T>
 */
public interface IQueryPageLamda<T extends IQueryPageLamda,T1> extends IQueryPage<T>{
    default T orderByAsc(DlzFn<T1, ?>... column) {
        return sort(Order.ascs(column));
    }

    default T orderByDesc(DlzFn<T1, ?>... column) {
        return sort(Order.descs(column));
    }
}
