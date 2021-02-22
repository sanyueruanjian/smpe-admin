package marchsoft.modules.notice.netty.config;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import marchsoft.modules.notice.netty.consumer.NotificationConsumer;
import marchsoft.modules.notice.netty.handler.BasicSocketServerHandler;
import marchsoft.modules.notice.netty.handler.WebSocketUserHandler;


/**
 * @author Wangmingcan
 * @date 2021/2/21 16:26
 * @description
 */
public class NettyChannelInitializer extends ChannelInitializer {

    @Override
    protected void initChannel(Channel channel){
        //获取到pipeline
        ChannelPipeline pipeline = channel.pipeline();

        //因为是基于http协议，使用http的编码和解码器
        pipeline.addLast(new HttpServerCodec());

        //是以块方式写，添加 ChunkedWriteHandler 处理器
        pipeline.addLast(new ChunkedWriteHandler());

        /*
        说明：
        1. http 数据在传输过程中是分段，HttpObjectAggregator，就是可以将多个报文段聚合
        2. 这就是为什么，当浏览器发送大量数据时，就会发出多次http请求。
        */
        pipeline.addLast(new HttpObjectAggregator(8192));

        //拿用户的id，并存入concurrentMap
        pipeline.addLast(new WebSocketUserHandler());

        /*
         * 说明：
         *  1.对应websocket ，它的数据是以  帧（frame） 形式传播
         *  2. 可以看到WebSocketFrame ，下面有六个子类
         *  3. 浏览器请求时，  ws://localhost:7000/hello 表示请求的uri
         *  4. WebSocketServerProtocolHandler 核心功能是将 http协议升级为 ws 协议，保持长连接
         *  5. 是通过一个状态码 101
         */
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));

        //向pipeline加入一个netty提供的 IdleStateHandler
        //long readerIdleTime: 表示多长时间没有读，就会发送一个心跳检测包检测是否连接
        //long writerIdleTime: 表示多长时间没有写，就会发送一个心跳检测包检测是否连接
        //long addIdleTime: 表示多长时间没有读写， 就会发送一个心跳检测包检测是否连接
        //文档说明：
        //当 IdleStateEvent 出发后 ，就会传递给管道的下一个handler去处理，通过调用（触发），
        //下一个handler的userEventTiggered，在该方法中去处理IdleStateEvent
//        pipeline.addLast(new IdleStateHandler(3,5,7, TimeUnit.SECONDS));

        //自定义的handler，处理业务逻辑
        pipeline.addLast(new BasicSocketServerHandler());
    }
}
