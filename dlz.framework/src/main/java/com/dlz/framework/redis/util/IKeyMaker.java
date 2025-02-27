package com.dlz.framework.redis.util;

import java.io.Serializable;

/**
 * key构建器
 *
 * @author dk
 */
public interface IKeyMaker {

    /**
     * 取得完成的key(带前缀)
     * @param key
     * @param obj
     * @return
     */
    String getKeyWithPrefix(String key, Serializable... obj);

    /**
     * 取得客户端key(去掉前缀)
     * @param key
     * @return
     */
    String getClientKey(String key);

}