package com.huahong.barrage.handler;

import com.huahong.barrage.filter.BadMessageFilter;
import com.huahong.barrage.filter.Filter;
import com.huahong.barrage.filter.TextFilter;
import com.huahong.barrage.filter.YellowFilter;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


/**
 * 处理TextWebSocketFrame
 * @author huahong
 */
@Slf4j
@Component
public class TextWebSocketFrameHandler extends
        SimpleChannelInboundHandler<TextWebSocketFrame> {

	public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

	/**
	 * 服务端发送消息
	 * @param ctx
	 * @param msg
	 * @throws Exception
	 */
	@Override
	protected void channelRead0(ChannelHandlerContext ctx,
			TextWebSocketFrame msg) throws Exception {
		Channel incoming = ctx.channel();
		StringBuilder message = new StringBuilder(msg.text());
		List<TextFilter> filters = new ArrayList<>();
		filters.add(new YellowFilter());
		filters.add(new BadMessageFilter());
		doFilter(message,filters);
		for (Channel channel : channels) {
            if (channel != incoming){
				channel.writeAndFlush(new TextWebSocketFrame(message.toString()));
			} else {
				channel.writeAndFlush(new TextWebSocketFrame("我发送的:"+message.toString()));
			}
        }
	}

	/**
	 * 过滤消息
	 * @param msg
	 * @param filters
	 */
	private void doFilter(StringBuilder msg,List<TextFilter> filters){
		String oldMessage = msg.toString();
		for(TextFilter filter:filters){
			oldMessage = filter.filter(oldMessage);
		}
		msg.delete(0,msg.length());
		msg.append(oldMessage);
	}

	/**
	 * 客户端加入
	 * @param ctx
	 * @throws Exception
	 */
	@Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        // Broadcast a message to multiple Channels
        channels.writeAndFlush(new TextWebSocketFrame("[SERVER] - " + incoming.remoteAddress() + " 加入"));
        channels.add(incoming);
		log.info("Client:"+incoming.remoteAddress() +"加入");
    }

	/**
	 * 客户端离开
	 * @param ctx
	 * @throws Exception
	 */
	@Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        // Broadcast a message to multiple Channels
        channels.writeAndFlush(new TextWebSocketFrame("[SERVER] - " + incoming.remoteAddress() + " 离开"));
		log.info("Client:" + incoming.remoteAddress() + "离开");

    }

	/**
	 * 客户端上线
 	 * @param ctx
	 * @throws Exception
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
		log.info("Client:"+incoming.remoteAddress()+"在线");
	}

	/**
	 * 客户端掉线
	 * @param ctx
	 * @throws Exception
	 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
		log.info("Client:"+incoming.remoteAddress()+"掉线");
	}

	/**
	 * 异常捕捉
	 * @param ctx
	 * @param cause
	 * @throws Exception
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
    	Channel incoming = ctx.channel();
		log.error("Client:"+incoming.remoteAddress()+"异常");
		// 当出现异常就关闭连接
		cause.printStackTrace();
        ctx.close();
	}

}
