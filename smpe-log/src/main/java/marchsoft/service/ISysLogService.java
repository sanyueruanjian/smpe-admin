package marchsoft.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import marchsoft.base.IBasicService;
import marchsoft.entity.SysLog;
import marchsoft.entity.dto.SysLogDTO;
import marchsoft.entity.dto.SysLogQueryCriteria;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.scheduling.annotation.Async;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 日志 服务类
 * </p>
 *
 * @author RenShiWei
 * @since 2020-12-14
 */
public interface ISysLogService extends IBasicService<SysLog> {

    /**
     * description:保存日志记录
     * '@Async'异步调用
     *
     * @param joinPoint /
     * @param sysLog    /
     * @author RenShiWei
     * Date: 2020/12/14 17:38
     */
    @Async
    void save(ProceedingJoinPoint joinPoint, SysLog sysLog);

    /**
     * description: 分页查询日志记录
     *
     * @param criteria /
     * @param page     /
     * @return 日志的详细记录
     * Date: 2021/1/14 15:02
     * @author ZhangYuKun
     */
    IPage<SysLogDTO> queryAll(SysLogQueryCriteria criteria, IPage<SysLog> page);

    /**
     * description: 查询全部日志记录
     *
     * @param criteria /
     * @return 所有的日志详细记录
     * @author ZhangYuKun
     * Date: 2021/1/14:20:48
     */
    List<SysLogDTO> queryAll(SysLogQueryCriteria criteria);

    /**
     * description: 查询异常日志详情信息
     *
     * @param id /
     * @return 异常日志详情
     * @author ZhangYuKun
     * Date: 2021/1/15 18:45
     */
    SysLogDTO findByErrDetail(Long id);

    /**
     * description:导出日志
     *
     * @param sysLogDTOList 待导出的日志
     * @param response   /
     * @throws IOException IO异常
     * @author ZhangYuKun
     * Date: 2021/1/16 16:54
     */
    void download(List<SysLogDTO> sysLogDTOList, HttpServletResponse response) throws IOException;

    /**
     * description:删除所有错误日志
     *
     * @author ZhangYuKun
     * Date: 2021/1/15 22:13
     */
    void delAllByError();

    /**
     * description:删除所有INFO日志
     *
     * @author ZhangYuKun
     * Date: 2021/1/16 16:40
     */
    void delAllByInfo();
}
