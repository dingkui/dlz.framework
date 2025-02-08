package com.dlz.test.framework.db.config;

import com.dlz.framework.config.DlzProperties;
import com.dlz.framework.db.config.DlzDbConfig;
import com.dlz.framework.db.config.DlzDbProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author: dk
 * @date: 2020-10-15
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({DlzDbProperties.class, DlzProperties.class})
public class DlzDbConfigs extends DlzDbConfig {

}
