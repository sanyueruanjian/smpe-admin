package marchsoft.modules.quartz.service;

import marchsoft.base.PageVO;
import marchsoft.modules.quartz.entity.QuartzJob;
import marchsoft.modules.quartz.entity.QuartzLog;
import marchsoft.modules.quartz.entity.dto.JobQueryCriteria;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * @author lixiangxiang
 * @description /
 * @date 2021/1/14 16:25
 */
public interface QuartzJobService {

    /**
     * description: 创建定时任务
     *
     * @author: lixiangxiang
     * @param resources /
     * @return void
     * @date 2021/1/14 16:54
     */
    void create(QuartzJob resources);

    /**
     * description: 执行子任务
     *
     * @author: lixiangxiang
     * @param tasks /
     * @return void
     * @date 2021/1/15 15:55
     * @throws InterruptedException /
     */
    void executionSubJob(String[] tasks) throws InterruptedException;

    /**
     *
     * description: 更改定时任务状态
     *
     * @author: lixiangxiang
     * @param quartzJob /
     * @return void
     * @date 2021/1/18 19:33
     */
    void updateIsPause(QuartzJob quartzJob);

    /**
     * description: 查询全部
     *
     * @author: lixiangxiang
     * @param criteria 查询条件
     * @return java.util.List<marchsoft.modules.quartz.entity.QuartzJob>
     * @date 2021/1/20 16:15
     */
    List<QuartzJob> queryAll(JobQueryCriteria criteria);

    /**
     * description: 查询全部
     *
     * @author: lixiangxiang
     * @param criteria 条件
     * @param pageVO 分页vo
     * @return java.lang.Object
     * @date 2021/1/18 19:34
     */
    Object queryAll(JobQueryCriteria criteria, PageVO pageVO);

    /**
     * 根据ID查询
     * @param id ID
     * @return /
     */
    QuartzJob findById(Long id);

    /**
     * description: 修改任务
     *
     * @author: lixiangxiang
     * @param resources /
     * @return void
     * @date 2021/1/19 21:27
     */
    void update(QuartzJob resources);

    /**
     * description: 删除任务
     *
     * @author: lixiangxiang
     * @param ids /
     * @return void
     * @date 2021/1/19 22:03
     */
    void delete(Set<Long> ids);

    /**
     * description: 立即执行任务
     *
     * @author: lixiangxiang
     * @param resource /
     * @return void
     * @date 2021/1/20 11:46
     */
    void execution(QuartzJob resource);

    /**
     * description: 下载
     *
     * @author: lixiangxiang
     * @param queryAll 导出的数据
     * @param response /
     * @return void
     * @date 2021/1/20 16:18
     * @throws IOException /
     */
    void download(List<QuartzJob> queryAll, HttpServletResponse response) throws IOException;

    /**
     * description: 分页查询日志
     *
     * @author: lixiangxiang
     * @param criteria 条件
     * @param pageVO 分页参数
     * @return java.lang.Object
     * @date 2021/1/21 10:38
     */
    Object queryAllLog(JobQueryCriteria criteria, PageVO pageVO);

    /**
     * description: 导出日志信息
     *
     * @author: lixiangxiang
     * @param response /
     * @param quartzLogs /
     * @return void
     * @date 2021/1/21 16:02
     */
    void downloadLog(HttpServletResponse response, List<QuartzLog> quartzLogs) throws IOException;

    /**
     * description: 查询全部日志信息
     *
     * @author: lixiangxiang
     * @param criteria 条件
     * @return java.util.List<marchsoft.modules.quartz.entity.QuartzLog>
     * @date 2021/1/21 16:06
     */
    List<QuartzLog>  queryAllLog(JobQueryCriteria criteria);


}
