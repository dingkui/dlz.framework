package com.dlz.framework.db.util;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.dlz.comm.exception.SystemException;
import com.dlz.comm.util.StringUtils;
import com.dlz.comm.util.system.Reflections;

import java.util.List;

/**
 * 数据库查询工具
 *
 * @author dk
 * @since 2020-01-15
 */
public class DbUtil {
    public static <T> QueryWrapper<T> mkWrapper(QueryWrapper<T> queryWrapper, T bean, List<OrderItem> orders) {
        if (queryWrapper == null) {
            queryWrapper = new QueryWrapper();
        }
        if (bean != null) {
            TableInfo tableInfo = TableInfoHelper.getTableInfo(bean.getClass());
            Object keyValue = ReflectionKit.getFieldValue(bean, tableInfo.getKeyProperty());
            if (StringUtils.isNotEmpty(keyValue)) {
                queryWrapper.eq(tableInfo.getKeyColumn(), keyValue);
            } else {
                for (TableFieldInfo field : tableInfo.getFieldList()) {
                    Object value = ReflectionKit.getFieldValue(bean, field.getProperty());
                    if (StringUtils.isNotEmpty(value)) {
                        queryWrapper.eq(field.getColumn(), value);
                    }
                }
            }
        }
        if (orders != null) {
            for (OrderItem orderItem : orders) {
                if (StringUtils.isEmpty(orderItem.getColumn())) {
                    continue;
                }
                if (orderItem.isAsc()) {
                    queryWrapper.orderByAsc(orderItem.getColumn());
                } else {
                    queryWrapper.orderByDesc(orderItem.getColumn());
                }
            }
        }
        return queryWrapper;
    }

    public static <T> QueryWrapper<T> mkWrapper(T bean) {
        return mkWrapper(null, bean, null);
    }
    public static <T> QueryWrapper<T> mkWrapper(Class<T> tClass) {
        try {
            return new QueryWrapper<>(tClass.newInstance());
        } catch (Exception e) {
            throw new SystemException(e.getMessage(),e);
        }
    }
    public static <T> LambdaQueryWrapper<T> lbWrapper(Class<T> tClass) {
        return new LambdaQueryWrapper<>(tClass);
    }

    public static <T> Class<T> getEntityClass(Wrapper<T> wrapper) {
        Class<T> entityClass=null;
        if(wrapper instanceof AbstractWrapper){
            entityClass = ((AbstractWrapper) wrapper).getEntityClass();
        }
        if(entityClass==null){
            Class<?> entityClass2 = Reflections.getClassGenricType(wrapper.getClass(),0);
            if(entityClass2!=Object.class){
                entityClass = (Class<T>) entityClass2;
            }
        }
        if(entityClass==null){
            throw new SystemException("entityClass is null,you can create Wrapper use:\n" +
                    "1: DbUtil.mkWrapper(Bean.class)\n" +
                    "2: DbUtil.lbWrapper(Bean.class)\n" +
                    "3: new QueryWrapper<>(bean)\n" +
                    "4: new LambdaQueryWrapper<>(Bean.class)\n" +
                    "5: new LambdaQueryWrapper<Bean>(){}\n" +
                    "6: new QueryWrapper<Bean>(){}");
        }
        return entityClass;
    }
}
