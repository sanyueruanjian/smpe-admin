package marchsoft.modules.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import marchsoft.base.PageVO;
import marchsoft.modules.system.entity.Job;
import marchsoft.modules.system.entity.dto.JobDTO;
import marchsoft.modules.system.entity.dto.JobQueryCriteria;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 岗位 服务类
 * </p>
 *
 * @author Wangmingcan
 * @since 2020-08-17
 */
public interface IJobService extends IService<Job> {

    /**
     * Description:
     * 根据用户id获取
     *
     * @param id: 用户id
     * @return java.util.Set<marchsoft.modules.system.entity.Job>
     * @author liuxingxing
     * @date 2020/11/24 13:07
     **/
    Set<Job> findByUserId(Long id);

    /**
     * Description:
     * 根据ID查询
     *
     * @param id: 岗位id
     * @return marchsoft.modules.system.entity.dto.JobDTO
     * @author liuxingxing
     * @date 2020/11/24 13:08
     **/
    JobDTO findById(Long id);

    /**
     * Description:
     * 查询全部数据
     *
     * @param criteria: Job查询类
     * @return java.util.List<marchsoft.modules.system.entity.dto.JobDTO>
     * @author liuxingxing
     * @date 2020/11/24 13:08
     **/
    List<JobDTO> queryAll(JobQueryCriteria criteria);

    /**
     * Description:
     * 分页查询
     *
     * @param criteria: Job查询类
     * @param pageVO:   分页插件
     * @return java.util.Map<java.lang.String, java.lang.Object>
     * @author liuxingxing
     * @date 2020/11/24 13:09
     **/
    IPage<JobDTO> queryAll(JobQueryCriteria criteria, PageVO pageVO);

    /**
     * Description:
     * 待导出的数据
     *
     * @param queryAll: 所有要查询的JobDTO实体
     * @param response:
     * @throws IOException io异常
     * @author liuxingxing
     * @date 2020/11/24 13:12
     **/
    void download(List<JobDTO> queryAll, HttpServletResponse response) throws IOException;

    /**
     * Description:
     * 创建Job
     *
     * @param resources: 要创建的Job实体
     * @author liuxingxing
     * @date 2020/11/24 13:13
     **/
    void create(Job resources);

    /**
     * Description:
     * 更新Job实体
     *
     * @param resources: 要更新的Job实体
     * @author liuxingxing
     * @date 2020/11/24 13:13
     **/
    void update(Job resources);

    /**
     * Description:
     * 验证此job是否被用户关联
     *
     * @param ids: 部门id集
     * @author liuxingxing
     * @date 2020/11/24 13:16
     **/
    void verification(Set<Long> ids);

    /**
     * Description:
     * 删除
     *
     * @param ids: 部门id集
     * @author liuxingxing
     * @date 2020/11/24 13:16
     **/
    void delete(Set<Long> ids);
}
