package marchsoft.modules.quartz.utils;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import marchsoft.exception.BadRequestException;
import marchsoft.modules.quartz.entity.QuartzJob;
import marchsoft.modules.quartz.entity.QuartzLog;
import marchsoft.modules.quartz.mapper.QuartzLogMapper;
import marchsoft.modules.quartz.service.QuartzJobService;
import marchsoft.utils.*;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.quartz.QuartzJobBean;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author lixiangxiang
 * @description 定时任务具体实现 与持久层结合
 * @date 2021/1/14 21:32
 */
@Async
@SuppressWarnings({"unchecked","all"})
@Slf4j
public class ExecutionJob extends QuartzJobBean {

    //获取线程池
    private final static ThreadPoolExecutor EXECUTOR = ThreadPoolExecutorUtil.getPoll();

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        //通过JobExecutionContext对象得到QuartzJob实例。
        QuartzJob quartzJob =(QuartzJob) context.getMergedJobDataMap().get(QuartzJob.JOB_KEY);
        //使用SpringContextHolder获取bean实例
        QuartzLogMapper quartzLogMapper = SpringContextHolder.getBean(QuartzLogMapper.class);
        QuartzJobService quartzJobService = SpringContextHolder.getBean(QuartzJobService.class);
        RedisUtils redisUtils = SpringContextHolder.getBean(RedisUtils.class);

        String uuid = quartzJob.getUuid();

        QuartzLog quartzLog = new QuartzLog();
        quartzLog.setJobName(quartzJob.getJobName());
        quartzLog.setBeanName(quartzJob.getBeanName());
        quartzLog.setMethodName(quartzJob.getMethodName());
        quartzLog.setParams(quartzJob.getParams());
        long startTime = System.currentTimeMillis();
        quartzLog.setCronExpression(quartzJob.getCronExpression());

        //执行任务
        try {
            log.info("-----------------------------------");
            log.info(StrUtil.format("【定时任务开始执行】 任务名称: {} ", quartzJob.getJobName()));
            //创建定时任务线程
            QuartzRunnable task = new QuartzRunnable(quartzJob.getBeanName(),quartzJob.getMethodName(),quartzJob.getParams());
           //用future管理task
            Future<?> future = EXECUTOR.submit(task);
            future.get();
            //计算运行时间
            long times = System.currentTimeMillis() - startTime;
            quartzLog.setTime(times);
            if(StringUtils.isNotBlank(uuid)){
                //将执行结果存入redis，以uuid为唯一标识
                redisUtils.set(uuid,true);
            }
            //任务状态
            quartzLog.setIsSuccess(true);
            log.info(StrUtil.format("【定时任务执行完毕】 任务名称: {}, 执行时间: {} ms", quartzJob.getJobName(),times));
            log.info("------------------------------------------------------");
            //判断是否存在子任务
            if(quartzJob.getSubTask() != null){
                String[] tasks = quartzJob.getSubTask().split("[,，]");
                //执行子任务
                if(ArrayUtil.isNotEmpty(tasks)){
                    quartzJobService.executionSubJob(tasks);
                }
            }
        } catch (Exception e) {
            //保存执行状态
            if (StringUtils.isNotBlank(uuid)) {
                redisUtils.set(uuid, false);
            }
            log.info(StrUtil.format("【任务执行失败】任务名称: {} ", quartzJob.getJobName()));
            log.info("-------------------------------------------");
            long times = System.currentTimeMillis() - startTime;
            quartzLog.setTime(times);
            //任务状态 0; 成功 1 ;失败 0
            quartzLog.setIsSuccess(false);
            //存入异常信息
            quartzLog.setExceptionDetail(ThrowableUtil.getStackTrace(e));
            //如果任务失败则暂停
            if (quartzJob.getPauseAfterFailure() != null && quartzJob.getPauseAfterFailure()) {
                quartzJob.setIsPause(false);
                //更新状态,让任务暂停
                quartzJobService.updateIsPause(quartzJob);
            }
            //将错误信息以email方式告知
            if (quartzJob.getEmail() != null) {
                System.out.println("发送邮箱");
            }
            e.printStackTrace();
            throw new BadRequestException("定时任务执行失败");
        }finally {
            //日志信息存入数据库
            quartzLogMapper.insert(quartzLog);
        }
    }
}
