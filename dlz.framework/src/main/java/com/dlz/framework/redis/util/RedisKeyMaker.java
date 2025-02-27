package com.dlz.framework.redis.util;

import com.dlz.comm.util.ValUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import java.io.Serializable;

/**
 * Redis key构建器
 *
 * @author dk
 */
@Slf4j
public class RedisKeyMaker implements IKeyMaker{
    public static final String keySplit = ":";
    public static final String prefix_auto = "auto";
    private String prefix = prefix_auto;

    @Autowired
    public void setEnvironment(Environment environment) {
        this.prefix = environment.getProperty("dlz.redis.prefix", prefix_auto);
        if (prefix.equals(prefix_auto)) {
            prefix = environment.getProperty("spring.application.name", prefix_auto) + "." +
                    environment.getProperty("spring.profiles.active", "dev");
        }
    }

    public String getKeyWithPrefix(String key, Serializable... obj) {
        if(!key.startsWith(prefix)){
            return key;
        }
        StringBuilder sb = new StringBuilder(prefix);
        sb.append(keySplit).append(key);
        for (int i = 0; i < obj.length; i++) {
            sb.append(keySplit).append(ValUtil.toStr(obj[i]));
        }
        return sb.toString().replaceAll("[:]+", ":").replaceAll(":$", "");
    }

    public String getClientKey(String key) {
        return key.substring(prefix.length() + 1);
    }

//
//    public static void main(String[] args) {
//        RedisKeyMaker keyMaker = new RedisKeyMaker();
//        System.out.println(keyMaker.getRedisKey(":xxx:xxx::"));
//        System.out.println(keyMaker.getRedisKey(":xxx::xxx::"));
//        System.out.println(keyMaker.getRedisKey(":xxx::xxx::","aa"));
//        System.out.println(keyMaker.getRedisKey(":xxx::xxx::",":aa:"));
//        System.out.println(keyMaker.getRedisKey(":xxx::xxx::","aa:"));
//        System.out.println(keyMaker.getRedisKey(":xxx::xxx::", "*:"));
//        System.out.println(keyMaker.getClientKey("auto::xxx:xxx::"));
////        System.out.println(keyMaker.getRedisKeys("auto::xxx:xxx::"));
//    }
}