package com.dlz.plugin.netty.base.listener;

/**
 * 服务器端回馈处理监听
 * @author dingkui
 *
 */
public interface ISocketListener {
	/**
	 * 处理传入数据，并返回信息
	 * @param reciveStr 传入数据
	 */
	String deal(String reciveStr, String channelId);
}
