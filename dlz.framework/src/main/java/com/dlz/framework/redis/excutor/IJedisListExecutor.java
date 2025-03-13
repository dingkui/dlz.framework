package com.dlz.framework.redis.excutor;

import java.util.List;

/**
 * Redis key构建器
 *
 * @author dk
 */
public interface IJedisListExecutor extends IJedisExecutor {
    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束 0 到 -1代表所有值
     * @return
     */
    default List<String> lrange(String key, long start, long end) {
        return excute(j -> j.lrange(getRedisKey(key), start, end));
    }

    /**
     * 获取list缓存的长度
     *
     * @param key 键
     * @return
     */
    default long llen(String key) {
        return excute(j -> j.llen(getRedisKey(key)));
    }

    /**
     * 通过索引 获取list中的值
     *
     * @param key   键
     * @param index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
    default Object lGetIndex(String key, long index) {
        return excute(j -> j.lindex(getRedisKey(key), index));
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    default Long lpush(String key, String... value) {
        return lpush(key, 0, value);
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param time  时间(秒)
     * @param value 值
     * @return
     */
    default Long lpush(String key, int seconds, String... value) {
        return excute(j -> {
            String key1 = getRedisKey(key);
            Long lpush = j.lpush(key1, value);
            if (seconds > 0) {
                j.expire(key1, seconds);
            }
            return lpush;
        });
    }


    /**
     * 根据索引修改list中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return /
     */
    default Boolean lUpdateIndex(String key, long index, String value) {
        excute(j -> j.lset(getRedisKey(key), index, value));
        return true;
    }

    /**
     * 移除N个值为value
     *
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    default long lRemove(String key, long count, String value) {
        return excute(j -> j.lrem(getRedisKey(key), count, value));
    }
}