package com.dlz.framework.cache.aspect;

import com.dlz.comm.json.JSONMap;

import java.lang.reflect.Method;

/**
 * 缓存键值生成接口
 * 
 * 用于定义自定义缓存键值的生成规则，配合 @CacheAnno 注解使用
 * 
 * @author dk 2020-06-05
 */
public interface ICacheKeyIf {
    /**
     * 根据方法和参数生成缓存键值
     * 
     * @param method 被注解的方法对象
     * @param paraMap 方法参数映射
     * @return 生成的缓存键值字符串
     */
    String getKey(Method method, JSONMap paraMap);
}