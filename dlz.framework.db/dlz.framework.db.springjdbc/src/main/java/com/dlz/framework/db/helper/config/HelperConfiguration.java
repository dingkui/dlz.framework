package com.dlz.framework.db.helper.config;

import com.dlz.framework.db.dao.IDlzDao;
import com.dlz.framework.db.enums.DbTypeEnum;
import com.dlz.framework.db.helper.support.AsyncUtils;
import com.dlz.framework.db.helper.support.SqlHelper;
import com.dlz.framework.db.helper.support.dbs.DbOpMysql;
import com.dlz.framework.db.helper.support.dbs.DbOpPostgresql;
import com.dlz.framework.db.helper.support.dbs.DbOpSqlite;
import com.dlz.framework.db.holder.SqlHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@EnableConfigurationProperties({HelperProperties.class})
public class HelperConfiguration {
    @Autowired
    HelperProperties properties;

    @Bean("sqlThreadPool")
    @Lazy
    public AsyncTaskExecutor sqlThreadPool() {
        ThreadPoolTaskExecutor asyncTaskExecutor = new ThreadPoolTaskExecutor();
        asyncTaskExecutor.setMaxPoolSize(properties.maxPoolSize);
        asyncTaskExecutor.setCorePoolSize(properties.corePoolSize);
        asyncTaskExecutor.setThreadNamePrefix("sql-thread-pool-");
        asyncTaskExecutor.initialize();
        log.info("AsyncTaskExecutor init ...");
        return asyncTaskExecutor;
    }

    @Lazy
    @Bean(name = "dlzHelperDbOp")
    @ConditionalOnMissingBean(name = "dlzHelperDbOp")
    public SqlHelper dlzHelperDbOp(IDlzDao dao) {
        log.info("DbOp init dbType is:" + properties.getType());
        if (SqlHolder.properties.getDbtype() == DbTypeEnum.SQLITE) {
            return new DbOpSqlite(dao);
        } else if (SqlHolder.properties.getDbtype() == DbTypeEnum.MYSQL) {
            return new DbOpMysql(dao);
        } else if (SqlHolder.properties.getDbtype() == DbTypeEnum.POSTGRESQL) {
            return new DbOpPostgresql(dao);
        }
        return new DbOpMysql(dao);
    }

    @Bean
    @Lazy
    public AsyncUtils asyncUtils(SqlHelper op) {
        log.info("AsyncUtils init ...");
        return new AsyncUtils(op);
    }
}
