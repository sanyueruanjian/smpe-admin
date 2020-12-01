package marchsoft.modules.system.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import marchsoft.config.MybatisRedisCache;
import marchsoft.modules.system.entity.Menu;
import marchsoft.modules.system.entity.User;
import marchsoft.modules.system.entity.bo.MenuBO;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Select;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * 功能描述： 系统菜单 Mapper 接口
 *
 * @author Jiaoqianjin
 * Date: 2020/11/23 9:33
 */
@CacheNamespace(implementation = MybatisRedisCache.class, eviction = MybatisRedisCache.class)
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * description 通过角色id和关联表roles_menus查询该角色拥有的菜单
     *
     * @param id 菜单id
     * @return Set<Menu>
     * @author Jiaoqianjin
     * @date 2020-11-23 15:45
     */
    @Select("SELECT m.menu_id,m.pid,m.sub_count,m.type,m.title,m.name,m.component,m.menu_sort,m.icon,m.path," +
            "m.i_frame,m.cache,m.hidden,m.permission,m.create_by,m.update_by,m.create_time,m.update_time " +
            "FROM sys_menu m, sys_roles_menus rm " +
            "WHERE m.menu_id = rm.menu_id AND rm.role_id = ${id}")
    @Result(column = "menu_id", property = "id")
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
    @Select({"<script> SELECT m.menu_id,m.pid,m.sub_count,m.type,m.title,m.name,m.component,m.menu_sort,m.icon,m.path,m.i_frame,m.cache," +
            "m.hidden,m.permission,m.create_by,m.update_by,m.create_time,m.update_time " +
            "FROM sys_menu m, sys_roles_menus r " +
            "WHERE m.menu_id = r.menu_id " +
            "AND r.role_id IN" +
            "<foreach collection='roleIds' item='item' index='index' open='(' separator=',' close=')'> " +
            "#{item}" +
            "</foreach>" +
            "AND type != ${type} order by m.menu_sort asc" +
            "</script>"
    })
    @Result(column = "menu_id", property = "id")
    LinkedHashSet<Menu> findByRoleIdsAndTypeNot(Set<Long> roleIds, int type);

    /**
     * 功能描述：根据Pid查询对应菜单
     *
     * @param queryWrapper 请求参数
     * @return /
     * @author Jiaoqianjin
     * Date: 2020/11/26 11:14
     */
    @Select("SELECT menu_id,pid,sub_count,type,title,name,component,menu_sort,icon,path,i_frame," +
            "cache,hidden,permission,create_by,update_by,create_time,update_time " +
            "FROM sys_menu ${ew.customSqlSegment}")
    @Result(column = "menu_id", property = "id")
    List<Menu> getMenusByPid(@Param(Constants.WRAPPER) LambdaQueryWrapper<Menu> queryWrapper);

    /**
     * 功能描述：根据菜单Id查询菜单（不使用mb单表查询，使用mapper二级缓存）
     *
     * @param menuId 菜单id
     * @return /
     * @author Jiaoqianjin
     * Date: 2020/11/26 11:29
     */
    @Select("SELECT menu_id,pid,sub_count,type,title,name,component,menu_sort,icon,path,i_frame," +
            "cache,hidden,permission,create_by,update_by,create_time,update_time FROM sys_menu " +
            "WHERE menu_id = #{id}")
    @Result(column = "menu_id", property = "id")
    Menu getMenuById(Long menuId);

    @Select("SELECT menu_id,pid,sub_count,type,title,name,component,menu_sort,icon,path,i_frame," +
            "cache,hidden,permission,create_by,update_by,create_time,update_time " +
            "FROM sys_menu ${ew.customSqlSegment}")
    @Result(column = "menu_id", property = "id")
    List<Menu> queryAll(@Param(Constants.WRAPPER)  LambdaQueryWrapper<Menu> menuDtoQueryWrapper);
}
