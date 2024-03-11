package com.dlz.comm.cache;

import com.dlz.comm.exception.SystemException;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 缓存记录
 *
 * @author dk
 */
@Slf4j
public class CacheHolder {
    private static Map<String, ICache> CacheSet = new ConcurrentHashMap<>();

    public static void clearAll() {
        for (Map.Entry<String, ICache> deal : CacheSet.entrySet()) {
            deal.getValue().removeAll(deal.getKey());
        }
    }

    public static void clear(String cacheName) {
        CacheSet.get(cacheName).removeAll(cacheName);
    }

    public static ICache get(String cacheName) {
        return CacheSet.get(cacheName);
    }

    public static ICache get(String cacheName, ICache cache) {
        if (CacheSet.containsKey(cacheName)) {
            return CacheSet.get(cacheName);
        }
        SystemException.notNull(cache, () -> "缓存已经存在，不能重复定义：" + cacheName);
        CacheSet.put(cacheName, cache);
        return cache;
    }

    public static void add(String cacheName, ICache cache) {
        SystemException.isTrue(CacheSet.containsKey(cacheName), () -> "缓存已经存在，不能重复定义：" + cacheName);
        SystemException.isTrue(cache == null, () -> "ICache 未定义！");
        CacheSet.put(cacheName, cache);
    }
}