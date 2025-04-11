package com.dlz.framework.db.modal.para;

import com.dlz.framework.db.inf.IOperatorExec;
import com.dlz.framework.db.inf.ISqlWrapperSearch;

/**
 * 删除语句生成器
 *
 * @author dk
 */
public class WrapperDelete<T> extends AWrapperSearch<WrapperDelete<T>,T, MakerDelete> implements
        ISqlWrapperSearch<WrapperDelete<T>, T>,
        IOperatorExec {

    public static <T> WrapperDelete<T> wrapper(T conditionBean) {
        return new WrapperDelete(conditionBean);
    }

    public static <T> WrapperDelete<T> wrapper(Class<T> beanClass) {
        return new WrapperDelete(beanClass);
    }

    public WrapperDelete(Class<T> beanClass) {
        super(beanClass);
        setPm(new MakerDelete(getTableName()));
    }

    public WrapperDelete(T conditionBean) {
        super(conditionBean);
        setPm(new MakerDelete(getTableName()));
    }

    @Override
    public WrapperDelete<T> me() {
        return this;
    }
}
