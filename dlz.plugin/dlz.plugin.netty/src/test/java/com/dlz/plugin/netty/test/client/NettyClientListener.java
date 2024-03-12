package com.dlz.plugin.netty.test.client;

import com.dlz.plugin.netty.base.listener.ISocketListener;
import lombok.extern.slf4j.Slf4j;

/**
 * 客户端同步请求回调处理监听
 * @author dingkui
 *
 */
@Slf4j
public class NettyClientListener implements ISocketListener {
	/**
	 * 客户端收到传入数据
	 * （同步模式下，将处理结果保存到监听器中，在后续操作中取得）
	 * @param reciveStr 传入数据
	 * @return
	 */
	public String deal(String reciveStr,String channelId){
		System.out.println("NettyClientListener deal:msg="+reciveStr);
		if(reciveStr.indexOf("answer")>-1){
			return null;
		}
		return reciveStr + " this is client answer!";
	}
}
