package marchsoft.modules.notice.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import marchsoft.annotation.AnonymousAccess;
import marchsoft.base.PageVO;
import marchsoft.exception.BadRequestException;
import marchsoft.modules.notice.entity.Notice;
import marchsoft.modules.notice.entity.dto.NoticeQueryCriteria;
import marchsoft.modules.notice.entity.dto.NoticeSendDTO;
import marchsoft.modules.notice.entity.dto.NoticeSendQueryCriteria;
import marchsoft.response.Result;
import marchsoft.utils.SecurityUtils;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

import marchsoft.modules.notice.service.INoticeSendService;
import marchsoft.modules.notice.entity.NoticeSend;

import java.util.List;
import java.util.Set;


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
@Api(tags = "通知：通知发送")
@RequestMapping("/api/noticeSend")
public class NoticeSendController {

    private final INoticeSendService noticeSendService;

    private static final String ENTITY_NAME = "NoticeSend";

    @ApiOperation("查询已发送通知")
    @GetMapping
//    @PreAuthorize("@smpe.check('noticeSend:list')")
    @AnonymousAccess
    public Result<Object> query(NoticeSendQueryCriteria criteria, PageVO pageVO) {
//        log.info(StrUtil.format("【查询通知发送 /api/noticeSend】操作人id：{}，岗位查询条件 criteria：{}", SecurityUtils.getCurrentUserId(),
//                criteria));
        return Result.success(noticeSendService.queryAll(criteria, pageVO.buildPage()));
    }

    @ApiOperation("发送通知")
    @PostMapping
//    @PreAuthorize("@smpe.check('noticeSend:add')")
    @AnonymousAccess
    public Result<Object> send(@RequestBody NoticeSendDTO resources) {
//        log.info(StrUtil.format("【发送通知 /api/noticeSend】操作人id：{}，接收人id：{}", SecurityUtils.getCurrentUserId(),
//                resources.getUserId()));
        if (ObjectUtil.isNotNull(resources.getId())) {
            log.error(StrUtil.format("【发送通知失败】操作人id：{}，通知发送实体默认id应该为空，Notice：{}", SecurityUtils.getCurrentUserId(),
                    resources));
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        noticeSendService.send(resources);
        return Result.success();
    }

    @ApiOperation("更新通知已读")
    @PutMapping
//    @PreAuthorize("@smpe.check('noticeSend:edit')")
    @AnonymousAccess
    public Result<Object> update(@RequestBody Set<Long> ids) {
//        log.info(StrUtil.format("【更新通知已读 /api/noticeSend】操作人id：{}，被更新通知已读id：{}", SecurityUtils.getCurrentUserId(), ids.toString()));
        if (ids.size() == 0) {
            throw new BadRequestException("更新消息id集合不能为空");
        }
        noticeSendService.update(ids);
        return Result.success();
    }

    @ApiOperation("删除已发通知")
    @DeleteMapping
//    @PreAuthorize("@smpe.check('noticeSend:del')")
    @AnonymousAccess
    public Result<Object> delete(@RequestBody Set<Long> ids) {
//        log.info(StrUtil.format("【删除已发通知 /api/noticeSend】操作人id：{}，被删除已发通知id：{}", SecurityUtils.getCurrentUserId(), ids.toString()));
        // 验证是否被用户关联
        noticeSendService.delete(ids);
        return Result.success();
    }

}
