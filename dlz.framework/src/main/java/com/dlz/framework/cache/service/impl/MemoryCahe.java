package com.dlz.framework.cache.service.impl;

import com.dlz.comm.util.JacksonUtil;
import com.dlz.comm.util.ValUtil;
import com.dlz.framework.cache.ICache;
import com.dlz.framework.redis.util.JedisKeyUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 使用内存实现缓存
 *
 * @author dk
 */
@Slf4j
public class MemoryCahe implements ICache {
    private final static Map<String, Map<Serializable, Element>> CACHE = new HashMap<>();
    private static ExpiredRunnable Expired = null;
    private static Long BEGIN = System.currentTimeMillis() / 1000;

    public MemoryCahe() {
        if (Expired == null) {
            synchronized (MemoryCahe.class) {
                if (Expired == null) {
                    Expired = new ExpiredRunnable();
                    new Thread(Expired).start();
                }
            }
        }
    }

    protected static Map<Serializable, Element> getCache(String name) {
        Map<Serializable, Element> cache = CACHE.get(name);
        if (cache == null) {
            synchronized (MemoryCahe.class) {
                cache = CACHE.get(name);
                if (cache == null) {
                    cache = new ConcurrentHashMap<>();
                    CACHE.put(name, cache);
                }
            }
        }
        return cache;
    }

    @Override
    public <T extends Serializable> T get(String name, Serializable key, Type tClass) {
        Element obj = getCache(name).get(key);
        if (obj == null) {
            return null;
        }
        if (tClass != null) {
            return ValUtil.getObj(obj.item, JacksonUtil.getJavaType(tClass));
        }
        return (T) obj.item;
    }

    @Override
    public void put(String name, Serializable key, Serializable value, int seconds) {
        Element element = new Element(value);
        if (seconds > 0) {
            element.expired = System.currentTimeMillis() / 1000 + seconds - BEGIN;
            Expired.setExpiredRange(element.expired);
        }
        getCache(name).put(ValUtil.getStr(key), element);
    }

    @Override
    public void remove(String name, Serializable key) {
        getCache(name).remove(ValUtil.getStr(key));
    }

    @Override
    public void removeAll(String name) {
        getCache(name).clear();
    }

    @Override
    public Set<String> keys(String name, String keyPrefix) {
        Stream<String> stringStream = getCache(name).keySet().stream()
                .map(key -> ValUtil.getStr(key));
        if("*".equals(keyPrefix)||".*".equals(keyPrefix)){
            return stringStream
                    .collect(Collectors.toSet());
        }
        String keyMatch=keyPrefix.replaceAll("\\.\\*","*").replaceAll("\\*",".*");
        return stringStream.filter(key->key.matches(keyMatch)).collect(Collectors.toSet());
    }

    @Override
    public Map<String,Serializable> all(String name,String keyPrefix){
        Map<Serializable, Element> cache = getCache(name);
        Map<String,Serializable> map=new HashMap<>();
        if("*".equals(keyPrefix)||".*".equals(keyPrefix)){
            cache.entrySet().forEach(item->{
                map.put(ValUtil.getStr(item.getKey()),item.getValue().item);
            });
        }else{
            String keyMatch=keyPrefix.replaceAll("\\.\\*","*").replaceAll("\\*",".*");
            cache.entrySet().forEach(item->{
                String key = ValUtil.getStr(item.getKey());
                if(key.matches(keyMatch)){
                    map.put(key,item.getValue().item);
                }
            });
        }
        return map;
    }

    class Element {
        Long expired;
        Serializable item;

        Element(Serializable item) {
            this.item = item;
        }
    }

    class ExpiredRunnable implements Runnable {
        Long begin;
        Long end;

        public void setExpiredRange(Long expired) {//设置过期区间
            if (begin == null || expired < begin) {
                begin = expired;
            }
            if (end == null || expired > end) {
                end = expired;
            }
        }

        public void run() {//重写run方法
            while (true) {
                try {
                    Thread.sleep(1000);//实现定时去删除过期
                    expired(System.currentTimeMillis() / 1000 - MemoryCahe.BEGIN);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private boolean expired(long curr) {
            if (begin == null || end == null || begin > curr || end < curr) {
                return false;
            }
            begin = null;
            end = null;
            MemoryCahe.CACHE.entrySet().stream().parallel().forEach(item -> {
                Map<Serializable, Element> cache = item.getValue();
                cache.forEach((key, value) -> {
                    if (value.expired != null) {
                        if (value.expired < curr) {
                            cache.remove(key);
                        } else {
                            setExpiredRange(value.expired);
                        }
                    }
                });
            });
            return true;
        }

        private void expiredWithLog(long curr) {
            Long l = System.currentTimeMillis();
            BigDecimal n1 = MemoryCahe.CACHE.entrySet().stream().map(item ->
                    new BigDecimal(item.getValue().size())
            ).reduce(BigDecimal.ZERO, BigDecimal::add);

            if (!expired(curr)) {
                log.debug("sikp:" + n1.intValue());
            }

            BigDecimal n2 = MemoryCahe.CACHE.entrySet().stream().map(item ->
                    new BigDecimal(item.getValue().size())
            ).reduce(BigDecimal.ZERO, BigDecimal::add);

            log.debug("sum:" + n1.intValue() + " " + n2.intValue()
                    + " memory:" + Runtime.getRuntime().totalMemory()
                    + " time:" + (System.currentTimeMillis() - l)
                    + " " + begin + " " + curr + " " + end
            );
        }
    }
}

