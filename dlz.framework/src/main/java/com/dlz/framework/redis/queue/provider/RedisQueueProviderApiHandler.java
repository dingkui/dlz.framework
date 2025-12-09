package com.dlz.framework.redis.queue.provider;

import com.dlz.comm.exception.SystemException;
import com.dlz.comm.util.ExceptionUtils;
import com.dlz.comm.util.ValUtil;
import com.dlz.framework.redis.excutor.JedisExecutor;
import com.dlz.framework.redis.queue.annotation.AnnoRedisQueueProvider;
import com.dlz.framework.redis.util.IKeyMaker;
import com.dlz.framework.spring.iproxy.ApiProxyHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Method;

/**
 * Redis队列生产者动态代理实现类
 */
//@Component
//@Conditional(RedisQueueCondition.class)
@Slf4j
public class RedisQueueProviderApiHandler extends ApiProxyHandler {
    @Autowired
    private IKeyMaker keyMaker;
    @Autowired
    JedisExecutor jedisExecutor;

    @Override
    public Object done(Class<?> clas, Method method, Object[] args) throws Exception {
        long rId = 0;
        if (method.isAnnotationPresent(AnnoRedisQueueProvider.class)) {
            if (args == null || args.length != 1) {
                throw new Exception("队列接口只能发送一个参数");
            }
            AnnoRedisQueueProvider redisQueueProvider = method.getDeclaredAnnotation(AnnoRedisQueueProvider.class);
            String queueName = redisQueueProvider.value();
            SystemException.notEmpty(queueName, () -> "生产者未配置队列名字:" + clas + "." + method.getName());
            String redisQueueName = keyMaker.getKeyWithPrefix(queueName);
            String json = ValUtil.toStr(args[0]);
            try {
                rId = jedisExecutor.excute(j -> j.rpush(redisQueueName, json));
                log.debug("同步发消息成功!{} -> {}", redisQueueName, json);
            } catch (Exception e) {
                log.error("同步发消息失败!{} -> {}", redisQueueName, json);
                log.error(ExceptionUtils.getStackTrace(e));
            }
            return rId;
//            try (Jedis jedis = jedisPool.getResource()) {
//                switch (redisQueueProvider.strategy()) {
//                    case SYNC:
//                        String json = ValUtil.toStr(args[0]);
//                        try {
//                            rId = jedis.rpush(redisQueueName, json);
//                            log.debug("同步发消息成功!{} -> {}", redisQueueName, json);
//                        } catch (Exception e) {
//                            log.error("同步发消息失败!{} -> {}", redisQueueName, json);
//                            log.error(e.getMessage(), e);
//                        }
//                        return rId;
            //异步发送并发时有问题，取消该配置
//                    case ASYNC:
//                        Executors.newSingleThreadExecutor().submit(() -> {
//                            String json2 = ValUtil.toStr(args[0]);
//                            try {
//                                jedis.rpush(redisQueueName, json2);
//                                log.debug("异步发消息成功!{} -> {}", redisQueueName, json2);
//                            } catch (Exception e) {
//                                log.error("异步发消息失败!{} -> {}", redisQueueName, json2);
//                                log.error(e.getMessage(), e);
//                            }
//                        });
//                        return 1;
//                }
//            }
        }
        return rId;
    }
}
