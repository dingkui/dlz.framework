package com.dlz.framework.db.modal;

import com.dlz.comm.exception.SystemException;
import com.dlz.comm.util.StringUtils;
import com.dlz.comm.util.system.FieldReflections;
import com.dlz.framework.db.modal.wrapper.PojoDelete;
import com.dlz.framework.db.modal.wrapper.PojoInsert;
import com.dlz.framework.db.modal.wrapper.PojoQuery;
import com.dlz.framework.db.modal.wrapper.PojoUpdate;
import com.dlz.framework.db.util.DbConvertUtil;

import java.lang.reflect.Field;
import java.util.function.Function;

public class DbPojo {
    public <T> PojoQuery<T> select(Class<T> re) {
        return PojoQuery.wrapper(re);
    }

    public <T> PojoQuery<T> select(T conditionBean) {
        return PojoQuery.wrapper(conditionBean);
    }

    public <T> PojoDelete<T> delete(Class<T> beanClass) {
        return PojoDelete.wrapper(beanClass);
    }

    public <T> PojoDelete<T> delete(T condition) {
        return PojoDelete.wrapper(condition);
    }

    public <T> PojoInsert<T> insert(T bean) {
        return PojoInsert.wrapper(bean);
    }

    public <T> PojoUpdate<T> update(Class<T> beanClass) {
        return PojoUpdate.wrapper(beanClass);
    }

    public <T> PojoUpdate<T> update(T value, Function<String, Boolean> ignore) {
        return PojoUpdate.wrapper((Class<T>) value.getClass()).set(value, ignore);
    }

    public <T> PojoUpdate<T> update(T value) {
        return PojoUpdate.wrapper((Class<T>) value.getClass()).set(value);
    }


    public <T> int save(T bean) {
        return insert(bean).execute();
    }

    public <T> int insertOrUpdate(T obj, String idName) {
        final Field field = FieldReflections.getField(obj, idName, false);
        final Object id = FieldReflections.getValue(obj, field);
        if (StringUtils.isEmpty(id)) {
            return insert(obj).execute();
        }
        return update(obj, name -> name.equalsIgnoreCase(idName)).eq(idName, id).execute();
    }

    public <T> int insertOrUpdate(T obj) {
        return insertOrUpdate(obj, "id");
    }

    public <T> int updateById(T obj) {
        return updateById(obj, "id");
    }

    public <T> int updateById(T obj, String idName) {
        final Object id = FieldReflections.getValue(obj, DbConvertUtil.toFieldName(idName), true);
        if (StringUtils.isEmpty(id)) {
            throw new SystemException(idName + "不能为空");
        }
        return update((Class<T>) obj.getClass()).set(obj, name -> name.equalsIgnoreCase(idName)).eq(idName, id).execute();
    }

    public <T> T getById(Class<T> c, Object id, String idName) {
        if (StringUtils.isEmpty(id)) {
            throw new SystemException(idName + "不能为空");
        }
        return select(c).eq(idName, id).queryBean();
    }

    public <T> T getById(Class<T> c, Object id) {
        return getById(c, id, "id");
    }

    public <T> int removeByIds(Class<T> c, String ids) {
        return removeByIds(c, ids, "id");
    }

    public <T> int removeByIds(Class<T> c, String ids, String idName) {
        if (StringUtils.isEmpty(ids)) {
            throw new SystemException(idName + "不能为空");
        }
        return delete(c).in(idName, ids).execute();
    }
}