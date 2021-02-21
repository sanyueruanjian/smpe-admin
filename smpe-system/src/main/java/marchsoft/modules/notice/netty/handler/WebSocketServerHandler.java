package marchsoft.modules.notice.netty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import marchsoft.modules.notice.netty.config.WebSocketUserUtil;

/**
 * @author Wangmingcan
 * @date 2021/2/21 16:38
 * @description
 */
@Slf4j
public class WebSocketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) {

    }
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {

    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        WebSocketUserUtil.remove(ctx.channel());
        log.info("【Websocket】" + ctx.channel().id().asLongText() + "离开了");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        WebSocketUserUtil.remove(ctx.channel());
        cause.printStackTrace();
        log.error("【Websocket】信息传输发生异常 : " + cause.toString() + "，" +
                "原因 ："+ cause.getMessage());
//        ctx.close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        if(evt instanceof IdleStateEvent){
            // 将 evt 向下转型 IdleStateEvent
            IdleStateEvent event = (IdleStateEvent)evt;
            String eventType = null;
            switch (event.state()){
                case ALL_IDLE:
                    eventType = "读写空闲";
                    break;
                case READER_IDLE:
                    eventType = "读空闲";
                    break;
                case WRITER_IDLE:
                    eventType = "写空闲";
                    break;
                default:
            }
            log.info(ctx.channel().remoteAddress() + "超时：" + eventType);

        }
    }
}
