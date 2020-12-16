package marchsoft.service;

import marchsoft.base.IBasicService;
import marchsoft.entity.SysLog;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.scheduling.annotation.Async;

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

}
