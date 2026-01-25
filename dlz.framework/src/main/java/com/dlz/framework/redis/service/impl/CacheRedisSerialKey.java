package com.dlz.framework.redis.service.impl;

import com.dlz.comm.cache.ICache;
import com.dlz.framework.redis.excutor.JedisExecutor;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Set;

/**
 * 使用Redis实现缓存
 *
 * @author dk
 */
public class CacheRedisSerialKey implements ICache {
    @Autowired
    JedisExecutor jedisExecutor;

    private String getRedisKey(String key, Serializable... other){
        return jedisExecutor.getRedisKey(key, other);
    }

    @Override
    public <T extends Serializable> T get(String name, Serializable key, Type type) {
        return jedisExecutor.getSe(getRedisKey(name,key),type);
    }

    @Override
    public void put(String name, Serializable key, Serializable value, int seconds) {
        jedisExecutor.setSe(getRedisKey(name, key),value,seconds);
    }

    @Override
    public void remove(String name, Serializable key) {
        jedisExecutor.del(getRedisKey(name, key));
    }

    @Override
    public void removeAll(String name) {
        jedisExecutor.execute(j -> {
            Set<String> keys = j.keys(getRedisKey(name+"*"));
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