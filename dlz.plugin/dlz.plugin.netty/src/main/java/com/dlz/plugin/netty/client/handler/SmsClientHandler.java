package com.dlz.plugin.netty.client.handler;

import com.dlz.comm.util.ExceptionUtils;
import com.dlz.plugin.netty.base.codec.ICoder;
import com.dlz.plugin.netty.base.handler.BaseHandler;
import com.dlz.plugin.netty.base.listener.ISocketListener;
import com.dlz.plugin.netty.client.NettyClient;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.Charset;

@Slf4j
public class SmsClientHandler extends BaseHandler {
    private final NettyClient client;

    public SmsClientHandler(ISocketListener lisner, ICoder coder, NettyClient instance) {
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
    public final Charset GBK=Charset.forName("GBK");
    /**
     * 处理客户端websocket请求的核心方法
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg != null){
            ByteBuf msg1 = (ByteBuf) msg;
            byte[] re=new byte[msg1.readableBytes()];
            msg1.readBytes(re);
            String m = new String(re,GBK);
            Object o = coder.mkOut(lisner.deal(m, ctx.channel().id().asLongText()));
            if(o!=null){
                ctx.writeAndFlush(o);
            }
        }
    }
}