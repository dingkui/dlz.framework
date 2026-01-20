package com.dlz.framework.redis.excutor;

import java.util.Set;

/**
 * Redis key构建器
 *
 * @author dk
 */
public interface IJedisSetExecutor extends IJedisExecutor {

    /**
     * 根据key获取Set中的所有值
     *
     * @param key 键
     */
    default Set<String> smembers(String key) {
        return excute(j -> j.smembers(getRedisKey(key)));
    }

    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    default Boolean sHasKey(String key, String value) {
        return excute(j -> j.sismember(getRedisKey(key), value));
    }

    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    default long sSet(String key, int seconds, String... values) {
        return excute(j -> {
            String key1 = getRedisKey(key);
            Long sadd = j.sadd(key1, values);
            if (seconds > 0) {
                j.expire(key1, seconds);
            }
            return sadd;
        });
    }

    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    default long sSet(String key, String... values) {
        return sSet(getRedisKey(key), 0, values);
    }

    /**
     * 移除值为value的
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    default Long setRemove(String key, String... values) {
        return excute(j -> j.srem(getRedisKey(key), values));
    }
}