package com.dlz.framework.redis.excutor;

import com.dlz.framework.redis.util.JedisKeyUtils;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Redis 执行器：Key（键）
 * http://doc.redisfans.com/
 *
 * @author dk
 */
public interface IJedisKeyExecutor extends IJedisExecutor {
    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param seconds 时间(秒)
     */
    default Boolean expire(String key, int seconds) {
        if (seconds > 0) {
            excuteByJedis(j -> j.expire(JedisKeyUtils.getRedisKey(key), seconds));
        }
        return true;
    }

    /**
     * 缓存类型
     *
     * @param key 键
     */
    default String type(String key) {
        return excuteByJedis(j -> j.type(JedisKeyUtils.getRedisKey(key)));
    }

    /**
     * 查找匹配key
     *
     * @param pattern key
     * @return /
     */
    default Set<String> keys(String name,String pattern) {
        int len=name.length()+1;
        return excuteByJedis(j -> j.keys(JedisKeyUtils.getRedisKey(name,pattern)))
                .stream()
                .map(o -> JedisKeyUtils.getClientKey(o).substring(len))
                .collect(Collectors.toSet());
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    default Boolean exists(String key) {
        return excuteByJedis(j -> j.exists(JedisKeyUtils.getRedisKey(key)));
    }

    /**
     * 删除缓存
     *
     * @param keys 可以传一个值 或多个
     */
    default Long del(String... keys) {
        return excuteByJedis(j -> j.del(JedisKeyUtils.getRedisKeyArray(keys)));
    }
}