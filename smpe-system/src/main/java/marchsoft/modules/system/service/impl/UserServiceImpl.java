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
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marchsoft.config.bean.FileProperties;
import marchsoft.enums.ResultEnum;
import marchsoft.exception.BadRequestException;
import marchsoft.modules.security.service.OnlineUserService;
import marchsoft.modules.security.service.UserCacheClean;
import marchsoft.modules.system.entity.Job;
import marchsoft.modules.system.entity.Role;
import marchsoft.modules.system.entity.User;
import marchsoft.modules.system.entity.bo.UserBO;
import marchsoft.modules.system.entity.dto.*;
import marchsoft.modules.system.mapper.JobMapper;
import marchsoft.modules.system.mapper.RoleMapper;
import marchsoft.modules.system.mapper.UserMapper;
import marchsoft.modules.system.service.IUserService;
import marchsoft.modules.system.service.mapstruct.UserMapStruct;
import marchsoft.utils.FileUtils;
import marchsoft.utils.PageUtil;
import marchsoft.utils.SecurityUtils;
import marchsoft.utils.StringUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.cache.annotation.CacheConfig;
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
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private final UserMapper userMapper;
    private final UserMapStruct userMapStruct;
    private final OnlineUserService onlineUserService;
    private final UserCacheClean userCacheClean;
    private final FileProperties fileProperties;
    private final JobMapper jobMapper;

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
            log.error("【查询用户id失败】用户名不存在。用户名：" + username);
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
        System.out.println(criteria);
        System.out.println(page);
        IPage<UserBO> userPage = userMapper.queryUserDetailsListPage(buildUserQueryCriteria(criteria), page);
        System.out.println(userPage.getRecords());
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
        if (ObjectUtil.isNotNull(criteria.getId())) {
            wrapper.eq(User::getId, criteria.getId());
        }
        if (StrUtil.isNotBlank(criteria.getBlurry())) {
            wrapper.like(User::getEmail, criteria.getBlurry()).or()
                    .like(User::getNickName, criteria.getBlurry()).or()
                    .like(User::getUsername, criteria.getBlurry());
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
        userInsertOrUpdateDTO.setEnabled(true);
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        //判断用户名不能重复
        queryWrapper.eq(User::getUsername, userInsertOrUpdateDTO.getUsername());
        // modify @RenShiWei 2020/11/24 description:list() ——> count()
        if (this.count(queryWrapper) > 0) {
            log.error("【新增用户失败】" + "操作人id：" + SecurityUtils.getCurrentUserId() + "新增用户用户名已存在：" + userInsertOrUpdateDTO.getUsername());
            throw new BadRequestException(ResultEnum.USER_USERNAME_EXIST);
        }

        //判断邮箱不能重复
        queryWrapper.clear();
        queryWrapper.eq(User::getEmail, userInsertOrUpdateDTO.getEmail());
        // modify @RenShiWei 2020/11/24 description:增加邮箱判断
        if (this.count(queryWrapper) > 0) {
            log.error("【新增用户失败】" + "操作人id：" + SecurityUtils.getCurrentUserId() + "新增用户邮箱已存在：" + userInsertOrUpdateDTO.getEmail());
            throw new BadRequestException(ResultEnum.USER_EMAIL_EXIST);
        }

        //属性拷贝
        User user = new User();
        BeanUtil.copyProperties(userInsertOrUpdateDTO, user);
        //新增用户
        boolean save = save(user);
        if (! save) {
            log.error("【新增用户失败】" + "操作人id：" + SecurityUtils.getCurrentUserId() + "新增用户用户名：" + userInsertOrUpdateDTO.getUsername());
            throw new BadRequestException(ResultEnum.INSERT_OPERATION_FAIL);
        }

        //维护中间表
        int count = userMapper.saveUserAtRole(user.getId(), userInsertOrUpdateDTO.getRoles());
        if (count <= 0) {
            log.error("【新增用户失败】维护角色中间表失败。" + "操作人id：" + SecurityUtils.getCurrentUserId());
            throw new BadRequestException(ResultEnum.OPERATION_MIDDLE_FAIL);
        }

        count = userMapper.saveUserAtJob(user.getId(), userInsertOrUpdateDTO.getJobs());
        if (count <= 0) {
            log.error("【新增用户失败】维护岗位中间表失败。" + "操作人id：" + SecurityUtils.getCurrentUserId());
            throw new BadRequestException(ResultEnum.OPERATION_MIDDLE_FAIL);
        }

        log.info("【新增用户成功】" + "操作人id：" + SecurityUtils.getCurrentUserId() + "新增用户用户名：" + userInsertOrUpdateDTO.getUsername());
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
            log.error("【修改用户信息失败】此用户不存在" + "操作人id：" + SecurityUtils.getCurrentUserId() + "修改用户id：" + userInsertOrUpdateDTO.getId());
            throw new BadRequestException(ResultEnum.ALTER_DATA_NOT_EXIST);
        }
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();

        //如果用户名修改了
        if (StrUtil.isNotBlank(userInsertOrUpdateDTO.getUsername()) && ! userInsertOrUpdateDTO.getUsername().equals(userBO.getUsername())) {
            //判断用户名不能重复
            queryWrapper.eq(User::getUsername, userInsertOrUpdateDTO.getUsername());
            // modify @RenShiWei 2020/11/24 description:list() ——> count()
            if (this.count(queryWrapper) > 0) {
                log.error("【修改用户失败】" + "操作人id：" + SecurityUtils.getCurrentUserId() + "修改用户用户名已存在：" + userInsertOrUpdateDTO.getUsername());
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
                log.error("【修改用户失败】" + "操作人id：" + SecurityUtils.getCurrentUserId() + "修改用户邮箱已存在：" + userInsertOrUpdateDTO.getUsername());
                throw new BadRequestException(ResultEnum.USER_EMAIL_EXIST);
            }
        }

        //流操作，获取缓存中的当前用户的角色id集合和岗位id集合
        Set<Long> roleIds = userBO.getRoles().stream().map(Role::getId).collect(Collectors.toSet());
        Set<Long> jobIds = userBO.getJobs().stream().map(Job::getId).collect(Collectors.toSet());
        //如果角色id集合、岗位id集合与原先不同，先删除再新增（即修改操作）
        if (! CollectionUtils.isEqualCollection(roleIds, userInsertOrUpdateDTO.getRoles())) {
            Integer count = userMapper.delUserAtRole(userInsertOrUpdateDTO.getId());
            Integer count2 = userMapper.saveUserAtRole(userInsertOrUpdateDTO.getId(),
                    userInsertOrUpdateDTO.getRoles());
            if (count <= 0 && count2 <= 0) {
                log.error("【修改用户失败】维护角色中间表失败。" + "操作人id：" + SecurityUtils.getCurrentUserId() + "修改用户id：" + userInsertOrUpdateDTO.getId());
                throw new BadRequestException(ResultEnum.OPERATION_MIDDLE_FAIL);
            }
        }
        if (! CollectionUtils.isEqualCollection(jobIds, userInsertOrUpdateDTO.getJobs())) {
            Integer count = jobMapper.delUserAtJob(userInsertOrUpdateDTO.getId());
            Integer count2 = userMapper.saveUserAtJob(userInsertOrUpdateDTO.getId(),
                    userInsertOrUpdateDTO.getJobs());
            if (count <= 0 && count2 <= 0) {
                log.error("【修改用户失败】维护岗位中间表失败。" + "操作人id：" + SecurityUtils.getCurrentUserId() + "修改用户id：" + userInsertOrUpdateDTO.getId());
                throw new BadRequestException(ResultEnum.OPERATION_MIDDLE_FAIL);
            }
        }

        User user = new User();
        BeanUtil.copyProperties(userInsertOrUpdateDTO, user);
        boolean isUpdate = this.updateById(user);
        if (! isUpdate) {
            log.error("【修改用户信息失败】" + "操作人id：" + SecurityUtils.getCurrentUserId() + "修改用户id：" + userInsertOrUpdateDTO.getId());
        }

        // 如果用户被禁用，则清除用户登录信息
        if (! user.getEnabled()) {
            onlineUserService.kickOutForUsername(userBO.getUsername());
        }

        log.info("【修改用户信息成功】" + "操作人id：" + SecurityUtils.getCurrentUserId() + "修改用户id：" + userInsertOrUpdateDTO.getId());
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
        user.setAvatarPath(Objects.requireNonNull(file).getPath());
        user.setAvatarName(file.getName());
        updateById(user);
        if (StringUtils.isNotBlank(oldPath)) {
            FileUtil.del(oldPath);
        }
        String username = user.getUsername();
        if (ObjectUtil.isNull(user)) {
            throw new BadRequestException(ResultEnum.USER_NOT_EXIST);
        }
        flushCache(username);
        Map<String, String> map = new HashMap<>(1);
        map.put("avatar", file.getName());
        log.info("【修改用户头像成功】" + "用户id：" + SecurityUtils.getCurrentUserId() + "上传文件名：" + file.getName());
        return map;
    }

    /**
     * 清理 登陆时 用户缓存信息
     *
     * @param username /
     */
    private void flushCache(String username) {
        userCacheClean.cleanUserCache(username);
    }

}
