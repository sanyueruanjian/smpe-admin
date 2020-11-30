package marchsoft.modules.system.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marchsoft.base.PageVO;
import marchsoft.config.bean.RsaProperties;
import marchsoft.enums.ResultEnum;
import marchsoft.exception.BadRequestException;
import marchsoft.modules.system.entity.User;
import marchsoft.modules.system.entity.dto.*;
import marchsoft.modules.system.entity.vo.UserPassVo;
import marchsoft.modules.system.service.IDeptService;
import marchsoft.modules.system.service.IRoleService;
import marchsoft.modules.system.service.IUserService;
import marchsoft.response.Result;
import marchsoft.utils.RsaUtils;
import marchsoft.utils.SecurityUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统用户 前端控制器
 * </p>
 *
 * @author Wangmingcan
 * @since 2020-08-17
 */
@Slf4j
@Api(tags = "系统：用户管理")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final PasswordEncoder passwordEncoder;
    private final IUserService userService;
    private final IDeptService deptService;
    private final IRoleService roleService;

    @ApiOperation(value = "导出用户数据", notes = " \n author：RenShiWei 2020/11/24")
    @ApiImplicitParam(name = "criteria", value = "条件")
    @GetMapping(value = "/download")
    @PreAuthorize("@smpe.check('user:list')")
    public void download(HttpServletResponse response, UserQueryCriteria criteria) throws IOException {
        log.info("【导出用户数据】操作人userId:" + SecurityUtils.getCurrentUserId());
        //每页条数设置为-1，是查询全部
        PageVO pageVO = new PageVO();
        pageVO.setSize(- 1);
        Result<IPage<UserDTO>> pageResult = queryUser(criteria, pageVO);
        List<UserDTO> userDTOList = pageResult.getData().getRecords();
        userService.download(userDTOList, response);
    }

    @ApiOperation(value = "查询用户详细信息集合", notes = "根据各种条件查询，可分页 \n author：RenShiWei 2020/11/24")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "criteria", value = "条件"),
            @ApiImplicitParam(name = "pageVO", value = "分页条件")
    })
    @GetMapping
    @PreAuthorize("@smpe.check('user:list')")
    public Result<IPage<UserDTO>> queryUser(UserQueryCriteria criteria, PageVO pageVO) {
        //预查询部门id集合（没有同级部门）
        if (ObjectUtil.isNotNull(criteria.getDeptId())) {
            //添加当前部门id
            criteria.getDeptIds().add(criteria.getDeptId());
            //添加子部门id
            criteria.getDeptIds().addAll(deptService.getDeptChildren(
                    deptService.findByPid(criteria.getDeptId())));
        }

        // 数据权限
        List<Long> dataScopeList = SecurityUtils.getCurrentUserDataScope();

        // 不为空并且数据权限不为空则取交集
        if (CollectionUtil.isNotEmpty(criteria.getDeptIds()) && CollectionUtil.isNotEmpty(dataScopeList)) {
            // 取交集
            criteria.getDeptIds().retainAll(dataScopeList);
            if (CollectionUtil.isNotEmpty(criteria.getDeptIds())) {
                //查询交集部门id集合的用户数据
                return Result.success(userService.queryUserDetailsList(criteria, pageVO.buildPage()));
            }
        } else {
            // 否则取并集（）
            criteria.getDeptIds().addAll(dataScopeList);
            //查询并集部门id集合的用户数据
            return Result.success(userService.queryUserDetailsList(criteria, pageVO.buildPage()));
        }
        return null;
    }

    @ApiOperation("新增用户")
    @PostMapping
    @ApiImplicitParam(name = "userInsertOrUpdateDTO", value = "新增用户参数列表")
    @PreAuthorize("@smpe.check('user:add')")
    public Result<Void> insertUserWithDetail(@RequestBody UserInsertOrUpdateDTO userInsertOrUpdateDTO) {
        if (! checkLevel(userInsertOrUpdateDTO.getRoles())) {
            log.error("【新增用户失败】用户角色权限不足。" + "操作人id：" + SecurityUtils.getCurrentUserId() + "。新增用户用户名：" + userInsertOrUpdateDTO.getUsername());
            throw new BadRequestException(ResultEnum.IDENTITY_NOT_POW);
        }
        // 默认密码 123456
        userInsertOrUpdateDTO.setPassword(passwordEncoder.encode("123456"));
        userService.insertUserWithDetail(userInsertOrUpdateDTO);
        return Result.success();
    }

    @ApiOperation("修改用户")
    @PutMapping
    @ApiImplicitParam(name = "userInsertOrUpdateDTO", value = "修改用户参数列表")
    @PreAuthorize("@smpe.check('user:edit')")
    public Result<Void> updateUserWithDetail(@RequestBody UserInsertOrUpdateDTO userInsertOrUpdateDTO) {
        if (! checkLevel(userInsertOrUpdateDTO.getRoles())) {
            log.error("【修改用户失败】用户角色权限不足。" + "操作人id：" + SecurityUtils.getCurrentUserId() + "。修改用户id：" + userInsertOrUpdateDTO.getId());
            throw new BadRequestException(ResultEnum.IDENTITY_NOT_POW);
        }
        userService.updateUserWithDetail(userInsertOrUpdateDTO);
        return Result.success();
    }

    @ApiOperation("修改用户：个人中心")
    @ApiImplicitParam(name = "userPersonalInfo", value = "修改个人信息参数列表")
    @PutMapping(value = "center")
    public Result<Void> updateUserPersonalInfo(@RequestBody UserPersonalInfoDTO userPersonalInfoDTO) {
        if (! userPersonalInfoDTO.getId().equals(SecurityUtils.getCurrentUserId())) {
            log.error("【修改用户个人资料失败】不能修改他人资料。" + "操作人id：" + SecurityUtils.getCurrentUserId() + "。修改用户id：" + userPersonalInfoDTO.getId());
            throw new BadRequestException("不能修改他人资料");
        }
        User user = new User();
        BeanUtil.copyProperties(userPersonalInfoDTO, user);
        userService.updateById(user);
        return Result.success();
    }

    @ApiOperation("删除用户")
    @DeleteMapping
    @PreAuthorize("@smpe.check('user:del')")
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> delete(@RequestBody Set<Long> ids) {
        for (Long id : ids) {
            if (! checkLevel(id)) {
                log.error("【删除用户失败】角色权限不足，不能删除。" + "操作人id：" + SecurityUtils.getCurrentUserId() + "。预删除用户id：" + id);
                throw new BadRequestException("角色权限不足，不能删除：" + userService.getById(id).getUsername());
            }
        }
        boolean isDel = userService.removeByIds(ids);
        if (! isDel) {
            log.error("【删除用户失败】角色权限不足，不能删除。" + "操作人id：" + SecurityUtils.getCurrentUserId() + "。预删除用户id集合：" + ids);
            throw new BadRequestException("【删除用户失败】" + "操作人id：" + SecurityUtils.getCurrentUserId());
        }
        return Result.success();
    }

    @ApiOperation("修改密码")
    @PostMapping(value = "/updatePass")
    public Result<Void> updatePass(UserPassVo passVo) throws Exception {
        String oldPass = RsaUtils.decryptByPrivateKey(RsaProperties.privateKey, passVo.getOldPass());
        String newPass = RsaUtils.decryptByPrivateKey(RsaProperties.privateKey, passVo.getNewPass());
        //获取现在的密码
        User user = userService.getById(SecurityUtils.getCurrentUserId());
        String password = user.getPassword();
        if (! passwordEncoder.matches(oldPass, password)) {
            log.error("修改密码失败】修改失败，旧密码错误" + "用户id：" + SecurityUtils.getCurrentUserId());
            throw new BadRequestException("修改密码失败】修改失败，旧密码错误");
        }
        if (passwordEncoder.matches(newPass, password)) {
            log.error("修改密码失败】新密码不能与旧密码相同" + "用户id：" + SecurityUtils.getCurrentUserId());
            throw new BadRequestException("修改密码失败】新密码不能与旧密码相同");
        }
        boolean isUpdate = userService.updateById(user.setPassword(passwordEncoder.encode(newPass)));
        if (! isUpdate) {
            log.error("【修改密码失败】" + "用户id：" + SecurityUtils.getCurrentUserId());
            throw new BadRequestException("【修改密码失败");
        }
        log.info("【修改密码成功】");
        return Result.success();
    }

    @ApiOperation("修改头像")
    @PostMapping(value = "/updateAvatar")
    public Result<Map<String, String>> updateAvatar(@RequestParam MultipartFile avatar) {
        return Result.success(userService.updateAvatar(avatar));
    }

    /**
     * description:操作多个角色时，判断用户权限（通过role的level）
     *
     * @param roleIds 预操作角色的id集合
     * @return true 有权限
     * @author RenShiWei
     * Date: 2020/11/25 11:22
     */
    private boolean checkLevel(Set<Long> roleIds) {
        Integer currentLevel = Collections.min(roleService.findRoleByUserId(SecurityUtils
                .getCurrentUserId()).stream().map(RoleSmallDTO::getLevel).collect(Collectors.toList()));
        Integer optLevel = roleService.findRoleMinLeave(roleIds);
        System.out.println("currentLevel" + currentLevel);
        System.out.println("optLevel:" + optLevel);
        //level 越小权限越大
        return currentLevel <= optLevel;
    }

    /**
     * description:操作用户时，判断用户权限
     *
     * @param userId 预操作用户id
     * @return true 有权限
     * @author RenShiWei
     * Date: 2020/11/25 11:22
     */
    private boolean checkLevel(Long userId) {
        Integer currentLevel =
                Collections.min(roleService.findRoleByUserId(SecurityUtils.getCurrentUserId()).stream().map
                        (RoleSmallDTO::getLevel).collect(Collectors.toList()));
        Integer optLevel =
                Collections.min(roleService.findRoleByUserId(userId).stream().map(RoleSmallDTO::getLevel).collect
                        (Collectors.toList()));
        System.out.println("currentLevel" + currentLevel);
        System.out.println("optLevel:" + optLevel);
        //level 越小权限越大
        return currentLevel <= optLevel;
    }

}

