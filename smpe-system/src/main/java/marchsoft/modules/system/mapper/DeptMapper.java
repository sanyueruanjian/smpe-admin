package marchsoft.modules.system.mapper;

import marchsoft.base.BasicMapper;
import marchsoft.config.mybatisplus.MybatisRedisCache;
import marchsoft.modules.system.entity.Dept;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Set;

/**
 * <p>
 * 部门 Mapper 接口
 * </p>
 *
 * @author Wangmingcan
 * @since 2020-08-17
 */
@Component
@CacheConfig(cacheNames = "dept")
public interface DeptMapper extends BasicMapper<Dept> {


    /**
     * description 通过角色id和关联表roles_depts查询角色对应的所有部门
     *
     * @param roleId 角色id
     * @return Set<Dept> 角色对应的所有部门
     * @author Wangmingcan
     * @date 2020-08-23 15:42
     */
    @Select("SELECT d.id, d.pid, d.sub_count, d.name, d.dept_sort, d.enabled, d.create_by, d.update_by, d" +
            ".create_time, d.update_time FROM sys_dept d, sys_roles_depts r WHERE d.id = r.dept_id AND d.is_deleted=0" +
            " AND r.role_id" +
            " = #{roleId}")
    @Cacheable(key = "'role:' + #p0")
    Set<Dept> findByRoleId(Long roleId);

    @Cacheable(key = "'id:' + #p0")
    Dept selectById(Long id);

    /**
     * description:删除角色，维护角色部门中间表
     *
     * @param roleId 角色id
     * @author RenShiWei
     * Date: 2020/11/25 9:28
     */
    @Delete("DELETE FROM sys_roles_depts WHERE role_id=#{roleId}")
    void delRoleAtDept(Long roleId);
}
