package com.dlz.framework.redis.service.impl;

import com.dlz.comm.cache.ICache;
import com.dlz.comm.util.ValUtil;
import com.dlz.framework.redis.excutor.JedisExecutor;
import com.dlz.framework.redis.util.JedisKeyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 使用Redis实现缓存
 * 缓存存储使用hash方式
 *
 * @author dk
 */
@Slf4j
public class CacheRedisJsonHash implements ICache {
    @Autowired
    JedisExecutor jedisExecutor;
    @Override
    public <T extends Serializable> T get(String name, Serializable key, Type type) {
        return jedisExecutor.hgetSo(name, ValUtil.toStr(key),type);
    }

    @Override
    public void put(String name, Serializable key, Serializable value, int seconds) {
        jedisExecutor.hsetSo(name, ValUtil.toStr(key),  value, seconds);
    }

    @Override
    public void remove(String name, Serializable key) {
        jedisExecutor.hdel(name, ValUtil.toStr(key));
    }

    @Override
    public void removeAll(String name) {
        jedisExecutor.del(name);
    }

    @Override
    public Set<String> keys(String name,String keyPrefix) {
        Stream<String> stream = jedisExecutor.hgetAll(name).keySet().stream().map(key -> ValUtil.toStr(key));
        if("*".equals(keyPrefix)||".*".equals(keyPrefix)){
            return stream.collect(Collectors.toSet());
        }
        String keyMatch=keyPrefix.replaceAll("\\.\\*","*").replaceAll("\\*",".*");
        return stream.filter(key->key.matches(keyMatch)).collect(Collectors.toSet());
    }
    @Override
    public Map<String,Serializable> all(String name, String keyPrefix){
        Map<String,String> re = jedisExecutor.hgetAll(name);
        Map<String,Serializable> map=new HashMap<>();
        if("*".equals(keyPrefix)||".*".equals(keyPrefix)){
            re.entrySet().forEach(item->{
                map.put(item.getKey(),JedisKeyUtils.getResult(item.getValue(), null));
            });
        }else{
            String keyMatch=keyPrefix.replaceAll("\\.\\*","*").replaceAll("\\*",".*");
            re.entrySet().forEach(item->{
                if(item.getKey().matches(keyMatch)){
                    map.put(item.getKey(),JedisKeyUtils.getResult(item.getValue(), null));
                }
            });
        }
        return map;
    }
}