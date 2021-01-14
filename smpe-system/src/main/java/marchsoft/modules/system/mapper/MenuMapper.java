package marchsoft.modules.system.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import marchsoft.base.BasicMapper;
import marchsoft.config.mybatisplus.MybatisRedisCache;
import marchsoft.modules.system.entity.Menu;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * 功能描述： 系统菜单 Mapper 接口
 *
 * @author Jiaoqianjin
 * Date: 2020/11/23 9:33
 */
@CacheConfig(cacheNames = "menu")
public interface MenuMapper extends BasicMapper<Menu> {

    /**
     * description 通过角色id和关联表roles_menus查询该角色拥有的菜单
     *
     * @param id 菜单id
     * @return Set<Menu>
     * @author Jiaoqianjin
     * @date 2020-11-23 15:45
     */
    @Select("SELECT m.id,m.pid,m.sub_count,m.type,m.title,m.name,m.component,m.menu_sort,m.icon,m.path," +
            "m.i_frame,m.cache,m.hidden,m.permission,m.create_by,m.update_by,m.create_time,m.update_time " +
            "FROM sys_menu m, sys_roles_menus rm " +
            "WHERE m.id = rm.menu_id AND rm.role_id = ${id} AND m.is_deleted=0")
    @Cacheable(key = "'role:' + #p0")
    Set<Menu> findByRoleId(Long id);

    /**
     * description 通过角色id集合查找类型不为type的菜单
     *
     * @param roleIds 角色id集合，经StringUtils处理，没有[]，只剩 1, 2, 3...
     * @param type    菜单的类型
     * @return /
     * @author Jiaoqianjin
     * @date 2020-11-23 15:46
     */
    @Select({"<script> SELECT m.id,m.pid,m.sub_count,m.type,m.title,m.name,m.component,m.menu_sort,m.icon,m" +
            ".path,m.i_frame,m.cache," +
            "m.hidden,m.permission,m.create_by,m.update_by,m.create_time,m.update_time " +
            "FROM sys_menu m, sys_roles_menus r " +
            "WHERE m.id = r.menu_id AND m.is_deleted=0 " +
            "AND r.role_id IN" +
            "<foreach collection='roleIds' item='item' index='index' open='(' separator=',' close=')'> " +
            "#{item}" +
            "</foreach>" +
            "AND type != ${type} order by m.menu_sort asc" +
            "</script>"
    })
    LinkedHashSet<Menu> findByRoleIdsAndTypeNot(Set<Long> roleIds, int type);

    /**
     * 功能描述：根据Pid查询对应菜单
     *
     * @param queryWrapper 请求参数
     * @return /
     * @author Jiaoqianjin
     * Date: 2020/11/26 11:14
     */
    @Select("SELECT id,pid,sub_count,type,title,name,component,menu_sort,icon,path,i_frame," +
            "cache,hidden,permission,create_by,update_by,create_time,update_time " +
            "FROM sys_menu ${ew.customSqlSegment}")
    List<Menu> getMenusByPid(@Param(Constants.WRAPPER) LambdaQueryWrapper<Menu> queryWrapper);

    /**
     * 功能描述：根据菜单Id查询菜单（不使用mb单表查询，使用mapper二级缓存）
     *
     * @param menuId 菜单id
     * @return /
     * @author Jiaoqianjin
     * Date: 2020/11/26 11:29
     */
    @Select("SELECT id,pid,sub_count,type,title,name,component,menu_sort,icon,path,i_frame," +
            "cache,hidden,permission,create_by,update_by,create_time,update_time FROM sys_menu " +
            "WHERE id = #{id} AND is_deleted=0")
    @Cacheable(key = "'id:' + #p0")
    Menu getMenuById(Long menuId);

    @Select("SELECT id,pid,sub_count,type,title,name,component,menu_sort,icon,path,i_frame," +
            "cache,hidden,permission,create_by,update_by,create_time,update_time " +
            "FROM sys_menu ${ew.customSqlSegment}")
    List<Menu> queryAll(@Param(Constants.WRAPPER) LambdaQueryWrapper<Menu> menuDtoQueryWrapper);
}
