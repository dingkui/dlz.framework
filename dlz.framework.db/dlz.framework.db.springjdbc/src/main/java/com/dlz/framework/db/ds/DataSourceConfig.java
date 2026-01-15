package com.dlz.framework.db.ds;

import com.dlz.comm.exception.SystemException;
import com.dlz.framework.db.convertor.rowMapper.MySqlColumnMapRowMapper;
import com.dlz.framework.db.convertor.rowMapper.OracleColumnMapRowMapper;
import com.dlz.framework.db.convertor.rowMapper.ResultMapRowMapper;
import com.dlz.framework.db.enums.DbTypeEnum;
import com.dlz.framework.db.helper.support.SqlHelper;
import com.dlz.framework.db.helper.support.dbs.DbOpDm8;
import com.dlz.framework.db.helper.support.dbs.DbOpMysql;
import com.dlz.framework.db.helper.support.dbs.DbOpPostgresql;
import com.dlz.framework.db.helper.support.dbs.DbOpSqlite;
import lombok.AccessLevel;
import lombok.Setter;

import javax.sql.DataSource;

public class DataSourceConfig {
    protected final DataSourceProperty property;

    public DataSourceConfig(DataSourceProperty property) {
        this.property = property;
    }

    @Setter(AccessLevel.NONE)
    private ResultMapRowMapper rowMapper;

    public ResultMapRowMapper getRowMapper() {
        if (rowMapper != null) {
            return rowMapper;
        }
        DbTypeEnum dbType = getDbType();
        if (dbType == DbTypeEnum.MYSQL || dbType == DbTypeEnum.POSTGRESQL) {
            rowMapper = new MySqlColumnMapRowMapper();
        } else if (dbType == DbTypeEnum.ORACLE || dbType == DbTypeEnum.DM8) {
            rowMapper = new OracleColumnMapRowMapper();
        } else {
            rowMapper = new ResultMapRowMapper();
        }
        return rowMapper;
    }

    @Setter
    private DataSource dataSource;

    public DataSource getDataSource() {
        if (dataSource != null) {
            return dataSource;
        }
        try {
            if ("hikaricp".equals(property.getType())) {
                dataSource = new DataSourceCreatorHikari().createDataSource(property);
            } else {
                IDataSourceCreator dataSourceCreator = (IDataSourceCreator) Class.forName(property.getCreatorClassName()).newInstance();
                dataSource = dataSourceCreator.createDataSource(property);
            }
            // 创建新的数据源
            return dataSource;
        } catch (Exception e) {
            throw new RuntimeException("取得数据源失败: " + e.getMessage(), e);
        }
    }

    private DbTypeEnum dbType;

    public DbTypeEnum getDbType() {
        if (dbType != null) {
            return dbType;
        }
        final String lowerCase = property.getUrl().toLowerCase();
        if (lowerCase.indexOf(":mysql") > -1
                || lowerCase.indexOf(":mariadb") > -1) {
            dbType = DbTypeEnum.MYSQL;
        } else if (lowerCase.indexOf(":postgresql") > -1) {
            dbType = DbTypeEnum.POSTGRESQL;
        } else if (lowerCase.indexOf(":oracle") > -1) {
            dbType = DbTypeEnum.ORACLE;
        } else if (lowerCase.indexOf(":dm") > -1) {
            dbType = DbTypeEnum.DM8;
        } else if (lowerCase.indexOf(":sqlite") > -1) {
            dbType = DbTypeEnum.SQLITE;
        } else if (lowerCase.indexOf(":sqlserver") > -1) {
            dbType = DbTypeEnum.MSSQL;
        } else {
            throw new SystemException("未识别的数据库类型:" + property.getUrl());
        }
        return dbType;
    }

    private SqlHelper helpler;
    public SqlHelper getSqlHelper() {
        if (helpler != null) {
            return helpler;
        }
        final DbTypeEnum dbType = getDbType();

        if (dbType == DbTypeEnum.SQLITE) {
            helpler = new DbOpSqlite();
        } else if (dbType == DbTypeEnum.POSTGRESQL) {
            helpler = new DbOpPostgresql();
        } else if (dbType == DbTypeEnum.ORACLE || dbType == DbTypeEnum.DM8) {
            helpler = new DbOpDm8();
        } else {
            helpler = new DbOpMysql();
        }
        return helpler;
    }

    public void close() throws Exception {
        if (dataSource != null) {
            if (dataSource instanceof AutoCloseable) {
                ((AutoCloseable) dataSource).close();
            }
            dataSource = null;
        }
        if (rowMapper != null) {
            rowMapper = null;
        }
    }
}
