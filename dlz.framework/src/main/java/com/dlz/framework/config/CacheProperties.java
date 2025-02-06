package com.dlz.framework.config;

import com.dlz.comm.cache.ICache;
import com.dlz.comm.cache.MemoryCahe;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "dlz.cache")
public class CacheProperties {
    private boolean anno = false;
    private Class<? extends ICache> cacheClass = MemoryCahe.class;
}