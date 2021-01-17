package marchsoft.controller;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marchsoft.annotation.Log;
import marchsoft.base.PageVO;
import marchsoft.entity.dto.SysLogDTO;
import marchsoft.entity.dto.SysLogQueryCriteria;
import marchsoft.enums.SysLogEnum;
import marchsoft.response.Result;
import marchsoft.service.ISysLogService;
import marchsoft.utils.SecurityUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;


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

    @Log("导出数据")
    @ApiOperation(value = "导出日志", notes = " author：ZhangYuKun 2021/1/15")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "response", value = "Servlet响应"),
            @ApiImplicitParam(name = "criteria", value = "条件")
    })
    @GetMapping(value = "/download")
    @PreAuthorize("@smpe.check()")
    public void downLoad(HttpServletResponse response, SysLogQueryCriteria criteria) throws Exception {
        log.info(StrUtil.format("【导出日志数据 /api/logs/download】操作人userId:{}; 日志查询条件 criteria:{}"), SecurityUtils.getCurrentUserId(), criteria);
        criteria.setLogType(Convert.toStr(SysLogEnum.INFO.getLogType()));
        logService.download(logService.queryAll(criteria), response);
    }

    @Log("导出错误数据")
    @ApiOperation(value = "导出错误日志", notes = " author：ZhangYuKun 2021/1/15")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "response", value = "Servlet响应"),
            @ApiImplicitParam(name = "criteria", value = "条件")
    })
    @GetMapping(value = "/error/download")
    @PreAuthorize("@smpe.check()")
    public void downloadErrorLog(HttpServletResponse response, SysLogQueryCriteria criteria) throws Exception {
        log.info(StrUtil.format("【导出错误日志数据 /api/logs/error/download】操作人userId:{}; 日志查询条件 criteria:{}"), SecurityUtils.getCurrentUserId(), criteria);
        criteria.setLogType(Convert.toStr(SysLogEnum.ERROR.getLogType()));
        logService.download(logService.queryAll(criteria), response);
    }

    @ApiOperation(value = "条件查询日志信息", notes = "根据各种条件查询，可分页 \n author：ZhangYuKun 2021/1/14")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "criteria", value = "条件"),
            @ApiImplicitParam(name = "pageVO", value = "分页条件")
    })
    @GetMapping
    @PreAuthorize("@smpe.check()")
    public Result<IPage<SysLogDTO>> query(SysLogQueryCriteria criteria, PageVO pageVO) {
        log.info(StrUtil.format("【查询日志数据 /api/logs】操作人userId:{}; 日志查询条件 criteria:{}; 分页pageVo:{}"), SecurityUtils.getCurrentUserId(), criteria, pageVO);
        criteria.setLogType(Convert.toStr(SysLogEnum.INFO.getLogType()));
        return Result.success(logService.queryAll(criteria, pageVO.buildPage()));
    }

    @ApiOperation(value = "条件查询错误日志", notes = "根据各种条件查询，可分页 \n author：ZhangYuKun 2021/1/15")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "criteria", value = "条件"),
            @ApiImplicitParam(name = "pageVO", value = "分页条件")
    })
    @GetMapping(value = "/error")
    @PreAuthorize("@smpe.check()")
    public Result<IPage<SysLogDTO>> queryErrorLog(SysLogQueryCriteria criteria, PageVO pageVO) {
        log.info(StrUtil.format("【查询日志数据 /api/logs/error】操作人userId:{}; 日志查询条件 criteria:{}; 分页pageVo:{}"), SecurityUtils.getCurrentUserId(), criteria, pageVO);
        criteria.setLogType(Convert.toStr(SysLogEnum.ERROR.getLogType()));
        return Result.success(logService.queryAll(criteria, pageVO.buildPage()));
    }

    @ApiOperation(value = "查询错误日志详情", notes = " author：ZhangYuKun 2021/1/15")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "异常日志id")
    })
    @GetMapping("/error/{id}")
    @PreAuthorize("@smpe.check()")
    public Result<SysLogDTO> queryErrorLogs(@PathVariable Long id) {
        log.info(StrUtil.format("【查询日志数据 /api/logs/error/{id}】操作人userId:{};"), SecurityUtils.getCurrentUserId());
        return Result.success(logService.findByErrDetail(id));
    }

    @Log("删除所有ERROR日志")
    @ApiOperation(value = "删除所有ERROR日志", notes = " author：ZhangYuKun 18:37 2021/1/15")
    @DeleteMapping(value = "/del/error")
    @PreAuthorize("@smpe.check()")
    public Result<Object> delAllErrorLog() {
        log.info(StrUtil.format("【查询日志数据 /api/logs/del/error】操作人userId:{};"), SecurityUtils.getCurrentUserId());
        logService.delAllByError();
        return Result.success();
    }

    @Log("删除所有INFO日志")
    @ApiOperation(value = "删除所有INFO日志", notes = " author：ZhangYuKun 18:39 2021/1/15")
    @DeleteMapping(value = "/del/info")
    @PreAuthorize("@smpe.check()")
    public Result<Object> delAllInfoLog() {
        log.info(StrUtil.format("【查询日志数据 /api/logs/del/info】操作人userId:{};"), SecurityUtils.getCurrentUserId());
        logService.delAllByInfo();
        return Result.success();
    }

}
