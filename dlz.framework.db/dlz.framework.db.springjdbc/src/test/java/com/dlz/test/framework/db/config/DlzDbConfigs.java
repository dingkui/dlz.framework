package com.dlz.test.framework.db.config;

import com.dlz.framework.config.DlzProperties;
import com.dlz.framework.db.DlzTestDao;
import com.dlz.framework.db.config.DlzDbConfig;
import com.dlz.framework.db.config.DlzDbProperties;
import com.dlz.framework.db.convertor.dbtype.TableColumnMapper;
import com.dlz.framework.db.convertor.rowMapper.ResultMapRowMapper;
import com.dlz.framework.db.dao.IDlzDao;
import com.dlz.framework.db.holder.SqlHolder;
import com.dlz.framework.db.util.DbConvertUtil;
import com.dlz.framework.db.util.DbLogUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * @author dk
 * date 2020-10-15
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({DlzDbProperties.class, DlzProperties.class})
public class DlzDbConfigs extends DlzDbConfig {

//    @Bean(name = "dlzDao")
//    @Lazy
//    public IDlzDao dlzDao2(DlzDbProperties properties, ResultMapRowMapper resultRowMapper) {
//        SqlHolder.init(properties);
//        DbLogUtil.init(properties);
//        final IDlzDao dlzDao = new DlzTestDao();
//        DbConvertUtil.tableCloumnMapper= new TableColumnMapper(dlzDao);
//        if(log.isInfoEnabled()){
//            log.info("init test dlzDao:"+DlzTestDao.class.getName());
//            log.info("init resultRowMapper:"+resultRowMapper.getClass().getName());
//            log.info("init tableCloumnMapper:"+TableColumnMapper.class.getName());
//        }
//        return dlzDao;
//    }

}
