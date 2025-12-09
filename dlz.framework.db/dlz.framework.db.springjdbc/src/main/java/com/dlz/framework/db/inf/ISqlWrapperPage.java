package com.dlz.framework.db.inf;

import com.dlz.comm.fn.DlzFn;
import com.dlz.framework.db.modal.result.Order;

/**
 * 添加and or条件
 *
 * @param <T>
 */
public interface ISqlWrapperPage<T extends ISqlWrapperPage,T1> extends ISqlMakerPage<T> {
    default T orderByAsc(DlzFn<T1, ?>... column) {
        return sort(Order.ascs(column));
    }

    default T orderByDesc(DlzFn<T1, ?>... column) {
        return sort(Order.descs(column));
    }
}
