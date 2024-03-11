package com.dlz.framework.cache.service;

import com.dlz.comm.cache.ICache;
import com.dlz.framework.cache.service.AbstractCache;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.lang.reflect.Type;

/**
 * 只从缓存中读取对象
 *
 * @author dk
 */
@Slf4j
public class ObjectCache extends AbstractCache<String, Serializable> {
    /**
     * 构造函数
     *
     * @param cache             缓存实现
     * @param cacheName         缓存名称
     * @param timeToLiveSeconds 缓存时间：秒
     */
    public ObjectCache(String cacheName, ICache cache, int timeToLiveSeconds) {
        super(cache, cacheName, timeToLiveSeconds);
    }

    public ObjectCache(String cacheName, ICache cache) {
        this(cacheName, cache, 0);
    }

    /**
     * 缓存中读取对象
     */
    public Serializable get(String key) {
        return getFromCache(key);
    }

    /**
     * 缓存中读取对象
     */
    public <T extends Serializable> T get(String key, Type tClass) {
        return getCache().get(getCacheName(), key, tClass);
    }
}