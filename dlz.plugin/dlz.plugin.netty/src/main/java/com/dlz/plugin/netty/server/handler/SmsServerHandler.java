package com.dlz.plugin.netty.server.handler;

import com.dlz.plugin.netty.base.codec.ICoder;
import com.dlz.plugin.netty.base.handler.BaseHandler;
import com.dlz.plugin.netty.base.listener.ISocketListener;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;

@Slf4j
public class SmsServerHandler extends BaseHandler {
    /**
     * 存储每一个客户端接入进来时的channel对象
     */
    public static ChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public SmsServerHandler(ISocketListener lisner, ICoder coder) {
        super(lisner, coder);
    }

    public static void send(Object msg) {
        if (msg != null) {
            group.writeAndFlush(msg);
        }
    }

//    @Override
//    public boolean isSharable() {
//        logger.debug("handler-sharable");
//        return super.isSharable();
//    }
//
//    @Override
//    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
//        logger.debug("channelRegistered "+ctx.name());
//        super.channelRegistered(ctx);
//    }
//
//
//    @Override
//    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
//        logger.debug("channelUnregistered");
//        super.channelUnregistered(ctx);
//    }

    /**
     * 客户端与服务端创建连接的时候调用
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
		log.debug("客户端已连接："+ctx.channel().remoteAddress()+" "+ctx.channel().id().asLongText());
        group.add(ctx.channel());
    }

    /**
     * 客户端与服务端断开连接时调用
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		log.debug("客户端与服务端连接关闭channelActive...");
        group.remove(ctx.channel());
    }

    /**
     * 服务端接收客户端发送过来的数据结束之后调用
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
//		logger.debug("信息接收完毕channelReadComplete...");
    }

    /**
     * 工程出现异常的时候调用
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error(cause.getMessage());
        log.error("客户端：" + ctx.channel().remoteAddress());
        ctx.close();
    }

//	/**
//	 * 服务端处理客户端websocket请求的核心方法，这里接收了客户端发来的信息
//	 */
//	@Override
//	public void channelRead(ChannelHandlerContext channelHandlerContext, Object info) throws Exception {
//		RequestInfo requestInfo = (RequestInfo) info;
//		String info2 = requestInfo.getInfo();
//		logger.debug("服务端，接受到：" + info2);
//		// 单独回复客户端信息
//		requestInfo.setInfo("单独回复："+info2);
//		channelHandlerContext.writeAndFlush(requestInfo);
//		// 服务端使用这个就能向 每个连接上来的客户端群发消息
//		requestInfo.setInfo("群发回复："+info2);
//		NettyConfig.group.writeAndFlush(requestInfo);
//		Iterator<Channel> iterator = NettyConfig.group.iterator();
//		while (iterator.hasNext()) {
//			// 打印出所有客户端的远程地址
//			logger.debug((iterator.next()).remoteAddress());
//		}
//	}

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