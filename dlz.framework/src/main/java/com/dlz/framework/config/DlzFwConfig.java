package com.dlz.framework.config;

import com.dlz.comm.cache.ICache;
import com.dlz.comm.cache.MemoryCahe;
import com.dlz.framework.cache.aspect.CacheAspect;
import com.dlz.framework.holder.SpringHolder;
import com.dlz.framework.redis.RedisKeyMaker;
import com.dlz.framework.redis.excutor.JedisExecutor;
import com.dlz.framework.redis.queue.provider.RedisQueueProviderApiHandler;
import com.dlz.framework.spring.iproxy.ApiProxyHandler;
import com.dlz.framework.spring.iproxy.ApiScaner;
import com.dlz.framework.spring.scaner.DlzSpringScaner;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import redis.clients.jedis.JedisPool;

/**
 * @author: dk
 * @date: 2020-10-15
 */
@Slf4j
@Setter
@Getter
public class DlzFwConfig {
	protected String getApiScanPath(){
		return "com/dlz/**/I*Api.class";
	}
	/**
	 * spring 容器启动开始执行
	 * @return
	 */
    @Bean
    public BeanFactoryPostProcessor myBeanFactory() {
        return new BeanFactoryPostProcessor() {
            @Override
            public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
				SpringHolder.init(beanFactory);
				String apiScanPath = getApiScanPath();
				if(apiScanPath !=null){
					log.info("dlz spring init ...,resoucePath={}", apiScanPath);
					new DlzSpringScaner().doComponents(new ApiScaner(apiScanPath));
				}
            }
        };
    }

	/**
	 * 缓存实现
	 * @return
	 */
	@Bean(name = "dlzCache")
    @ConditionalOnMissingBean(name = "dlzCache")
	@Lazy
    public ICache dlzCache() {
    	log.info("default dlzCache init MemoryCahe...");
        return new MemoryCahe();
    }

	/**
	 * redis key构建器
	 * @return
	 */
	@Bean(name = "redisKeyMaker")
	@ConditionalOnMissingBean(name = "redisKeyMaker")
	@Lazy
	public RedisKeyMaker redisKeyMaker() {
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
	 * @param cache
	 * @return
	 */
    @Bean(name = "cacheAspect")
	@ConditionalOnMissingBean(name = "cacheAspect")
    public CacheAspect cacheAspect(ICache cache) {
		log.info("default cacheAspect init ...");
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
    public JedisExecutor jedisExecutor() {
		log.info("default jedisExecutor init ...");
    	return new JedisExecutor();
    }

	/**
	 * 系统配置取值器
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
		return new ICustomConfig(){
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
