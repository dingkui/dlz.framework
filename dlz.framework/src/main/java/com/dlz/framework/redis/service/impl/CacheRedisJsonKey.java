package com.dlz.framework.redis.service.impl;

import com.dlz.framework.cache.ICache;
import com.dlz.framework.redis.excutor.JedisExecutor;
import com.dlz.framework.redis.util.JedisKeyUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 使用Redis实现缓存
 * 缓存存储使用键值对方式
 *
 * @author dk
 */
public class CacheRedisJsonKey implements ICache {
    @Autowired
    JedisExecutor jedisExecutor;
    @Override
    public <T extends Serializable> T get(String name, Serializable key, Type type) {
        return jedisExecutor.getSo(JedisKeyUtils.getKey(name,key), type);
    }

    @Override
    public void put(String name, Serializable key, Serializable value, int seconds) {
        jedisExecutor.setSo(JedisKeyUtils.getKey(name,key), value, seconds);
    }

    @Override
    public void remove(String name, Serializable key) {
        jedisExecutor.del(JedisKeyUtils.getKey(name,key));
    }

    @Override
    public void removeAll(String name) {
        jedisExecutor.excuteByJedis(j -> {
            Set<String> keys = j.keys(JedisKeyUtils.getRedisKey(name +"*"));
            if(keys.size()>0){
                j.del(keys.toArray(new String[keys.size()]));
            }
            return true;
        });
    }

    @Override
    public Set<String> keys(String name,String keyPrefix) {
        return jedisExecutor.keys(name ,keyPrefix);
    }
}