package marchsoft.modules.system.controller;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marchsoft.base.PageVO;
import marchsoft.exception.BadRequestException;
import marchsoft.modules.system.entity.Dept;
import marchsoft.modules.system.entity.dto.DeptDTO;
import marchsoft.modules.system.entity.dto.DeptQueryCriteria;
import marchsoft.modules.system.service.IDeptService;
import marchsoft.response.Result;
import marchsoft.utils.SecurityUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * <p>
 * 部门 前端控制器
 * </p>
 *
 * @author Wangmingcan
 * @since 2020-08-17
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dept")
@Api(tags = "系统：部门管理")
public class DeptController {

    private final IDeptService deptService;

    private static final String ENTITY_NAME = "dept";

    @ApiOperation("导出部门数据")
    @GetMapping("/download")
    @PreAuthorize("@smpe.check('dept:list')")
    public void download(HttpServletResponse response, DeptQueryCriteria criteria) throws Exception {
        log.info("【导出部门数据 /api/dept/download】操作人userId:" + SecurityUtils.getCurrentUserId() + "; 部门查询条件 criteria= " + criteria);
        deptService.download(deptService.queryAll(criteria, false), response);
    }

    @ApiOperation("查询部门")
    @GetMapping
    @PreAuthorize("@smpe.check('user:list','dept:list')")
    public Result<Object> query(DeptQueryCriteria criteria, PageVO pageVO) {
        log.info("【查询部门 /api/dept】操作人userId:" + SecurityUtils.getCurrentUserId() + "; 部门查询条件 criteria= " + criteria + "; 分页pageVo= " + pageVO);
        pageVO.setSize(-1);
        return Result.success(deptService.queryAll(criteria, pageVO, true));
    }

    @ApiOperation("查询部门:根据ID获取同级与上级数据")
    @PostMapping("/superior")
    @PreAuthorize("@smpe.check('user:list','dept:list')")
    public Result<Object> getSuperior(@RequestBody List<Long> ids) {
        log.info("【查询部门:根据ID获取同级与上级数据 /api/dept/superior】操作人userId:" + SecurityUtils.getCurrentUserId() + "; 查询部门id集 " +
                "ids= " + ids.toString());
        Set<DeptDTO> deptDtos = new LinkedHashSet<>();
        for (Long id : ids) {
            DeptDTO deptDto = deptService.findById(id);
            List<DeptDTO> depts = deptService.getSuperior(deptDto, new ArrayList<>());
            deptDtos.addAll(depts);
        }
        return Result.success(deptService.buildTree(new ArrayList<>(deptDtos)));
    }

    @ApiOperation("新增部门")
    @PostMapping
    @PreAuthorize("@smpe.check('dept:add')")
    public Result<Object> create(@RequestBody Dept dept) {
        log.info("【新增部门 /api/dept】操作人userId:" + SecurityUtils.getCurrentUserId() + "; 新增部门实体= " + dept);
        if (ObjectUtil.isNotNull(dept.getId())) {
            log.error("【新增部门失败】操作人userId:" + SecurityUtils.getCurrentUserId() + "; 新增部门实体默认id应该为空，dept：" + dept);
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        deptService.create(dept);
        return Result.success();
    }

    @ApiOperation("修改部门")
    @PutMapping
    @PreAuthorize("@smpe.check('dept:edit')")
    public Result<Object> update(@RequestBody Dept dept) {
        log.info("【修改部门 /api/dept】操作人userId:" + SecurityUtils.getCurrentUserId() + "; 修改部门id= " + dept.getId());
        deptService.updateDept(dept);
        return Result.success();
    }

    @ApiOperation("删除部门")
    @DeleteMapping
    @PreAuthorize("@smpe.check('dept:del')")
    public Result<Object> delete(@RequestBody Set<Long> ids) {
        log.info("【删除部门 /api/dept】操作人userId:" + SecurityUtils.getCurrentUserId() + "; 被删除部门id集= " + ids.toString());
        // 要删除的dept集合
        Set<DeptDTO> deptDtoS = new HashSet<>();

        for (Long id : ids) {
            // 获取此部门的子部门
            List<Dept> deptList = deptService.findByPid(id);
            deptDtoS.add(deptService.findById(id));
            if (CollectionUtil.isNotEmpty(deptList)) {
                deptDtoS = deptService.getDeleteDepts(deptList, deptDtoS);
            }
        }
        // 验证是否被角色或用户关联
        deptService.verification(deptDtoS);
        deptService.deleteDept(deptDtoS);
        return Result.success();

    }
}

