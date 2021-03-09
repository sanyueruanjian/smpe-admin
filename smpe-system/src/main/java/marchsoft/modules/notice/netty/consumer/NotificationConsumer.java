package marchsoft.modules.notice.netty.consumer;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marchsoft.modules.notice.entity.Notice;
import marchsoft.modules.notice.netty.handler.BasicSocketServerHandler;
import marchsoft.modules.notice.service.INoticeService;
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

    private final INoticeService noticeService;

    @RabbitListener(queuesToDeclare = @Queue("notification"))
    public void receiveA(Notice msg){
        saveAndSend(msg);
    }

    @RabbitListener(queuesToDeclare = @Queue("notification"))
    public void receiveB(Notice msg){
        saveAndSend(msg);
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveAndSend(Notice msg){
        BasicSocketServerHandler.send(msg);
        noticeService.save(msg);
    }

}
