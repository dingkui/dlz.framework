package com.dlz.framework.db.ds;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class DataSourceProperty {
    private String name;
    private String creatorClassName = "com.dlz.framework.db.ds.DataSourceCreatorHikari"; // hikaricp, druid, tomcat, dbcp2
    private String type = "hikaricp"; // hikaricp, druid, tomcat, dbcp2
    private String driverClassName;
    private String dbProductName;
    private String url;
    private String username;
    private String password;
    private String testQuery;

    // 连接池配置
    private int maxPoolSize = 10;
    private int minIdle = 2;
    private long connectionTimeout = 30000;
    private long validationTimeout = 3000;
    private long idleTimeout = 600000;
    private long maxLifetime = 1800000;
    private long leakDetectionThreshold = 60000;

    // 额外配置
    private Map<String, Object> additionalProperties = new HashMap<>();


}
