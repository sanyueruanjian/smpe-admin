package marchsoft.modules.quartz.controller;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marchsoft.annotation.AnonymousAccess;
import marchsoft.annotation.Log;
import marchsoft.exception.BadRequestException;
import marchsoft.modules.quartz.entity.QuartzJob;
import marchsoft.modules.quartz.service.QuartzJobService;
import marchsoft.response.Result;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    @PreAuthorize("@smpe.check('quartz:add')")
    public Result<Object> create(@Validated @RequestBody QuartzJob resources){
        if(resources.getId() != null) {
            log.info("新建定时任务不能穿id");
            throw new BadRequestException("A new "+ ENTITY_NAME +" cannot already have an ID");
        }
        quartzJobService.create(resources);
        return Result.success();
    }
}
