package com.dlz.framework.db.modal.condition;

import com.dlz.framework.db.modal.result.Order;
import com.dlz.framework.db.modal.result.Page;
import com.dlz.framework.util.system.MFunction;

import java.util.Arrays;
import java.util.List;

/**
 * 添加and or条件
 *
 * @param <T>
 */
public interface IQueryPageLamda<T extends IQueryPageLamda,T1> extends IQueryPage<T>{
    default T orderByAsc(MFunction<T1, ?>... column) {
        return sort(Order.ascs(column));
    }

    default T orderByDesc(MFunction<T1, ?>... column) {
        return sort(Order.descs(column));
    }
}
