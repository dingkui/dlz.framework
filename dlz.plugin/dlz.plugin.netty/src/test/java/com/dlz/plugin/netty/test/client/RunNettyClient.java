package com.dlz.plugin.netty.test.client;


import com.dlz.plugin.netty.client.NettyClient;
import com.dlz.plugin.netty.test.codec.SmsCoder;
import lombok.extern.slf4j.Slf4j;

/**
 * 服务器端
 * @author dingkui
 *
 */
@Slf4j
public class RunNettyClient {
	public static void main(String[] args) throws InterruptedException {
		NettyClient client=new NettyClient(899, "192.168.1.3", new NettyClientListener(), new SmsCoder());
		client.conn();
		client.send("xxx");

		Thread.sleep(5000l);
		client.send("xxx22");
		client.send("xxx2233");
//		for (int j = 1; j < 31; j++) {
//			int finalJ = j;
//			new Thread(() -> {
//				String msg="x";
//				while (msg.length()<125){
//					msg+="x";
//				}
//				msg+=("-"+ finalJ);
//				for (int i = 1; i < 201; i++) {
//					client.send(msg+"-"+i);
//				}
//			}).start();
//		}
	}
}