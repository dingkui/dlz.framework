package com.dlz.test.framework.config;

import com.dlz.framework.config.DlzFwConfig;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

/**
 * @author dk
 * date 2020-10-15
 */
@Slf4j
@Configuration
@Data
public class DlzFwConfigs extends DlzFwConfig {
//    /**
//     * 缓存实现
//     */
//    @Bean(name = "dlzCache")
//    public CacheRedisJsonKey dlzCache() {
//        log.info("default dlzCache RedisJsonKey init ...");
//        return new CacheRedisJsonKey();
//    }
}
