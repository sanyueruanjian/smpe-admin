package marchsoft.modules.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marchsoft.enums.DataScopeEnum;
import marchsoft.enums.ResultEnum;
import marchsoft.exception.BadRequestException;
import marchsoft.modules.security.service.UserCacheClean;
import marchsoft.modules.system.entity.Dept;
import marchsoft.modules.system.entity.Menu;
import marchsoft.modules.system.entity.Role;
import marchsoft.modules.system.entity.bo.RoleBO;
import marchsoft.modules.system.entity.dto.*;
import marchsoft.modules.system.mapper.DeptMapper;
import marchsoft.modules.system.mapper.RoleMapper;
import marchsoft.modules.system.mapper.UserMapper;
import marchsoft.modules.system.service.IRoleService;
import marchsoft.modules.system.service.mapstruct.RoleMapStruct;
import marchsoft.modules.system.service.mapstruct.RoleSmallMapStruct;
import marchsoft.utils.*;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author Marchsoft
 * @since 2020-08-17
 */
@Slf4j
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "role")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    private final RoleMapper roleMapper;
    private final UserMapper userMapper;
    private final RoleMapStruct roleMapStruct;
    private final RoleSmallMapStruct roleSmallMapStruct;
    private final DeptMapper deptMapper;

    /**
     * description:根据角色id查询一条角色信息
     * 不使用mp的原因：调用mybatis二级缓存自动维护
     *
     * @param roleId 角色id
     * @return /
     * @author RenShiWei
     * Date: 2020/11/26 9:20
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public RoleDTO findById(Long roleId) {
        RoleBO roleBO = roleMapper.findById(roleId);
        if (ObjectUtil.isEmpty(roleBO)) {
            log.error("【查询角色失败】用户名不存在。角色id：" + roleId);
            throw new BadRequestException(ResultEnum.DATA_NOT_FOUND);
        }
        return roleMapStruct.toDto(roleBO);
    }

    /**
     * description:查询所有角色详细信息（包含角色对应的菜单、部门、用户）
     * 默认按照角色的level升序
     *
     * @return 所有角色详细信息（包含角色对应的菜单、部门、用户）
     * @author RenShiWei
     * Date: 2020/11/26 14:46
     */
    @Override
    public List<RoleDTO> findRoleDetailAll() {
        //默认按照角色的级别升序
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(Role::getLevel);
        return roleMapStruct.toDto(roleMapper.findRoleDetailAllPage(wrapper));
    }

    /**
     * description:查询所有角色详细信息（包含角色对应的菜单、部门、用户），按照条件查询
     *
     * @param criteria 角色条件构造
     * @return 所有角色详细信息（包含角色对应的菜单、部门、用户）
     * @author RenShiWei
     * Date: 2020/11/26 14:46
     */
    @Override
    public List<RoleDTO> findRoleDetailAll(RoleQueryCriteria criteria) {
        return roleMapStruct.toDto(roleMapper.findRoleDetailAllPage(buildUserQueryCriteria(criteria)));
    }

    /**
     * description:查询所有角色详细信息（包含角色对应的菜单、部门、用户），可分页
     *
     * @param criteria 角色条件构造
     * @param page     分页条件
     * @return 角色详细信息（包含角色对应的菜单、部门、用户），分页
     * @author RenShiWei
     * Date: 2020/11/26 14:46
     */
    @Override
    public IPage<RoleDTO> findRoleDetailAll(RoleQueryCriteria criteria, IPage<Role> page) {
        IPage<RoleBO> rolePage = roleMapper.findRoleDetailAllPage(buildUserQueryCriteria(criteria), page);
        List<RoleDTO> roleDtoList = roleMapStruct.toDto(rolePage.getRecords());
        return PageUtil.toMapStructPage(rolePage, roleDtoList);
    }

    /**
     * description: 构建查询角色的LambdaQueryWrapper
     *
     * @param criteria 条件
     * @return /
     * @author RenShiWei
     * Date: 2020/11/24 15:53
     */
    private LambdaQueryWrapper<Role> buildUserQueryCriteria(RoleQueryCriteria criteria) {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(criteria.getBlurry())) {
            wrapper.like(Role::getName, criteria.getBlurry()).or()
                    .like(Role::getDescription, criteria.getBlurry());
        }
        if (ObjectUtil.isNotNull(criteria.getStartTime()) && ObjectUtil.isNotNull(criteria.getEndTime())) {
            wrapper.between(Role::getCreateTime, criteria.getStartTime(), criteria.getEndTime());
        }
        return wrapper;
    }

    /**
     * description:通过用户id查询角色（只有角色信息）
     *
     * @param userId 用户id
     * @return 用户的角色集合（只有角色信息）
     * @author RenShiWei
     * Date: 2020/11/26 16:18
     */
    @Override
    public List<RoleSmallDTO> findRoleByUserId(Long userId) {
        Set<Role> roles = roleMapper.findRoleByUserId(userId);
        if (CollectionUtil.isEmpty(roles)) {
            throw new BadRequestException(ResultEnum.DATA_NOT_FOUND);
        }
        return roleSmallMapStruct.toDto(new ArrayList<>(roles));
    }

    /**
     * description:新增角色（维护部门、菜单中间表）
     *
     * @param roleInsertOrUpdateDTO /
     * @author RenShiWei
     * Date: 2020/11/26 20:12
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertRole(RoleInsertOrUpdateDTO roleInsertOrUpdateDTO) {
        // modify @RenShiWei 2020/11/26 description:角色名重复判断条件  查询一条 ——> count
        if (isExistRoleName(roleInsertOrUpdateDTO.getName())) {
            log.error("【新增角色失败】" + "操作人id：" + SecurityUtils.getCurrentUserId() + "新增角色名已存在：" + roleInsertOrUpdateDTO.getName());
            throw new BadRequestException(ResultEnum.ROLE_NAME_EXIST);
        }

        //属性拷贝
        Role role = new Role();
        BeanUtil.copyProperties(roleInsertOrUpdateDTO, role);
        boolean save = save(role);
        if (!save) {
            log.error("【新增角色失败】" + "操作人id：" + SecurityUtils.getCurrentUserId() + "新增角色名：" + roleInsertOrUpdateDTO.getName());
            throw new BadRequestException(ResultEnum.INSERT_OPERATION_FAIL);
        }
        // MODIFY:@Jiaoqianjin 2020/12/1 description: 新增角色 不用 维护角色菜单中间表
        /*
          维护中间表
         */
        //如果DataScope是自定义，维护角色部门中间表
        if (roleInsertOrUpdateDTO.getDataScope().equals(DataScopeEnum.CUSTOMIZE.getValue())) {
            if (CollectionUtil.isNotEmpty(roleInsertOrUpdateDTO.getDepts())) {
                Integer count = roleMapper.saveRoleAtDept(role.getId(), roleInsertOrUpdateDTO.getDepts());
                if (count <= 0) {
                    log.error("【新增角色失败】维护角色部门中间表失败。" + "操作人id：" + SecurityUtils.getCurrentUserId() + "角色id：" + role.getId());
                    throw new BadRequestException(ResultEnum.OPERATION_MIDDLE_FAIL);
                }
            }
        }

        log.info("【新增角色成功】" + "操作人id：" + SecurityUtils.getCurrentUserId() + "角色id：" + role.getId());
    }

    /**
     * description:修改角色（维护部门、菜单中间表）
     *
     * @param roleInsertOrUpdateDTO /
     * @author RenShiWei
     * Date: 2020/11/26 20:12
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRole(RoleInsertOrUpdateDTO roleInsertOrUpdateDTO) {
        RoleBO roleBO = roleMapper.findById(roleInsertOrUpdateDTO.getId());
        if (ObjectUtil.isEmpty(roleBO)) {
            log.error("【修改角色信息失败】此角色不存在" + "操作人id：" + SecurityUtils.getCurrentUserId() + "修改角色id：" + roleInsertOrUpdateDTO.getId());
            throw new BadRequestException(ResultEnum.ALTER_DATA_NOT_EXIST);
        }
        //角色名重复判断条件
        if (!roleInsertOrUpdateDTO.getName().equals(roleBO.getName())) {
            if (isExistRoleName(roleInsertOrUpdateDTO.getName())) {
                log.error("【修改角色失败】角色名已存在" + "操作人id：" + SecurityUtils.getCurrentUserId() + "角色名：" + roleInsertOrUpdateDTO.getName());
                throw new BadRequestException(ResultEnum.ROLE_NAME_EXIST);
            }
        }
        // MODIFY:@Jiaoqianjin 2020/12/1 description: 新增角色 不用 维护角色菜单中间表
        /*
            维护中间表（4种情况）
         */
        Set<Long> deptIds = roleBO.getDepts().stream().map(Dept::getId).collect(Collectors.toSet());
        try {
            //维护角色部门中间表
            if (!CollectionUtils.isEqualCollection(deptIds, roleInsertOrUpdateDTO.getDepts())) {
                //传入和原来的DeptIds都为null，不处理
                if (CollectionUtil.isEmpty(roleInsertOrUpdateDTO.getDepts())) {
                    //传入deptIds为null
//                    if (CollectionUtil.isNotEmpty(roleBO.getDepts())) {
//                        //如果原本不是null，删除中间表数据
//                        deptMapper.delRoleAtDept(roleInsertOrUpdateDTO.getId());
//                    }
                    // MODIFY:@Jiaoqianjin 2020/12/1 description://传入deptIds为null,不管之前的为不为null,都执行删除，为了清除缓存
                    deptMapper.delRoleAtDept(roleInsertOrUpdateDTO.getId());
                } else {
                    //传入deptIds不为null
//                    if (CollectionUtil.isNotEmpty(roleBO.getDepts())) {
//                        //原来的不为空，先删除在增加
//                        deptMapper.delRoleAtDept(roleInsertOrUpdateDTO.getId());
//                    }
                    // MODIFY:@Jiaoqianjin 2020/12/1 description://传入deptIds为null,不管之前的为不为null,都执行删除，为了清除缓存
                    //传入deptIds不为null
                    deptMapper.delRoleAtDept(roleInsertOrUpdateDTO.getId());
                    //原来的为空，直接增加
                    //两种情况的增加
                    roleMapper.saveRoleAtDept(roleInsertOrUpdateDTO.getId(), roleInsertOrUpdateDTO.getDepts());
                }
            }
        } catch (Exception e) {
            log.error("【修改角色失败】维护角色部门表失败。" + "操作人id：" + SecurityUtils.getCurrentUserId() + "修改角色id：" + roleInsertOrUpdateDTO.getId());
            throw new BadRequestException(ResultEnum.OPERATION_MIDDLE_FAIL);
        }

        Role role = new Role();
        BeanUtil.copyProperties(roleInsertOrUpdateDTO, role);
        boolean isUpdate = this.updateById(role);
        if (!isUpdate) {
            log.error("【修改角色失败】" + "操作人id：" + SecurityUtils.getCurrentUserId() + "修改角色id：" + roleInsertOrUpdateDTO.getId());
        }

        log.info("【修改角色成功】" + "操作人id：" + SecurityUtils.getCurrentUserId() + "修改角色id：" + roleInsertOrUpdateDTO.getId());

    }

    /**
     * description: 修改角色绑定的菜单
     *
     * @param roleId  角色id
     * @param menuIds 菜单id集合
     * @author RenShiWei
     * Date: 2020/11/27 11:46
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMenu(Long roleId, Set<Long> menuIds) {
        RoleBO roleBO = roleMapper.findById(roleId);
        Set<Long> oldMenuIds = roleBO.getMenus().stream().map(Menu::getId).collect(Collectors.toSet());
        //对比之前有修改再进行操作（先删除后修改）
        // MODIFY:@Jiaoqianjin 2020/11/28 description: CollectionUtil.containsAll() --> CollectionUtils
        //  .isEqualCollection()
        if (!CollectionUtils.isEqualCollection(menuIds, oldMenuIds)) {
            Integer count = roleMapper.delRoleAtMenu(roleId);
            Integer count2 = roleMapper.saveRoleAtMenu(roleId, menuIds);
            if (count <= 0 && count2 <= 0) {
                log.error("【修改角色菜单失败】维护角色菜单表失败。" + "操作人id：" + SecurityUtils.getCurrentUserId() + "修改角色id：" + roleId);
                throw new BadRequestException(ResultEnum.OPERATION_MIDDLE_FAIL);
            }
            log.info("【修改角色菜单成功】" + "操作人id：" + SecurityUtils.getCurrentUserId() + "修改角色id：" + roleId);
        }

    }

    /**
     * description:判断角色名是否存在
     *
     * @param roleName 角色名
     * @return 是否存在
     * @author RenShiWei
     * Date: 2020/11/26 17:09
     */
    private boolean isExistRoleName(String roleName) {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Role::getName, roleName);
        int count = this.count(wrapper);
        return count > 0;
    }

    /**
     * description:根据角色名查询一条角色信息
     *
     * @param roleName 角色名
     * @return 一条角色信息
     * @author RenShiWei
     * Date: 2020/11/26 16:54
     */
    @Override
    public Role findByName(String roleName) {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Role::getName, roleName);
        return getOne(queryWrapper);
    }

    /**
     * description:判断角色是否与用户绑定
     *
     * @param roleIds 角色id集合
     * @return /
     * @author RenShiWei
     * Date: 2020/11/27 12:16
     */
    @Override
    public boolean isRolesWithUser(Set<Long> roleIds) {
        if (userMapper.countByUserByRoleIds(roleIds) > 0) {
            log.info("【角色与用户存在绑定关系】角色id集合：" + roleIds);
            return true;
        }
        return false;
    }

    /**
     * description:批量删除角色
     *
     * @param roleIds 角色Id集合
     * @author RenShiWei
     * Date: 2020/11/27 12:24
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> roleIds) {
//        for (Long id : roleIds) {
//            // 更新相关缓存
//            delCaches(id, null);
//        }
        boolean isDel = removeByIds(roleIds);
        if (!isDel) {
            log.error("【删除角色失败】角色id集合：" + roleIds);
            throw new BadRequestException("删除角色失败");
        }
    }


    /**
     * description 查询用户的角色权限信息
     *
     * @param user 用户信息
     * @return 用户的角色权限信息
     * @author Wangmingcan
     * @date 2020-08-23 16:06
     */
    @Override
    public List<GrantedAuthority> mapToGrantedAuthorities(UserDTO user) {
        Set<String> permissions = new HashSet<>();
        // 如果是管理员直接返回
        if (user.getIsAdmin()) {
            permissions.add("admin");
            return permissions.stream().map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }
        Set<RoleBO> roles = roleMapper.findWithMenuByUserId(user.getId());
        permissions = roles.stream()
                .flatMap(role -> role.getMenus().stream())
                .filter(menu -> StringUtils.isNotBlank(menu.getPermission()))
                .map(Menu::getPermission).collect(Collectors.toSet());
        return permissions.stream().map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    /**
     * description:根据角色id集合查询角色最小级别
     *
     * @param roleIds 角色id集合
     * @return 角色最小级别
     * @author RenShiWei
     * Date: 2020/11/25 9:52
     */
    @Override
    public Integer findRoleMinLeave(Set<Long> roleIds) {
        Set<Integer> levelSet = new HashSet<>();
        for (Long roleId : roleIds) {
            levelSet.add(findById(roleId).getLevel());
        }
        return CollectionUtil.min(levelSet);
    }

    /**
     * description 导出数据
     *
     * @param roleDTOList 角色数据
     * @param response    /
     * @throws IOException IO异常
     * @author Wangmingcan
     * @date 2020-08-28 18:44
     */
    @Override
    public void download(List<RoleDTO> roleDTOList, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();

        for (RoleDTO role : roleDTOList) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("角色名称", role.getName());
            map.put("角色级别", role.getLevel());
            map.put("描述", role.getDescription());
            map.put("创建日期", role.getCreateTime() == null ? null : LocalDateTimeUtil.format(role.getCreateTime(),
                    DatePattern.NORM_DATETIME_FORMATTER));
            list.add(map);
        }
        FileUtils.downloadExcel(list, response);
    }


    /**
     * description:根据用户id获取角色信息（含菜单）
     *
     * @param userId 用户id
     * @return 角色信息（含菜单）
     * @author RenShiWei
     * Date: 2020/11/27 12:59
     */
    @Override
    public Set<RoleBO> findWithMenuByUserId(Long userId) {
        return roleMapper.findWithMenuByUserId(userId);
    }

    /**
     * description:解绑菜单
     *
     * @param menuId 菜单id
     * @author RenShiWei
     * Date: 2020/11/30 17:35
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void untiedMenu(Long menuId) {
        // 更新菜单
        roleMapper.untiedMenu(menuId);
        // MODIFY:@Jiaoqianjin 2020/12/1 description: 菜单绑定角色可以为空
//        Integer count = roleMapper.untiedMenu(menuId);
//        if (count <= 0) {
//            log.error("【角色解绑菜单失败】菜单id：" + menuId);
//            throw new BadRequestException("角色解绑菜单失败");
//        }
    }

}
