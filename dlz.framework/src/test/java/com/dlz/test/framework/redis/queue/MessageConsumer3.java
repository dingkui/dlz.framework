package com.dlz.test.framework.redis.queue;

import com.dlz.framework.redis.queue.annotation.AnnoRedisQueueConsumer;
import com.dlz.framework.redis.queue.consumer.ARedisQueueConsumer;
import org.springframework.stereotype.Component;

//继承AbstractRedisQueueConsumer并通过RedisQueueConsumer注解标明队列名称即可
@Component
@AnnoRedisQueueConsumer("queue3")
public class MessageConsumer3 extends ARedisQueueConsumer<String> {
	@Override
	public void doConsume(String msgList) {
		System.out.println(msgList);
	}
}