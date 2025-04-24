package com.dlz.framework.config;

import com.dlz.comm.cache.CacheUtil;
import com.dlz.comm.cache.ICache;
import com.dlz.comm.util.StringUtils;
import com.dlz.framework.cache.aspect.CacheAspect;
import com.dlz.framework.holder.SpringHolder;
import com.dlz.framework.redis.excutor.JedisExecutor;
import com.dlz.framework.redis.queue.provider.RedisQueueProviderApiHandler;
import com.dlz.framework.redis.util.IKeyMaker;
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
import org.springframework.core.env.Environment;
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
    public BeanFactoryPostProcessor myBeanFactory(Environment env) {
        return beanFactory -> {
            SpringHolder.init(beanFactory);
            String apiScanPath = env.getProperty("dlz.fw.api-scan-path");
            if (StringUtils.isNotEmpty(apiScanPath)) {
                if(log.isInfoEnabled()) {
                    log.info("dlz spring apiScan init,resoucePath:{}", apiScanPath);
                }
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
        if(log.isInfoEnabled()){
            log.info("dlzCache init:" + cacheClass.getName());
        }
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
        if(log.isInfoEnabled()){
            log.info("default redisKeyMaker init:"+RedisKeyMaker.class.getName());
        }
        return new RedisKeyMaker();
    }

    @Bean(name = "redisPool")
    @ConditionalOnMissingBean(name = "redisPool")
    @Lazy
    public JedisPool redisPool() {
        JedisConfig jedisConfig = SpringHolder.registerBean(JedisConfig.class);
        final JedisPool jedisPool = jedisConfig.redisPoolFactory();
        if(log.isInfoEnabled()){
            log.info("default redisPool init:"+jedisPool.getClass().getName());
        }
        return jedisPool;
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
        if(log.isInfoEnabled()){
            log.info("dlz.cache.anno:CacheAspect init ...");
        }
        return new CacheAspect(cache);
    }

    /**
     * redis生产者消费者模式,开启本功能依赖开启dlz.fw.api-scan-path路径扫描
     * @return
     */
    @Bean(name = "redisQueueProviderApiHandler")
    @Lazy
    @ConditionalOnMissingBean(name = "redisQueueProviderApiHandler")
    public ApiProxyHandler redisQueueProviderApiHandler() {
        if(log.isInfoEnabled()){
            log.info("default redisQueueProviderApiHandler init:"+RedisQueueProviderApiHandler.class.getName());
        }
        return new RedisQueueProviderApiHandler();
    }

    @Bean(name = "jedisExecutor")
    @Lazy
    @ConditionalOnMissingBean(name = "jedisExecutor")
    public JedisExecutor jedisExecutor(JedisPool jedisPool,IKeyMaker keyMaker) {
        if(log.isInfoEnabled()){
            log.info("default jedisExecutor init:"+JedisExecutor.class.getName());
        }
        return new JedisExecutor(jedisPool,keyMaker);
    }

    /**
     * 系统配置取值器
     *
     * @return
     */
    @Bean
    @Lazy
    public BootConfig bootConfig() {
        if(log.isInfoEnabled()){
            log.info("default BootConfig init:"+BootConfig.class.getName());
        }
        return new BootConfig();
    }

    @Bean(name = "customConfig")
    @Lazy
    @ConditionalOnMissingBean(name = "customConfig")
    public ICustomConfig customConfig() {
        if(log.isInfoEnabled()){
            log.info("default customConfig init :"+ICustomConfig.class.getName());
        }
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
