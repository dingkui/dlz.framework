package com.dlz.framework.db.ds;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * 动态 JdbcTemplate 实现
 * 支持根据线程上下文自动选择数据源
 */
@Slf4j
public class DynamicJdbcTemplate extends JdbcTemplate {

    public DynamicJdbcTemplate(DataSource dataSource) {
        DBDynamic.setDefaultDataSource(dataSource);
    }

    @Override
    public DataSource getDataSource() {
        // 从连接池管理器获取对应的数据源
        return DBDynamic.getDataSource();
    }

    @Override
    public void setDataSource(DataSource dataSource) {
        // 覆盖此方法以防止直接设置数据源
        // 数据源应该始终从 ConnectionPoolManager 动态获取
        throw new UnsupportedOperationException("不允许直接设置数据源，请通过 ConnectionPoolManager 管理");
    }
}