package com.dlz.framework.db.modal.para;

import com.dlz.framework.db.inf.IOperatorDelete;
import com.dlz.framework.db.inf.ISqlWrapperSearch;

/**
 * 删除语句生成器
 *
 * @author dk
 */
public class WrapperDelete<T> extends AWrapperSearch<WrapperDelete<T>,T, MakerDelete> implements
        ISqlWrapperSearch<WrapperDelete<T>, T>,
        IOperatorDelete<WrapperDelete<T>> {

    public static <T> WrapperDelete<T> wrapper(T conditionBean) {
        return new WrapperDelete(conditionBean);
    }

    public static <T> WrapperDelete<T> wrapper(Class<T> beanClass) {
        return new WrapperDelete(beanClass);
    }

    private WrapperDelete(Class<T> beanClass) {
        super(beanClass);
        setPm(new MakerDelete(getTableName()));
    }

    private WrapperDelete(T conditionBean) {
        super(conditionBean);
        setPm(new MakerDelete(getTableName()));
    }

    @Override
    public WrapperDelete<T> me() {
        return this;
    }
}
