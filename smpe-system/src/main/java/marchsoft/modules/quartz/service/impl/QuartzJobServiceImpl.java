package marchsoft.modules.quartz.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marchsoft.base.BasicServiceImpl;
import marchsoft.base.PageVO;
import marchsoft.exception.BadRequestException;
import marchsoft.modules.quartz.entity.QuartzJob;
import marchsoft.modules.quartz.entity.QuartzLog;
import marchsoft.modules.quartz.entity.dto.JobQueryCriteria;
import marchsoft.modules.quartz.mapper.QuartzJobMapper;
import marchsoft.modules.quartz.mapper.QuartzLogMapper;
import marchsoft.modules.quartz.service.QuartzJobService;
import marchsoft.modules.quartz.utils.QuartzManage;
import marchsoft.utils.*;
import org.quartz.CronExpression;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lixiangxiang
 * @description
 * @date 2021/1/14 16:28
 */
@Slf4j
@Service(value = "quartzJobService")
@RequiredArgsConstructor
public class QuartzJobServiceImpl extends BasicServiceImpl<QuartzJobMapper, QuartzJob> implements QuartzJobService {

    private final QuartzJobMapper quartzJobMapper;
    private final QuartzLogMapper quartzLogMapper;
    private final QuartzManage quartzManage;
    private final RedisUtils redisUtils;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(QuartzJob resources) {
        //验证cron表达式是否正确，如果不正确抛出异常
        if (!CronExpression.isValidExpression(resources.getCronExpression())){
            throw new BadRequestException("cron表达式格式错误");
        }
        //数据库保存数据
        quartzJobMapper.insert(resources);
        //quartz增加任务
        quartzManage.addJob(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void executionSubJob(String[] tasks) throws InterruptedException {
        for (String id : tasks) {
            if(StringUtils.isBlank(id)){
                continue;
            }
            QuartzJob quartzJob = findById(Long.parseLong(id));
            //执行任务
            String uuid = IdUtil.simpleUUID();
            quartzJob.setUuid(uuid);
            execution(quartzJob);
            //获取任务执行状态，如果执行失败则停止后面子任务执行
            Boolean result = (Boolean) redisUtils.get(uuid);
            while (ObjectUtil.isNull(result)) {
                //休眠5秒。再次获取子任务的状态
                Thread.sleep(5000);
                result = (Boolean) redisUtils.get(uuid);
            }
            if(!result) {
                redisUtils.del(uuid);
                break;
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateIsPause(QuartzJob quartzJob) {
       if (quartzJob.getIsPause()) {
           quartzManage.resumeJob(quartzJob);
           quartzJob.setIsPause(false);

       }else {
           quartzManage.pauseJob(quartzJob);
           quartzJob.setIsPause(true);
       }
       quartzJobMapper.updateById(quartzJob);
    }

    @Override
    public List<QuartzJob> queryAll(JobQueryCriteria criteria) {
        return quartzJobMapper.selectList(buildJobQueryCriteria(criteria));
    }

    @Override
    public Object queryAll(JobQueryCriteria criteria, PageVO pageVO) {
        return  quartzJobMapper.selectPage(pageVO.buildPage(),buildJobQueryCriteria(criteria));
    }

    @Override
    public QuartzJob findById(Long id) {
        QuartzJob quartzJob = quartzJobMapper.selectById(id);
        if(ObjectUtil.isNull(quartzJob)){
            throw new BadRequestException("不存在id为"+id+"的任务");
        }
        ValidationUtil.isNull(quartzJob.getId(),"QuartzJob","id",id);
        return quartzJob;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(QuartzJob resources) {
        if(!CronExpression.isValidExpression(resources.getCronExpression())) {
            throw new BadRequestException("cron表达式错误");
        }
        if(StringUtils.isNotBlank(resources.getSubTask())){
            List<String> tasks = Arrays.asList(resources.getSubTask().split("[,，]"));
            if(tasks.contains(resources.getId().toString())) {
                throw new BadRequestException("子任务中不能添加当前任务ID");
            }
        }
        quartzJobMapper.updateById(resources);
        quartzManage.updateJobCron(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        for(Long id : ids) {
            QuartzJob quartzJob = findById(id);
            quartzManage.deleteJob(quartzJob);
            quartzJobMapper.deleteById(id);
        }
    }

    @Override
    public void execution(QuartzJob quartzJob) {
        quartzManage.runJobNow(quartzJob);
    }

    @Override
    public void download(List<QuartzJob> quartzJobs, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = quartzJobs.stream().map(quartzJob -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("任务名称", quartzJob.getJobName());
            map.put("Bean名称", quartzJob.getBeanName());
            map.put("执行方法", quartzJob.getMethodName());
            map.put("参数", quartzJob.getParams());
            map.put("表达式", quartzJob.getCronExpression());
            map.put("状态", quartzJob.getIsPause() ? "暂停中" : "运行中");
            map.put("描述", quartzJob.getDescription());
            map.put("创建日期", ObjectUtil.isNull(quartzJob.getCreateTime())?null:
                    LocalDateTimeUtil.format(quartzJob.getCreateTime(),
                    DatePattern.NORM_DATETIME_FORMATTER));
            return map;
        }).collect(Collectors.toList());
        FileUtils.downloadExcel(list,response);
    }

    @Override
    public Object queryAllLog(JobQueryCriteria criteria, PageVO pageVO) {
        return quartzLogMapper.selectPage(pageVO.buildPage(),buildLogQueryCriteria(criteria));
    }

    @Override
    public List<QuartzLog> queryAllLog(JobQueryCriteria criteria) {
        return quartzLogMapper.selectList(buildLogQueryCriteria(criteria));
    }

    @Override
    public void downloadLog(HttpServletResponse response, List<QuartzLog> quartzLogs) throws IOException {
        List<Map<String, Object>> list = quartzLogs.stream().map(quartzLog -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("任务名称", quartzLog.getJobName());
            map.put("Bean名称", quartzLog.getBeanName());
            map.put("执行方法", quartzLog.getMethodName());
            map.put("参数", quartzLog.getParams());
            map.put("表达式", quartzLog.getCronExpression());
            map.put("异常详情", quartzLog.getExceptionDetail());
            map.put("耗时/毫秒", quartzLog.getTime());
            map.put("状态", quartzLog.getIsSuccess() ? "成功" : "失败");
            map.put("创建日期", ObjectUtil.isNull(quartzLog.getCreateTime())?null:
                    LocalDateTimeUtil.format(quartzLog.getCreateTime(),
                            DatePattern.NORM_DATETIME_FORMATTER));
            return map;
        }).collect(Collectors.toList());
        FileUtils.downloadExcel(list, response);
    }
    /**
     * description: 构建查询quartzJob的queryWrapper
     *
     * @author: lixiangxiang
     * @param criteria 查询条件
     * @return com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<marchsoft.modules.quartz.entity.QuartzJob>
     * @date 2021/1/18 20:27
     */
    private LambdaQueryWrapper<QuartzJob> buildJobQueryCriteria (JobQueryCriteria criteria) {
        LambdaQueryWrapper<QuartzJob> wrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(criteria.getJobName())) {
            wrapper.like(QuartzJob::getJobName,criteria.getJobName());
        }
        if (ObjectUtil.isNotNull(criteria.getEndTime())&&ObjectUtil.isNotNull(criteria.getStartTime())) {
            wrapper.between(QuartzJob::getCreateTime,criteria.getStartTime(),criteria.getEndTime());
        }
        return wrapper;
    }

   /**
    * description: 构建查询quartzLog的queryWrapper
    *
    * @author: lixiangxiang
    * @param criteria /
    * @return com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<marchsoft.modules.quartz.entity.QuartzLog>
    * @date 2021/1/21 10:45
    */
    private LambdaQueryWrapper<QuartzLog> buildLogQueryCriteria (JobQueryCriteria criteria) {
        LambdaQueryWrapper<QuartzLog> wrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(criteria.getJobName())) {
            wrapper.like(QuartzLog::getJobName,criteria.getJobName());
        }
        if(ObjectUtil.isNotNull(criteria.getIsSuccess())) {
            wrapper.eq(QuartzLog::getIsSuccess,criteria.getIsSuccess());
        }
        if (ObjectUtil.isNotNull(criteria.getEndTime())&&ObjectUtil.isNotNull(criteria.getStartTime())) {
            wrapper.between(QuartzLog::getCreateTime,criteria.getStartTime(),criteria.getEndTime());
        }
        return wrapper;
    }

}
