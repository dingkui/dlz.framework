package com.dlz.framework.db.config;

import com.dlz.framework.config.DlzFwConfig;
import com.dlz.framework.db.convertor.DbConvertUtil;
import com.dlz.framework.db.convertor.dbtype.TableColumnMapper;
import com.dlz.framework.db.dao.DlzDao;
import com.dlz.framework.db.dao.IDlzDao;
import com.dlz.framework.db.enums.DbTypeEnum;
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
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * @author: dk
 * @date: 2020-10-15
 */
@Slf4j
@EnableConfigurationProperties({DlzDbProperties.class})
public class DlzDbConfig extends DlzFwConfig {
    @Bean(name = "dlzDao")
    @Lazy
    @ConditionalOnMissingBean(name = "dlzDao")
    public IDlzDao dlzDao(JdbcTemplate jdbc,DlzDbProperties properties) {
        log.info("default dlzDao init ...");
        SqlHolder.init(properties);
        return new DlzDao(jdbc);
    }

    @Bean(name = "commService")
    @Lazy
    @ConditionalOnMissingBean(name = "commService")
    public ICommService commService(IDlzDao dao) {
        log.info("default commService init ...");
        CommServiceImpl commService = new CommServiceImpl(dao);
        SqlHolder.loadDbSql(commService);
        TableColumnMapper tableCloumnMapper = new TableColumnMapper(dao);
        DbConvertUtil.tableCloumnMapper=tableCloumnMapper;
        return commService;
    }

    @Bean(name = "JdbcTemplate")
    @Lazy
    @ConditionalOnMissingBean(name = "JdbcTemplate")
    public JdbcTemplate JdbcTemplate(DataSource dataSource) {
        log.info("default JdbcTemplate init ...");
        return new JdbcTemplate(dataSource);
    }

    /**
     * 数据库日志输出调用代码位置
     * dlz.db.log.show-caller=true时生效
     * @return
     */
    @Bean
    @ConditionalOnProperty(value = "dlz.db.log.show-caller", havingValue = "true")
    public LoggingAspect loggingAspect() {
        log.info("dlz.db.log.show-caller:LoggingAspect init ...");
        return new LoggingAspect();
    }

    /**
     * sqlHelper
     */
    @Lazy
    @Bean(name = "dlzHelperDbOp")
    @ConditionalOnMissingBean(name = "dlzHelperDbOp")
    public SqlHelper dlzHelperDbOp(IDlzDao dao,DlzDbProperties properties) {
        log.info("dlzHelper init dbType is:" + properties.getDbtype());
        SqlHelper helpler;
        if (properties.getDbtype() == DbTypeEnum.SQLITE) {
            helpler = new DbOpSqlite(dao);
        } else if (properties.getDbtype() == DbTypeEnum.POSTGRESQL) {
            helpler = new DbOpPostgresql(dao);
        }else{
            helpler = new DbOpMysql(dao);
        }
        //自动扫描
        if(properties.getHelper().autoUpdate){
            log.info("dlzHelper autoUpdate ...");
            new Thread(()-> HelperScan.scan(properties.getHelper().packageName, helpler)).start();
        }
        return helpler;
    }
}
