package marchsoft.modules.quartz.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marchsoft.modules.quartz.entity.QuartzJob;
import marchsoft.modules.quartz.mapper.QuartzJobMapper;
import marchsoft.modules.quartz.utils.QuartzManage;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author lixiangxiang
 * @description 项目启动时重新激活启用的定时任务
 * @date 2021/1/25 16:13
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class JobRunner implements ApplicationRunner {
    private final QuartzJobMapper quartzJobMapper;
    private final QuartzManage quartzManage;
    @Override
    public void run(ApplicationArguments applicationArguments) {
        log.info("--------------------注入定时任务---------------------");
        LambdaQueryWrapper<QuartzJob> wrapper = new LambdaQueryWrapper<>();
        List<QuartzJob> quartzJobs = quartzJobMapper.selectList(wrapper.eq(QuartzJob::getIsPause,false));
        quartzJobs.forEach(quartzManage::addJob);
        log.info("--------------------定时任务注入完成---------------------");
    }
}
