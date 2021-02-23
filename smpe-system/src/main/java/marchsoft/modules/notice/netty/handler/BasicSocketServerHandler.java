package marchsoft.modules.notice.netty.handler;

import cn.hutool.json.JSONUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import marchsoft.modules.notice.entity.NoticeSend;
import marchsoft.modules.notice.netty.config.WebSocketUserUtil;

/**
 * @author Wangmingcan
 * @date 2021/2/21 16:38
 * @description
 */
@Slf4j
public class BasicSocketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) {

    }
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {

    }

    public static void send(NoticeSend msg) {
        //获取发送对象的通道并进行转发
        Channel channel = WebSocketUserUtil.get(String.valueOf(msg.getUserId()));
        if (channel != null) {
            channel.writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(msg)));
            log.info("【Websocket】createBy : " + msg.getCreateBy() + "向userId : "
                    + msg.getUserId() + "发送了 : " + msg.getId());
        }
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
