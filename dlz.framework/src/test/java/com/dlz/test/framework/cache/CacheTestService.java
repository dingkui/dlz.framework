package com.dlz.test.framework.cache;

import com.dlz.framework.cache.aspect.CacheAnno;
import org.springframework.stereotype.Component;

//继承AbstractRedisQueueConsumer并通过RedisQueueConsumer注解标明队列名称即可
@Component
public class CacheTestService {
    @CacheAnno(value = "getInfo1", key = "$if:com.dlz.test.framework.cache.CacheKeyTest")
    public String getInfo1(String a, String b) {
        String x = "getInfo1:" + a + ">>>>>" + b;
        System.out.println(x);
        return x;
    }

    @CacheAnno(value = "getInfo2", key = " a + b ")
    public String getInfo2(String a, String b) {
        String x = "getInfo2:" + a + ">>>>>" + b;
        System.out.println(x);
        return x;
    }

    @CacheAnno(value = "getInfo3", key = " $method ")
    public String getInfo3(String a, String b) {
        String x = "getInfo3:" + a + ">>>>>" + b;
        System.out.println(x);
        return x;
    }
}