package com.dlz.framework.db.ds;

import javax.sql.DataSource;

public interface IDataSourceCreator {
    DataSource createDataSource(DataSourceProperty properties);
}
