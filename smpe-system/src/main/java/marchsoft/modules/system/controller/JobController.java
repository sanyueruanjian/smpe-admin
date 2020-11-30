package marchsoft.modules.system.controller;


import cn.hutool.core.util.ObjectUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marchsoft.base.PageVO;
import marchsoft.exception.BadRequestException;
import marchsoft.modules.system.entity.Job;
import marchsoft.modules.system.entity.dto.JobQueryCriteria;
import marchsoft.modules.system.service.IJobService;
import marchsoft.response.Result;
import marchsoft.utils.SecurityUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * <p>
 * 岗位 前端控制器
 * </p>
 *
 * @author Wangmingcan
 * @since 2020-08-17
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "系统：岗位管理")
@RequestMapping("/api/job")
@Slf4j
public class JobController {

    private final IJobService jobService;
    private static final String ENTITY_NAME = "job";

    @ApiOperation("导出岗位数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@smpe.check('job:list')")
    public void download(HttpServletResponse response, JobQueryCriteria criteria) throws IOException {
        log.info("【导出岗位数据 /api/job/download】操作人userId:" + SecurityUtils.getCurrentUserId() + "; 岗位查询条件 criteria= " + criteria);
        jobService.download(jobService.queryAll(criteria), response);
    }

    @ApiOperation("查询岗位")
    @GetMapping
    @PreAuthorize("@smpe.check('job:list','user:list')")
    public Result<Object> query(JobQueryCriteria criteria, PageVO pageVO) {
        log.info("【查询岗位 /api/job】操作人userId:" + SecurityUtils.getCurrentUserId() + "; 岗位查询条件 criteria= " + criteria);
        return Result.success(jobService.queryAll(criteria, pageVO));
    }

    @ApiOperation("新增岗位")
    @PostMapping
    @PreAuthorize("@smpe.check('job:add')")
    public Result<Object> create(@RequestBody Job resources) {
        log.info("【新增岗位 /api/job】操作人userId:" + SecurityUtils.getCurrentUserId() + "; 岗位名称= " + resources.getName());
        if (ObjectUtil.isNotNull(resources.getId())) {
            log.error("【新增岗位失败】操作人userId:" + SecurityUtils.getCurrentUserId() + "; 新增岗位实体默认id应该为空，Job：" + resources);
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        jobService.create(resources);
        return Result.success();
    }

    @ApiOperation("修改岗位")
    @PutMapping
    @PreAuthorize("@smpe.check('job:edit')")
    public Result<Object> update(@RequestBody Job resources) {
        log.info("【修改岗位 /api/job】操作人userId:" + SecurityUtils.getCurrentUserId() + "; 被修改岗位id= " + resources.getId());
        jobService.update(resources);
        return Result.success();
    }

    @ApiOperation("删除岗位")
    @DeleteMapping
    @PreAuthorize("@smpe.check('job:del')")
    public Result<Object> delete(@RequestBody Set<Long> ids) {
        log.info("【删除岗位 /api/job】操作人userId:" + SecurityUtils.getCurrentUserId() + "; 被删除岗位id= " + ids.toString());
        // 验证是否被用户关联
        jobService.verification(ids);
        jobService.delete(ids);
        return Result.success();
    }
}

