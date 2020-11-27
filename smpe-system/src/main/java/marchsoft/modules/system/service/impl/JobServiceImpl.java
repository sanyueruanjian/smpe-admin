package marchsoft.modules.system.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import marchsoft.bean.PageVO;
import marchsoft.enums.ResultEnum;
import marchsoft.exception.BadRequestException;
import marchsoft.exception.EntityExistException;
import marchsoft.modules.system.entity.Job;
import marchsoft.modules.system.entity.dto.JobDTO;
import marchsoft.modules.system.entity.dto.JobQueryCriteria;
import marchsoft.modules.system.mapper.JobMapper;
import marchsoft.modules.system.mapper.UserMapper;
import marchsoft.modules.system.service.IJobService;
import marchsoft.modules.system.service.mapstruct.JobMapStruct;
import marchsoft.utils.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 * 岗位 服务实现类
 * </p>
 *
 * @author liuxingxing
 * @since 2020-11-26
 */
@Service
@RequiredArgsConstructor
public class JobServiceImpl extends ServiceImpl<JobMapper, Job> implements IJobService {

    private final JobMapper jobMapper;
    private final UserMapper userMapper;
    private final RedisUtils redisUtils;
    private final JobMapStruct jobMapStruct;

    @Override
    public Set<Job> findByUserId(Long id) {
        return jobMapper.findByUserId(id);
    }

    @Override
    public JobDTO findById(Long id) {
        Job job = getById(id);
        if (job == null) {
            job = new Job();
        }
        ValidationUtil.isNull(job.getId(), "Job", "id", id);
        return jobMapStruct.toDto(job);
    }

    @Override
    public List<JobDTO> queryAll(JobQueryCriteria criteria) {
        List<Job> jobs = jobMapper.selectList(this.analysisQueryCriteria(criteria));
        return jobMapStruct.toDto(jobs);
    }

    @Override
    public IPage<JobDTO> queryAll(JobQueryCriteria criteria, PageVO pageVO) {
        IPage<Job> jobPage = jobMapper.selectPage(pageVO.buildPage(), this.analysisQueryCriteria(criteria));
        List<JobDTO> jobDtos = jobMapStruct.toDto(jobPage.getRecords());
        IPage<JobDTO> iPage = pageVO.buildPage();
        iPage.setTotal(iPage.getTotal()).setRecords(jobDtos);
        return iPage;
    }

    @Override
    public void download(List<JobDTO> jobDtos, HttpServletResponse response) throws IOException {
        if (CollectionUtil.isEmpty(jobDtos)) {
            throw new BadRequestException(ResultEnum.FILE_DOWNLOAD_FAIL_NOT_DATA);
        }
        List<Map<String, Object>> list = new ArrayList<>();
        for (JobDTO jobDto : jobDtos) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("岗位名称", jobDto.getName());
            map.put("岗位状态", jobDto.getEnabled() ? "启用" : "停用");
            map.put("创建日期", jobDto.getCreateTime() == null ? null : jobDto.getCreateTime().toString());
            list.add(map);
        }
        FileUtils.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(Job resources) {
        LambdaQueryWrapper<Job> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Job::getName, resources.getName());
        if (count(queryWrapper) > 0) {
            throw new EntityExistException(Job.class, "name", resources.getName());
        }
        LocalDateTime now = LocalDateTimeUtil.now();
        String username = SecurityUtils.getCurrentUsername();
        resources.setCreateTime(now).setUpdateTime(now)
                .setCreateBy(username)
                .setUpdateBy(username);
        save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Job resources) {
        Job job = getById(resources.getId());
        if (job == null) {
            job = new Job();
        }
        LambdaQueryWrapper<Job> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Job::getName, resources.getName());
        Job old = getOne(queryWrapper);
        if (old != null && ! old.getId().equals(resources.getId())) {
            throw new EntityExistException(Job.class, "name", resources.getName());
        }
        ValidationUtil.isNull(job.getId(), "Job", "id", resources.getId());
        resources.setUpdateBy(SecurityUtils.getCurrentUsername()).setUpdateTime(LocalDateTimeUtil.now());
        updateById(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        removeByIds(ids);
        // 删除缓存
        redisUtils.delByKeys("job::id:", ids);
    }

    @Override
    public void verification(Set<Long> ids) {
        if (userMapper.countByJobs(StringUtils.strip(ids.toString(), "[", "]")) > 0) {
            throw new BadRequestException("所选的岗位中存在用户关联，请解除关联再试！");
        }
    }

    /**
     * Description:
     * 根据JobCruteruia 解析出查询Wrapper
     *
     * @param criteria: 岗位查询解析
     * @return com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<marchsoft.modules.system.entity.Job>
     * @author liuxingxing
     * @date 2020/11/24 13:28
     **/
    private LambdaQueryWrapper<Job> analysisQueryCriteria(JobQueryCriteria criteria) {
        LambdaQueryWrapper<Job> wrapper = new LambdaQueryWrapper<>();
        if (! StrUtil.isBlank(criteria.getName())) {
            // 默认使用Like匹配
            wrapper.like(Job::getName, criteria.getName());
        }
        if (! ObjectUtil.isNull(criteria.getEnabled())) {
            wrapper.eq(Job::getEnabled, criteria.getEnabled());
        }
        if (! ObjectUtil.isNull(criteria.getStratTime())) {
            // 如果只有开始时间，就默认从开始到现在
            wrapper.between(Job::getCreateTime, criteria.getStratTime(),
                    ObjectUtil.isNull(criteria.getEndTime()) ? LocalDateTime.now() : criteria.getEndTime());
        }
        return wrapper;
    }

}
