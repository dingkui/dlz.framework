package com.dlz.framework.db.config;

import com.dlz.framework.db.enums.DbTypeEnum;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Arrays;
import java.util.List;
@Data
@ConfigurationProperties(prefix = "dlz.db")
public class DlzDbProperties {
    /**
     * 数据库类型
     */
    private DbTypeEnum dbtype = DbTypeEnum.MYSQL;
    /**
     * 数据库支持类
     */
    private String dbSupport = "";
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
    private String sql= "select sql_key as `key` ,sql_value as `sql`,sql_role as `role` from sys_sql";
    /**
     * 从数据库中取得sql配置是否开启,默认关闭
     **/
    private boolean useDbSql= false;
    /**
     * 数据库结构缓存时间，-1为不失效，单位为秒
     **/
    private int tableCacheTime= -1;
    /**
     * sqlHelper配置
     */
    private Helper helper=new Helper();
    /**
     * 日志配置
     */
    private Log log=new Log();
    /**
     * sqlHelper配置
     */
    @Data
    public static class Log {
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
         * 日志中是否显示运行sql调用处,默认关闭
         */
        private boolean showCaller = false;
    }
    /**
     * sqlHelper配置
     */
    @Data
    public static class Helper {
        /**
         * 自动更新数据库扫码数据包
         */
        String packageName="com.dlz";
        /**
         * 是否开启自动更新数据库，生产环境不应开启，可提高启动速度
         */
        boolean autoUpdate=false;
    }
}