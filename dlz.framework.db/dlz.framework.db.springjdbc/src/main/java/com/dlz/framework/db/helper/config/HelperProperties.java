package com.dlz.framework.db.helper.config;

import com.dlz.framework.db.enums.DbTypeEnum;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("dlz.db.helper")
public class HelperProperties {
	/**
	 * 自动更新数据库扫码数据包
	 */
	String packageName="com.dlz";
	/**
	 * 是否开启自动更新数据库，生产环境不应开启，可提高启动速度
	 */
	boolean autoUpdate=false;
	/**
	 * 数据库类型
	 */
	DbTypeEnum type = DbTypeEnum.MYSQL;
	/**
	 * 数据库更新最大线程数
	 */
	int maxPoolSize=50;
	/**
	 * 数据库类型
	 */
	int corePoolSize=2;
}
