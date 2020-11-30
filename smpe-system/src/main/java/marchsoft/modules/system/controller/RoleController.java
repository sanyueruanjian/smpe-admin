package marchsoft.modules.system.controller;


import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marchsoft.base.PageVO;
import marchsoft.enums.ResultEnum;
import marchsoft.exception.BadRequestException;
import marchsoft.modules.system.entity.dto.*;
import marchsoft.modules.system.service.IRoleService;
import marchsoft.response.Result;
import marchsoft.utils.SecurityUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author Wangmingcan
 * @since 2020-08-17
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@Api(tags = "系统：角色管理")
@RequestMapping("/api/roles")
public class RoleController {

    private final IRoleService roleService;

    @ApiOperation(value = "获取单个角色", notes = " \n author：RenShiWei 2020/11/26")
    @ApiImplicitParam(name = "id", value = "角色id")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@smpe.check('roles:list')")
    public Result<RoleDTO> findById(@PathVariable Long id) {
        return Result.success(roleService.findById(id));
    }

    @ApiOperation(value = "导出角色数据", notes = " \n author：RenShiWei 2020/11/26")
    @ApiImplicitParam(name = "criteria", value = "条件")
    @GetMapping(value = "/download")
    @PreAuthorize("@smpe.check('role:list')")
    public void download(HttpServletResponse response, RoleQueryCriteria criteria) throws IOException {
        roleService.download(roleService.findRoleDetailAll(criteria), response);
    }

    @ApiOperation(value = "返回全部的角色", notes = "默认按照角色的level升序 \n author：RenShiWei 2020/11/26")
    @GetMapping(value = "/all")
    @PreAuthorize("@smpe.check('roles:list','user:add','user:edit')")
    public Result<List<RoleDTO>> query() {
        return Result.success(roleService.findRoleDetailAll());
    }

    @ApiOperation(value = "多条件查询角色", notes = "根据各种条件查询，可分页 \n author：RenShiWei 2020/11/26")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "criteria", value = "条件"),
            @ApiImplicitParam(name = "pageVO", value = "分页条件")
    })
    @GetMapping
    @PreAuthorize("@smpe.check('roles:list')")
    public Result<IPage<RoleDTO>> query(RoleQueryCriteria criteria, PageVO pageVO) {
        return Result.success(roleService.findRoleDetailAll(criteria, pageVO.buildPage()));
    }

    @ApiOperation("获取当前登录用户级别")
    @GetMapping(value = "/level")
    public Result<Dict> getLevel() {
        return Result.success(Dict.create().set("level", getLevels(null)));
    }

    @ApiOperation(value = "新增角色", notes = " \n author：RenShiWei 2020/11/26")
    @ApiImplicitParam(name = "roleInsertOrUpdateDTO", value = "新增角色参数列表")
    @PostMapping
    @PreAuthorize("@smpe.check('roles:add')")
    public Result<Void> create(@RequestBody RoleInsertOrUpdateDTO roleInsertOrUpdateDTO) {
        if (roleInsertOrUpdateDTO.getId() != null) {
            log.error("【新增角色失败】新增角色不应该传入角色id");
            throw new BadRequestException("角色id已存在");
        }
        getLevels(roleInsertOrUpdateDTO.getLevel());
        roleService.insertRole(roleInsertOrUpdateDTO);
        return Result.success();
    }

    @ApiOperation(value = "修改角色", notes = " \n author：RenShiWei 2020/11/26")
    @ApiImplicitParam(name = "roleInsertOrUpdateDTO", value = "修改角色参数列表")
    @PutMapping
    @PreAuthorize("@smpe.check('roles:edit')")
    public Result<Void> update(@RequestBody RoleInsertOrUpdateDTO roleInsertOrUpdateDTO) {
        getLevels(roleInsertOrUpdateDTO.getLevel());
        roleService.updateRole(roleInsertOrUpdateDTO);
        return Result.success();
    }

    @ApiOperation(value = "修改角色菜单", notes = " \n author：RenShiWei 2020/11/27")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "角色id"),
            @ApiImplicitParam(name = "menus", value = "菜单id集合")
    })
    @PutMapping(value = "/menu")
    @PreAuthorize("@smpe.check('roles:edit')")
    public Result<Void> updateMenu(@RequestBody RoleMenuUpdateDTO roleMenuUpdateDTO) {
        RoleDTO role = roleService.findById(roleMenuUpdateDTO.getId());
        getLevels(role.getLevel());
        roleService.updateMenu(roleMenuUpdateDTO.getId(), roleMenuUpdateDTO.getMenus());
        return Result.success();
    }

    @ApiOperation(value = "删除角色", notes = " \n author：RenShiWei 2020/11/27")
    @ApiImplicitParam(name = "ids", value = "角色id集合")
    @DeleteMapping
    @PreAuthorize("@smpe.check('roles:del')")
    public Result<Void> delete(@RequestBody Set<Long> ids) {
        for (Long id : ids) {
            RoleDTO role = roleService.findById(id);
            getLevels(role.getLevel());
        }
        // 验证是否被用户关联
        if (roleService.isRolesWithUser(ids)) {
            throw new BadRequestException("所选角色存在用户关联，请解除关联再试！");
        }
        roleService.delete(ids);
        return Result.success();
    }

    /**
     * description 获取当前用户的角色级别，并进行角色级别检查
     *
     * @param level 操作角色的level
     * @return 当前角色的最大角色level（越小角色级别越大）
     * @author Wangmingcan
     * @date 2020-08-28 18:47
     */
    private int getLevels(Integer level) {
        List<Integer> levelList =
                roleService.findRoleByUserId(SecurityUtils.getCurrentUserId()).stream().map(RoleSmallDTO::getLevel).collect(Collectors.toList());
        int min = Collections.min(levelList);
        if (level != null) {
            if (level < min) {
                log.error("【权限不足】" + "您的角色级别：" + min + "，低于操作的角色级别：" + level);
                throw new BadRequestException(ResultEnum.IDENTITY_NOT_POW);
            }
        }
        return min;
    }

}

