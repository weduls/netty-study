package org.example.netty;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof String) {
            System.out.printf("received msg : %s", msg);
            ctx.writeAndFlush("This is Server")
                    // ChannelFutureListener Close를 보내줘야 client쪽에서 요청이 끝난걸 알수있음
                    // 붙이지 않으면 client쪽은 계속 대
                    .addListener(ChannelFutureListener.CLOSE);
        }
    }
}
