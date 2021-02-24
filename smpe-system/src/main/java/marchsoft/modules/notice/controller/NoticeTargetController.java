package marchsoft.modules.notice.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import marchsoft.annotation.AnonymousAccess;
import marchsoft.base.PageVO;
import marchsoft.exception.BadRequestException;
import marchsoft.modules.notice.entity.dto.NoticeTargetQueryCriteria;
import marchsoft.response.Result;
import marchsoft.utils.SecurityUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

import marchsoft.modules.notice.service.INoticeTargetService;
import marchsoft.modules.notice.entity.NoticeTarget;

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
@Api(tags = "通知：通知内容")
@RequestMapping("/api/noticeTarget")
public class NoticeTargetController {

    private final INoticeTargetService noticeTargetService;

    private static final String ENTITY_NAME = "NoticeTarget";

    @ApiOperation("查询通知内容")
    @GetMapping
    @PreAuthorize("@smpe.check('noticeTarget:list','user:list')")
    public Result<Object> query(NoticeTargetQueryCriteria criteria, PageVO pageVO) {
        log.info(StrUtil.format("【查询通知内容 /api/noticeTarget】操作人id：{}，通知内容查询条件 criteria：{}",
                SecurityUtils.getCurrentUserId(), criteria));
        return Result.success(noticeTargetService.queryAll(criteria, pageVO));
    }

    @ApiOperation("新增通知内容")
    @PostMapping
    @PreAuthorize("@smpe.check('noticeTarget:add')")
    public Result<Object> create(@RequestBody NoticeTarget resources) {
        log.info(StrUtil.format("【新增通知内容 /api/noticeTarget】操作人id：{}，通知内容：{}",
                SecurityUtils.getCurrentUserId(), resources.getContent()));
        if (ObjectUtil.isNotNull(resources.getId())) {
            log.error(StrUtil.format("【新增通知内容失败】操作人id：{}，新增通知内容实体默认id应该为空，NoticeTarget：{}", SecurityUtils.getCurrentUserId(),
                    resources));
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        noticeTargetService.create(resources);
        return Result.success();
    }

    @ApiOperation("修改通知内容")
    @PutMapping
    @PreAuthorize("@smpe.check('noticeTarget:edit')")
    public Result<Object> update(@RequestBody NoticeTarget resources) {
        log.info(StrUtil.format("【修改通知内容 /api/noticeTarget】操作人id：{}，被修改通知内容id：{}",
                SecurityUtils.getCurrentUserId(), resources.getId()));
        noticeTargetService.update(resources);
        return Result.success();
    }

    @ApiOperation("删除通知内容")
    @DeleteMapping
    @PreAuthorize("@smpe.check('noticeTarget:del')")
    public Result<Object> delete(@RequestBody Set<Long> ids) {
        log.info(StrUtil.format("【删除通知内容 /api/noticeTarget】操作人id：{}，被删除通知内容id：{}",
                SecurityUtils.getCurrentUserId(), ids.toString()));
        // 验证是否被用户关联
        noticeTargetService.verification(ids);
        noticeTargetService.delete(ids);
        return Result.success();
    }

}
