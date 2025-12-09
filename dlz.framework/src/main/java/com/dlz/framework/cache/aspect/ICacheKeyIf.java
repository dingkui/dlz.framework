package com.dlz.framework.cache.aspect;

import com.dlz.comm.json.JSONMap;

import java.lang.reflect.Method;

/**
 * AOP 缓存操作
 * 系统方法添加注解 @CacheAnno则支持缓存
 * @author dk 2020-06-05
 */
public interface ICacheKeyIf {
    String getKey(Method method,JSONMap paraMap);
}
