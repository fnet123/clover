package com.gome.clover.common.netty.client;

import com.alibaba.fastjson.JSON;
import com.gome.clover.common.compress.CompressUtil;
import com.gome.clover.common.zeromq.ZeroMQEntity;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.http.*;
import java.net.URI;
import java.util.logging.Logger;

import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.LoggerFactory;

/**
 * 时间服务 客户端
 * @author linyoufa
 *
 */
public class ObjectReqClient {

	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ObjectReqClient.class);
	public void connect(int port ,String host, final ZeroMQEntity entity) throws Exception {
		EventLoopGroup workerGroup = new NioEventLoopGroup();

		try {
			Bootstrap b = new Bootstrap();
			b.group(workerGroup);
			b.channel(NioSocketChannel.class);
			b.option(ChannelOption.SO_KEEPALIVE, true);
			b.handler(new ChannelInitializer<SocketChannel>() {
				/**
				@Override
				public void initChannel(SocketChannel ch) throws Exception {
					// 客户端接收到的是httpResponse响应，所以要使用HttpResponseDecoder进行解码
					ch.pipeline().addLast(new HttpResponseDecoder());
					// 客户端发送的是httprequest，所以要使用HttpRequestEncoder进行编码
					ch.pipeline().addLast(new HttpRequestEncoder());
					ch.pipeline().addLast(new SubReqClientHandel(entity));
				} **/
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					/**
					ch.pipeline().addLast(new ObjectDecoder(1024*1024*1024*2-1, ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())));
					ch.pipeline().addLast(new ObjectEncoder());
					ch.pipeline().addLast(new SubReqClientHandel(entity));
					 **/
					ChannelPipeline pipeline = ch.pipeline();
					pipeline.addLast("frameDecoder",new LengthFieldBasedFrameDecoder(1024*1024*1024*2-1, 0,4, 0, 4));
					pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));
					pipeline.addLast("decoder",new ObjectDecoder(1024*1024*1024*2-1, ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())));
					pipeline.addLast("encoder",new ObjectEncoder());
					pipeline.addLast("handler", new SubReqClientHandel());
				};

			});
			try {
				byte[] entityBytes = CompressUtil.compress(JSON.toJSONString(entity).getBytes());
				Channel  channel = b.connect(host, port).sync().channel();
				channel.writeAndFlush(entityBytes).sync();
				logger.info("连接向Server（"+host+":"+port+"）写数据完成,数据包大小："+entityBytes.length );
			}catch (Exception e){
				logger.error(String.format("连接Server失败", host, port), e);
			}
			/**
			// Start the client.
			ChannelFuture f = b.connect(host, port).sync();
			URI uri = new URI("http://"+host+":"+port);
			String msg = "Are you ok?";

			DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET,
					uri.toASCIIString(), Unpooled.wrappedBuffer(msg.getBytes("UTF-8")));

			// 构建http请求
			request.headers().set(HttpHeaders.Names.HOST, host);
			request.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
			request.headers().set(HttpHeaders.Names.CONTENT_LENGTH, request.content().readableBytes());
			// 发送http请求
			f.channel().write(request);
			f.channel().flush();
			f.channel().closeFuture().sync();
			 **/
		} finally {
			workerGroup.shutdownGracefully();
		}

	}
	public static void main(String[] args) throws Exception {
		ObjectReqClient client = new ObjectReqClient();
		//client.connect("127.0.0.1", 8844);
	}









	/**
	public static void main(String[] args) throws Exception {
		int defualtPort=8080;
		if(args!=null && args.length>0){
			defualtPort=Integer.valueOf(args[0]);
		}
		//new ObjectReqClient().connect(defualtPort,"127.0.0.1");
	}

	public void connect(int port,String host ,final ZeroMQEntity entity)throws Exception{

		EventLoopGroup clientGroup= new NioEventLoopGroup();

		try{

			Bootstrap b= new Bootstrap();
			b.group(clientGroup).
			channel(NioSocketChannel.class)
			.option(ChannelOption.TCP_NODELAY, true)//表示持续发送TCP数据包 而不是等到一定大小才发送
			.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					// TODO Auto-generated method stub
					ch.pipeline().addLast(new ObjectDecoder(1024 * 1024, ClassResolvers.softCachingConcurrentResolver(this.getClass().getClassLoader())));
					ch.pipeline().addLast(new ObjectEncoder());
					ch.pipeline().addLast(new SubReqClientHandel(entity));
				}
			});
			// 等待完成 连接
			ChannelFuture f=b.connect(host,port).sync();
			//等待 客户端 链路关闭
			f.channel().closeFuture().sync();
		}finally{
			clientGroup.shutdownGracefully();
		}
	}
**/

}
