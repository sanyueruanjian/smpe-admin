package marchsoft.modules.system.service.impl;

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


    @Override
    public Set<Dept> findByRoleId(Long id) {
        return deptMapper.findByRoleId(id);
    }

    @Override
    public DeptDTO findById(Long id) {
        Dept dept = deptMapper.selectById(id);
        if (dept == null) {
            dept = new Dept();
        }
        ValidationUtil.isNull(dept.getId(), "Dept", "id", id);
        return deptMapStruct.toDto(dept);
    }

    @Override
    public List<Dept> findByPid(long pid) {
        LambdaQueryWrapper<Dept> deptLambdaQueryWrapper = new LambdaQueryWrapper<>();
        deptLambdaQueryWrapper.eq(Dept::getPid, pid);
        return list(deptLambdaQueryWrapper);
    }

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


    @Override
    public List<DeptDTO> queryAll(DeptQueryCriteria criteria, Boolean isQuery) {
        List<Dept> depts = this.deptMapper.selectList(analysisQueryCriteria(criteria));
        return deptMapStruct.toDto(depts);
    }

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
        returnPage.setRecords(deptDtos).setTotal(page.getTotal());
        return returnPage;
    }


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

    @Override
    public Object buildTree(List<DeptDTO> deptDtos) {
        Set<DeptDTO> trees = new LinkedHashSet<>();
        Set<DeptDTO> depts = new LinkedHashSet<>();
        List<String> deptNames = deptDtos.stream().map(DeptDTO::getName).collect(Collectors.toList());
        boolean isChild;
        for (DeptDTO DeptDTO : deptDtos) {
            isChild = false;
            if (DeptDTO.getPid() == 0) {
                trees.add(DeptDTO);
            }
            for (DeptDTO it : deptDtos) {
                if (it.getPid() != 0 && DeptDTO.getId().equals(it.getPid())) {
                    isChild = true;
                    if (DeptDTO.getChildren() == null) {
                        DeptDTO.setChildren(new ArrayList<>());
                    }
                    DeptDTO.getChildren().add(it);
                }
            }
            if (isChild) {
                depts.add(DeptDTO);
            } else if (DeptDTO.getPid() != 0 && !deptNames.contains(findById(DeptDTO.getPid()).getName())) {
                depts.add(DeptDTO);
            }
        }

        if (CollectionUtil.isEmpty(trees)) {
            trees = depts;
        }
        Map<String, Object> map = new HashMap<>(2);
        map.put("totalElements", deptDtos.size());
        map.put("content", CollectionUtil.isEmpty(trees) ? deptDtos : trees);
        return map;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(Dept dept) {
        save(dept.setSubCount(0));
        //清理缓存
//        redisUtils.del("dept::pid" + (dept.getPid() == null ? 0 : dept.getPid()));
        updateSubCnt(ObjectUtil.isNull(dept.getPid()) ? 0 : dept.getPid());
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

    @Override
    public Set<DeptDTO> getDeleteDepts(List<Dept> menuList, Set<DeptDTO> DeptDTOS) {
        for (Dept dept : menuList) {
            DeptDTOS.add(deptMapStruct.toDto(dept));
            LambdaQueryWrapper<Dept> deptLambdaQueryWrapper = new LambdaQueryWrapper<>();
            deptLambdaQueryWrapper.eq(Dept::getPid, dept.getId());
            List<Dept> depts = list(deptLambdaQueryWrapper);
            if (depts != null && depts.size() != 0) {
                getDeleteDepts(depts, DeptDTOS);
            }
        }
        return DeptDTOS;
    }

    @Override
    public void verification(Set<DeptDTO> DeptDTOS) {
        Set<Long> deptIds = DeptDTOS.stream().map(DeptDTO::getId).collect(Collectors.toSet());
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.in(User::getDeptId, deptIds);
        if (userService.count(userLambdaQueryWrapper) > 0) {
            throw new BadRequestException("所选部门存在用户关联，请解除后再试！");
        }
        if (roleMapper.countByDepts(StringUtils.strip(deptIds.toString(), "[", "]")) > 0) {
            throw new BadRequestException("所选部门存在角色关联，请解除后再试！");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDept(Set<DeptDTO> DeptDTOS) {
        for (DeptDTO DeptDTO : DeptDTOS) {
            // 清理缓存
            this.removeById(DeptDTO.getId());
            updateSubCnt(DeptDTO.getPid());
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
        if (!StrUtil.isBlank(criteria.getName())) {
            // 默认使用Like匹配
            wrapper.like(Dept::getName, criteria.getName());
        }
        if (!ObjectUtil.isNull(criteria.getEnabled())) {
            wrapper.eq(Dept::getEnabled, criteria.getEnabled());
        }
        if (!ObjectUtil.isNull(criteria.getPid())) {
            // 查询父部门为pid
            wrapper.eq(Dept::getPid, criteria.getPid());
        } else if (!ObjectUtil.isNull(criteria.getPidIsNull())) {
            // 查询父部门为0的（即：顶层部门）
            wrapper.eq(Dept::getPid, 0L);
        }
        if (!ObjectUtil.isNull(criteria.getStratTime())) {
            // 如果只有开始时间，就默认从开始到现在
            wrapper.between(Dept::getCreateTime, criteria.getStratTime(),
                    ObjectUtil.isNull(criteria.getEndTime()) ? LocalDateTime.now() : criteria.getEndTime());
        }
        return wrapper;
    }


}
