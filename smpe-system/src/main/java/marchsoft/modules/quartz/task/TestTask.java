package marchsoft.modules.quartz.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author lixiangxiang
 * @description 定时任务测试
 * @date 2021/1/15 22:28
 */

@Slf4j
@Component
public class TestTask {
    public void run() {
        log.info("run 运行成功");
    }
}
