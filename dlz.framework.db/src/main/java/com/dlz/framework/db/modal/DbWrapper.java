package com.dlz.framework.db.modal;

import com.dlz.comm.exception.SystemException;
import com.dlz.comm.fn.DlzFn;
import com.dlz.comm.util.StringUtils;
import com.dlz.comm.util.system.FieldReflections;
import com.dlz.framework.db.modal.para.*;
import com.dlz.framework.db.util.DbConvertUtil;

import java.lang.reflect.Field;
import java.util.function.Function;

public class DbWrapper {
    public  <T> WrapperQuery<T> select(Class<T> re) {
        return WrapperQuery.wrapper(re);
    }

    public <T> WrapperQuery<T> select(T conditionBean) {
        return WrapperQuery.wrapper(conditionBean);
    }

    public <T> WrapperDelete<T> delete(Class<T> beanClass) {
        return WrapperDelete.wrapper(beanClass);
    }

    public <T> WrapperDelete<T> delete(T condition) {
        return WrapperDelete.wrapper(condition);
    }

    public <T> WrapperInsert<T> insert(T bean) {
        return WrapperInsert.wrapper(bean);
    }

    public <T> WrapperUpdate<T> update(Class<T> beanClass) {
        return WrapperUpdate.wrapper(beanClass);
    }

    public <T> WrapperUpdate<T> update(T value, Function<String, Boolean> ignore) {
        return WrapperUpdate.wrapper((Class<T>) value.getClass()).set(value, ignore);
    }

    public <T> WrapperUpdate<T> update(T value) {
        return WrapperUpdate.wrapper((Class<T>) value.getClass()).set(value);
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
