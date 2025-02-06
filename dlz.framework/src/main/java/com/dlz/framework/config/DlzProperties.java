package com.dlz.framework.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "dlz")
public class DlzProperties {
    /**
     * api组件扫描路径,如：com\/dlz\/**\/I*Api.class
     */
    private String apiScanPath="";
}