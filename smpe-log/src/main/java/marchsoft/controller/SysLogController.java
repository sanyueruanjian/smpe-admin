package marchsoft.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marchsoft.base.PageVO;
import marchsoft.entity.dto.SysLogDTO;
import marchsoft.entity.dto.SysLogQueryCriteria;
import marchsoft.response.Result;
import marchsoft.service.ISysLogService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * <p>
 * 日志 前端控制器
 * </p>
 *
 * @author RenShiWei
 * @since 2020-12-14
 */
@RequiredArgsConstructor
@RestController
@Slf4j
@Api(tags = "日志模块")
@RequestMapping("/api/logs")
public class SysLogController {
    private final ISysLogService logService;

    @ApiOperation(value = "多条件查询日志信息", notes = "根据各种条件查询，可分页 \n author：ZhangYuKun 2021/1/14")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "criteria", value = "条件"),
            @ApiImplicitParam(name = "pageVO", value = "分页条件")
    })
    @GetMapping
    @PreAuthorize("@smpe.check('logs:list')")
    public Result<IPage<SysLogDTO>> query(SysLogQueryCriteria criteria, PageVO pageVO) {
        return Result.success(logService.queryAll(criteria, pageVO.buildPage()));
    }

    @ApiOperation(value = "返回全部的日志信息", notes = " author：ZhangYuKun 2021/1/14")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "criteria", value = "条件")
    })
    @GetMapping("/all")
    @PreAuthorize("@smpe.check('logs:list')")
    public Result<List<SysLogDTO>> query(SysLogQueryCriteria criteria) {
        return Result.success(logService.queryAll(criteria));
    }

}
