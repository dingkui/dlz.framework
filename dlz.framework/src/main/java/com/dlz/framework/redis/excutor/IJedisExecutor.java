package com.dlz.framework.redis.excutor;

import redis.clients.jedis.Jedis;

import java.io.Serializable;
import java.util.function.Function;

/**
 * Redis key构建器
 *
 * @author dk
 */
interface IJedisExecutor {
    /**
     * 处理 jedis请求
     *
     * @param j 处理逻辑，通过 lambda行为参数化
     * @return 处理结果
     */
    <T> T excute(Function<Jedis, T> j);

    /**
     * 将业务keu构建成带项目前缀的key
     * @param key
     * @param other
     */
    String getRedisKey(String key, Serializable... other);

    /**
     * 将redis中的key转为客户端使用的key
     * @param key
     */
    String getClientKey(String key);

    String[] getKeyArray(String... other);
}