package com.dlz.plugin.netty.client.handler;

import com.dlz.comm.util.ExceptionUtils;
import com.dlz.plugin.netty.base.codec.ICoder;
import com.dlz.plugin.netty.base.handler.BaseHandler;
import com.dlz.plugin.netty.base.listener.ISocketListener;
import com.dlz.plugin.netty.client.NettyClient;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class ClientHandler extends BaseHandler {
    private final NettyClient client;

    public ClientHandler(ISocketListener lisner, ICoder coder, NettyClient instance) {
		super(lisner, coder);
		this.client=instance;
	}
    // 连接成功后，向server发送消息
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Object hellor = coder.mkOut("hellor");
        if(hellor!=null){
            ctx.writeAndFlush(hellor);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    	if(cause!=null) {
    		log.error(cause.getMessage());
    		log.error(ctx.channel().remoteAddress().toString());
    	}
        if(null != ctx) ctx.close();
        if(null != client){
            if(cause!=null) {
                client.shutdownAndRetry(ctx.channel().remoteAddress().toString()+ cause.getMessage());
                if(!(cause instanceof IOException)){
                    log.error(ExceptionUtils.getStackTrace(cause.getMessage(),cause));
                }
            }else{
                client.shutdownAndRetry("异常重连");
            }
        }else{
            if(!(cause instanceof IOException)){
                log.error(ExceptionUtils.getStackTrace(cause.getMessage(),cause));
            }else{
                log.error(cause.getMessage());
                log.error(ctx.channel().remoteAddress().toString());
            }
        }
    }
}