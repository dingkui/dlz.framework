package com.dlz.framework.redis.queue.annotation;

import java.lang.annotation.*;

/**
 * redis队列生产者注解
 * eg:
 * <pre>
 * @author dingkui
 * @AnnoApi(handler="redisQueueProvider")
 * public interface ITestQueueProviderApi {
 *    @AnnoRedisQueueProvider(value="queue1",strategy=SendStrategyEn.ASYNC)
 *    void sendMessage1(String msg);
 *
 *    @AnnoRedisQueueProvider(value="queue2",strategy=SendStrategyEn.ASYNC)
 *    void sendMessage2(List<String> msgList);
 * }
 * </pre>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AnnoRedisQueueProvider {

    /**
     * 队列名称
     */
    String value() default "";

    /**
     * 发送策略，默认同步
     * 异步发送并发时有问题，取消该配置 2020-10-15
     */
//    SendStrategyEn strategy() default SendStrategyEn.SYNC;
}
