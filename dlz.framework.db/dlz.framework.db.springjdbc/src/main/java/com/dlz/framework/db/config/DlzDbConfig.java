package com.dlz.framework.db.config;

import com.dlz.framework.db.DbInfo;
import com.dlz.framework.db.convertor.ConvertUtil;
import com.dlz.framework.db.convertor.dbtype.ITableCloumnMapper;
import com.dlz.framework.db.convertor.dbtype.TableCloumnMapper;
import com.dlz.framework.db.dao.DlzDao;
import com.dlz.framework.db.dao.IDlzDao;
import com.dlz.framework.db.holder.DefaultSqlholder;
import com.dlz.framework.db.holder.ISqlHolder;
import com.dlz.framework.db.service.ICommService;
import com.dlz.framework.db.service.impl.CommServiceImpl;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
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

    @Bean(name = "dbInfo")
    @Lazy
    @ConditionalOnMissingBean(name = "dbInfo")
    public DbInfo dbInfo(ISqlHolder sql,DlzDbProperties dbProperties) {
        log.info("default dbInfo init ...");
        return new DbInfo(sql, dbProperties);
    }

    @Bean(name = "dlzDao")
    @Lazy
    @DependsOn({"dbInfo"})
    @ConditionalOnMissingBean(name = "dlzDao")
    public IDlzDao dlzDao(JdbcTemplate jdbc,DlzDbProperties dbProperties) {
        log.info("default dlzDao init ...");
        return new DlzDao(jdbc,dbProperties);
    }

    @Bean(name = "sqlHolder")
    @Lazy
    @ConditionalOnMissingBean(name = "sqlHolder")
    public ISqlHolder sqlHolder(DlzDbProperties dbProperties) {
        log.info("default sqlHolder init ...");
        return new DefaultSqlholder(dbProperties);
    }

    @Bean(name = "commService")
    @Lazy
    @ConditionalOnMissingBean(name = "commService")
    public ICommService commService(IDlzDao dao) {
        log.info("default commService init ...");
        return new CommServiceImpl(dao);
    }

    @Bean(name = "JdbcTemplate")
    @Lazy
    @ConditionalOnMissingBean(name = "JdbcTemplate")
    public JdbcTemplate JdbcTemplate(DataSource dataSource) {
        log.info("default MyJdbcTemplate init ...");
        return new JdbcTemplate(dataSource);
    }
}
