package com.dlz.plugin.netty.test.server;


import com.dlz.plugin.netty.server.NettyServer;
import com.dlz.plugin.netty.test.codec.SmsCoder;
import lombok.extern.slf4j.Slf4j;

/**
 * 服务器端
 *
 * @author dingkui
 */
@Slf4j
public class RunNettyServer {
    public static void main(String[] args) {
        NettyServer server = new NettyServer(899, new NettyServerListener(), new SmsCoder());
        new Thread(
                () -> {
                    for (int i = 0; i < 10; i++) {
                        try {
                            Thread.sleep(15000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        StringBuilder sb = new StringBuilder(i+":13545239574:0:服务器发送消息");
                        for (int j = 0; j < 2; j++) {
                            sb.append("消息"+j+"|");
                        }
                        sb.append("->").append(i).append("]");
                        server.send(sb.toString());
                    }
                }
        ).start();
        server.start();
    }

}