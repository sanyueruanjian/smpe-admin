package marchsoft.modules.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import marchsoft.modules.system.entity.Role;
import marchsoft.modules.system.entity.bo.RoleBO;
import marchsoft.modules.system.entity.dto.*;
import org.springframework.security.core.GrantedAuthority;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author Wangmingcan
 * @since 2020-08-17
 */
public interface IRoleService extends IService<Role> {

    /**
     * description:根据角色id查询一条角色信息
     * 不使用mp的原因：调用mybatis二级缓存自动维护
     *
     * @param roleId 角色id
     * @return /
     * @author RenShiWei
     * Date: 2020/11/26 9:20
     */
    RoleDTO findById(Long roleId);

    /**
     * description:查询所有角色详细信息（包含角色对应的菜单、部门、用户）
     * 默认按照角色的level升序
     *
     * @return 所有角色详细信息（包含角色对应的菜单、部门、用户）
     * @author RenShiWei
     * Date: 2020/11/26 14:46
     */
    List<RoleDTO> findRoleDetailAll();

    /**
     * description:查询所有角色详细信息（包含角色对应的菜单、部门、用户），按照条件查询
     *
     * @param criteria 角色条件构造
     * @return 所有角色详细信息（包含角色对应的菜单、部门、用户）
     * @author RenShiWei
     * Date: 2020/11/26 14:46
     */
    List<RoleDTO> findRoleDetailAll(RoleQueryCriteria criteria);

    /**
     * description:查询所有角色详细信息（包含角色对应的菜单、部门、用户），可分页
     *
     * @param criteria 角色条件构造
     * @param page     分页条件
     * @return 角色详细信息（包含角色对应的菜单、部门、用户），分页
     * @author RenShiWei
     * Date: 2020/11/26 14:46
     */
    IPage<RoleDTO> findRoleDetailAll(RoleQueryCriteria criteria, IPage<Role> page);

    /**
     * description:通过用户id查询角色（只有角色信息）
     *
     * @param userId 用户id
     * @return 用户的角色集合（只有角色信息）
     * @author RenShiWei
     * Date: 2020/11/26 16:18
     */
    List<RoleSmallDTO> findRoleByUserId(Long userId);

    /**
     * description:新增角色（维护部门、菜单中间表）
     *
     * @param roleInsertOrUpdateDTO /
     * @author RenShiWei
     * Date: 2020/11/26 20:12
     */
    void insertRole(RoleInsertOrUpdateDTO roleInsertOrUpdateDTO);

    /**
     * description:修改角色（维护部门、菜单中间表）
     *
     * @param roleInsertOrUpdateDTO /
     * @author RenShiWei
     * Date: 2020/11/26 20:12
     */
    void updateRole(RoleInsertOrUpdateDTO roleInsertOrUpdateDTO);

    /**
     * description: 修改角色绑定的菜单
     *
     * @param roleId  角色id
     * @param menuIds 菜单id集合
     * @author RenShiWei
     * Date: 2020/11/27 11:46
     */
    void updateMenu(Long roleId, Set<Long> menuIds);

    /**
     * description:根据角色名查询一条角色信息
     *
     * @param roleName 角色名
     * @return 一条角色信息
     * @author RenShiWei
     * Date: 2020/11/26 16:54
     */
    Role findByName(String roleName);

    /**
     * description 导出数据
     *
     * @param roleDTOList 角色数据
     * @param response    /
     * @throws IOException IO异常
     * @author Wangmingcan
     * @date 2020-08-28 18:44
     */
    void download(List<RoleDTO> roleDTOList, HttpServletResponse response) throws IOException;

    /**
     * description:判断角色是否与用户绑定
     *
     * @param roleIds 角色id集合
     * @return /
     * @author RenShiWei
     * Date: 2020/11/27 12:16
     */
    boolean isRolesWithUser(Set<Long> roleIds);

    /**
     * description:批量删除角色
     *
     * @param roleIds 角色Id集合
     * @author RenShiWei
     * Date: 2020/11/27 12:24
     */
    void delete(Set<Long> roleIds);

    /**
     * description 查询用户的角色权限信息
     *
     * @param user 用户信息
     * @return 用户的角色权限信息
     * @author Wangmingcan
     * @date 2020-08-23 16:06
     */
    List<GrantedAuthority> mapToGrantedAuthorities(UserDTO user);

    /**
     * description:根据角色id集合查询角色最小级别
     *
     * @param roleIds 角色id集合
     * @return 角色最小级别
     * @author RenShiWei
     * Date: 2020/11/25 9:52
     */
    Integer findRoleMinLeave(Set<Long> roleIds);

    /**
     * description:根据用户id获取角色信息（含菜单）
     *
     * @param userId 用户id
     * @return 角色信息（含菜单）
     * @author RenShiWei
     * Date: 2020/11/27 12:59
     */
    Set<RoleBO> findWithMenuByUserId(Long userId);

    /**
     * description:解绑菜单
     *
     * @param menuId 菜单id
     * @author RenShiWei
     * Date: 2020/11/30 17:35
     */
    void untiedMenu(Long menuId);


}
