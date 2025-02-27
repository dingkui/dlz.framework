package com.dlz.framework.redis.excutor;

import com.dlz.comm.exception.RemoteException;
import com.dlz.comm.util.ExceptionUtils;
import com.dlz.framework.redis.util.IKeyMaker;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.Serializable;
import java.util.function.Function;

/**
 * Redis 执行器
 * http://doc.redisfans.com/
 *
 * @author dk
 */
@Slf4j
@AllArgsConstructor
public class JedisExecutor implements IJedisKeyExecutor, IJedisStringExecutor, IJedisHashExecutor, IJedisListExecutor, IJedisSetExecutor {
    @Autowired
    private final JedisPool jedisPool;
    @Autowired
    private final IKeyMaker keyMaker;

    /**
     * 处理 jedis请求
     *
     * @param j 处理逻辑，通过 lambda行为参数化
     * @return 处理结果
     */
    public <T> T excute(Function<Jedis, T> j) throws RemoteException {
        try (Jedis jedis = jedisPool.getResource()) {
            return j.apply(jedis);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(RemoteException.build("redis异常:" + e.getMessage(), e)));
        }
        return null;
    }

    @Override
    public String getRedisKey(String key, Serializable... other) {
        return keyMaker.getKeyWithPrefix(key, other);
    }

    @Override
    public String getClientKey(String key) {
        return keyMaker.getClientKey(key);
    }

    @Override
    public String[] getKeyArray(String... keys) {
        String[] newkeys = new String[keys.length];
        for (int i = 0; i < keys.length; i++) {
            newkeys[i] = getRedisKey(keys[i]);
        }
        return newkeys;
    }
}