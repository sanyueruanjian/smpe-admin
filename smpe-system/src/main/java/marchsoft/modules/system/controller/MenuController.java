package marchsoft.modules.system.controller;


import cn.hutool.core.collection.CollectionUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import marchsoft.exception.BadRequestException;
import marchsoft.modules.system.entity.Menu;
import marchsoft.modules.system.entity.dto.MenuDTO;
import marchsoft.modules.system.entity.dto.MenuQueryCriteria;
import marchsoft.modules.system.entity.vo.MenuVo;
import marchsoft.modules.system.service.IMenuService;
import marchsoft.modules.system.service.mapstruct.MenuMapStruct;
import marchsoft.response.Result;
import marchsoft.utils.SecurityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 功能描述： 系统菜单
 *
 * @author Wangmingcan
 * @since 2020-08-17
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "系统：菜单管理")
@RequestMapping("/api/menus")
public class MenuController {

    private final IMenuService menuService;
    private final MenuMapStruct menuMapStruct;
    private static final String ENTITY_NAME = "menu";

    @ApiOperation("导出菜单数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@smpe.check('menu:list')")
    public void download(HttpServletResponse response, MenuQueryCriteria criteria) throws Exception {
        menuService.download(menuService.queryAll(criteria, false), response);
    }

    @GetMapping(value = "/build")
    @ApiOperation("获取前端所需菜单")
    public Result<List<MenuVo>> buildMenus() {
        List<MenuDTO> menuDtoList = menuService.findMenuByUserId(SecurityUtils.getCurrentUserId());
        List<MenuDTO> menuDtos = menuService.buildTree(menuDtoList);
        return Result.success(menuService.buildMenus(menuDtos));
    }

    @ApiOperation("返回全部的菜单")
    @GetMapping(value = "/lazy")
    @PreAuthorize("@smpe.check('menu:list','roles:list')")
    public Result<List<MenuDTO>> query(@RequestParam Long pid) {
        return Result.success(menuService.getMenus(pid));
    }

    @ApiOperation("根据菜单ID返回所有子节点ID，包含自身ID")
    @GetMapping(value = "/child")
    @PreAuthorize("@smpe.check('menu:list','roles:list')")
    public Result<Set<Long>> child(@RequestParam Long id){
        Set<Menu> menuSet = new HashSet<>();
        List<MenuDTO> menuList = menuService.getMenus(id);
        menuSet.add(menuService.findOne(id));
        menuSet = menuService.getChildMenus(menuMapStruct.toEntity(menuList), menuSet);
        Set<Long> ids = menuSet.stream().map(Menu::getId).collect(Collectors.toSet());
        return Result.success(ids);
    }

    @ApiOperation("查询菜单")
    @GetMapping
    @PreAuthorize("@smpe.check('menu:list')")
    public Result<List<MenuDTO>> query(MenuQueryCriteria criteria) throws Exception {
        return Result.success(menuService.queryAll(criteria, true));
    }

    @ApiOperation("查询菜单:根据ID获取同级与上级数据")
    @PostMapping("/superior")
    @PreAuthorize("@smpe.check('menu:list')")
    public Result<List<MenuDTO>> getSuperior(@RequestBody List<Long> ids) {
        Set<MenuDTO> menuDtoS = new LinkedHashSet<>();
        if (CollectionUtil.isNotEmpty(ids)) {
            for (Long id : ids) {
                MenuDTO menuDto = menuService.findMenuById(id);
                menuDtoS.addAll(menuService.getSuperior(menuDto, new ArrayList<>()));
            }
            return Result.success(menuService.buildTree(new ArrayList<>(menuDtoS)));
        }
        return Result.success(menuService.getMenus(0L));
    }

    @ApiOperation("新增菜单")
    @PostMapping
    @PreAuthorize("@smpe.check('menu:add')")
    public Result<Void> create(@RequestBody Menu menu) {
        if (menu.getId() != null) {
            throw new BadRequestException("创建 " + ENTITY_NAME + " 失败，该菜单已存在");
        }
        menuService.create(menu);
        return Result.success();
    }

    @ApiOperation("修改菜单")
    @PutMapping
    @PreAuthorize("@smpe.check('menu:edit')")
    public Result<Void> update(@RequestBody Menu menu) {
        menuService.updateMenu(menu);
        return Result.success();
    }

    @ApiOperation("删除菜单")
    @DeleteMapping
    @PreAuthorize("@smpe.check('menu:del')")
    public Result<Void> delete(@RequestBody Set<Long> ids) {
        Set<Menu> menuSet = new HashSet<>();
        for (Long id : ids) {
            List<MenuDTO> menuList = menuService.getMenus(id);
            menuSet.add(menuService.findOne(id));
            menuSet = menuService.getDeleteMenus(menuMapStruct.toEntity(menuList), menuSet);
        }
        menuService.delete(menuSet);
        return Result.success();
    }
}

