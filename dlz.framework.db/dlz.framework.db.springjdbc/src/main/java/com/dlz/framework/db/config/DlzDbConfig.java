package com.dlz.framework.db.config;

import com.dlz.comm.exception.SystemException;
import com.dlz.comm.util.StringUtils;
import com.dlz.comm.util.system.Reflections;
import com.dlz.framework.config.DlzFwConfig;
import com.dlz.framework.db.convertor.clumnname.ColumnNameCamel;
import com.dlz.framework.db.convertor.clumnname.IColumnNameConvertor;
import com.dlz.framework.db.convertor.dbtype.TableColumnMapper;
import com.dlz.framework.db.convertor.rowMapper.MySqlColumnMapRowMapper;
import com.dlz.framework.db.convertor.rowMapper.OracleColumnMapRowMapper;
import com.dlz.framework.db.convertor.rowMapper.ResultMapRowMapper;
import com.dlz.framework.db.dao.DlzDao;
import com.dlz.framework.db.dao.IDlzDao;
import com.dlz.framework.db.ds.DBDynamic;
import com.dlz.framework.db.ds.DynamicJdbcTemplate;
import com.dlz.framework.db.enums.DbTypeEnum;
import com.dlz.framework.db.helper.support.HelperScan;
import com.dlz.framework.db.helper.support.SqlHelper;
import com.dlz.framework.db.helper.support.dbs.DbOpDm8;
import com.dlz.framework.db.helper.support.dbs.DbOpMysql;
import com.dlz.framework.db.helper.support.dbs.DbOpPostgresql;
import com.dlz.framework.db.helper.support.dbs.DbOpSqlite;
import com.dlz.framework.db.holder.SqlHolder;
import com.dlz.framework.db.service.ICommService;
import com.dlz.framework.db.service.impl.CommServiceImpl;
import com.dlz.framework.db.util.DbConvertUtil;
import com.dlz.framework.db.util.DbLogUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.NonNull;

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
    public IDlzDao dlzDao(JdbcTemplate jdbc, DlzDbProperties properties) {
        SqlHolder.init(properties);
        DbLogUtil.init(properties);
        final DlzDao dlzDao = new DlzDao(jdbc);
        DbConvertUtil.tableCloumnMapper = new TableColumnMapper(dlzDao);
        if (log.isInfoEnabled()) {
            log.info("init dlzDao:" + DlzDao.class.getName());
            log.info("init tableCloumnMapper:" + TableColumnMapper.class.getName());
        }
        return dlzDao;
    }

    @Bean(name = "commService")
    @Lazy
    @ConditionalOnMissingBean(name = "commService")
    public ICommService commService(IDlzDao dao) {
        CommServiceImpl commService = new CommServiceImpl(dao);
        if (log.isInfoEnabled()) {
            log.info("init commService:" + CommServiceImpl.class.getName());
        }
        SqlHolder.loadDbSql(commService);
        return commService;
    }

    @Bean(name = "JdbcTemplate")
    @Lazy
    @ConditionalOnMissingBean(name = "JdbcTemplate")
    public JdbcTemplate JdbcTemplate(DataSource dataSource) {
        if (log.isInfoEnabled()) {
            log.info("init JdbcTemplate:" + DynamicJdbcTemplate.class.getName());
        }
        return new DynamicJdbcTemplate(dataSource);
    }

    /**
     * sqlHelper
     */
    @Lazy
    @Bean(name = "dlzHelperDbOp")
    @ConditionalOnMissingBean(name = "dlzHelperDbOp")
    public SqlHelper dlzHelperDbOp(DlzDbProperties properties) {
        SqlHelper helpler = DBDynamic.getSqlHelper();
        //自动扫描
        if (properties.getHelper().autoUpdate) {
            log.info("dlzHelper autoUpdate ...");
            new Thread(() -> HelperScan.scan(properties.getHelper().packageName, helpler)).start();
        }
        return helpler;
    }
}
