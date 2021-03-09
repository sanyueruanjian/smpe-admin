package marchsoft.modules.notice.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import marchsoft.annotation.AnonymousAccess;
import marchsoft.base.PageVO;
import marchsoft.exception.BadRequestException;
import marchsoft.modules.notice.entity.NoticeTemplate;
import marchsoft.modules.notice.entity.dto.NoticeTemplateQueryCriteria;
import marchsoft.response.Result;
import marchsoft.utils.SecurityUtils;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

import marchsoft.modules.notice.service.INoticeTemplateService;

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
@Api(tags = "通知：通知模板")
@RequestMapping("/api/noticeTemplate")
public class NoticeTemplateController {

    private final INoticeTemplateService noticeTemplateService;

    private static final String ENTITY_NAME = "NoticeTemplate";

    @ApiOperation("查询通知模板")
    @GetMapping
//    @PreAuthorize("@smpe.check('noticeTemplate:list','user:list')")
    @AnonymousAccess
    public Result<Object> query(NoticeTemplateQueryCriteria criteria, PageVO pageVO) {
        log.info(StrUtil.format("【查询通知模板 /api/noticeTemplate】操作人id：{}，通知模板查询条件 criteria：{}", SecurityUtils.getCurrentUserId(),
                criteria));
        return Result.success(noticeTemplateService.queryAll(criteria, pageVO));
    }

    @ApiOperation("新增通知模板")
    @PostMapping
//    @PreAuthorize("@smpe.check('noticeTemplate:add')")
    @AnonymousAccess
    public Result<Object> create(@RequestBody NoticeTemplate resources) {
        log.info(StrUtil.format("【新增通知模板 /api/noticeTemplate】操作人id：{}，通知模板名称：{}", SecurityUtils.getCurrentUserId(),
                resources.getTitle()));
        if (ObjectUtil.isNotNull(resources.getId())) {
            log.error(StrUtil.format("【新增通知模板失败】操作人id：{}，新增通知模板实体默认id应该为空，NoticeTemplate：{}", SecurityUtils.getCurrentUserId(),
                    resources));
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        noticeTemplateService.create(resources);
        return Result.success();
    }

    @ApiOperation("修改通知模板")
    @PutMapping
//    @PreAuthorize("@smpe.check('noticeTemplate:edit')")
    @AnonymousAccess
    public Result<Object> update(@RequestBody NoticeTemplate resources) {
        log.info(StrUtil.format("【修改通知模板 /api/noticeTemplate】操作人id：{}，被修改通知模板id：{}", SecurityUtils.getCurrentUserId(),
                resources.getId()));
        noticeTemplateService.update(resources);
        return Result.success();
    }

    @ApiOperation("删除通知模板")
    @DeleteMapping
//    @PreAuthorize("@smpe.check('noticeTemplate:del')")
    @AnonymousAccess
    public Result<Object> delete(@RequestBody Set<Long> ids) {
        log.info(StrUtil.format("【删除通知模板 /api/noticeTemplate】操作人id：{}，被删除通知模板id：{}", SecurityUtils.getCurrentUserId(), ids.toString()));
        // 验证是否被用户关联
        noticeTemplateService.verification(ids);
        noticeTemplateService.delete(ids);
        return Result.success();
    }

}
