package com.dlz.framework.db.config;

import com.dlz.framework.db.convertor.ConvertUtil;
import com.dlz.framework.db.convertor.dbtype.ITableCloumnMapper;
import com.dlz.framework.db.convertor.dbtype.TableCloumnMapper;
import com.dlz.framework.db.dao.DlzDao;
import com.dlz.framework.db.dao.IDlzDao;
import com.dlz.framework.db.holder.SqlHolder;
import com.dlz.framework.db.service.ICommService;
import com.dlz.framework.db.service.impl.CommServiceImpl;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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
@Getter
@Setter
@EnableConfigurationProperties({DlzDbProperties.class})
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
}
