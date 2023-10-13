package com.dlz.framework.db.config;

import com.dlz.framework.db.enums.DbTypeEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Arrays;
import java.util.List;

@ConfigurationProperties(prefix = "dlz.db")
@Getter
@Setter
public class DlzDbProperties {
    /**
     * 是否启动jdbcSql
     */
    private boolean jdbcSql=true;
    /**
     * 是否显示结果日志
     */
    private boolean showResult = false;
    /**
     * 是否显示配置的sql
     */
    private boolean showKeySql = true;
    /**
     * 是否显示运行sql
     */
    private boolean showRunSql = false;
    /**
     * 日志中是否显示运行sql
     */
    private boolean showCaller = false;
    /**
     * 数据库类型
     */
    private DbTypeEnum dbtype = DbTypeEnum.MYSQL;
    /**
     * 数据库中blob类型编码
     */
    private String blob_charsetname = "GBK";
    /**
     * sql路径配置
     **/
    private List<String> sqllist= Arrays.asList("jar.app.*");
    /**
     * 从数据库中取得sql配置的sql
     **/
    private String sql= "select key,sql from sys_sql";
    /**
     * 从数据库中取得sql配置是否开启
     **/
    private boolean useDbSql= false;
}