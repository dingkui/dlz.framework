package com.dlz.framework.redis.queue.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * redis队列消费者注解
 * 用法
 * eg:
 * <pre>
 * @Component
 * @AnnoRedisQueueConsumer("queue1")
 * public class MessageConsumer1 extends ARedisQueueConsumer<String> {
 *    @Override
 *    public void doConsume(String message) {
 * 		System.out.println(message);
 *    }
 * }
 * @Component
 * @AnnoRedisQueueConsumer("queue2")
 * public class MessageConsumer2 extends ARedisQueueConsumer<List<String>> {
 *    @Override
 *    public void doConsume(List<String> msgList) {
 * 		System.out.println(msgList);
 *    }
 * }
 * </pre>
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
//@Component
//@Conditional(RedisQueueCondition.class)
public @interface AnnoRedisQueueConsumer {
    /**
     * 队列名称 
     */
    String value() default "";

    int num() default 1;
}
