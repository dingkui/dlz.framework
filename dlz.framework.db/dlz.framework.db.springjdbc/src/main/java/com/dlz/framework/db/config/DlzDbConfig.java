package com.dlz.framework.db.config;

import com.dlz.framework.db.DbInfo;
import com.dlz.framework.db.cache.DbOprationCache;
import com.dlz.framework.db.cache.TableInfoCache;
import com.dlz.framework.db.convertor.dbtype.ATableCloumnMapper;
import com.dlz.framework.db.convertor.dbtype.TableCloumnMapper;
import com.dlz.framework.db.dao.DaoOperator;
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
    @Bean
    @Lazy
    public DbOprationCache dbOprationCache() {
        log.info("default DbOprationCache init ...");
        return new DbOprationCache();
    }

    @Bean
    @Lazy
    public TableInfoCache tableInfoCache() {
        log.info("default TableInfoCache init ...");
        return new TableInfoCache();
    }

    @Bean(name = "tableCloumnMapper")
    @Lazy
    @ConditionalOnMissingBean(name = "tableCloumnMapper")
    public ATableCloumnMapper tableCloumnMapper() {
        log.info("default tableCloumnMapper init ...");
        return new TableCloumnMapper();
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
        return new DaoOperator(jdbc,dbProperties);
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
