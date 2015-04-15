package com.gome.clover.common.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
public class ObjectRespServer {

	public void bind(int port)throws Exception{
		//配置服务端的NIo 线程组
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try{
			ServerBootstrap b= new ServerBootstrap();
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
					.option(ChannelOption.SO_BACKLOG, 1024)
					.option(ChannelOption.TCP_NODELAY, true)
					.handler(new LoggingHandler(LogLevel.INFO))
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast(new ObjectDecoder(1024*1024*1024*2,ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())));
							ch.pipeline().addLast(new ObjectEncoder());
							ch.pipeline().addLast(new SubReqServerHandel());

						};
					});
			//绑定 端口 同步等待成功
			ChannelFuture future=b.bind(port).sync();
			//等待服务器监听 关闭
			future.channel().closeFuture().sync();
		}finally{
			//优雅的退出 释放线程资源 
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}


	}
	public void start(int port) throws Exception {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						public void initChannel(SocketChannel ch) throws Exception {
							/**
							 // server端发送的是httpResponse，所以要使用HttpResponseEncoder进行编码
							 ch.pipeline().addLast(new ObjectEncoder());
							 // server端接收到的是httpRequest，所以要使用HttpRequestDecoder进行解码
							 ch.pipeline().addLast(new ObjectDecoder(1024*1024*1024*2-1, ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())));
							 ch.pipeline().addLast(new SubReqServerHandel());
							 **/

							ChannelPipeline pipeline = ch.pipeline();
							pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(1024*1024*1024*2-1, 0, 4, 0, 4));
							pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));
							pipeline.addLast("decoder", new ObjectDecoder(1024 * 1024 * 1024 * 2 - 1, ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())));
							pipeline.addLast("encoder", new ObjectEncoder());
							pipeline.addLast(new SubReqServerHandel());
						}
					}).option(ChannelOption.SO_BACKLOG, 128)
					.childOption(ChannelOption.SO_KEEPALIVE, true);

			ChannelFuture f = b.bind(port).sync();
			f.channel().closeFuture().sync();
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}
	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		int defualtPort=8080;
		if(args!=null && args.length>0){
			defualtPort=Integer.valueOf(args[0]);
		}

		new ObjectRespServer().bind(defualtPort);
	}
}
