package marchsoft.modules.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marchsoft.base.PageVO;
import marchsoft.enums.ResultEnum;
import marchsoft.exception.BadRequestException;
import marchsoft.modules.system.entity.Job;
import marchsoft.modules.system.entity.dto.JobDTO;
import marchsoft.modules.system.entity.dto.JobQueryCriteria;
import marchsoft.modules.system.mapper.JobMapper;
import marchsoft.modules.system.mapper.UserMapper;
import marchsoft.modules.system.service.IJobService;
import marchsoft.modules.system.service.mapstruct.JobMapStruct;
import marchsoft.utils.FileUtils;
import marchsoft.utils.RedisUtils;
import marchsoft.utils.SecurityUtils;
import marchsoft.utils.StringUtils;
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
@Slf4j
@Service
@RequiredArgsConstructor
public class JobServiceImpl extends ServiceImpl<JobMapper, Job> implements IJobService {

    private final JobMapper jobMapper;
    private final UserMapper userMapper;
    private final JobMapStruct jobMapStruct;

    /**
     * Description:
     * 根据用户id获取
     *
     * @param id: 用户id
     * @return java.util.Set<marchsoft.modules.system.entity.Job>
     * @author liuxingxing
     * @date 2020/11/24 13:07
     **/
    @Override
    public Set<Job> findByUserId(Long id) {
        return jobMapper.findByUserId(id);
    }

    /**
     * Description:
     * 根据ID查询
     *
     * @param id: 岗位id
     * @return marchsoft.modules.system.entity.dto.JobDTO
     * @author liuxingxing
     * @date 2020/11/24 13:08
     **/
    @Override
    public JobDTO findById(Long id) {
        Job job = getById(id);
        if (ObjectUtil.isEmpty(job)) {
            log.error("【查询岗位失败】" + "操作人id：" + SecurityUtils.getCurrentUserId() + "\t查询岗位id：" + id);
            throw new BadRequestException(ResultEnum.DATA_NOT_FOUND);
        }
        return jobMapStruct.toDto(job);
    }

    /**
     * Description:
     * 查询全部数据
     *
     * @param criteria: Job查询类
     * @return java.util.List<marchsoft.modules.system.entity.dto.JobDTO>
     * @author liuxingxing
     * @date 2020/11/24 13:08
     **/
    @Override
    public List<JobDTO> queryAll(JobQueryCriteria criteria) {
        List<Job> jobs = jobMapper.selectList(this.analysisQueryCriteria(criteria));
        return jobMapStruct.toDto(jobs);
    }

    /**
     * Description:
     * 分页查询
     *
     * @param criteria: Job查询类
     * @param pageVO:   分页插件
     * @return java.util.Map<java.lang.String, java.lang.Object>
     * @author liuxingxing
     * @date 2020/11/24 13:09
     **/
    @Override
    public IPage<JobDTO> queryAll(JobQueryCriteria criteria, PageVO pageVO) {
        IPage<Job> jobPage = jobMapper.selectPage(pageVO.buildPage(), this.analysisQueryCriteria(criteria));
        List<JobDTO> jobDtos = jobMapStruct.toDto(jobPage.getRecords());
        IPage<JobDTO> iPage = pageVO.buildPage();
        BeanUtil.copyProperties(jobPage, iPage);
        iPage.setRecords(jobDtos);
        return iPage;
    }

    /**
     * Description:
     * 待导出的数据
     *
     * @param jobDtos:  所有要查询的JobDTO实体
     * @param response:
     * @throws IOException io异常
     * @author liuxingxing
     * @date 2020/11/24 13:12
     **/
    @Override
    public void download(List<JobDTO> jobDtos, HttpServletResponse response) throws IOException {
        if (CollectionUtil.isEmpty(jobDtos)) {
            log.error("【导出岗位失败】" + "操作人id：" + SecurityUtils.getCurrentUserId() + "\t导出数据集合为空jobDtos：" + jobDtos);
            throw new BadRequestException(ResultEnum.FILE_DOWNLOAD_FAIL_NOT_DATA);
        }

        List<Map<String, Object>> list = new ArrayList<>();
        for (JobDTO jobDto : jobDtos) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("岗位名称", jobDto.getName());
            map.put("岗位状态", jobDto.getEnabled() ? "启用" : "停用");
            map.put("创建日期", ObjectUtil.isNull(jobDto.getCreateTime()) ? null : LocalDateTimeUtil.format(jobDto.getCreateTime(),
                    DatePattern.NORM_DATETIME_FORMATTER));
            list.add(map);
        }
        FileUtils.downloadExcel(list, response);
    }

    /**
     * Description:
     * 创建Job
     *
     * @param resources: 要创建的Job实体
     * @author liuxingxing
     * @date 2020/11/24 13:13
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(Job resources) {
        LambdaQueryWrapper<Job> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Job::getName, resources.getName());
        if (count(queryWrapper) > 0) {
            log.error("【创建岗位失败】" + "操作人id：" + SecurityUtils.getCurrentUserId() + "\t创建目标（名称）已存在 job：" + resources);
            throw new BadRequestException("已存在：" + resources.getName());
        }
        save(resources);
        log.info("【创建岗位成功】" + "操作人id：" + SecurityUtils.getCurrentUserId() + "\t创建目标job：" + resources);
    }

    /**
     * Description:
     * 更新Job实体
     *
     * @param resources: 要更新的Job实体
     * @author liuxingxing
     * @date 2020/11/24 13:13
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Job resources) {
        Job job = getById(resources.getId());
        if (ObjectUtil.isEmpty(job)) {
            log.error("【修改岗位失败】" + "操作人id：" + SecurityUtils.getCurrentUserId() + "\t修改目标job为空，目标jobId：" + resources.getId());
            throw new BadRequestException("修改失败，当前数据id不存在");
        }
        LambdaQueryWrapper<Job> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Job::getName, resources.getName());
        Job old = getOne(queryWrapper);
        if (ObjectUtil.isNotNull(old) && !old.getId().equals(resources.getId())) {
            log.error("【修改岗位失败】" + "操作人id：" + SecurityUtils.getCurrentUserId() + "\t修改目标（名称）已存在，修改目标job：" + resources + "\t存在目标job：" + old);
            throw new BadRequestException("已存在：" + resources.getName());
        }
        updateById(resources);
        log.info("【修改岗位成功】" + "操作人id：" + SecurityUtils.getCurrentUserId() + "\t修改目标job：" + resources);
    }

    /**
     * Description:
     * 删除
     *
     * @param ids: 部门id集
     * @author liuxingxing
     * @date 2020/11/24 13:16
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        removeByIds(ids);
        log.info("【删除岗位成功】" + "操作人id：" + SecurityUtils.getCurrentUserId() + "\t删除目标jobs：" + ids.toString());
    }

    /**
     * Description:
     * 验证此job是否被用户关联
     *
     * @param ids: 部门id集
     * @author liuxingxing
     * @date 2020/11/24 13:16
     **/
    @Override
    public void verification(Set<Long> ids) {
        if (userMapper.countByJobs(ids) > 0) {
            log.error("【删除岗位失败】" + "操作人id：" + SecurityUtils.getCurrentUserId() + "\t所选的岗位"+ids.toString()+"中存在用户关联，请解除关联再试！");
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
        if (StrUtil.isNotBlank(criteria.getName())) {
            // 默认使用Like匹配
            wrapper.like(Job::getName, criteria.getName());
        }
        if (ObjectUtil.isNotNull(criteria.getEnabled())) {
            wrapper.eq(Job::getEnabled, criteria.getEnabled());
        }
        if (ObjectUtil.isNotNull(criteria.getStratTime())) {
            // 如果只有开始时间，就默认从开始到现在
            wrapper.between(Job::getCreateTime, criteria.getStratTime(),
                    ObjectUtil.isNull(criteria.getEndTime()) ? LocalDateTime.now() : criteria.getEndTime());
        }
        return wrapper;
    }

}
