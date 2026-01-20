package com.dlz.framework.cache.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *  缓存清除注解
 *
 * @author dk 2018-05-28
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CacheEvictAnno {
    /**
     * key prefix
     *
     * @return String
     */
    String value() default "";

	/**
	 * 键值的key 方法的参数名 如 id,key,bean等，支持 bean.id形式
	 *
	 * @return  键值的key
	 */
	String key();

//	/**
//	 * 缓存实现
//	 */
//	Class<? extends ICache> cacheClass() default CacheEhcahe.class;
//
//    /**
//     * 缓存时间 毫秒
//     */
//    long cacheTime() default 3600000L;

}
