package marchsoft.modules.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marchsoft.base.BasicServiceImpl;
import marchsoft.config.bean.FileProperties;
import marchsoft.config.bean.RsaProperties;
import marchsoft.enums.DataScopeEnum;
import marchsoft.enums.ResultEnum;
import marchsoft.exception.BadRequestException;
import marchsoft.modules.security.service.OnlineUserService;
import marchsoft.modules.security.service.UserCacheClean;
import marchsoft.modules.system.entity.Job;
import marchsoft.modules.system.entity.Role;
import marchsoft.modules.system.entity.User;
import marchsoft.modules.system.entity.bo.UserBO;
import marchsoft.modules.system.entity.dto.*;
import marchsoft.modules.system.entity.vo.UserPassVo;
import marchsoft.modules.system.mapper.JobMapper;
import marchsoft.modules.system.mapper.UserMapper;
import marchsoft.modules.system.service.IUserService;
import marchsoft.modules.system.service.mapstruct.UserMapStruct;
import marchsoft.utils.*;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统用户 服务实现类
 * </p>
 *
 * @author Marchsoft
 * @since 2020-08-17
 */
@Slf4j
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "user")
public class UserServiceImpl extends BasicServiceImpl<UserMapper, User> implements IUserService {

    private final UserMapper userMapper;
    private final UserMapStruct userMapStruct;
    private final OnlineUserService onlineUserService;
    private final UserCacheClean userCacheClean;
    private final FileProperties fileProperties;
    private final JobMapper jobMapper;
    private final RedisUtils redisUtils;
    private final PasswordEncoder passwordEncoder;

    /**
     * description:根据用户名查用户id
     * 单表不使用mp，为了走mybatis二级缓存
     *
     * @param username 用户名
     * @return 用户id
     * @author RenShiWei
     * Date: 2020/11/25 16:19
     */
    @Override
    public Long findUserIdByName(String username) {
        Long id = userMapper.findUserIdByName(username);
        if (ObjectUtil.isEmpty(id)) {
            log.error(StrUtil.format("【查询用户id失败】用户名不存在。用户名：{}", username));
        }
        return id;
    }

    /**
     * description:根据用户id查询详细信息（用户信息，部门信息，角色信息，权限信息等）
     * modify @RenShiWei 2020/11/25 description:【缓存策略变更】 redis缓存——>mybatis二级缓存 (@Cacheable(key = "'id:' + #p0"))
     *
     * @param id 用户id
     * @return /
     * @author RenShiWei
     * Date: 2020/11/21 21:27
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserBO findUserDetailById(Long id) {
        UserBO userBO = userMapper.findUserDetailById(id);
        if (ObjectUtil.isNull(userBO)) {
            throw new BadRequestException(ResultEnum.DATA_NOT_FOUND);
        }
        return userBO;
    }

    /**
     * description: 根据条件构造返回查询的所有匹配的用户详细信息集合（包含角色信息、部门信息、岗位信息等）
     *
     * @param criteria 条件
     * @return 用户集合
     * @author RenShiWei
     * Date: 2020/11/24 15:53
     */
    @Override
    public List<UserDTO> queryUserDetailsList(UserQueryCriteria criteria) {
        List<UserBO> userList = userMapper.queryUserDetailsList(buildUserQueryCriteria(criteria));
        return userMapStruct.toDto(userList);
    }

    /**
     * description: 根据条件构造返回查询的所有匹配的用户详细信息集合（包含角色信息、部门信息、岗位信息等）(可分页)
     *
     * @param criteria 条件
     * @return 用户集合
     * @author RenShiWei
     * Date: 2020/11/24 15:53
     */
    @Override
    public IPage<UserDTO> queryUserDetailsList(UserQueryCriteria criteria, IPage<User> page) {
        IPage<UserBO> userPage = userMapper.queryUserDetailsListPage(buildUserQueryCriteria(criteria), page);
        List<UserDTO> userDTOList = userMapStruct.toDto(userPage.getRecords());
        return PageUtil.toMapStructPage(userPage, userDTOList);
    }

    /**
     * description: 构建查询用户的LambdaQueryWrapper
     *
     * @param criteria 条件
     * @return /
     * @author RenShiWei
     * Date: 2020/11/24 15:53
     */
    private LambdaQueryWrapper<User> buildUserQueryCriteria(UserQueryCriteria criteria) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getDeleted, false);
        if (ObjectUtil.isNotNull(criteria.getId())) {
            wrapper.eq(User::getId, criteria.getId());
        }
        if (StrUtil.isNotBlank(criteria.getBlurry())) {
            wrapper.and(i -> i.like(User::getEmail, criteria.getBlurry()).or()
                    .like(User::getNickName, criteria.getBlurry()).or()
                    .like(User::getUsername, criteria.getBlurry()));
        }
        if (ObjectUtil.isNotNull(criteria.getEnabled())) {
            wrapper.eq(User::getEnabled, criteria.getEnabled());
        }
        //getDeptId已经做过处理
        if (CollectionUtil.isNotEmpty(criteria.getDeptIds())) {
            wrapper.in(User::getDeptId, criteria.getDeptIds());
        }
        if (ObjectUtil.isNotNull(criteria.getStartTime()) && ObjectUtil.isNotNull(criteria.getEndTime())) {
            wrapper.between(User::getCreateTime, criteria.getStartTime(), criteria.getEndTime());
        }
        return wrapper;
    }

    /**
     * description 导出数据
     *
     * @param userDTOList 待导出的数据
     * @param response    /
     * @author Wangmingcan
     * @date 2020-08-27 09:25
     */
    @Override
    public void download(List<UserDTO> userDTOList, HttpServletResponse response) throws IOException {
        if (CollectionUtil.isEmpty(userDTOList)) {
            throw new BadRequestException(ResultEnum.FILE_DOWNLOAD_FAIL_NOT_DATA);
        }

        List<Map<String, Object>> list = new ArrayList<>();
        for (UserDTO userDTO : userDTOList) {
            List<String> roles = userDTO.getRoles().stream().map(RoleSmallDTO::getName).collect(Collectors.toList());
            List<String> jobs = userDTO.getJobs().stream().map(JobSmallDTO::getName).collect(Collectors.toList());

            Map<String, Object> map = new LinkedHashMap<>();
            map.put("用户名", userDTO.getUsername());
            map.put("角色", StringUtils.strip(roles.toString(), "[", "]"));
            map.put("部门", userDTO.getDept().getName());
            map.put("岗位", StringUtils.strip(jobs.toString(), "[", "]"));
            map.put("邮箱", userDTO.getEmail());
            map.put("状态", userDTO.getEnabled() ? "启用" : "禁用");
            map.put("手机号码", userDTO.getPhone());
            map.put("修改密码的时间", userDTO.getPwdResetTime() == null ? null :
                    LocalDateTimeUtil.format(userDTO.getPwdResetTime(), DatePattern.NORM_DATETIME_FORMATTER));
            map.put("创建日期", userDTO.getCreateTime() == null ? null : LocalDateTimeUtil.format(userDTO.getCreateTime()
                    , DatePattern.NORM_DATETIME_FORMATTER));
            list.add(map);
        }
        FileUtils.downloadExcel(list, response);
    }

    /**
     * description:新增用户(维护岗位、角色表)
     *
     * @param userInsertOrUpdateDTO /
     * @author RenShiWei
     * Date: 2020/11/24 20:52
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertUserWithDetail(UserInsertOrUpdateDTO userInsertOrUpdateDTO) {
        // 默认密码 123456
        userInsertOrUpdateDTO.setPassword(passwordEncoder.encode("123456"));
        userInsertOrUpdateDTO.setEnabled(true);
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        //判断用户名不能重复
        queryWrapper.eq(User::getUsername, userInsertOrUpdateDTO.getUsername());
        // modify @RenShiWei 2020/11/24 description:list() ——> count()
        if (this.count(queryWrapper) > 0) {
            log.error(StrUtil.format("【新增用户失败】新增用户用户名已存在。操作人id：{}，用户名：{}", SecurityUtils.getCurrentUserId(),
                    userInsertOrUpdateDTO.getUsername()));
            throw new BadRequestException(ResultEnum.USER_USERNAME_EXIST);
        }

        //判断邮箱不能重复
        queryWrapper.clear();
        queryWrapper.eq(User::getEmail, userInsertOrUpdateDTO.getEmail());
        // modify @RenShiWei 2020/11/24 description:增加邮箱判断
        if (this.count(queryWrapper) > 0) {
            log.error(StrUtil.format("【新增用户失败】新增用户邮箱已存在。操作人id：{}，邮箱：{}", SecurityUtils.getCurrentUserId(),
                    userInsertOrUpdateDTO.getEmail()));
            throw new BadRequestException(ResultEnum.USER_EMAIL_EXIST);
        }

        // modify @RenShiWei 2021/2/6 description:增加用户部门的权限判断
        checkDataScope(userInsertOrUpdateDTO.getDeptId());

        //属性拷贝
        User user = new User();
        BeanUtil.copyProperties(userInsertOrUpdateDTO, user);
        //新增用户
        boolean save = save(user);
        if (! save) {
            log.error(StrUtil.format("【新增用户失败】操作人id：{}，用户名：{}", SecurityUtils.getCurrentUserId(),
                    userInsertOrUpdateDTO.getUsername()));
            throw new BadRequestException(ResultEnum.INSERT_OPERATION_FAIL);
        }

        //维护中间表
        int count = userMapper.saveUserAtRole(user.getId(), userInsertOrUpdateDTO.getRoles());
        if (count <= 0) {
            log.error(StrUtil.format("【新增用户失败】维护角色中间表失败。操作人id：{}", SecurityUtils.getCurrentUserId()));
            throw new BadRequestException(ResultEnum.OPERATION_MIDDLE_FAIL);
        }

        count = userMapper.saveUserAtJob(user.getId(), userInsertOrUpdateDTO.getJobs());
        if (count <= 0) {
            log.error(StrUtil.format("【新增用户失败】维护岗位中间表失败。操作人id：{}", SecurityUtils.getCurrentUserId()));
            throw new BadRequestException(ResultEnum.OPERATION_MIDDLE_FAIL);
        }

        log.info(StrUtil.format("【新增用户成功】操作人id：{}，用户名：{}", SecurityUtils.getCurrentUserId(),
                userInsertOrUpdateDTO.getUsername()));
    }

    /**
     * description:修改用户(维护岗位、角色表)
     *
     * @param userInsertOrUpdateDTO /
     * @author RenShiWei
     * Date: 2020/11/24 21:35
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserWithDetail(UserInsertOrUpdateDTO userInsertOrUpdateDTO) {
        // 根据id查询用户数据
        UserBO userBO = this.findUserDetailById(userInsertOrUpdateDTO.getId());
        if (ObjectUtil.isEmpty(userBO)) {
            log.error(StrUtil.format("【修改用户信息失败】此用户不存在。操作人id：{}，修改用户id：{}", SecurityUtils.getCurrentUserId(),
                    userInsertOrUpdateDTO.getId()));
            throw new BadRequestException(ResultEnum.ALTER_DATA_NOT_EXIST);
        }
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();

        //如果用户名修改了
        if (StrUtil.isNotBlank(userInsertOrUpdateDTO.getUsername()) && ! userInsertOrUpdateDTO.getUsername().equals(userBO.getUsername())) {
            //判断用户名不能重复
            queryWrapper.eq(User::getUsername, userInsertOrUpdateDTO.getUsername());
            // modify @RenShiWei 2020/11/24 description:list() ——> count()
            if (this.count(queryWrapper) > 0) {
                log.error(StrUtil.format("【修改用户失败】修改用户用户名已存在。操作人id：{}，修改用户用户名：{}", SecurityUtils.getCurrentUserId(),
                        userInsertOrUpdateDTO.getUsername()));
                throw new BadRequestException(ResultEnum.USER_USERNAME_EXIST);
            }
        }

        //如果邮箱修改了
        if (StrUtil.isNotBlank(userInsertOrUpdateDTO.getEmail()) && ! userInsertOrUpdateDTO.getEmail().equals(userBO.getEmail())) {
            //判断邮箱不能重复
            queryWrapper.clear();
            queryWrapper.eq(User::getEmail, userInsertOrUpdateDTO.getEmail());
            // modify @RenShiWei 2020/11/24 description:增加邮箱判断
            if (this.count(queryWrapper) > 0) {
                log.error(StrUtil.format("【修改用户失败】修改用户邮箱已存在。操作人id：{}，修改用户邮箱：{}", SecurityUtils.getCurrentUserId(),
                        userInsertOrUpdateDTO.getUsername()));
                throw new BadRequestException(ResultEnum.USER_EMAIL_EXIST);
            }
        }

        // modify @RenShiWei 2021/2/6 description:增加修改用户部门的权限判断
        checkDataScope(userInsertOrUpdateDTO.getDeptId());

        //流操作，获取缓存中的当前用户的角色id集合和岗位id集合
        Set<Long> roleIds = userBO.getRoles().stream().map(Role::getId).collect(Collectors.toSet());
        Set<Long> jobIds = userBO.getJobs().stream().map(Job::getId).collect(Collectors.toSet());
        //如果角色id集合、岗位id集合与原先不同，先删除再新增（即修改操作）
        //如果角色发生变化
        if (! CollectionUtils.isEqualCollection(roleIds, userInsertOrUpdateDTO.getRoles())) {
            Integer count = userMapper.delUserAtRole(userInsertOrUpdateDTO.getId());
            Integer count2 = userMapper.saveUserAtRole(userInsertOrUpdateDTO.getId(),
                    userInsertOrUpdateDTO.getRoles());
            //清除缓存
            redisUtils.del(CacheKey.DATA_USER + userInsertOrUpdateDTO.getId());
            redisUtils.del(CacheKey.MENU_USER + userInsertOrUpdateDTO.getId());
            redisUtils.del(CacheKey.ROLE_AUTH + userInsertOrUpdateDTO.getId());
            redisUtils.del(CacheKey.ROLE_USER + userInsertOrUpdateDTO.getId());
            if (count <= 0 && count2 <= 0) {
                log.error(StrUtil.format("【修改用户失败】维护角色中间表失败。操作人id：{}，修改用户id：{}", SecurityUtils.getCurrentUserId(),
                        userInsertOrUpdateDTO.getId()));
                throw new BadRequestException(ResultEnum.OPERATION_MIDDLE_FAIL);
            }
        }
        //如果岗位发生变化
        if (! CollectionUtils.isEqualCollection(jobIds, userInsertOrUpdateDTO.getJobs())) {
            Integer count = jobMapper.delUserAtJob(userInsertOrUpdateDTO.getId());
            Integer count2 = userMapper.saveUserAtJob(userInsertOrUpdateDTO.getId(),
                    userInsertOrUpdateDTO.getJobs());
            //清除缓存
            redisUtils.del(CacheKey.JOB_USER + userInsertOrUpdateDTO.getId());
            if (count <= 0 && count2 <= 0) {
                log.error(StrUtil.format("【修改用户失败】维护岗位中间表失败。操作人id：{}，修改用户id：{}", SecurityUtils.getCurrentUserId(),
                        userInsertOrUpdateDTO.getId()));
                throw new BadRequestException(ResultEnum.OPERATION_MIDDLE_FAIL);
            }
        }

        User user = new User();
        BeanUtil.copyProperties(userInsertOrUpdateDTO, user);
        boolean isUpdate = this.updateById(user);
        if (! isUpdate) {
            log.error(StrUtil.format("【修改用户信息失败】操作人id：{}，修改用户id：{}", SecurityUtils.getCurrentUserId(),
                    userInsertOrUpdateDTO.getId()));
        }
        // 如果用户被禁用，则清除用户登录信息
        if (! user.getEnabled()) {
            onlineUserService.kickOutForUsername(userBO.getUsername());
        }
        log.info(StrUtil.format("【修改用户信息成功】操作人id：{}，修改用户id：{}", SecurityUtils.getCurrentUserId(),
                userInsertOrUpdateDTO.getId()));
        //刷新缓存
        delCaches(user.getId());
    }

    /**
     * description:检查操作用户的部门权限
     *
     * @param deptId 部门id
     * @author RenShiWei
     * Date: 2021/2/6 22:42
     */
    private void checkDataScope(Long deptId) {
        if (ObjectUtil.isNotNull(deptId)) {
            //DataScope不为"",并且不是全部，说明是自定义或者本级
            if (StrUtil.isBlank(SecurityUtils.getDataScopeType()) && ! SecurityUtils.getDataScopeType().equals(DataScopeEnum.ALL.getValue())) {
                //修改的部门在数据权限范围之外，抛出异常
                if (! CollectionUtil.contains(SecurityUtils.getCurrentUserDataScope(),
                        deptId)) {
                    log.error(StrUtil.format("【操作失败】操作用户部门的权限不足。操作人id：{}，修改的部门id：{}",
                            SecurityUtils.getCurrentUserId(), deptId));
                    throw new BadRequestException("【操作失败】操作用户部门的权限不足。");
                }
            }
        }
    }

    @Override
    public void updateUserPersonalInfo(UserPersonalInfoDTO userPersonalInfoDTO) {
        User user = new User();
        BeanUtil.copyProperties(userPersonalInfoDTO, user);
        boolean isUpdate = updateById(user);
        if (! isUpdate) {
            log.error(StrUtil.format("【修改用户失败】操作人id：{}", SecurityUtils.getCurrentUserId()));
            throw new BadRequestException(ResultEnum.OPERATION_MIDDLE_FAIL);
        }
        //清除缓存
        delCaches(userPersonalInfoDTO.getId());
    }

    @Override
    public void delete(Set<Long> ids) {
        boolean isDel = removeByIds(ids);
        if (! isDel) {
            log.error(StrUtil.format("【删除用户失败】角色权限不足，不能删除。操作人id：{}，预删除用户id集合：{}", SecurityUtils.getCurrentUserId(),
                    ids));
            throw new BadRequestException("【删除用户失败】" + "操作人id：" + SecurityUtils.getCurrentUserId());
        }
        //清除缓存
        redisUtils.delByKeys(CacheKey.USER_ID, ids);
    }

    @Override
    public void updatePass(UserPassVo passVo) {
        try {
            String oldPass = RsaUtils.decryptByPrivateKey(RsaProperties.privateKey, passVo.getOldPass());
            String newPass = RsaUtils.decryptByPrivateKey(RsaProperties.privateKey, passVo.getNewPass());
            //获取现在的密码
            User user = getById(SecurityUtils.getCurrentUserId());
            String password = user.getPassword();
            if (! passwordEncoder.matches(oldPass, password)) {
                log.error(StrUtil.format("【修改密码失败】修改失败，旧密码错误。操作人id：{}", SecurityUtils.getCurrentUserId()));
                throw new BadRequestException("【修改密码失败】修改失败，旧密码错误");
            }
            if (passwordEncoder.matches(newPass, password)) {
                log.error(StrUtil.format("【修改密码失败】新密码不能与旧密码相同。操作人id：{}", SecurityUtils.getCurrentUserId()));
                throw new BadRequestException("【修改密码失败】新密码不能与旧密码相同");
            }
            boolean isUpdate = updateById(user.setPassword(passwordEncoder.encode(newPass)));

            if (! isUpdate) {
                log.error(StrUtil.format("【修改密码失败】操作人id：{}", SecurityUtils.getCurrentUserId()));
                throw new BadRequestException("【修改密码失败】");
            }

            //清除缓存
            delCaches(user.getId());
        } catch (Exception e) {
            log.error(StrUtil.format("【修改密码失败】操作人id：{}", SecurityUtils.getCurrentUserId()));
            throw new BadRequestException("【修改密码失败】" + e.getMessage());
        }
    }

    /**
     * description:修改头像
     *
     * @param multipartFile 头像文件
     * @return 图片路径
     * @author RenShiWei
     * Date: 2020/11/25 15:35
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, String> updateAvatar(MultipartFile multipartFile) {
        User user = getById(SecurityUtils.getCurrentUserId());
        String oldPath = user.getAvatarPath();
        File file = FileUtils.upload(multipartFile, fileProperties.getPath());
        user.setAvatarPath(Objects.requireNonNull(file).getName());
        updateById(user);
        if (StringUtils.isNotBlank(oldPath)) {
            FileUtil.del(oldPath);
        }
        if (ObjectUtil.isNull(user)) {
            throw new BadRequestException(ResultEnum.USER_NOT_EXIST);
        }
        Map<String, String> map = new HashMap<>(1);
        map.put("avatar", file.getName());
        log.info(StrUtil.format("【修改用户头像成功】用户id：{}，上传文件名：{}", SecurityUtils.getCurrentUserId(),
                file.getName()));
        //刷新缓存
        delCaches(user.getId());
        return map;
    }

    /**
     * 清理缓存
     *
     * @param id /
     */
    private void delCaches(Long id) {
        redisUtils.del(CacheKey.USER_ID + id);
        redisUtils.del(CacheKey.DATA_USER + id);
        flushCache(id);
    }

    /**
     * 清理 登陆时 用户缓存信息
     *
     * @param userId 用户id
     */
    private void flushCache(Long userId) {
        userCacheClean.cleanUserCache(userId);
    }

}
