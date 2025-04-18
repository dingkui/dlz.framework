package com.dlz.framework.db.modal.para;

import com.dlz.comm.exception.DbException;
import com.dlz.comm.util.system.FieldReflections;
import com.dlz.framework.db.inf.ISqlPara;
import com.dlz.framework.db.holder.BeanInfoHolder;
import com.dlz.framework.db.modal.items.JdbcItem;
import com.dlz.framework.db.modal.items.SqlItem;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;
import java.util.List;

/**
 * 插入语句生成器
 *
 * @author dk
 */
public abstract class AWrapper<T,P extends ParaMap> implements ISqlPara {
    private final Class<T> beanClass;
    protected final T bean;
    private final String tableName;
    private final List<Field> fields;
    private boolean isGenerator = false;
    @Getter
    @Setter
    private P pm;

    public AWrapper(Class<T> beanClass) {
        this.beanClass = beanClass;
        if(beanClass == Class.class){
            throw new DbException("bean需要为实体对象",1002);
        }
        this.bean=null;
        tableName= BeanInfoHolder.getTableName(beanClass);
        fields= BeanInfoHolder.getBeanFields(beanClass);
    }
    public AWrapper(T bean) {
        this.bean = bean;
        if(bean == null){
            throw new DbException("bean不能为空",1002);
        }
        this.beanClass = (Class<T>) bean.getClass();
        tableName= BeanInfoHolder.getTableName(beanClass);
        fields= BeanInfoHolder.getBeanFields(beanClass);
    }

    protected String getTableName() {
        return tableName;
    }

    protected void generatWithBean(T bean) {
        if (!isGenerator && bean != null) {
            fields.forEach(field->{
                Object fieldValue = FieldReflections.getValue(bean, field);
                if (fieldValue != null) {
                    wrapValue(BeanInfoHolder.getColumnName(field), fieldValue);
                }
            });
            isGenerator = true;
        }
    }

    /**
     * 自动构建参数
     *
     * @param columnName
     * @param value
     */
    protected abstract void wrapValue(String columnName, Object value);

    public Class<T> getBeanClass() {
        return beanClass;
    }


    @Override
    public JdbcItem jdbcSql() {
        generatWithBean(bean);
        return pm.jdbcSql();
    }

    @Override
    public JdbcItem jdbcCnt() {
        throw new RuntimeException("不支持");
    }

    @Override
    public SqlItem getSqlItem() {
        return pm.getSqlItem();
    }
}
