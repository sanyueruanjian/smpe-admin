package marchsoft.modules.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marchsoft.enums.ResultEnum;
import marchsoft.exception.BadRequestException;
import marchsoft.modules.system.entity.Menu;
import marchsoft.modules.system.entity.dto.MenuDTO;
import marchsoft.modules.system.entity.dto.MenuQueryCriteria;
import marchsoft.modules.system.entity.dto.RoleSmallDTO;
import marchsoft.modules.system.entity.vo.MenuMetaVo;
import marchsoft.modules.system.entity.vo.MenuVo;
import marchsoft.modules.system.mapper.MenuMapper;
import marchsoft.modules.system.service.IMenuService;
import marchsoft.modules.system.service.IRoleService;
import marchsoft.modules.system.service.mapstruct.MenuMapStruct;
import marchsoft.utils.FileUtils;
import marchsoft.utils.SecurityUtils;
import marchsoft.utils.StringUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统菜单 服务实现类
 * </p>
 *
 * @author Wangmingcan
 * @since 2020-08-17
 */
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "menu")
@Slf4j
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    private final MenuMapper menuMapper;

    private final IRoleService roleService;

    private final MenuMapStruct menuMapStruct;

    /**
     * 功能描述：根据角色id获取
     *
     * @param id 角色id
     * @return Menu集合
     * @author Jiaoqianjin
     * Date: 2020/11/26 17:05
     */
    @Override
    public Set<Menu> findByRoleId(Long id) {
        return menuMapper.findByRoleId(id);
    }

    /**
     * 功能描述：根据菜单id 查询菜单
     *
     * @param id 菜单id
     * @return /
     * @author Jiaoqianjin
     * Date: 2020/11/26 17:24
     */
    @Override
    public MenuDTO findMenuById(Long id) {
        Menu menu = menuMapper.getMenuById(id);
        if (ObjectUtil.isEmpty(menu)) {
            throw new BadRequestException(ResultEnum.DATA_NOT_FOUND);
        }
        return menuMapStruct.toDto(menu);
    }

    /**
     * 功能描述：根据当前用户id获取菜单
     *
     * @param currentUserId 当前用户id
     * @return /
     * @author Jiaoqianjin
     * Date: 2020/11/26 17:06
     */
    @Override
//    @Cacheable(key = "'user:' + #p0")
    public List<MenuDTO> findMenuByUserId(Long currentUserId) {
        List<RoleSmallDTO> roles = roleService.findRoleByUserId(currentUserId);
        Set<Long> roleIds = roles.stream().map(RoleSmallDTO::getId).collect(Collectors.toSet());
        LinkedHashSet<Menu> menus = menuMapper.findByRoleIdsAndTypeNot(roleIds, 2);
        return menus.stream().map(menuMapStruct::toDto).collect(Collectors.toList());
    }

    /**
     * 功能描述： 查询全部
     *
     * @param criteria 请求参数
     * @return isQuery 是否查询子菜单
     * @author jiaoqianjin
     * Date: 2020/11/23 21:23
     */
    @Override
    public List<MenuDTO> queryAll(MenuQueryCriteria criteria, Boolean isQuery) {
        LambdaQueryWrapper<Menu> menuDtoQueryWrapper = new LambdaQueryWrapper<>();
        if (isQuery) {
            menuDtoQueryWrapper.eq(Menu::getPid, criteria.getPid());
        }
        // 判断是否添加菜单标题or菜单组件or菜单权限模糊查询条件
        if (StrUtil.isNotEmpty(criteria.getBlurry())) {
            menuDtoQueryWrapper.like(Menu::getTitle, criteria.getBlurry()).or()
                    .like(Menu::getComponent, criteria.getBlurry()).or()
                    .like(Menu::getPermission, criteria.getBlurry());
        }
        // 判断是否添加创建时间范围条件
        if (ObjectUtil.isNotNull(criteria.getStartTime()) && ObjectUtil.isNotNull(criteria.getEndTime())) {
            menuDtoQueryWrapper.between(Menu::getCreateTime, criteria.getStartTime(), criteria.getEndTime());
        }
        List<Menu> menus = menuMapper.queryAll(menuDtoQueryWrapper);
        return menuMapStruct.toDto(menus);
    }

    /**
     * 功能描述：懒加载菜单数据
     *
     * @param pid 父级id
     * @return /
     * @author Jiaoqianjin
     * Date: 2020/11/26 17:15
     */
    @Override
//    @Cacheable(key = "'pid:' + #p0")
    public List<MenuDTO> getMenus(Long pid) {
        List<Menu> menus;
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getPid, pid);
        menus = menuMapper.getMenusByPid(queryWrapper);
        return menuMapStruct.toDto(menus);
    }

    /**
     * 功能描述：导出菜单数据
     *
     * @param menuDtoList 全部菜单数据
     * @param response    response
     * @throws IOException /
     * @author Jiaoqianjin
     * Date: 2020/11/26 17:10
     */
    @Override
    public void download(List<MenuDTO> menuDtoList, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (MenuDTO menuDTO : menuDtoList) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("菜单标题", menuDTO.getTitle());
            map.put("菜单类型", menuDTO.getType() == null ? "目录" : menuDTO.getType() == 1 ? "菜单" : "按钮");
            map.put("权限标识", menuDTO.getPermission());
            map.put("外链菜单", menuDTO.getIFrame() ? "是" : "否");
            map.put("菜单可见", menuDTO.getHidden() ? "否" : "是");
            map.put("是否缓存", menuDTO.getCache() ? "是" : "否");
            map.put("创建日期", menuDTO.getCreateTime() == null ? null :
                    LocalDateTimeUtil.format(menuDTO.getCreateTime(), DatePattern.NORM_DATETIME_FORMATTER));
            list.add(map);
        }
        FileUtils.downloadExcel(list, response);
    }

    /**
     * description 构建菜单树
     *
     * @param menuDtos 菜单DTO
     * @return List<MenuDto>
     * @author Wangmingcan
     * @date 2020-08-23 10:43
     */
    @Override
    public List<MenuDTO> buildTree(List<MenuDTO> menuDtos) {
        List<MenuDTO> trees = new ArrayList<>();
        Set<Long> ids = new HashSet<>();
        for (MenuDTO menuDTO : menuDtos) {
            if (menuDTO.getPid().equals(0L)) {
                trees.add(menuDTO);
            }
            for (MenuDTO it : menuDtos) {
                if (menuDTO.getId().equals(it.getPid())) {
                    if (menuDTO.getChildren() == null) {
                        menuDTO.setChildren(new ArrayList<>());
                    }
                    menuDTO.getChildren().add(it);
                    ids.add(it.getId());
                }
            }
        }
        if (trees.size() == 0) {
            trees = menuDtos.stream().filter(s -> ! ids.contains(s.getId())).collect(Collectors.toList());
        }
        return trees;
    }

    /**
     * 功能描述：返回前端所需菜单
     *
     * @param menuDtos menuDto集合
     * @return /
     * @author Jiaoqianjin
     * Date: 2020/11/26 17:09
     */
    @Override
    public List<MenuVo> buildMenus(List<MenuDTO> menuDtos) {
        List<MenuVo> list = new LinkedList<>();
        menuDtos.forEach(menuDTO -> {
                    if (menuDTO != null) {
                        List<MenuDTO> menuDtoList = menuDTO.getChildren();
                        MenuVo menuVo = new MenuVo();
                        menuVo.setName(ObjectUtil.isNotEmpty(menuDTO.getComponentName()) ?
                                menuDTO.getComponentName() : menuDTO.getTitle());
                        // 一级目录需要加斜杠，不然会报警告
                        menuVo.setPath(menuDTO.getPid().equals(0L) ? "/" + menuDTO.getPath() : menuDTO.getPath());
                        menuVo.setHidden(menuDTO.getHidden());
                        // 如果不是外链
                        if (! menuDTO.getIFrame()) {
                            if (menuDTO.getPid().equals(0L)) {
                                menuVo.setComponent(StrUtil.isEmpty(menuDTO.getComponent()) ? "Layout" :
                                        menuDTO.getComponent());
                            } else if (! StrUtil.isEmpty(menuDTO.getComponent())) {
                                menuVo.setComponent(menuDTO.getComponent());
                            }
                        }
                        menuVo.setMeta(new MenuMetaVo(menuDTO.getTitle(), menuDTO.getIcon(), ! menuDTO.getCache()));
                        if (menuDtoList != null && menuDtoList.size() != 0) {
                            menuVo.setAlwaysShow(true);
                            menuVo.setRedirect("noredirect");
                            menuVo.setChildren(buildMenus(menuDtoList));
                            // 处理是一级菜单并且没有子菜单的情况
                        } else if (menuDTO.getPid().equals(0L)) {
                            MenuVo menuVo1 = new MenuVo();
                            menuVo1.setMeta(menuVo.getMeta());
                            // 非外链
                            if (! menuDTO.getIFrame()) {
                                menuVo1.setPath("index");
                                menuVo1.setName(menuVo.getName());
                                menuVo1.setComponent(menuVo.getComponent());
                            } else {
                                menuVo1.setPath(menuDTO.getPath());
                            }
                            menuVo.setName(null);
                            menuVo.setMeta(null);
                            menuVo.setComponent("Layout");
                            List<MenuVo> list1 = new ArrayList<>();
                            list1.add(menuVo1);
                            menuVo.setChildren(list1);
                        }
                        list.add(menuVo);
                    }
                }
        );
        return list;
    }

    /**
     * 功能描述：根据当前菜单信息获取上级数据
     *
     * @param menuDto 本机菜单
     * @param menus   上级菜单数据
     * @return /
     * @author Jiaoqianjin
     * Date: 2020/11/26 17:25
     */
    @Override
    public List<MenuDTO> getSuperior(MenuDTO menuDto, List<Menu> menus) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        if (menuDto.getPid().equals(0L)) {
            queryWrapper.eq(Menu::getPid, 0L);
            menus.addAll(list(queryWrapper));
            return menuMapStruct.toDto(menus);
        }
        queryWrapper.eq(Menu::getPid, menuDto.getPid());
        menus.addAll(list(queryWrapper));
        return getSuperior(findMenuById(menuDto.getPid()), menus);
    }

    /**
     * 功能描述：添加菜单
     *
     * @param menu 添加的菜单数据
     * @author Jiaoqianjin
     * Date: 2020/11/26 17:26
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(Menu menu) {
        LambdaQueryWrapper<Menu> titleQueryWrapper = new LambdaQueryWrapper<>();
        titleQueryWrapper.eq(Menu::getTitle, menu.getTitle());
        // MODIFY:@Jiaoqianjin 2020/11/26 description: list() --> count()
        if (this.count(titleQueryWrapper) > 0) {
            log.error("【新增菜单失败】" + "操作人id：" + SecurityUtils.getCurrentUserId() + "新增菜单标题已存在：" + menu.getTitle());
            throw new BadRequestException("菜单标题已存在");
        }
        LambdaQueryWrapper<Menu> componentQuery = new LambdaQueryWrapper<>();
        componentQuery.eq(Menu::getName, menu.getName());
        // MODIFY:@Jiaoqianjin 2020/11/26 description: list() --> count()
        if (StringUtils.isNotBlank(menu.getName())) {
            if (this.count(componentQuery) > 0) {
                log.error("【新增菜单失败】" + "操作人id：" + SecurityUtils.getCurrentUserId() + "新增组件名已存在：" + menu.getTitle());
                throw new BadRequestException("组件名称已存在");
            }
        }
//        if (menu.getPid().equals(0L)) {
//            menu.setPid(null);
//        }
        if (menu.getIFrame()) {
            String http = "http://", https = "https://";
            if (! (menu.getPath().toLowerCase().startsWith(http) || menu.getPath().toLowerCase().startsWith(https))) {
                log.error("【新增菜单失败】" + "操作人id：" + SecurityUtils.getCurrentUserId() + "外链必须以http://或者https://开头:" + menu.getIFrame());
                throw new BadRequestException("外链必须以http://或者https://开头");
            }
        }
        boolean insert = menu.insert();
        if (! insert) {
            log.error("【新增菜单失败】" + "操作人id：" + SecurityUtils.getCurrentUserId());
            throw new BadRequestException(ResultEnum.INSERT_OPERATION_FAIL);
        }
        log.info("【新增菜单成功】" + "操作人id：" + SecurityUtils.getCurrentUserId() + "新增菜单Id：" + menu.getId());
        // 计算子节点数目
        menu.setSubCount(0);
        // 更新父节点菜单数目
        updateSubCnt(menu.getPid());
    }

    private void updateSubCnt(Long menuId) {
        if (! menuId.equals(0L)) {
            LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Menu::getPid, menuId);
            int count = this.count(queryWrapper);
            LambdaUpdateWrapper<Menu> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.set(Menu::getSubCount, count).eq(Menu::getId, menuId);
            boolean idUpdate = this.update(updateWrapper);
            if (! idUpdate) {
                log.error("【更新父节点菜单数目失败】" + "操作人id：" + SecurityUtils.getCurrentUserId() + "菜单Id：" + menuId);
                throw new BadRequestException("更新父节点菜单数目失败");
            }
            log.info("【更新父节点菜单数目成功】" + "操作人id：" + SecurityUtils.getCurrentUserId() + " 菜单Id：" + menuId);
        }
    }

    /**
     * 功能描述：修改菜单
     *
     * @param resources 菜单数据
     * @author Jiaoqianjin
     * Date: 2020/11/26 17:27
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMenu(Menu resources) {
        if (resources.getId().equals(resources.getPid())) {
            log.error("【修改菜单失败】" + "操作人id：" + SecurityUtils.getCurrentUserId() + "上级不能为自己,菜单Id：" + resources.getId());
            throw new BadRequestException("上级不能为自己");
        }
        Menu menu = this.getById(resources.getId());
        if (ObjectUtil.isEmpty(menu)) {
            log.error("【修改菜单信息失败】此菜单不存在" + "操作人id：" + SecurityUtils.getCurrentUserId() + "修改菜单Id：" + resources.getId());
            throw new BadRequestException(ResultEnum.ALTER_DATA_NOT_EXIST);
        }
        if (resources.getIFrame()) {
            String http = "http://", https = "https://";
            if (! (resources.getPath().toLowerCase().startsWith(http) || resources.getPath().toLowerCase().startsWith(https))) {
                log.error("【修改菜单失败】" + "操作人id：" + SecurityUtils.getCurrentUserId() + "外链必须以http://或者https://开头:" + menu.getIFrame());
                throw new BadRequestException("外链必须以http://或者https://开头");
            }
        }
        // 判断菜单标题是否已经存在
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getTitle, resources.getTitle());
        Menu menu1 = getOne(queryWrapper);
        if (menu1 != null && ! menu1.getId().equals(menu.getId())) {
            log.error("【修改菜单失败】" + "操作人id：" + SecurityUtils.getCurrentUserId() + "该菜单标题已存在：" + menu.getTitle());
            throw new BadRequestException("菜单标题已存在");
        }
        // 判断菜单组件名称是否已经存在
        if (StringUtils.isNotBlank(resources.getName())) {
            LambdaQueryWrapper<Menu> nameQuery = new LambdaQueryWrapper<>();
            nameQuery.eq(Menu::getName, resources.getName());
            menu1 = getOne(nameQuery);
            if (menu1 != null && ! menu1.getId().equals(menu.getId())) {
                log.error("【修改菜单失败】" + "操作人id：" + SecurityUtils.getCurrentUserId() + "该菜单组件名称已存在：" + menu.getName());
                throw new BadRequestException("组件名称已存在");
            }
        }

//        if (resources.getPid().equals(0L)) {
//            resources.setPid(null);
//        }

        // 记录的父节点ID
        Long oldPid = menu.getPid();
        Long newPid = resources.getPid();
        // 属性拷贝
        BeanUtil.copyProperties(resources, menu);
        menu.insertOrUpdate();
        log.info("【修改菜单成功】" + "操作人id：" + SecurityUtils.getCurrentUserId() + "菜单Id：" + menu.getId());
        // 计算父级菜单节点数目
        updateSubCnt(oldPid);
        updateSubCnt(newPid);
    }

    /**
     * 功能描述：根据ID查询菜单
     *
     * @param id 菜单id
     * @return /
     * @author Jiaoqianjin
     * Date: 2020/11/26 17:27
     */
    @Override
    public Menu findOne(Long id) {
        Menu menu = menuMapper.getMenuById(id);
        if (ObjectUtil.isEmpty(menu)) {
            throw new BadRequestException(ResultEnum.DATA_NOT_FOUND);
        }
        return menu;
    }

    /**
     * 功能描述：递归查询出要删除的菜单数据
     *
     * @param menuList 当前菜单数据
     * @param menuSet  删除的菜单数据
     * @return /
     * @author Jiaoqianjin
     * Date: 2020/11/26 17:28
     */
    @Override
    public Set<Menu> getDeleteMenus(List<Menu> menuList, Set<Menu> menuSet) {
        // 递归找出待删除的菜单
        for (Menu menu1 : menuList) {
            menuSet.add(menu1);
            LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Menu::getPid, menu1.getId());
            List<Menu> menus = list(queryWrapper);
            if (menus != null && menus.size() != 0) {
                getDeleteMenus(menus, menuSet);
            }
        }
        return menuSet;
    }

    /**
     * 功能描述：批量删除菜单
     *
     * @param menuSet 菜单数据
     * @author Jiaoqianjin
     * Date: 2020/11/26 17:28
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Menu> menuSet) {

        for (Menu menu : menuSet) {
            // 解绑菜单
            roleService.untiedMenu(menu.getId());
            // 更新父节点菜单数目
            updateSubCnt(menu.getPid());
        }
        Set<Long> menuIds = menuSet.stream().map(Menu::getId).collect(Collectors.toSet());
        boolean isRemove = this.removeByIds(menuIds);
        if(!isRemove) {
            log.error("【删除菜单失败】" + "操作人id：" + SecurityUtils.getCurrentUserId() + "菜单数据：" + menuIds);
        }
        log.info("【删除菜单成功】" + "操作人id：" + SecurityUtils.getCurrentUserId() + "菜单数据：" + menuIds);
    }

    /**
     * 功能描述：根据菜单ID返回所有子节点ID，包含自身ID
     *
     * @param menuList 当前id的子菜单
     * @param menuSet  当前菜单和所有子节点菜单
     * @return /
     * @author Jiaoqianjin
     * Date: 2020/11/28 16:03
     */
    @Override
    public Set<Menu> getChildMenus(List<Menu> menuList, Set<Menu> menuSet) {
        for (Menu menu : menuList) {
            menuSet.add(menu);
            LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Menu::getPid, menu.getId());
            List<Menu> menus = menuMapper.getMenusByPid(queryWrapper);
            if (menus != null && menus.size() != 0) {
                getChildMenus(menus, menuSet);
            }
        }
        return menuSet;
    }
}
