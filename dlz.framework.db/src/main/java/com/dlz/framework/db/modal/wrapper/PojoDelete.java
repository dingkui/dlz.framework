package com.dlz.framework.db.modal.wrapper;

import com.dlz.framework.db.inf.ISqlQuery;
import com.dlz.framework.db.inf.ICondAddByLamda;
import com.dlz.framework.db.inf.IExecuteDelete;
import com.dlz.framework.db.modal.para.APojoQuery;

/**
 * 删除语句生成器
 *
 * @author dk
 */
public class PojoDelete<T> extends APojoQuery<PojoDelete<T>,T, TableDelete> implements
        ISqlQuery<PojoDelete<T>>,
        ICondAddByLamda<PojoDelete<T>, T>,
        IExecuteDelete<PojoDelete<T>> {

    public static <T> PojoDelete<T> wrapper(T conditionBean) {
        return new PojoDelete(conditionBean);
    }

    public static <T> PojoDelete<T> wrapper(Class<T> beanClass) {
        return new PojoDelete(beanClass);
    }

    private PojoDelete(Class<T> beanClass) {
        super(beanClass);
        setPm(new TableDelete(getTableName()));
    }

    private PojoDelete(T conditionBean) {
        super(conditionBean);
        setPm(new TableDelete(getTableName()));
    }

    @Override
    public PojoDelete<T> me() {
        return this;
    }
}
