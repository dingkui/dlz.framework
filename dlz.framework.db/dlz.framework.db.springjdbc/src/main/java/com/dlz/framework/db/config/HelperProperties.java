package com.dlz.framework.db.config;

import lombok.Data;

/**
 * sqlHelper配置
 */
@Data
public class HelperProperties {
	/**
	 * 自动更新数据库扫码数据包
	 */
	String packageName="com.dlz";
	/**
	 * 是否开启自动更新数据库，生产环境不应开启，可提高启动速度
	 */
	boolean autoUpdate=false;
}
