package marchsoft.modules.quartz.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marchsoft.base.BasicServiceImpl;
import marchsoft.exception.BadRequestException;
import marchsoft.modules.quartz.entity.QuartzJob;
import marchsoft.modules.quartz.mapper.QuartzJobMapper;
import marchsoft.modules.quartz.mapper.QuartzLogMapper;
import marchsoft.modules.quartz.service.QuartzJobService;
import marchsoft.modules.quartz.utils.QuartzManage;
import marchsoft.utils.RedisUtils;
import org.quartz.CronExpression;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author lixiangxiang
 * @description
 * @date 2021/1/14 16:28
 */
@Slf4j
@Service(value = "quartzJobService")
@RequiredArgsConstructor
public class QuartzJobServiceImpl extends BasicServiceImpl<QuartzJobMapper,QuartzJob> implements QuartzJobService {

    private final QuartzJobMapper quartzJObMapper;
    private final QuartzLogMapper quartzLogMapper;
    private final QuartzManage quartzManage;
    private final RedisUtils redisUtils;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(QuartzJob resources) {
        //验证cron表达式是否正确，如果不正确抛出异常
        if(!CronExpression.isValidExpression(resources.getCronExpression())){
            throw new BadRequestException("cron表达式格式错误");
        }
        //数据库保存数据
        int insert = quartzJObMapper.insert(resources);
        //quartz增加任务
        quartzManage.addJob(resources);
    }

    @Override
    public void executionSubJob(String[] tasks) throws InterruptedException {
        System.out.println("子任务执行");
    }

    @Override
    public void updateIsPause(QuartzJob quartzJob) {
        System.out.println("状态更新");
    }
}
