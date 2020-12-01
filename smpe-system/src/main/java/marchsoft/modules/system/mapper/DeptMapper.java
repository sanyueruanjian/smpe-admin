package marchsoft.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import marchsoft.config.MybatisRedisCache;
import marchsoft.modules.system.entity.Dept;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

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
@CacheNamespace(implementation = MybatisRedisCache.class, eviction = MybatisRedisCache.class)
public interface DeptMapper extends BaseMapper<Dept> {


    /**
     * description 通过角色id和关联表roles_depts查询角色对应的所有部门
     *
     * @param roleId 角色id
     * @return Set<Dept> 角色对应的所有部门
     * @author Wangmingcan
     * @date 2020-08-23 15:42
     */
    @Select("SELECT d.dept_id, d.pid, d.sub_count, d.name, d.dept_sort, d.enabled, d.create_by, d.update_by, d" +
            ".create_time, d.update_time FROM sys_dept d, sys_roles_depts r WHERE d.dept_id = r.dept_id AND r.role_id" +
            " = #{roleId}")
    @Result(column = "dept_id", property = "id")
    Set<Dept> findByRoleId(Long roleId);

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
