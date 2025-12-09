package com.dlz.plugin.netty.server;


import com.dlz.plugin.netty.base.codec.ICoder;
import com.dlz.plugin.netty.base.codec.MessageEncoder;
import com.dlz.plugin.netty.base.listener.ISocketListener;
import com.dlz.plugin.netty.server.handler.SmsServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;

/**
 * 服务器端
 * @author dingkui
 *
 */
@Slf4j
public class NettyServer {
	private ServerSocketChannel serverSocketChannel;
	private final ISocketListener listener;
	private final ICoder coder;
	private final int serverPort;

//	public NettyServer(int serverPort,ServerHandler serverHandler,MessageDecoder messageDecoder,MessageEncoder messageEncoder){
//		this.serverPort=serverPort;
//		this.serverHandler=serverHandler;
//		this.messageDecoder=messageDecoder;
//		this.messageEncoder=messageEncoder;
//	}

	public NettyServer(int serverPort, ISocketListener listener, ICoder coder){
		this.serverPort=serverPort;
		this.listener=listener;
		this.coder=coder;
	}

	public void send(String msg) {
		SmsServerHandler.send(coder.mkOut(msg));
	}

	public ServerSocketChannel getServerSocketChannel() {
		return serverSocketChannel;
	}

	public void start() {
		Thread thread = new Thread(() -> {
			// 服务端要建立两个group，一个负责接收客户端的连接，一个负责处理数据传输
			// 连接处理group
			EventLoopGroup boss = new NioEventLoopGroup();
			// 事件处理group
			EventLoopGroup worker = new NioEventLoopGroup();
			ServerBootstrap bootstrap = new ServerBootstrap();
			// 绑定处理group
			bootstrap.group(boss, worker).channel(NioServerSocketChannel.class)
					// 保持连接数
					.option(ChannelOption.SO_BACKLOG, 128)
//						.option(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(65535))
					// 有数据立即发送
					.childOption(ChannelOption.TCP_NODELAY, true)
					// 保持连接
					.childOption(ChannelOption.SO_KEEPALIVE, true)
					// 处理新连接
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel sc) throws Exception {
							// 增加任务处理
							ChannelPipeline p = sc.pipeline();
							p.addLast(new LineBasedFrameDecoder(1024));
//								p.addLast(new StringDecoder());
//								p.addLast(new TimeServerHandler());
//								p.addLast(new MessageDecoder(coder));
							p.addLast(new MessageEncoder(coder){});
							p.addLast(new SmsServerHandler(listener,coder));
						}
					});

			// 绑定端口，同步等待成功
			ChannelFuture future;
			try {
				future = bootstrap.bind(serverPort).sync();
				if (future.isSuccess()) {
					serverSocketChannel = (ServerSocketChannel) future.channel();
					log.info("服务端开启成功");
				} else {
					log.warn("服务端开启失败");
				}

				// 等待服务监听端口关闭,就是由于这里会将线程阻塞，导致无法发送信息，所以我这里开了线程
				future.channel().closeFuture().sync();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				// 优雅地退出，释放线程池资源
				boss.shutdownGracefully();
				worker.shutdownGracefully();
			}
		});
		thread.start();
	}
}