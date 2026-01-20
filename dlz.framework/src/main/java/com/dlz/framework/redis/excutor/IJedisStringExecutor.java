package com.dlz.framework.redis.excutor;

import com.dlz.comm.util.JacksonUtil;
import com.dlz.comm.util.ValUtil;
import com.dlz.comm.util.system.SerializeUtil;
import com.dlz.framework.redis.util.JedisKeyUtils;
import com.fasterxml.jackson.databind.JavaType;
import redis.clients.jedis.util.SafeEncoder;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Redis 执行器：Key（键）
 * http://doc.redisfans.com/
 *
 * @author dk
 */
public interface IJedisStringExecutor extends IJedisExecutor {
    /**
     * 批量获取
     *
     * @param keys
     */
    default List<String> mget(String... keys) {
        return excute(j -> j.mget(getKeyArray(keys)));
    }

    default String get(String key) {
        return excute(j -> j.get(getRedisKey(key)));
    }

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    default Boolean set(String key, String value) {
        return set(key, value, 0);
    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param seconds  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    default Boolean set(String key, String value, long seconds) {
        if(value == null){
            return false;
        }
        return excute(j -> {
            String key1 = getRedisKey(key);
            j.set(key1, value);
            if (seconds > 0) {
                j.expire(key1, seconds);
            }
            return true;
        });
    }


    /**
     * 递增 此时value值必须为int类型 否则报错
     *
     * @param key   键
     * @param delta 要增加几(大于0)
     */
    default long incrBy(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return excute(j -> j.incrBy(getRedisKey(key), delta));
    }


    /**
     * 递增 此时value值必须为int类型 否则报错
     *
     * @param key   键
     */
    default long incr(String key) {
        return excute(j -> j.incr(getRedisKey(key)));
    }

    /**
     * 递减
     *
     * @param key   键
     * @param delta 要减少几(小于0)
     */
    default long decrBy(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return excute(j -> j.decrBy(getRedisKey(key), delta));
    }

    // ===========getSo String中保存ObjectClasss,支持直接取得类型===========
    default Boolean setSo(String key, Serializable value) {
        return setSo(key, value, 0);
    }

    default Boolean setSo(String key, Serializable value, int second) {
        return set(key, JedisKeyUtils.getValueStr(value), second);
    }

    default <T> T getSo(String key, JavaType javaType) {
        String value = get(key);
        return JedisKeyUtils.getResult(value, javaType);
    }

    default <T> T getSo(String key) {
        return getSo(key, (JavaType) null);
    }

    default <T> T getSo(String key, Class<T> tClass) {
        return getSo(key, tClass == null ? null : JacksonUtil.mkJavaType(tClass));
    }

    default <T extends Serializable> T getSo(String key, Type type) {
        return getSo(key, type == null ? null : JacksonUtil.mkJavaType(type));
    }

    // ========getSe 序列化保存保存Object,支持直接取得类型==============
    default Boolean setSe(String key, Serializable value) {
        return setSe(key, value, 0);
    }

    default Boolean setSe(String key, Serializable value, int seconds) {
        return excute(j -> {
            byte[] key1 = SafeEncoder.encode(getRedisKey(key));
            j.set(key1, SerializeUtil.serialize(value));
            if (seconds > 0) {
                j.expire(key1, seconds);
            }
            return true;
        });
    }

    default Object getSe(String key) {
        return excute(j -> {
            byte[] key1 = SafeEncoder.encode(getRedisKey(key));
            byte[] bytes = j.get(key1);
            if (bytes==null){
                return null;
            }
            return SerializeUtil.deserialize(bytes);
        });
    }

    default <T> T getSe(String key, JavaType javaType) {
        Object value = getSe(key);
        if (javaType == null) {
            return (T) value;
        }
        return ValUtil.toObj(value, javaType);
    }

    default <T> T getSe(String key, Class<T> tClass) {
        return getSe(key, tClass == null ? null : JacksonUtil.mkJavaType(tClass));
    }

    default <T extends Serializable> T getSe(String key, Type type) {
        return getSe(key, type == null ? null : JacksonUtil.mkJavaType(type));
    }
}