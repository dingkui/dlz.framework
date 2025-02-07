package com.dlz.framework.redis.excutor;

import com.dlz.comm.util.JacksonUtil;
import com.dlz.comm.util.ValUtil;
import com.dlz.comm.util.system.SerializeUtil;
import com.dlz.framework.redis.util.JedisKeyUtils;
import com.fasterxml.jackson.databind.JavaType;
import redis.clients.jedis.util.SafeEncoder;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Redis key构建器
 *
 * @author dk
 */
interface IJedisHashExecutor extends IJedisExecutor {
    /**
     * HashGet
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    default String hget(String key, String item) {
        return excuteByJedis(j -> j.hget(JedisKeyUtils.getRedisKey(key), item));
    }

    /**
     * HashGet
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    default byte[] hget(byte[] key, byte[] item) {
        return excuteByJedis(j -> j.hget(key, item));
    }

    /**
     * 获取hashKey对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值
     */
    default Map<String, String> hgetAll(String key) {
        Map<String, String> result = excuteByJedis(j -> j.hgetAll(JedisKeyUtils.getRedisKey(key)));
        Map<String, String> map = new HashMap<>(result.size());
        result.forEach((k, v) -> map.put(k, v));
        return map;
    }

    /**
     * HashSet
     *
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     */
    default Boolean hset(String key, Map<String, String> map) {
        return hset(key, map, 0);
    }

    /**
     * HashSet 并设置时间
     *
     * @param key  键
     * @param map  对应多个键值
     * @param seconds 时间(秒)
     * @return true成功 false失败
     */
    default Boolean hset(String key, Map<String, String> map, int seconds) {
        return excuteByJedis(j -> {
            String key1 = JedisKeyUtils.getRedisKey(key);
            map.entrySet().forEach(m -> j.hset(key1, m.getKey(), m.getValue()));
            if (seconds > 0) {
                j.expire(key1, seconds);
            }
            return true;
        });
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @return true 成功 false失败
     */
    default Boolean hset(String key, String item, String value) {
        return hset(key, item, value, 0);
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @param seconds  时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    default Boolean hset(String key, String item, String value, int seconds) {
        return excuteByJedis(j -> {
            String key1 = JedisKeyUtils.getRedisKey(key);
            j.hset(key1, item, value);
            if (seconds > 0) {
                j.expire(key1, seconds);
            }
            return true;
        });
    }

    /**
     * 删除hash表中的值
     *
     * @param key   键 不能为null
     * @param items 项 可以使多个 不能为null
     */
    default void hdel(String key, String... items) {
        excuteByJedis(j -> {
            j.hdel(JedisKeyUtils.getRedisKey(key), items);
            return true;
        });
    }

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    default Boolean hexists(String key, String item) {
        return excuteByJedis(j -> j.hexists(JedisKeyUtils.getRedisKey(key), item));
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key  键
     * @param item 项
     * @param by   要增加几(大于0)
     * @return
     */
    default Long hincrBy(String key, String item, long by) {
        return excuteByJedis(j -> j.hincrBy(JedisKeyUtils.getRedisKey(key), item, by));
    }


    // ================================Object=================================
    default Boolean hsetSo(String key, String item, Serializable value) {
        return hsetSo(key, item, value, 0);
    }

    default Boolean hsetSo(String key, String item, Serializable value, int time) {
        return hset(key, item, JedisKeyUtils.getValueStr(value), time);
    }

    default <T> T hgetSo(String key, String item, JavaType javaType) {
        return JedisKeyUtils.getResult(hget(key, item), javaType);
    }

    default <T> T hgetSo(String key, String item) {
        return hgetSo(key, item, (JavaType) null);
    }

    default <T> T hgetSo(String key, String item, Class<T> tClass) {
        return hgetSo(key, item, tClass == null ? null : JacksonUtil.constructType(tClass));
    }

    default <T> T hgetSo(String key, String item, Type type) {
        return hgetSo(key, item, type == null ? null : JacksonUtil.getJavaType(type));
    }

    // ========hgetSe 序列化保存保存Object,支持直接取得类型==============
    default Boolean hsetSe(String key, String item, Serializable value) {
        return hsetSe(key, item, value, 0);
    }

    default Boolean hsetSe(String key, String item, Serializable value, int seconds) {
        return excuteByJedis(j -> {
            byte[] key1 = SafeEncoder.encode(JedisKeyUtils.getRedisKey(key));
            byte[] item1 = SafeEncoder.encode(item);
            j.hset(key1, item1, SerializeUtil.serialize(value));
            if (seconds > 0) {
                j.expire(key1, seconds);
            }
            return true;
        });
    }

    default Object hgetSe(String key, String item) {
        return excuteByJedis(j -> {
            byte[] key1 = SafeEncoder.encode(JedisKeyUtils.getRedisKey(key));
            byte[] item1 = SafeEncoder.encode(item);
            byte[] hget = j.hget(key1, item1);
            if (hget==null){
                return null;
            }
            return SerializeUtil.deserialize(hget);
//            final byte[] result = j.getClient().getBinaryBulkReply();
//            if (result==null){
//                return null;
//            }
//            return SerializeUtil.deserialize(result);
//            return SerializationUtils.deserialize(j.hget(key1, item1));
        });
    }

    default <T> T hgetSe(String key, String item, JavaType javaType) {
        Object value = hgetSe(key, item);
        if (javaType == null) {
            return (T) value;
        }
        return ValUtil.toObj(value, javaType);
    }

    default <T> T hgetSe(String key, String item, Class<T> tClass) {
        return hgetSe(key, item, tClass == null ? null : JacksonUtil.constructType(tClass));
    }

    default <T extends Serializable> T hgetSe(String key, String item, Type type) {
        return hgetSe(key, item, type == null ? null : JacksonUtil.getJavaType(type));
    }
}