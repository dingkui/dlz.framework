package com.dlz.framework.config;

import com.dlz.comm.cache.CacheUtil;
import com.dlz.comm.cache.ICache;
import com.dlz.comm.util.StringUtils;
import com.dlz.framework.cache.aspect.CacheAspect;
import com.dlz.framework.holder.SpringHolder;
import com.dlz.framework.redis.excutor.JedisExecutor;
import com.dlz.framework.redis.queue.provider.RedisQueueProviderApiHandler;
import com.dlz.framework.redis.util.IKeyMaker;
import com.dlz.framework.redis.util.JedisKeyUtils;
import com.dlz.framework.redis.util.RedisKeyMaker;
import com.dlz.framework.spring.iproxy.ApiProxyHandler;
import com.dlz.framework.spring.iproxy.ApiScaner;
import com.dlz.framework.spring.scaner.DlzSpringScaner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import redis.clients.jedis.JedisPool;

/**
 * @author: dk
 * @date: 2020-10-15
 */
@Slf4j
@EnableConfigurationProperties({DlzProperties.class})
public class DlzFwConfig {
    /**
     * spring 容器启动开始执行
     *
     * @return
     */
    @Bean
    public BeanFactoryPostProcessor myBeanFactory(DlzProperties properties) {
        return beanFactory -> {
            SpringHolder.init(beanFactory);
            String apiScanPath = properties.getApiScanPath();
            if (StringUtils.isNotEmpty(apiScanPath)) {
                log.info("dlz spring apiScan init ...,resoucePath={}", apiScanPath);
                new DlzSpringScaner().doComponents(new ApiScaner(apiScanPath));
            }
        };
    }

    /**
     * 缓存实现
     *
     * @return
     */
    @Bean(name = "dlzCache")
    @ConditionalOnMissingBean(name = "dlzCache")
    @Lazy
    public ICache dlzCache(DlzProperties properties) throws InstantiationException, IllegalAccessException {
        Class<? extends ICache> cacheClass = properties.getCache().getCacheClass();
        log.info("dlzCache init ..." + cacheClass.getName());
        ICache iCache = cacheClass.newInstance();
        CacheUtil.init(iCache);
        return iCache;
    }

    /**
     * redis key构建器
     *
     * @return
     */
    @Bean(name = "redisKeyMaker")
    @ConditionalOnMissingBean(name = "redisKeyMaker")
    @Lazy
    public IKeyMaker redisKeyMaker() {
        log.info("default redisKeyMaker init ...");
        return new RedisKeyMaker();
    }

    @Bean(name = "redisPool")
    @ConditionalOnMissingBean(name = "redisPool")
    @Lazy
    public JedisPool redisPool() {
        log.info("default redisPool init ...");
        JedisConfig jedisConfig = SpringHolder.registerBean(JedisConfig.class);
        return jedisConfig.redisPoolFactory();
    }

    /**
     * 缓存切面
     * dlz.cache.anno=true时生效
     * @param cache
     * @return
     */
    @Bean
    @ConditionalOnProperty(value = "dlz.cache.anno", havingValue = "true")
    public CacheAspect cacheAspect(ICache cache) {
        log.info("dlz.cache.anno:CacheAspect init ...");
        return new CacheAspect(cache);
    }

    @Bean(name = "redisQueueProviderApiHandler")
    @Lazy
    @ConditionalOnMissingBean(name = "redisQueueProviderApiHandler")
    public ApiProxyHandler redisQueueProviderApiHandler() {
        log.info("default redisQueueProviderApiHandler init ...");
        return new RedisQueueProviderApiHandler();
    }

    @Bean(name = "jedisExecutor")
    @Lazy
    @ConditionalOnMissingBean(name = "jedisExecutor")
    public JedisExecutor jedisExecutor(IKeyMaker keyMaker) {
        log.info("default jedisExecutor init ...");
        JedisKeyUtils.init(keyMaker);
        return new JedisExecutor();
    }

    /**
     * 系统配置取值器
     *
     * @return
     */
    @Bean
    @Lazy
    public BootConfig bootConfig() {
        log.info("default bootConfig init ...");
        return new BootConfig();
    }

    @Bean(name = "customConfig")
    @Lazy
    @ConditionalOnMissingBean(name = "customConfig")
    public ICustomConfig customConfig() {
        log.info("default customConfig init ...");
        return new ICustomConfig() {
            @Override
            public String get(String key) {
                return null;
            }

            @Override
            public void set(String key, String value) {
            }
        };
    }
}
