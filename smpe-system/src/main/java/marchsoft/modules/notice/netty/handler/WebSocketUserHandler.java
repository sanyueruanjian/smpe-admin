package marchsoft.modules.notice.netty.handler;

import cn.hutool.core.util.ObjectUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import lombok.extern.slf4j.Slf4j;
import marchsoft.modules.notice.netty.config.WebSocketUserUtil;

/**
 * @author Wangmingcan
 * @date 2021/2/21 16:34
 * @description
 */
@Slf4j
public class WebSocketUserHandler implements ChannelInboundHandler {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest) msg;
            //拿到请求地址
            String uri = request.uri();
            //判断是不是websocket请求，如果是取出参数
            String origin = request.headers().get("Origin");
            if (origin == null) {
                ctx.close();
            } else {
                if (uri != null && uri.contains("?")) {
                    String[] uriArray = uri.split("\\?");
                    if (uriArray.length > 1) {
                        Channel c = WebSocketUserUtil.get(uriArray[1]);
                        //如果存在直接踢出原先的
                        if (ObjectUtil.isNotNull(c)) {
                            c.close();
                            WebSocketUserUtil.remove(c);
                        }
                        WebSocketUserUtil.put(uriArray[1], ctx.channel());
                        request.setUri(uriArray[0]);
                        log.info("【Websocket】 userId : " + uriArray[1] +
                                "连接了webSocket, channel.id为 : " +
                                ctx.channel().id().asLongText());
                    }
                }
            }
        }
        ctx.fireChannelRead(msg);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext channelHandlerContext) {

    }

    @Override
    public void channelUnregistered(ChannelHandlerContext channelHandlerContext) {

    }

    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) {

    }

    @Override
    public void channelInactive(ChannelHandlerContext channelHandlerContext) {

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext channelHandlerContext) {

    }

    @Override
    public void userEventTriggered(ChannelHandlerContext channelHandlerContext, Object o) {

    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext channelHandlerContext) {

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable throwable) {

    }

    @Override
    public void handlerAdded(ChannelHandlerContext channelHandlerContext) {

    }

    @Override
    public void handlerRemoved(ChannelHandlerContext channelHandlerContext) {

    }
}
