package com.dlz.framework.db.modal.para;

import com.dlz.comm.util.StringUtils;
import com.dlz.comm.util.system.FieldReflections;
import com.dlz.framework.db.holder.BeanInfoHolder;
import com.dlz.framework.db.inf.ISqlWrapperQuery;
import com.dlz.framework.db.modal.condition.Condition;

import java.lang.reflect.Field;
import java.util.List;


/**
 * 构造单表的更新操作sql
 *
 * @author dingkui
 */
public abstract class AWrapperQuery<ME extends AWrapperQuery, T, PM extends AQuery> extends AWrapper<T, PM> implements ISqlWrapperQuery<ME, T> {
    protected AWrapperQuery(Class<T> beanClass) {
        super(beanClass);
    }

    protected AWrapperQuery(T conditionBean) {
        super(conditionBean);
    }

    public Condition where() {
        return getPm().where();
    }
    public ME where(Condition cond) {
        getPm().where(cond);
        return me();
    }
    public ME where(T bean) {
        this.bean = bean;
        return me();
    }

    public ME setAllowFullQuery(boolean allowFullQuery) {
        getPm().setAllowFullQuery(allowFullQuery);
        return me();
    }
    public boolean isAllowFullQuery() {
        return getPm().isAllowFullQuery();
    }
    @Override
    protected void wrapValues(List<Field> fields, T bean) {
        fields.forEach(field->{
            Object value = FieldReflections.getValue(bean, field);
            if (StringUtils.isNotEmpty(value)) {
                getPm().eq(BeanInfoHolder.getColumnName(field), value);
            }
        });
    }
}
