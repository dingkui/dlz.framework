package com.dlz.framework.db.config;

import com.dlz.framework.db.convertor.ConvertUtil;
import com.dlz.framework.db.convertor.dbtype.ITableCloumnMapper;
import com.dlz.framework.db.convertor.dbtype.TableCloumnMapper;
import com.dlz.framework.db.dao.DlzDao;
import com.dlz.framework.db.dao.IDlzDao;
import com.dlz.framework.db.enums.DbTypeEnum;
import com.dlz.framework.db.helper.support.AsyncUtils;
import com.dlz.framework.db.helper.support.HelperScan;
import com.dlz.framework.db.helper.support.SqlHelper;
import com.dlz.framework.db.helper.support.dbs.DbOpMysql;
import com.dlz.framework.db.helper.support.dbs.DbOpPostgresql;
import com.dlz.framework.db.helper.support.dbs.DbOpSqlite;
import com.dlz.framework.db.holder.SqlHolder;
import com.dlz.framework.db.service.ICommService;
import com.dlz.framework.db.service.impl.CommServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.sql.DataSource;

/**
 * @author: dk
 * @date: 2020-10-15
 */
@Slf4j
@EnableConfigurationProperties({DlzDbProperties.class,HelperProperties.class})
public class DlzDbConfig {
    @Bean(name = "tableCloumnMapper")
    @Lazy
    @ConditionalOnMissingBean(name = "tableCloumnMapper")
    public ITableCloumnMapper tableCloumnMapper(IDlzDao dao) {
        log.info("default tableCloumnMapper init ...");
        TableCloumnMapper tableCloumnMapper = new TableCloumnMapper(dao);
        ConvertUtil.tableCloumnMapper=tableCloumnMapper;
        return tableCloumnMapper;
    }
    @Bean(name = "dlzDao")
    @Lazy
    @ConditionalOnMissingBean(name = "dlzDao")
    public IDlzDao dlzDao(JdbcTemplate jdbc,DlzDbProperties dbProperties) {
        log.info("default dlzDao init ...");
        SqlHolder.init(dbProperties);
        return new DlzDao(jdbc);
    }

    @Bean(name = "commService")
    @Lazy
    @ConditionalOnMissingBean(name = "commService")
    public ICommService commService(IDlzDao dao) {
        log.info("default commService init ...");
        CommServiceImpl commService = new CommServiceImpl(dao);
        SqlHolder.loadDbSql(commService);
        return commService;
    }

    @Bean(name = "JdbcTemplate")
    @Lazy
    @ConditionalOnMissingBean(name = "JdbcTemplate")
    public JdbcTemplate JdbcTemplate(DataSource dataSource) {
        log.info("default MyJdbcTemplate init ...");
        return new JdbcTemplate(dataSource);
    }

    /**
     * 数据库日志输出调用代码位置
     * @return
     */
    @Bean
    @ConditionalOnProperty(value = "dlz.db.showCaller", havingValue = "true")
    public LoggingAspect loggingAspect() {
        log.info("dlz.db.showCaller:LoggingAspect init ...");
        return new LoggingAspect();
    }

    /**
     * sqlHelper
     */
    @Lazy
    @Bean(name = "dlzHelperDbOp")
    @ConditionalOnMissingBean(name = "dlzHelperDbOp")
    public SqlHelper dlzHelperDbOp(IDlzDao dao, DlzDbProperties properties) {
        log.info("DbOp init dbType is:" + properties.getDbtype());
        if (SqlHolder.properties.getDbtype() == DbTypeEnum.SQLITE) {
            return new DbOpSqlite(dao);
        } else if (SqlHolder.properties.getDbtype() == DbTypeEnum.MYSQL) {
            return new DbOpMysql(dao);
        } else if (SqlHolder.properties.getDbtype() == DbTypeEnum.POSTGRESQL) {
            return new DbOpPostgresql(dao);
        }
        return new DbOpMysql(dao);
    }
    @Bean("sqlThreadPool")
    @Lazy
    public AsyncTaskExecutor sqlThreadPool(HelperProperties helper) {
        ThreadPoolTaskExecutor asyncTaskExecutor = new ThreadPoolTaskExecutor();
        asyncTaskExecutor.setMaxPoolSize(helper.maxPoolSize);
        asyncTaskExecutor.setCorePoolSize(helper.corePoolSize);
        asyncTaskExecutor.setKeepAliveSeconds(10);
        asyncTaskExecutor.setAllowCoreThreadTimeOut(true);
        asyncTaskExecutor.setThreadNamePrefix("sql-thread-pool-");
        asyncTaskExecutor.initialize();
        log.info("AsyncTaskExecutor init ...");
        return asyncTaskExecutor;
    }
    @Bean
    @Lazy
    public AsyncUtils asyncUtils(SqlHelper op) {
        log.info("AsyncUtils init ...");
        return new AsyncUtils(op);
    }

    @Bean
    @ConditionalOnProperty(value = "dlz.db.helper.auto-update", havingValue = "false")
    public HelperScan helperScan(HelperProperties helper, AsyncUtils asyncUtils) {
        log.info("dlz.db.helper.autoUpdate:HelperScan init ...");
        return new HelperScan(helper.packageName,asyncUtils);
    }
}
