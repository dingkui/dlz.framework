package com.dlz.plugin.netty.client;

import com.dlz.plugin.netty.base.codec.ICoder;
import com.dlz.plugin.netty.base.codec.MessageEncoder;
import com.dlz.plugin.netty.base.listener.ISocketListener;
import com.dlz.plugin.netty.client.handler.SmsClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 异步客户端 采用长连接
 *
 * @author dingkui
 */
@Slf4j
public class NettyClient {
    private final int port;
    private final String host;
    private final ICoder coder;
    private final ISocketListener listener;
    private SocketChannel socketChannel;
    private NettyClient instance;
    static byte[] a = new byte[0];

    public NettyClient(int port, String host, ISocketListener listener, ICoder coder) {
        this.host = host;
        this.port = port;
        this.coder = coder;
        this.listener = listener;
        instance=this;
    }

    public void send(String msg) {
        if (socketChannel != null) {
            if (retryTimes > 0) {
                throw new RuntimeException("连接已中断，发送失败。。");
            }
            synchronized (a) {
                Object o = coder.mkOut(msg);
                if (o != null) {
                    socketChannel.writeAndFlush(o);
                }
            }
        } else {
            log.warn("socketChannel is null");
        }
    }

    public void conn() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
                Bootstrap bootstrap = new Bootstrap();
                bootstrap.channel(NioSocketChannel.class);
                // 保持连接
                bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
                // 有数据立即发送
                bootstrap.option(ChannelOption.TCP_NODELAY, true);

                bootstrap.option(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(65535));
                // 绑定处理group
                bootstrap.group(eventLoopGroup).remoteAddress(host, port);
                bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        // 增加任务处理
                        // 初始化编码器，解码器，处理器
                        ChannelPipeline p = socketChannel.pipeline();
						p.addLast(new LineBasedFrameDecoder(1024));
//						p.addLast(new StringDecoder());
//						p.addLast(new TimeClientHandler());
//                        p.addLast(new MessageDecoder(coder));
                        p.addLast(new MessageEncoder(coder) {});
                        p.addLast(new SmsClientHandler(listener, coder, instance));
                    }
                });
                // 进行连接
                ChannelFuture future;
                try {
                    future = bootstrap.connect().sync();
                    // 判断是否连接成功
                    if (future.isSuccess()) {
                        // 得到管道，便于通信
                        socketChannel = (SocketChannel) future.channel();
                        String message = "接服务器(" + host + ":" + port + ")成功...";
                        log.info(message);
                        retryTimes = 0;
                    } else {
                        log.error("连接服务器失败...");
                    }
                    // 等待客户端链路关闭，就是由于这里会将线程阻塞，导致无法发送信息，所以我这里开了线程
                    future.channel().closeFuture().sync();
                } catch (Exception e) {
                    shutdownAndRetry(e.getMessage());
                } finally {
                    // 优雅地退出，释放相关资源
                    eventLoopGroup.shutdownGracefully();
                }
            }
        }).start();
    }

    int retryTimes = 0;
    boolean retrying = false;
    Timer timer = new Timer();

    /**
     * 断线重连服务器
     * 第一次立即重连，在后面每一次重连失败，重连时间加10秒，达到120秒后，每120秒重试一次
     *
     * @param msg
     */
    public void shutdownAndRetry(String msg) {
        if (retrying) {
            log.error("重连中。。。");
            return;
        }
        retrying = true;

        retryTimes++;
        long times = retryTimes > 12 ? 120 : (retryTimes - 1) * 10;
        log.error("出现异常：[" + msg + "]," + (times > 0 ? times + "秒后将" : "") + "重连。。。");
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                retrying = false;
                log.warn("客户端重连服务器(" + host + ":" + port + ")第" + retryTimes + "次。。。");
                conn();
            }
        }, times * 1000);
    }
}