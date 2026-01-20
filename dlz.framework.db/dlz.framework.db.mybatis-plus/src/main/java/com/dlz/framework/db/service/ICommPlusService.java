package com.dlz.framework.db.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.dlz.comm.json.JSONMap;
import com.dlz.framework.db.util.DbUtil;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public interface ICommPlusService{
    /**
     * 添加
     * @param entity
     * @param <T>
     */
    <T> int save(T entity);


    /**
     * 删除
     * @param id
     * @param clazz
     * @param <T>
          */
    <T> int removeById(Serializable id, Class<T> clazz) ;
    <T> int removeByMap(JSONMap columnMap, Class<T> clazz);
    <T> int remove(Wrapper<T> queryWrapper, Class<T> clazz);
    default <T> int remove(Wrapper<T> wrapper){
        return remove(wrapper, DbUtil.getEntityClass(wrapper));
    }
    default <T> int remove(T bean){
        Assert.notNull(bean, "error: bean can not be null");
        return remove(new QueryWrapper<>(bean), (Class<T>)bean.getClass());
    }
    <T> int removeByIds(Collection<? extends Serializable> idList, Class<T> clazz);


    /**
     * 修改
     * @param entity
     * @param <T>
     */
    <T> int updateById(T entity) ;
    <T> int update(T entity, Wrapper<T> updateWrapper) ;
    default <T> int saveOrUpdate(T entity) {
        Assert.notNull(entity, "error: bean can not be null");
        Class<?> cls = entity.getClass();
        TableInfo tableInfo = TableInfoHelper.getTableInfo(cls);
        Assert.notNull(tableInfo, "error: can not execute. because can not find cache of TableInfo for entity!");
        String keyProperty = tableInfo.getKeyProperty();
        Assert.notEmpty(keyProperty, "error: can not execute. because can not find column for id from entity!");
        Object idVal = ReflectionKit.getFieldValue(entity, tableInfo.getKeyProperty());
        return StringUtils.checkValNull(idVal) || Objects.isNull(getById((Serializable) idVal,cls)) ? save(entity) : updateById(entity);
    }


    /**
     * 查询单条
     * @param queryWrapper
     * @param clazz
     * @param throwEx
     * @param <T>
          */
    <T> T getOne(Wrapper<T> queryWrapper, Class<T> clazz, boolean throwEx) ;
    default <T>  T getOne(Wrapper<T> wrapper){
        return getOne(wrapper, DbUtil.getEntityClass(wrapper));
    }
    default <T>  T getOne(Wrapper<T> queryWrapper, Class<T> clazz) {
        return getOne(queryWrapper,clazz,true);
    }
    default <T> T getOne(T bean){
        Assert.notNull(bean, "error: bean can not be null");
        return getOne(new QueryWrapper<>(bean), (Class<T>)bean.getClass(),true);
    }
    default <T>  T getFirst(Wrapper<T> queryWrapper, Class<T> clazz) {
        return getOne(queryWrapper,clazz,false);
    }
    default <T>  T getFirst(Wrapper<T> wrapper){
        return getFirst(wrapper, DbUtil.getEntityClass(wrapper));
    }
    default <T> T getFirst(T bean){
        Assert.notNull(bean, "error: bean can not be null");
        return getOne(new QueryWrapper<>(bean), (Class<T>)bean.getClass(),false);
    }
    default <T> Map<String, Object> getMap(Wrapper<T> queryWrapper, Class<T> clazz){
        List<Map<String, Object>> maps = listMaps(queryWrapper, clazz);
        if(maps.size()==0){
            return null;
        }
        Assert.notNull(maps.size()>1, "error: result num is {},but not one!", maps.size());
        return maps.get(0);
    }
    default <T> Map<String, Object> getMap(Wrapper<T> wrapper){
        return getMap(wrapper, DbUtil.getEntityClass(wrapper));
    }
    default <T> Map<String, Object> getMap(T bean){
        Assert.notNull(bean, "error: bean can not be null");
        return getMap(new QueryWrapper<>(bean), (Class<T>)bean.getClass());
    }
    default <T> Map<String, Object> getFirstMap(Wrapper<T> queryWrapper, Class<T> clazz) {
        List<Map<String, Object>> maps = listMaps(queryWrapper, clazz);
        if(maps.size()==0){
            return null;
        }
        return maps.get(0);
    }
    default <T> Map<String, Object> getFirstMap(T bean){
        Assert.notNull(bean, "error: bean can not be null");
        return getFirstMap(new QueryWrapper<>(bean), (Class<T>)bean.getClass());
    }
    <T> T getById(Serializable id, Class<T> clazz) ;


    <T> List<T> listByIds(Collection<? extends Serializable> idList, Class<T> clazz) ;
    <T> List<T> listByMap(JSONMap columnMap, Class<T> clazz) ;
    <T> List<T> list(Wrapper<T> queryWrapper, Class<T> clazz);
    default <T> List<T> list(Wrapper<T> wrapper){
        return list(wrapper, DbUtil.getEntityClass(wrapper));
    }
    default <T> List<T> list(T bean){
        Assert.notNull(bean, "error: bean can not be null");
        return list(new QueryWrapper<>(bean), (Class<T>)bean.getClass());
    }
    <T> List<Map<String, Object>> listMaps(Wrapper<T> queryWrapper, Class<T> clazz) ;
    default <T> List<Map<String, Object>> listMaps(Wrapper<T> wrapper){
        return listMaps(wrapper, DbUtil.getEntityClass(wrapper));
    }
    default <T> List<Map<String, Object>> listMaps(T bean){
        Assert.notNull(bean, "error: bean can not be null");
        return listMaps(new QueryWrapper<>(bean), (Class<T>)bean.getClass());
    }

    <T> int count(Wrapper<T> queryWrapper, Class<T> clazz) ;
    default <T> int count(Wrapper<T> wrapper){
        return count(wrapper, DbUtil.getEntityClass(wrapper));
    }
    default <T> int count(T bean){
        Assert.notNull(bean, "error: bean can not be null");
        return count(new QueryWrapper<>(bean), (Class<T>)bean.getClass());
    }

}
