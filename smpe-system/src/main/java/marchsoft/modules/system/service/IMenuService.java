package marchsoft.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import marchsoft.modules.system.entity.Menu;
import marchsoft.modules.system.entity.dto.MenuDTO;
import marchsoft.modules.system.entity.dto.MenuQueryCriteria;
import marchsoft.modules.system.entity.vo.MenuVo;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 系统菜单 服务类
 * </p>
 *
 * @author Wangmingcan
 * @since 2020-08-17
 */
public interface IMenuService extends IService<Menu> {

    /**
     * 功能描述：根据角色id获取
     *
     * @param id 角色id
     * @return Menu集合
     * @author Jiaoqianjin
     * Date: 2020/11/26 17:05
     */
    Set<Menu> findByRoleId(Long id);

    /**
     * 功能描述：根据当前用户id获取菜单
     *
     * @param currentUserId 当前用户id
     * @return /
     * @author Jiaoqianjin
     * Date: 2020/11/26 17:06
     */
    List<MenuDTO> findMenuByUserId(Long currentUserId);

    /**
     * 功能描述：构建菜单树
     *
     * @param menuDtos menuDto集合
     * @return /
     * @author Jiaoqianjin
     * Date: 2020/11/26 17:07
     */
    List<MenuDTO> buildTree(List<MenuDTO> menuDtos);

    /**
     * 功能描述：返回前端所需菜单
     *
     * @param menuDtos menuDto集合
     * @return /
     * @author Jiaoqianjin
     * Date: 2020/11/26 17:09
     */
    List<MenuVo> buildMenus(List<MenuDTO> menuDtos);

    /**
     * 功能描述：导出菜单数据
     *
     * @param menuDtoList 全部菜单数据
     * @param response    response
     * @throws IOException /
     * @author Jiaoqianjin
     * Date: 2020/11/26 17:10
     */
    void download(List<MenuDTO> menuDtoList, HttpServletResponse response) throws IOException;

    /**
     * 功能描述：查询全部数据
     *
     * @param criteria 请求参数
     * @param isQuery  是否查询子菜单
     * @return /
     * @throws Exception /
     * @author Jiaoqianjin
     * Date: 2020/11/23 21:23
     */
    List<MenuDTO> queryAll(MenuQueryCriteria criteria, Boolean isQuery) throws Exception;

    /**
     * 功能描述：懒加载菜单数据
     *
     * @param pid 父级id
     * @return /
     * @author Jiaoqianjin
     * Date: 2020/11/26 17:15
     */
    List<MenuDTO> getMenus(Long pid);

    /**
     * 功能描述：根据菜单id 查询菜单
     *
     * @param id 菜单id
     * @return /
     * @author Jiaoqianjin
     * Date: 2020/11/26 17:24
     */
    MenuDTO findMenuById(Long id);

    /**
     * 功能描述：根据当前菜单信息获取上级数据
     *
     * @param menuDto  本机菜单
     * @param menuList 上级菜单数据
     * @return /
     * @author Jiaoqianjin
     * Date: 2020/11/26 17:25
     */
    List<MenuDTO> getSuperior(MenuDTO menuDto, List<Menu> menuList);

    /**
     * 功能描述：添加菜单
     *
     * @param menu 添加的菜单数据
     * @author Jiaoqianjin
     * Date: 2020/11/26 17:26
     */
    void create(Menu menu);

    /**
     * 功能描述：修改菜单
     *
     * @param menu 菜单数据
     * @author Jiaoqianjin
     * Date: 2020/11/26 17:27
     */
    void updateMenu(Menu menu);

    /**
     * 功能描述：根据ID查询菜单
     *
     * @param id 菜单id
     * @return /
     * @author Jiaoqianjin
     * Date: 2020/11/26 17:27
     */
    Menu findOne(Long id);

    /**
     * 功能描述：递归查询出要删除的菜单数据
     *
     * @param menuList 当前菜单数据
     * @param menuSet  删除的菜单数据
     * @return /
     * @author Jiaoqianjin
     * Date: 2020/11/26 17:28
     */
    Set<Menu> getDeleteMenus(List<Menu> menuList, Set<Menu> menuSet);

    /**
     * 功能描述：批量删除菜单
     *
     * @param menuSet 菜单数据
     * @author Jiaoqianjin
     * Date: 2020/11/26 17:28
     */
    void delete(Set<Menu> menuSet);

    /**
     * 功能描述：根据菜单ID返回所有子节点ID，包含自身ID
     *
     * @param menuList 当前id的子菜单
     * @param menuSet  当前菜单和所有子节点菜单
     * @return /
     * @author Jiaoqianjin
     * Date: 2020/11/28 16:03
     */
    Set<Menu> getChildMenus(List<Menu> menuList, Set<Menu> menuSet);
}
