package com.dlz.framework.cache.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 缓存注解，用于标识需要缓存的方法
 * 
 * 该注解用于AOP切面编程中，标记需要进行缓存操作的方法
 * 
 * @author dk 2018-05-28
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CacheAnno {
    /**
     * 缓存键值的命名空间
     * 
     * @return 缓存键值的命名空间，默认为空字符串
     */
    String value() default "";

    /**
     * 缓存键值的参数名
     * 
     * 指定方法参数作为缓存的键值，支持形如 bean.id 的嵌套属性访问
     * 
     * @return 键值参数名，默认为 "id"
     */
    String key() default "id";

    // /**
    //  * 缓存实现类
    //  */
    // Class<? extends ICache> cacheClass() default CacheEhcahe.class;

    /**
     * 缓存过期时间
     * 
     * @return 缓存时间，单位毫秒，默认为3600000毫秒（1小时）
     */
    long cacheTime() default 3600000L;

}