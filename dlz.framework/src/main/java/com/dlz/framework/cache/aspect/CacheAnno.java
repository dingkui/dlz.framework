package com.dlz.framework.cache.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标识不是数据库操作元素
 *
 * @author dk 2018-05-28
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CacheAnno {
    /**
     * 表名
     *
     * @return 表名
     */
    String value() default "";

	/**
	 * 键值的key 方法的参数名 如 id,key,bean等，支持 bean.id形式
	 *
	 * @return 键值的key
	 */
	String key() default "id";

//	/**
//	 * 缓存实现
//	 */
//	Class<? extends ICache> cacheClass() default CacheEhcahe.class;

    /**
     * 缓存时间 毫秒
     *
     * @return 缓存时间
     */
    long cacheTime() default 3600000L;

}
