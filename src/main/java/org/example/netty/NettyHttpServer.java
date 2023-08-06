package org.example.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.concurrent.FutureListener;

public class NettyHttpServer {

    // client는 curl localhost:8080
    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup parentGroup = new NioEventLoopGroup();
        EventLoopGroup childGroup = new NioEventLoopGroup(4);
        EventExecutorGroup eventExecutorGroup = new DefaultEventExecutorGroup(4);

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap().group(parentGroup, childGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<>() {
                        @Override
                        protected void initChannel(Channel ch) {
                            ch.pipeline().addLast(
                                    eventExecutorGroup, new LoggingHandler(LogLevel.INFO)
                            );
                            ch.pipeline().addLast(
                                    // FullHttpRequest를 사용하기 위한 코덱과 aggregator
                                    new HttpServerCodec(),
                                    new HttpObjectAggregator(1024 * 1024),
                                    new NettyHttpServerHandler()
                            );
                        }
                    });
            serverBootstrap.bind(8080).sync()
                    .addListener((FutureListener<Void>) future -> {
                        if (future.isSuccess()) {
                            System.out.print("success to bind 8080");
                        } else {
                            System.out.print("error");
                        }
                    }).channel().closeFuture().sync();
        } finally {
            parentGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
            eventExecutorGroup.shutdownGracefully();
        }
    }
}
