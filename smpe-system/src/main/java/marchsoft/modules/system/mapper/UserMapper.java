package marchsoft.modules.system.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import marchsoft.annotation.Queries;
import marchsoft.annotation.Query;
import marchsoft.base.BasicMapper;
import marchsoft.modules.system.entity.User;
import marchsoft.modules.system.entity.bo.UserBO;
import org.apache.ibatis.annotations.*;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 系统用户 Mapper 接口
 * </p>
 *
 * @author Wangmingcan
 * @since 2020-08-17
 */
@Component
@CacheConfig(cacheNames = "user")
public interface UserMapper extends BasicMapper<User> {

    /**
     * description:根据用户名查用户id
     * 单表不使用mp，为了走mybatis二级缓存
     *
     * @param name 用户名
     * @return 用户id
     * @author RenShiWei
     * Date: 2020/11/25 16:19
     */
    @Select("SELECT id FROM sys_user u WHERE u.username = #{name} AND is_deleted=0")
    Long findUserIdByName(String name);

    /**
     * description:根据用户id查用户名
     *
     * @param id  用户id
     * @return 用户名
     * @author ZhangYuKun
     * Date: 2021/1/15 16:44
     */
    @Select("SELECT username FROM sys_user u WHERE u.id = #{id} AND is_deleted=0")
    String findUserNameById(Long id);

    /**
     * description 通过用户名查询该用户信息，包括所在部分，拥有的job，和角色（角色中又包含menu）
     * 该方法只在用户第一次登陆时执行，之后将结果信息存入缓存
     *
     * @param id 用户id
     * @return User
     * @author Wangmingcan
     * Date : 2020-08-23 15:50
     */
    @Select("SELECT id,dept_id,username,nick_name,gender,phone,email,avatar_path,password," +
            "is_admin,enabled,create_by,update_by,pwd_reset_time,create_time,update_time" +
            " FROM sys_user u WHERE u.id = #{id} AND is_deleted=0")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "dept_id", property = "deptId"),
    })
    @Queries({
            @Query(column = "id", property = "roles",
                    select = "marchsoft.modules.system.mapper.RoleMapper.findWithMenuByUserId"),
            @Query(column = "id", property = "jobs",
                    select = "marchsoft.modules.system.mapper.JobMapper.findByUserId"),
            @Query(column = "dept_id", property = "dept",
                    select = "marchsoft.modules.system.mapper.DeptMapper.selectById")
    })
    @Cacheable(key = "'id:' + #p0")
    UserBO findUserDetailById(Long id);

    /**
     * description:根据条件构造返回查询的所有匹配的用户详细信息集合（包含角色信息、部门信息、岗位信息等）
     *
     * @param queryWrapper /
     * @return /
     * @author RenShiWei
     * Date: 2020/11/24 16:06
     */
    @Select("SELECT id,dept_id,username,nick_name,gender,phone,email,avatar_path,password," +
            "is_admin,enabled,create_by,update_by,pwd_reset_time,create_time,update_time" +
            " FROM sys_user u #{ew.customSqlSegment}")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "dept_id", property = "deptId"),
    })
    @Queries({
            @Query(column = "id", property = "roles",
                    select = "marchsoft.modules.system.mapper.RoleMapper.findWithMenuByUserId"),
            @Query(column = "id", property = "jobs",
                    select = "marchsoft.modules.system.mapper.JobMapper.findByUserId"),
            @Query(column = "dept_id", property = "dept",
                    select = "marchsoft.modules.system.mapper.DeptMapper.selectById")
    })
    List<UserBO> queryUserDetailsList(@Param(Constants.WRAPPER) LambdaQueryWrapper<User> queryWrapper);

    /**
     * description:根据条件构造返回查询的所有匹配的用户详细信息集合（包含角色信息、部门信息、岗位信息等）
     *
     * @param queryWrapper /
     * @param page         /
     * @return /
     * @author RenShiWei
     * Date: 2020/11/24 16:06
     */
    @Select("SELECT id,dept_id,username,nick_name,gender,phone,email,avatar_path,password," +
            "is_admin,enabled,create_by,update_by,pwd_reset_time,create_time,update_time" +
            " FROM sys_user u ${ew.customSqlSegment}")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "dept_id", property = "deptId"),
    })
    @Queries({
            @Query(column = "id", property = "roles",
                    select = "marchsoft.modules.system.mapper.RoleMapper.findWithMenuByUserId"),
            @Query(column = "id", property = "jobs",
                    select = "marchsoft.modules.system.mapper.JobMapper.findByUserId"),
            @Query(column = "dept_id", property = "dept",
                    select = "marchsoft.modules.system.mapper.DeptMapper.selectById")
    })
    IPage<UserBO> queryUserDetailsListPage(@Param(Constants.WRAPPER) LambdaQueryWrapper<User> queryWrapper,
                                           IPage<User> page);

    /**
     * @param id 角色id
     * @author Wangmingcan
     * @date 2021-01-14 14:10
     * @description 根据角色中的部门查询用户Id （清理dept缓存时调用）
     */
    @Select("SELECT r.user_id FROM sys_users_roles r, sys_roles_depts d WHERE " +
            " r.role_id = d.role_id AND r.role_id = #{id} GROUP BY r.user_id")
    List<Long> findIdByDeptRoleId(Long id);

    /**
     * @param id 角色id
     * @author Wangmingcan
     * @date 2021-01-14 14:43
     * @description 根据角色查询用户Id（清理role缓存时调用）
     */
    @Select("SELECT user_id FROM sys_users_roles WHERE role_id = #{id}")
    List<Long> findIdByRoleId(Long id);

    /**
     * @param id 菜单id
     * @author Wangmingcan
     * @date 2021-01-14 14:27
     * @description 根据菜单查询用户Id (清理menu缓存时调用)
     */
    @Select("SELECT ur.user_id FROM sys_users_roles ur, sys_roles_menus rm WHERE " +
            " ur.role_id = rm.role_id AND rm.menu_id = #{id} GROUP BY ur.user_id")
    List<Long> findIdByMenuId(Long id);

    /**
     * @param id 岗位id
     * @author Wangmingcan
     * @date 2021-01-14 14:27
     * @description 根据岗位查询用户Id (清理job缓存时调用)
     */
    @Select("SELECT user_id FROM sys_users_jobs WHERE job_id = #{id} group by user_id")
    List<Long> findByJobId(Long id);

    /**
     * description:新增用户，维护用户角色中间表
     *
     * @param userId  新增用户id
     * @param roleIds 用户绑定角色id集合
     * @return 操作条数
     * @author RenShiWei
     * Date: 2020/11/24 21:08
     */
    @Insert({
            "<script>",
            "INSERT INTO sys_users_roles(user_id, role_id) VALUES ",
            "<foreach collection='roleIds' item='item' index='index' separator=','>",
            "(#{userId}, #{item})",
            "</foreach>",
            "</script>"
    })
    Integer saveUserAtRole(Long userId, Set<Long> roleIds);

    /**
     * description:新增用户，维护用户岗位中间表
     *
     * @param userId 新增用户id
     * @param jobIds 用户绑定岗位id集合
     * @return 操作条数
     * @author RenShiWei
     * Date: 2020/11/24 21:08
     */
    @Insert({
            "<script>",
            "INSERT INTO sys_users_jobs(user_id, job_id) VALUES ",
            "<foreach collection='jobIds' item='item' index='index' separator=','>",
            "(#{userId}, #{item})",
            "</foreach>",
            "</script>"
    })
    Integer saveUserAtJob(Long userId, Set<Long> jobIds);

    /**
     * description:删除用户维护用户角色中间表
     *
     * @param userId 用户id
     * @return 操作条数
     * @author RenShiWei
     * Date: 2020/11/25 9:28
     */
    @Delete("DELETE FROM sys_users_roles WHERE user_id=#{userId}")
    Integer delUserAtRole(Long userId);

    /**
     * description:删除用户维护用户岗位中间表
     *
     * @param userId 用户id
     * @return 操作条数
     * @author RenShiWei
     * Date: 2020/11/25 9:28
     */
    @Delete("DELETE FROM sys_users_jobs WHERE user_id=#{userId}")
    Integer delUserAtJob(Long userId);

    /**
     * description:判断角色是否与用户绑定
     *
     * @param roleIds 角色id集合
     * @return 操作数>1 存在角色与用户绑定
     * @author RenShiWei
     * Date: 2020/11/27 12:16
     */
    @Select({"<script>" +
            "SELECT count(1) FROM sys_user u, sys_users_roles r WHERE " +
            "u.id = r.user_id AND u.is_deleted=0 AND r.role_id IN " +
            "<foreach collection='roleIds' item='item' index='index' open='(' separator=',' close=')'> " +
            "#{item}" +
            "</foreach>" +
            "</script>"
    })
    Integer countByUserByRoleIds(@Param("roleIds") Set<Long> roleIds);

    /**
     * 根据岗位查询(验证此job是否被用户关联)
     *
     * @param ids /
     * @return /
     */
    @Select("<script>" +
            "SELECT count(1) FROM sys_user u, sys_users_jobs j WHERE u.id = j.user_id AND u.is_deleted=0 AND j.job_id" +
            " " +
            "IN " +
            "<foreach collection='ids' item='item' index='index' open='(' separator=',' close=')'> " +
            "#{item}" +
            "</foreach>" +
            "</script>")
    int countByJobs(@Param("ids") Set<Long> ids);

}
