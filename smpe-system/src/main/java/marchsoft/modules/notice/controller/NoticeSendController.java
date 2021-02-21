package marchsoft.modules.notice.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import marchsoft.annotation.AnonymousAccess;
import marchsoft.base.PageVO;
import marchsoft.modules.notice.entity.dto.NoticeQueryCriteria;
import marchsoft.modules.notice.entity.dto.NoticeSendQueryCriteria;
import marchsoft.response.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

import marchsoft.modules.notice.service.INoticeSendService;
import marchsoft.modules.notice.entity.NoticeSend;


/**
* <p>
*  前端控制器
* </p>
* @author wangmingcan
* @since 2021-02-19
*/
@RequiredArgsConstructor
@RestController
@Slf4j
@Api(tags = "模块")
@RequestMapping("/api/noticeSend")
public class NoticeSendController {

    private final INoticeSendService noticeSendService;

    @ApiOperation("查询已发送通知")
    @GetMapping
//    @PreAuthorize("@smpe.check('notice:list')")
    @AnonymousAccess
    public Result<Object> query(NoticeSendQueryCriteria criteria, PageVO pageVO) {
//        log.info(StrUtil.format("【查询通知模板 /api/notice】操作人id：{}，岗位查询条件 criteria：{}", SecurityUtils.getCurrentUserId(),
//                criteria));
        return Result.success(noticeSendService.queryAll(criteria, pageVO));
    }

}
