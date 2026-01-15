package com.dlz.framework.db.ds;

import com.dlz.comm.util.StringUtils;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class DataSourceCreatorHikari implements IDataSourceCreator {
    public DataSource createDataSource(DataSourceProperty properties) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(properties.getUrl());
        config.setUsername(properties.getUsername());
        config.setPassword(properties.getPassword());
        config.setDriverClassName(properties.getDriverClassName());

        // 连接池配置
        config.setMaximumPoolSize(properties.getMaxPoolSize());
        config.setMinimumIdle(properties.getMinIdle());
        config.setConnectionTimeout(properties.getConnectionTimeout());
        config.setIdleTimeout(properties.getIdleTimeout());
        config.setMaxLifetime(properties.getMaxLifetime());
        config.setLeakDetectionThreshold(properties.getLeakDetectionThreshold());

        // 其他配置
        config.setPoolName("dynamic-hikari-pool-" + properties.getName());
        if(!StringUtils.isEmpty(properties.getTestQuery())){
            config.setConnectionTestQuery(properties.getTestQuery());
            config.setValidationTimeout(properties.getValidationTimeout());
        }

        // 设置延迟初始化 - 只有在首次请求连接时才初始化
        config.setInitializationFailTimeout(-1); // -1 表示等待直到连接成功

        return new HikariDataSource(config);
    }

}
