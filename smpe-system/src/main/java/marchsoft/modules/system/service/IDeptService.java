package marchsoft.modules.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import marchsoft.base.PageVO;
import marchsoft.modules.system.entity.Dept;
import marchsoft.modules.system.entity.dto.DeptDTO;
import marchsoft.modules.system.entity.dto.DeptQueryCriteria;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 部门 服务类
 * </p>
 *
 * @author Wangmingcan
 * @since 2020-08-17
 */
public interface IDeptService extends IService<Dept> {


    /**
     * Description:
     * 根据部门id查询部门
     *
     * @param id: 部门id
     * @return marchsoft.modules.system.entity.dto.DeptDTO
     * @author liuxingxing
     * @date 2020/11/26 15:42
     **/
    DeptDTO findById(Long id);

    /**
     * Description:
     * 根据角色id查询角色所属部门
     *
     * @param id: 角色id
     * @return java.util.Set<marchsoft.modules.system.entity.Dept>
     * @author liuxingxing
     * @date 2020/11/26 15:43
     **/
    Set<Dept> findByRoleId(Long id);

    /**
     * Description:
     * 根据pid(父部门)查询子部门集
     *
     * @param pid: 父级部门id
     * @return java.util.List<marchsoft.modules.system.entity.Dept>
     * @author liuxingxing
     * @date 2020/11/26 15:44
     **/
    List<Dept> findByPid(long pid);

    /**
     * Description:
     * 获取 deptList中部门与其子部门的id
     *
     * @param deptList: 需要查找部门集合
     * @return java.util.List<java.lang.Long>
     * @author liuxingxing
     * @date 2020/11/26 15:45
     **/
    List<Long> getDeptChildren(List<Dept> deptList);

    /**
     * Description:
     * 导出queryAll的数据
     *
     * @param queryAll: 待到处数据
     * @param response: 服务器响应对象
     * @throws IOException io异常
     * @author liuxingxing
     * @date 2020/11/26 15:45
     **/
    void download(List<DeptDTO> queryAll, HttpServletResponse response) throws IOException;

    /**
     * Description:
     * 根据criteria条件和isQuery查询
     * todo isQuery的作用
     *
     * @param criteria: Dept查询条件
     * @param isQuery:  是否判断当前用户数据权限
     * @return java.util.List<marchsoft.modules.system.entity.dto.DeptDTO>
     * @author liuxingxing
     * @date 2020/11/26 15:45
     **/
    List<DeptDTO> queryAll(DeptQueryCriteria criteria, Boolean isQuery);

    /**
     * Description:
     * 根据criteria条件和isQuery查询
     * todo isQuery的作用
     *
     * @param criteria: Dept查询条件
     * @param isQuery:  是否判断当前用户数据权限
     * @param pageVO:   分页条件
     * @return java.util.List<marchsoft.modules.system.entity.dto.DeptDTO>
     * @author liuxingxing
     * @date 2020/11/26 15:45
     **/
    IPage<DeptDTO> queryAll(DeptQueryCriteria criteria, PageVO pageVO, Boolean isQuery);

    /**
     * Description:
     * 根据当前部门获取所有上级数据(递归使用找上级的上级)
     * 如果是顶级部门就获取所有顶级部门
     * FIXME 递归使用 后续会优化
     *
     * @param deptDTO: 当前部门
     * @param depts:   递归缓存数组（返回的结果）
     * @return java.util.List<marchsoft.modules.system.entity.dto.DeptDTO>
     * @author liuxingxing
     * @date 2020/11/26 15:45
     **/
    List<DeptDTO> getSuperior(DeptDTO deptDTO, List<Dept> depts);

    /**
     * Description:
     * 根据传入的部门集合构建部门树形结构
     *
     * @param deptDTOList: 部门集合
     * @return java.lang.Object
     * @author liuxingxing
     * @date 2020/11/26 15:45
     **/
    List<DeptDTO> buildTree(List<DeptDTO> deptDTOList);


    /**
     * Description:
     * 新增部门
     *
     * @param dept: 新增部门实体
     * @author liuxingxing
     * @date 2020/11/26 15:45
     **/
    void create(Dept dept);

    /**
     * Description:
     * 修改部门
     *
     * @param dept: 修改部门实体
     * @author liuxingxing
     * @date 2020/11/26 15:46
     **/
    void updateDept(Dept dept);

    /**
     * Description:
     * 获取待删除的部门（递归调用）
     * FIXME 递归获取删除部门列表（包含本部门和本部门的子部门）
     *
     * @param deptList:    要删除部门
     * @param deptDTOList:
     * @return java.util.Set<marchsoft.modules.system.entity.dto.DeptDTO>
     * @author liuxingxing
     * @date 2020/11/26 15:46
     **/
    Set<DeptDTO> getDeleteDepts(List<Dept> deptList, Set<DeptDTO> deptDTOList);

    /**
     * Description:
     * 验证是否被角色或用户关联
     *
     * @param deptDTOList:
     * @author liuxingxing
     * @date 2020/11/26 15:46
     **/
    void verification(Set<DeptDTO> deptDTOList);

    /**
     * Description:
     * 删除部门实体
     *
     * @param deptDTOList: 删除的部门
     * @author liuxingxing
     * @date 2020/11/26 15:46
     **/
    void deleteDept(Set<DeptDTO> deptDTOList);

}
