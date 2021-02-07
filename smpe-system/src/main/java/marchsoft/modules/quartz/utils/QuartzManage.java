package marchsoft.modules.quartz.utils;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import marchsoft.exception.BadRequestException;
import marchsoft.modules.quartz.entity.QuartzJob;
import marchsoft.utils.SecurityUtils;
import org.quartz.*;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

import static org.quartz.TriggerBuilder.newTrigger;

/**
 * @author lixiangxiang
 * @description 定时任务管理类
 * @date 2021/1/14 18:57
 */

@Slf4j
@Component
public class QuartzManage {

    private static final String JOB_NAME = "SMPE_TASK_";

    @Resource(name = "scheduler")
    private Scheduler scheduler;

    /**
     * description: 添加一个任务
     *
     * @author: lixiangxiang
     * @param quartzJob /
     * @return void
     * @date 2021/1/14 21:01
     */
     public void addJob(QuartzJob quartzJob){
         try {
             // 构建jobDetail,并与ExecutionJob类绑定(Job执行内容)
             JobDetail jobDetail = JobBuilder.newJob(ExecutionJob.class).
                     withIdentity(JOB_NAME+quartzJob.getId()).build();

             //通过触发器名和cron表达式创建Trigger
             Trigger cronTrigger = newTrigger()
                     //设置触发器的名字 作为任务标识
                     .withIdentity(JOB_NAME + quartzJob.getId())
                     //立即生效
                     .startNow()
                     //创建cron
                     .withSchedule(CronScheduleBuilder.cronSchedule(quartzJob.getCronExpression()))
                     .build();
             //把JOB_KEY和job信息放入jobDataMap中
             cronTrigger.getJobDataMap().put(QuartzJob.JOB_KEY,quartzJob);

             //重置启动时间
             ((CronTriggerImpl)cronTrigger).setStartTime(new Date());

             //执行定时任务
             scheduler.scheduleJob(jobDetail,cronTrigger);

             //如果设置暂停，暂停任务
             if (quartzJob.getIsPause()){
                pauseJob(quartzJob);
             }
         } catch (Exception e) {
             log.error(StrUtil.format("【创建定时任务失败】操作人id: {} 定时任务id：{}",
                     SecurityUtils.getCurrentUserId()),quartzJob.getId() ,e);
             throw new BadRequestException("创建定时任务失败");
         }
     }

    /**
     * description: 暂停一个job
     *
     * @author: lixiangxiang
     * @param quartzJob /
     * @return void
     * @date 2021/1/15 18:30
     */
    public void pauseJob(QuartzJob quartzJob) {
        try {
            JobKey jobKey = JobKey.jobKey(JOB_NAME+quartzJob.getId());
            scheduler.pauseJob(jobKey);
        } catch (Exception e) {
          log.error(StrUtil.format("【定时任务暂停失败】操作人id: {}，定时任务id：{}",
                  SecurityUtils.getCurrentUserId(),quartzJob.getId()),e);
          throw new BadRequestException("定时任务暂停失败");
        }
    }

    /**
     * description: 恢复一个job
     *
     * @author: lixiangxiang
     * @param quartzJob /
     * @return void
     * @date 2021/1/19 21:11
     */
    public void resumeJob(QuartzJob quartzJob) {
        try {
            //根据job的id生成TriggerKey 从而获取到 trigger
            TriggerKey triggerKey = TriggerKey.triggerKey(JOB_NAME + quartzJob.getId());
            CronTrigger trigger =(CronTrigger) scheduler.getTrigger(triggerKey);
            //如果不存在创建一个定时任务
            if(ObjectUtil.isNull(trigger)) {
                addJob(quartzJob);
            }
            JobKey jobKey = JobKey.jobKey(JOB_NAME + quartzJob.getId());
            scheduler.resumeJob(jobKey);
        } catch (Exception e) {
            log.error(StrUtil.format("【恢复定时任务失败】操作人id: {} ，定时任务id：{}",
                    SecurityUtils.getCurrentUserId(),quartzJob.getId()),e);
            throw new BadRequestException("恢复定时任务失败");
        }
    }

    /**
     * description: 更新job cron表达式
     *
     * @author: lixiangxiang
     * @param quartzJob /
     * @return void
     * @date 2021/1/19 21:36
     */
    public void updateJobCron(QuartzJob quartzJob) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(JOB_NAME + quartzJob.getId());
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            //如果不存在创建一个定时任务
            if(ObjectUtil.isNull(trigger)) {
                addJob(quartzJob);
                trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            }
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(quartzJob.getCronExpression());
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
            //重置启动时间
            ((CronTriggerImpl)trigger).setStartTime(new Date());
            //重新将quartzJob放入map
            trigger.getJobDataMap().put(QuartzJob.JOB_KEY,quartzJob);

            //执行多次任务
            scheduler.rescheduleJob(triggerKey,trigger);
            //暂停任务
            if (quartzJob.getIsPause()) {
                pauseJob(quartzJob);
            }
        } catch (Exception e) {
            log.error(StrUtil.format("【更新定时任务失败】操作人id: {} ，定时任务id：{}",
                    SecurityUtils.getCurrentUserId(),quartzJob.getId()));
            throw new BadRequestException("更新定时任务失败");
        }
    }

    /**
     * 删除一个job
     * @param quartzJob /
     */
    public void deleteJob(QuartzJob quartzJob){
        try {
            JobKey jobKey = JobKey.jobKey(JOB_NAME + quartzJob.getId());
            scheduler.pauseJob(jobKey);
            scheduler.deleteJob(jobKey);
        } catch (Exception e){
            log.error(StrUtil.format("【删除定时任务失败】操作人id: {} ，定时任务id：{}",
                    SecurityUtils.getCurrentUserId(),quartzJob.getId()));
            throw new BadRequestException("删除定时任务失败");
        }
    }

    /**
     * description: 立即执行任务
     *
     * @author: lixiangxiang
     * @param quartzJob /
     * @return void
     * @date 2021/1/20 11:47
     */
    public void runJobNow(QuartzJob quartzJob) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(JOB_NAME + quartzJob.getId());
            CronTrigger trigger = (CronTrigger)scheduler.getTrigger(triggerKey);
            //如果不存在创建一个定时任务
            if(ObjectUtil.isNull(trigger)) {
                addJob(quartzJob);
            }
            JobDataMap dataMap = new JobDataMap();
            dataMap.put(QuartzJob.JOB_KEY,quartzJob);
            JobKey jobKey = JobKey.jobKey(JOB_NAME + quartzJob.getId());
            scheduler.triggerJob(jobKey,dataMap);
        } catch (Exception e) {
            log.error(StrUtil.format("【定时任务执行失败】操作人id: {} ，定时任务id：{}",
                    SecurityUtils.getCurrentUserId(),quartzJob.getId()),e);
            throw new BadRequestException("定时任务执行失败");
        }
    }
}
