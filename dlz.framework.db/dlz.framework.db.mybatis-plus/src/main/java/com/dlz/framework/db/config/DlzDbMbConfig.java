package com.dlz.framework.db.config;

import com.dlz.framework.db.cache.MyBeanPostProcessor;
import com.dlz.framework.db.service.ICommPlusService;
import com.dlz.framework.db.service.impl.CommPlusServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;

/**
 * @author: dk
 * @date: 2020-10-15
 */
@Slf4j
public class DlzDbMbConfig extends DlzDbConfig{
    @Bean
    public MyBeanPostProcessor myBeanPostProcessor() {
        log.info("BeanPostProcessor init ...");
        return new MyBeanPostProcessor();
    }

    @Bean(name = "commPlusService")
    @Lazy
    public ICommPlusService commService(MyBeanPostProcessor dao) {
        log.info("default commPlusService init ...");
        ICommPlusService commService = new CommPlusServiceImpl(dao);
        return commService;
    }
}
