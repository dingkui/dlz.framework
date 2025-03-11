package com.dlz.comm.cache;

import com.dlz.comm.util.JacksonUtil;
import com.dlz.comm.util.VAL;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.Callable;

public interface ICache {
    <T extends Serializable> T get(String name, Serializable key, Type tClass);
    default <T> T getAndSet(String name, Serializable key, Callable<VAL<T,Integer>> valueLoader){
        try {
            T re = get(name, key, null);
            if (re == null && valueLoader != null) {
                VAL<T,Integer> v = valueLoader.call();
                if (v != null) {
                    re= v.v1;
                    put(name, key, (Serializable)re, v.v2);
                }
            }
            return re;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    default <T> List<T> getAndSetList(String name, Serializable key, Callable<VAL<List<T>,Integer>> valueLoader,Class<T> tClass){
        try {
            List<T> re = get(name, key, JacksonUtil.mkJavaType(List.class,tClass));
            if (re == null && valueLoader != null) {
                VAL<List<T>,Integer> v = valueLoader.call();
                if (v != null) {
                    re= v.v1;
                    put(name, key, (Serializable)re, v.v2);
                }
            }
            return re;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    default <T> List<T> getAndSetListForever(String name, Serializable key, Callable<List<T>> valueLoader,Class<T> tClass){
        try {
            return getAndSetList(name, key, ()->VAL.of(valueLoader.call(), -1),tClass);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    default <T> T getAndSetForever(String name, Serializable key, Callable<T> valueLoader){
        try {
            return getAndSet(name, key, ()->VAL.of(valueLoader.call(), -1));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    default <T extends Serializable> T get(String name, Serializable key) {
        return get(name, key, null);
    }

    default void put(String name, Serializable key, Serializable value) {
        put(name, key, value, -1);
    }

    void put(String name, Serializable key, Serializable value, int seconds);

    void remove(String name, Serializable key);

    void removeAll(String name);

    default Set<String> keys(String name){
        return keys(name,"*");
    }

    default Set<String> keys(String name,String keyPrefix){
        return Collections.emptySet();
    }

    default Map<String,Serializable> all(String name){
        return all(name,"*");
    }

    default Map<String,Serializable> all(String name,String keyPrefix){
        Map<String,Serializable> map=new HashMap<>();
        Set<String> keys = keys(name, keyPrefix);
        keys.forEach(key-> map.put(key,get(name,key)));
        return map;
    }
}