package com.dlz.framework.db.modal.para;

import com.dlz.comm.util.StringUtils;
import com.dlz.framework.db.inf.ISqlWrapperSearch;
import com.dlz.framework.db.modal.condition.Condition;


/**
 * 构造单表的更新操作sql
 *
 * @author dingkui
 */
public abstract class AWrapperSearch<ME extends AWrapperSearch, T, PM extends AMakerSearch> extends AWrapper<T, PM> implements ISqlWrapperSearch<ME, T> {
    public AWrapperSearch(Class<T> beanClass) {
        super(beanClass);
    }

    public AWrapperSearch(T conditionBean) {
        super(conditionBean);
    }

    public Condition where() {
        return getPm().where();
    }
    public ME where(Condition cond) {
        getPm().where(cond);
        return me();
    }

    public ME setAllowFullQuery(boolean allowFullQuery) {
        getPm().setAllowFullQuery(allowFullQuery);
        return me();
    }
    public boolean isAllowFullQuery() {
        return getPm().isAllowFullQuery();
    }

    protected void wrapValue(String columnName, Object value) {
        if (StringUtils.isNotEmpty(value)) {
            getPm().eq(columnName, value);
        }
    }
}
