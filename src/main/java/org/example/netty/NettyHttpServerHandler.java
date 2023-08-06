package org.example.netty;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;

import java.nio.charset.StandardCharsets;

public class NettyHttpServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest) msg;
            FullHttpResponse response = new DefaultFullHttpResponse(request.protocolVersion(), HttpResponseStatus.OK);
            // http sever protocol 규약
            response.headers().set("Content-type", "text/plain");
            response.content().writeCharSequence("Hello, word", StandardCharsets.UTF_8);

            ctx.writeAndFlush(response)
                    .addListener(ChannelFutureListener.CLOSE);
        }
    }
}
