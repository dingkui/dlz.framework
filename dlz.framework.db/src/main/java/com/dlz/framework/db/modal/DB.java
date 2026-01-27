package com.dlz.framework.db.modal;

import com.dlz.framework.db.ds.DBDynamic;

public class DB {
    public final static DbJdbc Jdbc = new DbJdbc();
    public final static DbTable Table = new DbTable();
    public final static DbSql Sql = new DbSql();
    public final static DbWrapper Wrapper = new DbWrapper();
    public final static DbBatch Batch = new DbBatch();
    public final static DBDynamic Dynamic = new DBDynamic();
}
