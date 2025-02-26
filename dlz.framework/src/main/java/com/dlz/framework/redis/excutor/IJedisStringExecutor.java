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
     * @return
     */
    default List<String> mget(String... keys) {
        return excuteByJedis(j -> j.mget(JedisKeyUtils.getRedisKeyArray(keys)));
    }

    default String get(String key) {
        return excuteByJedis(j -> j.get(JedisKeyUtils.getRedisKey(key)));
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
        return excuteByJedis(j -> {
            String key1 = JedisKeyUtils.getRedisKey(key);
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
     * @return
     */
    default long incrBy(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return excuteByJedis(j -> j.incrBy(JedisKeyUtils.getRedisKey(key), delta));
    }


    /**
     * 递增 此时value值必须为int类型 否则报错
     *
     * @param key   键
     * @return
     */
    default long incr(String key) {
        return excuteByJedis(j -> j.incr(JedisKeyUtils.getRedisKey(key)));
    }

    /**
     * 递减
     *
     * @param key   键
     * @param delta 要减少几(小于0)
     * @return
     */
    default long decrBy(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return excuteByJedis(j -> j.decrBy(JedisKeyUtils.getRedisKey(key), delta));
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
        return excuteByJedis(j -> {
            byte[] key1 = SafeEncoder.encode(JedisKeyUtils.getRedisKey(key));
            j.set(key1, SerializeUtil.serialize(value));
            if (seconds > 0) {
                j.expire(key1, (long)seconds);
            }
            return true;
        });
    }

    default Object getSe(String key) {
        return excuteByJedis(j -> {
            byte[] key1 = SafeEncoder.encode(JedisKeyUtils.getRedisKey(key));
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