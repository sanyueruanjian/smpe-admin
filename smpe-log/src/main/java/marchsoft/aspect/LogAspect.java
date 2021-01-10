package marchsoft.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marchsoft.entity.SysLog;
import marchsoft.enums.SysLogEnum;
import marchsoft.service.ISysLogService;
import marchsoft.utils.RequestHolder;
import marchsoft.utils.SecurityUtils;
import marchsoft.utils.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

/**
 * description:日志切面
 *
 * @author RenShiWei
 * Date: 2020/12/14 16:03
 */
@Component
@Aspect
@Slf4j
@RequiredArgsConstructor
public class LogAspect {

    private final ISysLogService sysLogService;

    /** 存储当前时间，便于计算请求耗时（使用后，必须要回收，即调用remove方法） */
    ThreadLocal<Long> currentTime;

    /**
     * description:配置切入点
     *
     * @author RenShiWei
     * Date: 2020/12/14 16:56
     */
    @Pointcut("@annotation(marchsoft.annotation.Log)")
    public void logPointcut() {
        // 该方法无方法体,主要为了让同类中其他方法使用此切入点
    }

    /**
     * description:配置环绕通知,使用在方法logPointcut()上注册的切入点
     *
     * @param joinPoint /
     * @author RenShiWei
     * Date: 2020/12/14 17:36
     */
    @Around("logPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result;
        currentTime = new ThreadLocal<>();
        currentTime.set(Instant.now().toEpochMilli());
        result = joinPoint.proceed();
        SysLog sysLog = new SysLog();
        //设置请求耗时
        sysLog.setRequestTime(Instant.now().toEpochMilli() - currentTime.get());
        //回收ThreadLocal资源
        currentTime.remove();
        HttpServletRequest request = RequestHolder.getHttpServletRequest();
        sysLog.setUserId(getUserId())
                .setBrowser(StringUtils.getBrowser(request))
                .setRequestIp(StringUtils.getIp(request))
                .setLogType(SysLogEnum.INFO.getLogType())
        ;
        sysLogService.save(joinPoint, sysLog);
        return result;
    }

    /**
     * description:配置异常通知
     *
     * @param joinPoint join point for advice
     * @param e         exception
     * @author RenShiWei
     * Date: 2020/12/14 17:56
     */
    @AfterThrowing(pointcut = "logPointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        SysLog sysLog = new SysLog();
        //设置请求耗时
        sysLog.setRequestTime(Instant.now().toEpochMilli() - currentTime.get());
        //回收ThreadLocal资源
        currentTime.remove();
        HttpServletRequest request = RequestHolder.getHttpServletRequest();
        sysLog.setUserId(getUserId())
                .setLogType(SysLogEnum.ERROR.getLogType())
                // TODO:@RenShiWei 2020/12/14 description:e.getMessage()是否有必要换成堆栈信息
                .setExceptionDetail(e.getMessage())
                .setBrowser(StringUtils.getBrowser(request))
                .setRequestIp(StringUtils.getIp(request))
        ;
        sysLogService.save((ProceedingJoinPoint) joinPoint, sysLog);
    }

    /**
     * description: 获取当前登录用户的id（异常返回0）
     *
     * @return /
     * @author RenShiWei
     * Date: 2020/12/14 17:36
     */
    private Long getUserId() {
        try {
            return SecurityUtils.getCurrentUserIdThrow();
        } catch (Exception e) {
            return 0L;
        }
    }


}
