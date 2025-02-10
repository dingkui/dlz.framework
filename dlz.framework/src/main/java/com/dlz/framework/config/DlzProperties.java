package com.dlz.framework.config;

import com.dlz.comm.cache.ICache;
import com.dlz.comm.cache.MemoryCahe;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "dlz")
public class DlzProperties {
    /**
     * api组件扫描路径,如：com\/dlz\/**\/I*Api.class
     */
    private String apiScanPath="";
    /**
     * 缓存配置
     */
    private Cache cache=new Cache();

    @Data
    public static class Cache {
        /**
         * 是否开启注解缓存，默认关闭
         */
        private boolean anno = false;
        /**
         * 缓存实现类
         */
        private Class<? extends ICache> cacheClass = MemoryCahe.class;
    }
}