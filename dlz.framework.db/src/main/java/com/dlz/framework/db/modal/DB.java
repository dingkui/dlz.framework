package com.dlz.framework.db.modal;

import com.dlz.comm.exception.SystemException;
import com.dlz.framework.db.ds.DBDynamic;
import com.dlz.framework.db.ds.DataSourceConfig;

import java.util.function.Supplier;

public class DB {
    /**
     * 原生JDBC操作，适合简单的少参数sql.可快速构建功能
     * 建议采用Sql操作
     */
    public final static DbJdbc Jdbc = new DbJdbc();
    /**
     * 根据表名操作数据库，适合为定义Pojo的情况下快速操作数据库
     * 建议定义Pojo，采用Pojo操作
     */
    public final static DbTable Table = new DbTable();
    /**
     * 预设sql语句操作,支持复杂自定义sql，支持参数设置，支持动态判断
     * sql:
     *   1.直接写：select * from user where id=#{id}
     *   2.xml预设sql：<sql id="key.selectUser">select * from user where id=#{id}</sql>
     *   3.db 预设sql：selectUser= select * from user where id=#{id}
     */
    public final static DbSql Sql = new DbSql();
    /**
     * 基于Pojo 和lambda 操作数据库,Lambda 表达式，IDE 自动补全，重构安全
     */
    public final static DbPojo Pojo = new DbPojo();
    /**
     * 批量操作数据库
     */
    public final static DbBatch Batch = new DbBatch();
    /**
     * 动态数据源操作
     */
    public final static DBDynamic Dynamic = new DBDynamic();
}
