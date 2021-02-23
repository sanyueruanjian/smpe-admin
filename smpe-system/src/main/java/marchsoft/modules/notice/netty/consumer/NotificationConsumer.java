package marchsoft.modules.notice.netty.consumer;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marchsoft.modules.notice.entity.NoticeSend;
import marchsoft.modules.notice.netty.handler.BasicSocketServerHandler;
import marchsoft.modules.notice.service.INoticeSendService;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Wangmingcan
 * @date 2021/2/22 15:35
 * @description
 */
@Slf4j
@Component
@AllArgsConstructor
public class NotificationConsumer{

    private final INoticeSendService noticeSendService;

    @RabbitListener(queuesToDeclare = @Queue("notification"))
    public void receiveA(NoticeSend msg){
        log.info("通知消费者A拿到了：" + msg.toString());
        saveAndSend(msg);
    }

    @RabbitListener(queuesToDeclare = @Queue("notification"))
    public void receiveB(NoticeSend msg){
        log.info("通知消费者B拿到了：" + msg.toString());
        saveAndSend(msg);
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveAndSend(NoticeSend msg){
        BasicSocketServerHandler.send(msg);
        noticeSendService.save(msg);
    }

}
