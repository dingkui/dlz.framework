package com.dlz.framework.cache;

import com.dlz.comm.cache.ICache;
import com.dlz.comm.util.JacksonUtil;
import com.dlz.comm.util.ValUtil;
import com.dlz.framework.holder.SpringHolder;
import lombok.extern.slf4j.Slf4j;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 使用ehcache实现缓存
 *
 * @author dk
 */
@Slf4j
public class CacheEhcahe implements ICache {
    private CacheManager manager;
    private CacheManager getManager(){
        if(manager ==null){
            synchronized (this.getClass()){
                if(manager ==null) {
                    manager = SpringHolder.getBean(CacheManager.class);
                    if (manager == null) {
                        manager = CacheManager.getInstance();
                    }
                }
            }
        }
        return manager;
    }

    protected Cache getCache(String name) {
        CacheManager manager = getManager();
        Cache cache = manager.getCache(name);
        if (cache == null) {
            synchronized(this.getClass()) {
                cache = manager.getCache(name);
                if (cache == null) {
                    log.info("缓存初始化：" + manager.getConfiguration().getDiskStoreConfiguration().getPath() + "/" + name);
                    manager.addCache(name);
                    cache = manager.getCache(name);
                }
            }
        }
        return cache;
    }

    @Override
    public <T extends Serializable> T get(String name, Serializable key, Type tClass) {
        Element element = getCache(name).get(key);
        if(element == null){
            return null;
        }
        Object obj = element.getObjectValue();
        if(tClass != null){
            return ValUtil.toObj(obj, JacksonUtil.mkJavaType(tClass));
        }
        return (T) obj;
    }

    @Override
    public void put(String name, Serializable key, Serializable value, int seconds) {
        Element element = new Element(key, value);
        if (seconds > -1) {
            element.setTimeToLive(ValUtil.toInt(seconds));
        }
        getCache(name).put(element);
    }

    @Override
    public void remove(String name, Serializable key) {
        getCache(name).remove(key.toString());
    }

    @Override
    public void removeAll(String name) {
        getCache(name).removeAll();
    }

    @Override
    public Set<String> keys(String name,String keyPrefix) {
        Stream<String> stream = getCache(name).getKeys().stream().map(key -> ValUtil.toStr(key));
        if("*".equals(keyPrefix)||".*".equals(keyPrefix)){
            return stream.collect(Collectors.toSet());
        }
        String keyMatch=keyPrefix.replaceAll("\\.\\*","*").replaceAll("\\*",".*");
        return stream.filter(key->key.matches(keyMatch)).collect(Collectors.toSet());
    }

    @Override
    public Map<String,Serializable> all(String name, String keyPrefix){
        Cache cache = getCache(name);
        Set<String> keys = keys(name, keyPrefix);
        Map<String,Serializable> map=new HashMap<>();
        keys.forEach(key->{
            Element element = cache.get(key);
            if(element != null){
                map.put(key,(Serializable)element.getObjectValue());
            }
        });
        return map;
    }

//    public static void main(String[] args) {
//        String s="asdas";
//        String keyPrefix="asd.*";
//        keyPrefix=keyPrefix.replaceAll("\\.\\*","*").replaceAll("\\*",".*");
//        System.out.println(s.matches(keyPrefix)+" "+keyPrefix);
//    }
}