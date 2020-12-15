package marchsoft.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marchsoft.annotation.Log;
import marchsoft.base.BasicServiceImpl;
import marchsoft.entity.SysLog;
import marchsoft.mapper.SysLogMapper;
import marchsoft.service.ISysLogService;
import marchsoft.utils.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 日志 服务实现类
 * </p>
 *
 * @author RenShiWei
 * @since 2020-12-14
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysSysLogServiceImpl extends BasicServiceImpl<SysLogMapper, SysLog> implements ISysLogService {

    private final SysLogMapper sysLogMapper;

    /**
     * description:保存日志记录
     *
     * @param joinPoint /
     * @param sysLog    /
     * @author RenShiWei
     * Date: 2020/12/14 17:38
     */
    @Override
    public void save(ProceedingJoinPoint joinPoint, SysLog sysLog) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Log aopLog = method.getAnnotation(Log.class);

        // 方法路径
        String methodName = joinPoint.getTarget().getClass().getName() + "." + signature.getName() + "()";

        // 描述
        if (sysLog != null) {
            sysLog.setDescription(aopLog.value());
        }

        assert sysLog != null;
        sysLog.setAddress(StringUtils.getCityInfo(sysLog.getRequestIp()))
                .setMethod(methodName)
                .setParams(getParameter(method, joinPoint.getArgs()))
        ;

        int count = sysLogMapper.insert(sysLog);
        if (count <= 0) {
            log.error("【接口切面日志保存失败】");
        }
    }

    /**
     * description:解析@Log标识的接口参数
     *
     * @param method 方法
     * @param args   参数
     * @return 参数json字符串
     * @author RenShiWei
     * Date: 2020/12/15 10:06
     */
    private String getParameter(Method method, Object[] args) {
        List<Object> argList = new ArrayList<>();
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            //将RequestBody注解修饰的参数作为请求参数
            RequestBody requestBody = parameters[i].getAnnotation(RequestBody.class);
            if (requestBody != null) {
                argList.add(args[i]);
            }
            //将RequestParam注解修饰的参数作为请求参数
            RequestParam requestParam = parameters[i].getAnnotation(RequestParam.class);
            if (requestParam != null) {
                Map<String, Object> map = new HashMap<>();
                String key = parameters[i].getName();
                if (! StrUtil.isEmpty(requestParam.value())) {
                    key = requestParam.value();
                }
                map.put(key, args[i]);
                argList.add(map);
            }
        }
        if (argList.size() == 0) {
            return "";
        }
        return argList.size() == 1 ? JSON.toJSONString(argList.get(0)) : JSON.toJSONString(argList);
    }


}

