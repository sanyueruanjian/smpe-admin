package marchsoft.modules.system.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import marchsoft.config.MybatisRedisCache;
import marchsoft.modules.system.entity.Role;
import marchsoft.modules.system.entity.bo.RoleBO;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 * @author Wangmingcan
 * @since 2020-08-17
 */
@Component
@CacheNamespace(implementation = MybatisRedisCache.class, eviction = MybatisRedisCache.class)
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * description:根据角色id查询一条角色信息
     * 不使用mp的原因：调用mybatis二级缓存自动维护
     *
     * @param roleId 角色id
     * @return /
     * @author RenShiWei
     * Date: 2020/11/26 9:20
     */
    @Select("SELECT role_id,name,level,description,data_scope,create_by,update_by,create_time,update_time " +
            "FROM sys_role r WHERE r.role_id = #{roleId}")
    @Results({
            @Result(column = "role_id", property = "id"),
            @Result(column = "role_id", property = "menus",
                    many = @Many(select = "marchsoft.modules.system.mapper.MenuMapper.findByRoleId",
                            fetchType = FetchType.EAGER)),
            @Result(column = "role_id", property = "depts",
                    many = @Many(select = "marchsoft.modules.system.mapper.DeptMapper.findByRoleId",
                            fetchType = FetchType.EAGER))
    })
    RoleBO findById(Long roleId);

    /**
     * description:查询所有角色详细信息（包含角色对应的菜单、部门、用户）
     *
     * @param queryWrapper 角色条件构造
     * @return 所有角色详细信息（包含角色对应的菜单、部门、用户）
     * @author RenShiWei
     * Date: 2020/11/26 14:46
     */
    @Select("SELECT role_id,name,level,description,data_scope,create_by,update_by,create_time,update_time " +
            "FROM sys_role ${ew.customSqlSegment}")
    @Results({
            @Result(column = "role_id", property = "id"),
            @Result(column = "role_id", property = "menus",
                    many = @Many(select = "marchsoft.modules.system.mapper.MenuMapper.findByRoleId",
                            fetchType = FetchType.EAGER)),
            @Result(column = "role_id", property = "depts",
                    many = @Many(select = "marchsoft.modules.system.mapper.DeptMapper.findByRoleId",
                            fetchType = FetchType.EAGER))
    })
    List<RoleBO> findRoleDetailAllPage(@Param(Constants.WRAPPER) LambdaQueryWrapper<Role> queryWrapper);

    /**
     * description:查询所有角色详细信息（包含角色对应的菜单、部门、用户），可分页
     *
     * @param queryWrapper 角色条件构造
     * @param page         分页条件
     * @return 角色详细信息（包含角色对应的菜单、部门、用户），分页
     * @author RenShiWei
     * Date: 2020/11/26 14:46
     */
    @Select("SELECT role_id,name,level,description,data_scope,create_by,update_by,create_time,update_time " +
            "FROM sys_role ${ew.customSqlSegment}")
    @Results({
            @Result(column = "role_id", property = "id"),
            @Result(column = "role_id", property = "menus",
                    many = @Many(select = "marchsoft.modules.system.mapper.MenuMapper.findByRoleId",
                            fetchType = FetchType.EAGER)),
            @Result(column = "role_id", property = "depts",
                    many = @Many(select = "marchsoft.modules.system.mapper.DeptMapper.findByRoleId",
                            fetchType = FetchType.EAGER))
    })
    IPage<RoleBO> findRoleDetailAllPage(@Param(Constants.WRAPPER) LambdaQueryWrapper<Role> queryWrapper,
                                        IPage<Role> page);

    /**
     * description:通过用户id查询角色（只有角色信息）
     *
     * @param userId 用户id
     * @return 用户的角色集合（只有角色信息）
     * @author RenShiWei
     * Date: 2020/11/26 16:18
     */
    @Select("SELECT r.role_id,r.name,r.level,r.description,r.data_scope,r.create_by,r.update_by,r.create_time,r" +
            ".update_time" +
            " FROM sys_role r, sys_users_roles ur WHERE r.role_id = ur.role_id AND ur.user_id = #{userId}")
    @Result(column = "role_id", property = "id")
    Set<Role> findRoleByUserId(Long userId);

    /**
     * description:新增角色，维护角色部门中间表
     *
     * @param roleId  新增角色id
     * @param deptIds 角色绑定部门id集合
     * @return 操作条数
     * @author RenShiWei
     * Date: 2020/11/24 21:08
     */
    @Insert({
            "<script>",
            "INSERT INTO sys_roles_depts(role_id, dept_id) VALUES ",
            "<foreach collection='deptIds' item='item' index='index' separator=','>",
            "(#{roleId}, #{item})",
            "</foreach>",
            "</script>"
    })
    Integer saveRoleAtDept(Long roleId, Set<Long> deptIds);

    /**
     * description:新增角色，维护角色菜单中间表
     *
     * @param roleId  新增角色id
     * @param menuIds 角色绑定菜单id集合
     * @return 操作条数
     * @author RenShiWei
     * Date: 2020/11/24 21:08
     */
    @Insert({
            "<script>",
            "INSERT INTO sys_roles_menus(role_id, menu_id) VALUES ",
            "<foreach collection='menuIds' item='item' index='index' separator=','>",
            "(#{roleId}, #{item})",
            "</foreach>",
            "</script>"
    })
    Integer saveRoleAtMenu(Long roleId, Set<Long> menuIds);

    /**
     * description:删除角色，维护角色部门中间表
     *
     * @param roleId 角色id
     * @author RenShiWei
     * Date: 2020/11/25 9:28
     */
    @Delete("DELETE FROM sys_roles_depts WHERE role_id=#{roleId}")
    void delRoleAtDept(Long roleId);

    /**
     * description:删除角色，维护角色菜单中间表
     *
     * @param roleId 角色id
     * @return 操作条数
     * @author RenShiWei
     * Date: 2020/11/25 9:28
     */
    @Delete("DELETE FROM sys_roles_menus WHERE role_id=#{roleId}")
    Integer delRoleAtMenu(Long roleId);


    /**
     * description 通过用户id查询角色（包含角色的菜单信息）
     *
     * @param userId 用户id
     * @return 角色（包含角色的菜单信息）
     * @author Wangmingcan
     * @date 2020-08-23 15:49
     */
    @Select("SELECT r.role_id,r.name,r.level,r.description,r.data_scope,r.create_by,r.update_by,r.create_time,r" +
            ".update_time " +
            "FROM sys_role r, sys_users_roles ur WHERE r.role_id = ur.role_id AND ur.user_id = ${userId}")
    @Results({
            @Result(column = "role_id", property = "id"),
            @Result(column = "role_id", property = "menus",
                    many = @Many(select = "marchsoft.modules.system.mapper.MenuMapper.findByRoleId",
                            fetchType = FetchType.EAGER))
    })
    Set<RoleBO> findWithMenuByUserId(Long userId);

    // MODIFY  description: 修改为script标签进行in查询  @liuxingxing 2020/11/29

    @Select("<script>" +
            "SELECT COUNT(1) FROM sys_role r, sys_roles_depts d WHERE " +
            "r.role_id = d.role_id AND d.dept_id IN " +
            "<foreach collection='deptIds' item='item' index='index' open='(' separator=',' close=')'> " +
            "#{item}" +
            "</foreach>" +
            "</script>")
    int countByDeptIds(@Param("deptIds") Set<Long> deptIds);

    // MODIFY description: 修改为script标签进行in查询  @liuxingxing 2020/11/29

    @Select("<script>" +
            "SELECT r.* FROM sys_role r, sys_roles_menus m WHERE " +
            "r.role_id = m.role_id AND m.menu_id IN " +
            "<foreach collection='menuIds' item='item' index='index' open='(' separator=',' close=')'> " +
            "#{item}" +
            "</foreach>" +
            "</script>"
    )
    @Result(column = "role_id", property = "id")
    List<Role> findInMenuId(@Param("menuIds") Set<Long> menuIds);


    /**
     * description:根据菜单id删除角色菜单中间表一条数据
     *
     * @param menuId 菜单id
     * @return 操作数
     * @author RenShiWei
     * Date: 2020/11/28 16:57
     */
    @Delete("DELETE FROM sys_roles_menus WHERE menu_id = ${menuId}")
    Integer untiedMenu(Long menuId);
}
