package com.gome.clover.common.tools;

import com.gome.clover.common.netty.client.SubReqClientHandel;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

/**
 * Created by xiaoxiangxu on 2015/3/27.
 */
public class NettyUtil {

    public void nettyReqClientConnect(int port,String host)throws Exception{

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
                            ch.pipeline().addLast(new SubReqClientHandel());

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


}
