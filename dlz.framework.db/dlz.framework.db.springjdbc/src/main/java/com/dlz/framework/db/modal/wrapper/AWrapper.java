package com.dlz.framework.db.modal.wrapper;

import com.dlz.comm.exception.DbException;
import com.dlz.comm.util.VAL;
import com.dlz.framework.db.helper.util.DbNameUtil;
import com.dlz.framework.db.modal.DbInfoCache;
import com.dlz.framework.util.system.FieldReflections;

import java.lang.reflect.Field;
import java.util.List;

/**
 * 插入语句生成器
 *
 * @author dk
 */
public abstract class AWrapper<T> {
    private final Class<T> beanClass;
    private final String tableName;
    private final List<Field> fields;
    private boolean isGenerator = false;

    public AWrapper(Class<T> beanClass) {
        this.beanClass = beanClass;
        if(beanClass == Class.class){
            throw new DbException("bean需要为实体对象",1002);
        }
        tableName= DbInfoCache.getTableName(beanClass);
        fields= DbInfoCache.getTableFields(beanClass);
    }

    protected String getTableName() {
        return tableName;
    }

    protected void generatWithBean(T bean) {
        if (!isGenerator && bean != null) {
            fields.forEach(fieldName->{
                Object fieldValue = FieldReflections.getValue(bean, fieldName);
                if (fieldValue != null) {
                    wrapValue(DbNameUtil.getDbClumnName(fieldName), fieldValue);
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

    /**
     * 构建sql
     * @param cnt 是否是查询数量
     * @return
     */
    public abstract VAL<String, Object[]> buildSql(boolean cnt);

    public Class<T> getBeanClass() {
        return beanClass;
    }
}
