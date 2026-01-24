package com.dlz.framework.db.ds;

import com.dlz.comm.exception.SystemException;
import com.dlz.comm.util.StringUtils;
import com.dlz.framework.db.convertor.rowMapper.ResultMapRowMapper;
import com.dlz.framework.db.enums.DbTypeEnum;
import com.dlz.framework.db.helper.support.SqlHelper;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * 动态数据源上下文持有者
 * 使用 ThreadLocal 管理当前线程的数据源选择
 */
@Slf4j
public class DBDynamic {
    public final static String defaultName = "default";
    private static final ThreadLocal<DataSourceConfig> HOLDER_config = new ThreadLocal<>();

    private static final Map<String, DataSourceConfig> configPool = new ConcurrentHashMap<>();

    public static <T> T use(String name, Supplier<T> c) {
        try {
            if (name == null || name.isEmpty()) {
                name = defaultName;
            }
            final DataSourceConfig confgig = configPool.get(name);
            if (confgig == null) {
                throw new SystemException("数据源不存在: " + name);
            }
            HOLDER_config.set(confgig);
            return c.get();
        } finally {
            HOLDER_config.remove();
        }
    }

    /**
     * 获取当前线程的数据源名称
     */
    private static DataSourceConfig getConfig() {
        DataSourceConfig config = HOLDER_config.get();
        if (config == null) {
            config = configPool.get(DBDynamic.defaultName);
        }
        if (config == null) {
            throw new SystemException("数据源不存在:" + defaultName);
        }
        return config;
    }

    /**
     * 获取当前线程的数据源名称
     */
    public static DataSource getDataSource() {
        return getConfig().getDataSource();
    }

    /**
     * 获取当前线程的数据源名称
     */
    public static String getUsedDataSourceName() {
        DataSourceConfig config = HOLDER_config.get();
        if (config == null) {
            return null;
        }
        return config.property.getName();
    }

    /**
     * 获取当前线程的数据源名称
     */
    public static ResultMapRowMapper getRowMapper() {
        return getConfig().getRowMapper();
    }

    /**
     * 获取当前线程的数据源名称
     */
    public static DbTypeEnum getDbType() {
        return getConfig().getDbType();
    }

    /**
     * 获取当前线程的数据源名称
     */
    public static SqlHelper getSqlHelper() {
        return getConfig().getSqlHelper();
    }


    /**
     * 更新数据源
     */
    public static synchronized boolean setDefaultDataSource(DataSource dataSource) {
        if (dataSource != null) {
            final DataSourceProperty defaultProperties = new DataSourceProperty();
            String name = DBDynamic.defaultName;
            defaultProperties.setName(name);
            final DataSourceConfig v = new DataSourceConfig(defaultProperties);
            v.setDataSource(dataSource);
            configPool.put(name, v);
            try {
                if(dataSource instanceof HikariDataSource){
                    HikariDataSource hds = (HikariDataSource) dataSource;
                    defaultProperties.setUrl(hds.getJdbcUrl());
                    defaultProperties.setUsername(hds.getUsername());
                }else{
                    Connection connection = dataSource.getConnection();
                    DatabaseMetaData metaData = connection.getMetaData();
                    defaultProperties.setDriverClassName(metaData.getDriverName());
                    defaultProperties.setUrl(metaData.getURL());
                    defaultProperties.setUsername(metaData.getUserName());
                    defaultProperties.setDbProductName(metaData.getDatabaseProductName());// 如 "MySQL", "Oracle", "PostgreSQL"
                    connection.close();
                }
            } catch (Exception e) {
                log.error("获取数据库类型失败: " + name, e);
                // 忽略错误
            }
            return true;
        }
        return false;
    }

    /**
     * 更新数据源
     */
    public static synchronized boolean setDataSource(DataSourceProperty properties) {
        if (properties == null) {
            throw new SystemException("数据源配置不能为空");
        }
        String name = properties.getName();
        if (StringUtils.isEmpty(name)) {
            name = DBDynamic.defaultName;
        }
        if (configPool.containsKey(name)) {
            // 关闭旧的数据源
            removeDataSource(name);
        }

        try {
            configPool.put(name, new DataSourceConfig(properties));
            if (name.equals(DBDynamic.defaultName)) {
                log.warn("修改系统默认数据源: " + properties.getUrl());
            }
            // 创建新的数据源
            return true;
        } catch (Exception e) {
            throw new RuntimeException("更新数据源失败: " + e.getMessage(), e);
        }
    }

    /**
     * 删除数据源
     */
    public static synchronized boolean removeDataSource(String name) {
        DataSourceConfig config = configPool.remove(name);
        if (config != null) {
            try {
                config.close();
                return true;
            } catch (Exception e) {
                log.warn("关闭数据源时发生错误", e);
                return false;
            } finally {
                configPool.remove(name);
            }
        }
        return false;
    }

    /**
     * 获取所有数据源名称
     */
    public static Set<String> getAllDataSourceNames() {
        return new HashSet<>(configPool.keySet());
    }

    /**
     * 获取当前线程的数据源名称
     */
    public static DataSourceProperty getDataSourceProperty(String name) {
        DataSourceConfig config = configPool.get(name);
        if (config == null) {
            throw new SystemException("数据源不存在:" + name);
        }
        return config.property;
    }
}