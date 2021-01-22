package marchsoft.modules.quartz.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marchsoft.annotation.Log;
import marchsoft.base.PageVO;
import marchsoft.exception.BadRequestException;
import marchsoft.modules.quartz.entity.QuartzJob;
import marchsoft.modules.quartz.entity.dto.JobQueryCriteria;
import marchsoft.modules.quartz.service.QuartzJobService;
import marchsoft.response.Result;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * @author lixiangxiang
 * @description 定时任务
 * @date 2021/1/14 16:19
 */
@Slf4j
@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
@Api(tags = "系统:定时任务管理")
public class QuartzJobController {

    private static final String ENTITY_NAME = "quartzJob";
    private  final QuartzJobService quartzJobService;

    @Log("新增定时任务")
    @PostMapping
    @PreAuthorize("@smpe.check('timing:add')")
    public Result<Object> create(@Validated @RequestBody QuartzJob resources){
        if(resources.getId() != null) {
            log.info("新建定时任务不能有id");
            throw new BadRequestException("A new "+ ENTITY_NAME +" cannot already have an ID");
        }
        quartzJobService.create(resources);
        return Result.success();
    }

    @ApiOperation("查询定时任务")
    @GetMapping
    @PreAuthorize("@smpe.check('timing:list')")
    public Result<Object> query(JobQueryCriteria criteria, PageVO pageVO) {
        return Result.success(quartzJobService.queryAll(criteria,pageVO));
    }


    @Log("更改定时任务状态")
    @ApiOperation("更改定时任务状态")
    @PutMapping(value = "/{id}")
    @PreAuthorize("@smpe.check('timing:edit')")
    public Result<Object> update (@PathVariable Long id) {
            quartzJobService.updateIsPause(quartzJobService.findById(id));
            return Result.success();
    }

    @Log("修改定时任务")
    @ApiOperation("修改定时任务")
    @PutMapping
    @PreAuthorize("@smpe.check('timing:edit')")
    public Result<Object> update(@Validated @RequestBody QuartzJob resources){
        quartzJobService.update(resources);
        return Result.success();
    }

    @Log("删除定时任务")
    @ApiOperation("删除定时任务")
    @DeleteMapping
    @PreAuthorize("@smpe.check('timing:del')")
    public Result<Void> delete(@RequestBody Set<Long> ids){
        quartzJobService.delete(ids);
        return Result.success();
    }

    @Log("执行定时任务")
    @ApiOperation("执行定时任务")
    @PutMapping(value = "/exec/{id}")
    @PreAuthorize("@smpe.check('timing:edit')")
    public Result<Object> execution(@PathVariable Long id) {
        quartzJobService.execution(quartzJobService.findById(id));
        return Result.success();
    }

    @ApiOperation("导出任务数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@smpe.check('timing:list')")
    public void download(HttpServletResponse response, JobQueryCriteria criteria) throws IOException {
        quartzJobService.download(quartzJobService.queryAll(criteria),response);
    }

    @ApiOperation("查询任务执行日志")
    @GetMapping(value = "/logs")
    @PreAuthorize("@smpe.check('timing:list')")
    public Result<Object> queryJobLog(JobQueryCriteria criteria, PageVO pageVO) {
        Object o = quartzJobService.queryAllLog(criteria, pageVO);
        return Result.success(o);
    }

    @ApiOperation("导出日志数据")
    @GetMapping(value = "/logs/download")
    @PreAuthorize("@smpe.check('timing:list')")
    public void downloading(HttpServletResponse response, JobQueryCriteria criteria) throws IOException {
        quartzJobService.downloadLog(response, quartzJobService.queryAllLog(criteria));
    }
}

