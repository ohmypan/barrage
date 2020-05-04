package com.huahong.barrage.server;


import com.huahong.barrage.handler.HttpRequestHandler;
import com.huahong.barrage.handler.TextWebSocketFrameHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;


/**
 * 服务端 ChannelInitializer
 * @author hh
 */
public class WebsocketBarrageServerInitializer extends
		ChannelInitializer<SocketChannel> {

	@Override
    public void initChannel(SocketChannel ch) throws Exception {
		 ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast("http-decodec",new HttpRequestDecoder());
		pipeline.addLast("http-aggregator",new HttpObjectAggregator(65536));
		pipeline.addLast("http-encodec",new HttpResponseEncoder());
		pipeline.addLast("http-chunked",new ChunkedWriteHandler());
		pipeline.addLast("http-request",new HttpRequestHandler("/ws"));
		pipeline.addLast("WebSocket-protocol",new WebSocketServerProtocolHandler("/ws"));
		pipeline.addLast("WebSocket-request",new TextWebSocketFrameHandler());
		pipeline.addLast("heart-beat",new IdleStateHandler(5,5,5, TimeUnit.SECONDS));

    }
}
