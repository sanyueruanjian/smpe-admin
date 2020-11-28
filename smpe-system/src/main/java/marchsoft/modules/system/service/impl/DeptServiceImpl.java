package marchsoft.modules.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import marchsoft.bean.PageVO;
import marchsoft.enums.DataScopeEnum;
import marchsoft.exception.BadRequestException;
import marchsoft.modules.system.entity.Dept;
import marchsoft.modules.system.entity.User;
import marchsoft.modules.system.entity.dto.DeptDTO;
import marchsoft.modules.system.entity.dto.DeptQueryCriteria;
import marchsoft.modules.system.mapper.DeptMapper;
import marchsoft.modules.system.mapper.RoleMapper;
import marchsoft.modules.system.mapper.UserMapper;
import marchsoft.modules.system.service.IDeptService;
import marchsoft.modules.system.service.IUserService;
import marchsoft.modules.system.service.mapstruct.DeptMapStruct;
import marchsoft.utils.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 部门 服务实现类
 * </p>
 *
 * @author Wangmingcan
 * @since 2020-08-17
 */
@Service
@RequiredArgsConstructor
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements IDeptService {

    private final DeptMapper deptMapper;

    private final DeptMapStruct deptMapStruct;

    private final RedisUtils redisUtils;

    private final UserMapper userMapper;

    private final IUserService userService;

    private final RoleMapper roleMapper;

    /**
     * Description:
     * 根据角色id查询角色所属部门
     *
     * @param id: 角色id
     * @return java.util.Set<marchsoft.modules.system.entity.Dept>
     * @author liuxingxing
     * @date 2020/11/26 15:43
     **/
    @Override
    public Set<Dept> findByRoleId(Long id) {
        return deptMapper.findByRoleId(id);
    }

    /**
     * Description:
     * 根据部门id查询部门
     *
     * @param id: 部门id
     * @return marchsoft.modules.system.entity.dto.DeptDTO
     * @author liuxingxing
     * @date 2020/11/26 15:42
     **/
    @Override
    public DeptDTO findById(Long id) {
        Dept dept = deptMapper.selectById(id);
        if (dept == null) {
            dept = new Dept();
        }
        ValidationUtil.isNull(dept.getId(), "Dept", "id", id);
        return deptMapStruct.toDto(dept);
    }

    /**
     * Description:
     * 根据pid(父部门)查询子部门集
     *
     * @param pid: 父级部门id
     * @return java.util.List<marchsoft.modules.system.entity.Dept>
     * @author liuxingxing
     * @date 2020/11/26 15:44
     **/
    @Override
    public List<Dept> findByPid(long pid) {
        LambdaQueryWrapper<Dept> deptLambdaQueryWrapper = new LambdaQueryWrapper<>();
        deptLambdaQueryWrapper.eq(Dept::getPid, pid);
        return list(deptLambdaQueryWrapper);
    }

    /**
     * Description:
     * 获取 deptList中部门与其子部门的id
     *
     * @param deptList: 需要查找部门集合
     * @return java.util.List<java.lang.Long>
     * @author liuxingxing
     * @date 2020/11/26 15:45
     **/
    @Override
    public List<Long> getDeptChildren(List<Dept> deptList) {
        List<Long> list = new ArrayList<>();
        deptList.forEach(dept -> {
                    if (dept != null && dept.getEnabled()) {
                        list.add(dept.getId());
                        List<Dept> depts = this.findByPid(dept.getId());   //源码中是新的sql
                        if (depts.size() != 0) {
                            list.addAll(getDeptChildren(depts));
                        }
                    }
                }
        );
        return list;
    }

    /**
     * Description:
     * 导出queryAll的数据
     *
     * @param deptDtos: 待到处数据
     * @param response: 服务器响应对象
     * @throws IOException io异常
     * @author liuxingxing
     * @date 2020/11/26 15:45
     **/
    @Override
    public void download(List<DeptDTO> deptDtos, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (DeptDTO deptDto : deptDtos) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("部门名称", deptDto.getName());
            map.put("部门状态", deptDto.getEnabled() ? "启用" : "停用");
            map.put("创建日期", deptDto.getCreateTime() == null ? null : deptDto.getCreateTime().toString());
            list.add(map);
        }
        FileUtils.downloadExcel(list, response);
    }

    /**
     * Description:
     * 根据criteria条件和isQuery查询
     * todo isQuery的作用
     *
     * @param criteria: Dept查询条件
     * @param isQuery:  是否判断当前用户数据权限
     * @return java.util.List<marchsoft.modules.system.entity.dto.DeptDTO>
     * @author liuxingxing
     * @date 2020/11/26 15:45
     **/
    @Override
    public List<DeptDTO> queryAll(DeptQueryCriteria criteria, Boolean isQuery) {
        List<Dept> depts = this.deptMapper.selectList(analysisQueryCriteria(criteria));
        return deptMapStruct.toDto(depts);
    }

    /**
     * Description:
     * 根据criteria条件和isQuery查询
     * todo isQuery的作用
     *
     * @param criteria: Dept查询条件
     * @param isQuery:  是否判断当前用户数据权限
     * @param pageVO:   分页条件
     * @return java.util.List<marchsoft.modules.system.entity.dto.DeptDTO>
     * @author liuxingxing
     * @date 2020/11/26 15:45
     **/
    @Override
    public IPage<DeptDTO> queryAll(DeptQueryCriteria criteria, PageVO pageVO, Boolean isQuery) {
        String dataScopeType = SecurityUtils.getDataScopeType();
        if (isQuery) {
            if (dataScopeType.equals(DataScopeEnum.ALL.getValue())) {
                criteria.setPidIsNull(true);
            }
        }
        IPage<Dept> page = this.deptMapper.selectPage(pageVO.buildPage(), analysisQueryCriteria(criteria));
        List<DeptDTO> deptDtos = deptMapStruct.toDto(page.getRecords());
        IPage<DeptDTO> returnPage = pageVO.buildPage();
        BeanUtil.copyProperties(returnPage, page);
        returnPage.setRecords(deptDtos);
        return returnPage;
    }

    /**
     * Description:
     * 根据当前部门获取同级与上级数据(递归使用)
     * FIXME 递归使用 后续会优化
     *
     * @param deptDto: 当前部门
     * @param depts:   递归缓存数组（返回的结果）
     * @return java.util.List<marchsoft.modules.system.entity.dto.DeptDTO>
     * @author liuxingxing
     * @date 2020/11/26 15:45
     **/
    @Override
    public List<DeptDTO> getSuperior(DeptDTO deptDto, List<Dept> depts) {
        LambdaQueryWrapper<Dept> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dept::getEnabled, true);
        if (deptDto.getPid() == 0) {
            queryWrapper.isNull(Dept::getPid);
            depts.addAll(list(queryWrapper));
            return deptMapStruct.toDto(depts);
        }
        queryWrapper.eq(Dept::getPid, deptDto.getPid());
        depts.addAll(list(queryWrapper));
        return getSuperior(findById(deptDto.getPid()), depts);
    }

    /**
     * Description:
     * 根据传入的部门集合构建部门树形结构
     *
     * @param deptDtos: 部门集合
     * @return java.lang.Object
     * @author liuxingxing
     * @date 2020/11/26 15:45
     **/
    @Override
    public List<DeptDTO> buildTree(List<DeptDTO> deptDtos) {
        Set<DeptDTO> trees = new LinkedHashSet<>();
        Set<DeptDTO> depts = new LinkedHashSet<>();
        List<String> deptNames = deptDtos.stream().map(DeptDTO::getName).collect(Collectors.toList());
        boolean isChild;
        for (DeptDTO deptDto : deptDtos) {
            isChild = false;
            if (deptDto.getPid() == 0) {
                trees.add(deptDto);
            }
            for (DeptDTO it : deptDtos) {
                if (it.getPid() != 0 && deptDto.getId().equals(it.getPid())) {
                    isChild = true;
                    if (deptDto.getChildren() == null) {
                        deptDto.setChildren(new ArrayList<>());
                    }
                    deptDto.getChildren().add(it);
                }
            }
            if (isChild) {
                depts.add(deptDto);
            } else if (deptDto.getPid() != 0 && ! deptNames.contains(findById(deptDto.getPid()).getName())) {
                depts.add(deptDto);
            }
        }

        if (CollectionUtil.isEmpty(trees)) {
            trees = depts;
        }
        return CollectionUtil.isEmpty(trees) ? deptDtos : new ArrayList<>(trees);
    }

    /**
     * Description:
     * 新增部门
     *
     * @param dept: 新增部门实体
     * @author liuxingxing
     * @date 2020/11/26 15:45
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(Dept dept) {
        save(dept.setSubCount(0));
        //清理缓存
        updateSubCnt(ObjectUtil.isNull(dept.getPid()) ? 0 : dept.getPid());
//        redisUtils.del("dept::pid" + (dept.getPid() == null ? 0 : dept.getPid()));
    }

    /**
     * Description:
     * 更新dept的子部门的数量
     *
     * @param deptId: 部门id
     * @author liuxingxing
     * @date 2020/11/26 21:09
     **/
    private void updateSubCnt(Long deptId) {
        if (deptId != 0) {
            LambdaQueryWrapper<Dept> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Dept::getPid, deptId);
            int count = count(queryWrapper);
            LambdaUpdateWrapper<Dept> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.set(Dept::getSubCount, count).eq(Dept::getId, deptId);
            update(updateWrapper);
        }
    }

    /**
     * Description:
     * 修改部门
     *
     * @param resources: 修改部门实体
     * @author liuxingxing
     * @date 2020/11/26 15:46
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDept(Dept resources) {
        // 旧部门 pid
        Long oldPid = findById(resources.getId()).getPid();
        Long newPid = resources.getPid();
        if (resources.getPid() != 0 && resources.getId().equals(resources.getPid())) {
            throw new BadRequestException("上级不能为自己");
        }
        Dept dept = getById(resources.getId());
        if (dept == null) {
            dept = new Dept();
        }
        ValidationUtil.isNull(dept.getId(), "Dept", "id", resources.getId());
        resources.setId(dept.getId());
        resources.insertOrUpdate();
        // 更新父节点中子节点数目
        updateSubCnt(oldPid);
        updateSubCnt(newPid);
        //清理缓存
//        delCaches(resources.getId(), oldPid, newPid);
    }

    /**
     * Description:
     * 获取待删除的部门（递归调用）
     * FIXME 递归获取删除部门列表（包含本部门和本部门的子部门）
     *
     * @param menuList: 要删除部门
     * @param deptDtoS:
     * @return java.util.Set<marchsoft.modules.system.entity.dto.DeptDTO>
     * @author liuxingxing
     * @date 2020/11/26 15:46
     **/
    @Override
    public Set<DeptDTO> getDeleteDepts(List<Dept> menuList, Set<DeptDTO> deptDtoS) {
        for (Dept dept : menuList) {
            deptDtoS.add(deptMapStruct.toDto(dept));
            LambdaQueryWrapper<Dept> deptLambdaQueryWrapper = new LambdaQueryWrapper<>();
            deptLambdaQueryWrapper.eq(Dept::getPid, dept.getId());
            List<Dept> depts = list(deptLambdaQueryWrapper);
            if (depts != null && depts.size() != 0) {
                getDeleteDepts(depts, deptDtoS);
            }
        }
        return deptDtoS;
    }

    /**
     * Description:
     * 验证是否被角色或用户关联
     *
     * @param deptDtoS:
     * @author liuxingxing
     * @date 2020/11/26 15:46
     **/
    @Override
    public void verification(Set<DeptDTO> deptDtoS) {
        Set<Long> deptIds = deptDtoS.stream().map(DeptDTO::getId).collect(Collectors.toSet());
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.in(User::getDeptId, deptIds);
        if (userService.count(userLambdaQueryWrapper) > 0) {
            throw new BadRequestException("所选部门存在用户关联，请解除后再试！");
        }
        if (roleMapper.countByDepts(StringUtils.strip(deptIds.toString(), "[", "]")) > 0) {
            throw new BadRequestException("所选部门存在角色关联，请解除后再试！");
        }
    }

    /**
     * Description:
     * 删除部门实体
     *
     * @param deptDtoS: 删除的部门
     * @author liuxingxing
     * @date 2020/11/26 15:46
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDept(Set<DeptDTO> deptDtoS) {
        for (DeptDTO deptDTO : deptDtoS) {
            // 清理缓存
            this.removeById(deptDTO.getId());
            updateSubCnt(deptDTO.getPid());
            //  delCaches(DeptDTO.getId(), DeptDTO.getPid(), null);
        }
    }


    /**
     * Description:
     * 清理缓存
     *
     * @param id:
     * @param oldPid:
     * @param newPid:
     * @author liuxingxing
     * @date 2020/11/27 12:58
     **/
    @Deprecated
    public void delCaches(Long id, Long oldPid, Long newPid) {
        List<User> users = userMapper.findByDeptRoleId(id);
        // 删除数据权限
        redisUtils.delByKeys("data::user:", users.stream().map(User::getId).collect(Collectors.toSet()));
        redisUtils.del("dept::id:" + id);
        redisUtils.del("dept::pid:" + (oldPid == null ? 0 : oldPid));
        redisUtils.del("dept::pid:" + (newPid == null ? 0 : newPid));
    }

    /**
     * Description:
     * 根据JobCruteruia 解析出查询Wrapper
     *
     * @param criteria: 岗位查询解析
     * @return com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<marchsoft.modules.system.entity.Job>
     * @author liuxingxing
     * @date 2020/11/24 13:28
     **/
    private LambdaQueryWrapper<Dept> analysisQueryCriteria(DeptQueryCriteria criteria) {
        LambdaQueryWrapper<Dept> wrapper = new LambdaQueryWrapper<>();
        if (! StrUtil.isBlank(criteria.getName())) {
            // 默认使用Like匹配
            wrapper.like(Dept::getName, criteria.getName());
        }
        if (! ObjectUtil.isNull(criteria.getEnabled())) {
            wrapper.eq(Dept::getEnabled, criteria.getEnabled());
        }
        if (! ObjectUtil.isNull(criteria.getPid())) {
            // 查询父部门为pid
            wrapper.eq(Dept::getPid, criteria.getPid());
        } else if (! ObjectUtil.isNull(criteria.getPidIsNull())) {
            // 查询父部门为0的（即：顶层部门）
            wrapper.eq(Dept::getPid, 0L);
        }
        if (! ObjectUtil.isNull(criteria.getStratTime())) {
            // 如果只有开始时间，就默认从开始到现在
            wrapper.between(Dept::getCreateTime, criteria.getStratTime(),
                    ObjectUtil.isNull(criteria.getEndTime()) ? LocalDateTime.now() : criteria.getEndTime());
        }
        return wrapper;
    }


}
