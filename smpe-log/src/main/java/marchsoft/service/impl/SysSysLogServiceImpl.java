package marchsoft.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marchsoft.annotation.Log;
import marchsoft.base.BasicServiceImpl;
import marchsoft.config.SysLogConfig;
import marchsoft.entity.SysLog;
import marchsoft.entity.bo.SysLogBO;
import marchsoft.entity.dto.SysLogDTO;
import marchsoft.entity.dto.SysLogQueryCriteria;
import marchsoft.enums.SysLogEnum;
import marchsoft.mapper.SysLogMapper;
import marchsoft.service.ISysLogService;
import marchsoft.service.mapstruct.SysLogMapStruct;
import marchsoft.utils.FileUtils;
import marchsoft.utils.PageUtil;
import marchsoft.utils.SecurityUtils;
import marchsoft.utils.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

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
    private final SysLogConfig sysLogConfig;
    private final SysLogMapper sysLogMapper;
    private final SysLogMapStruct sysLogMapStruct;

    /**
     * description:保存日志记录
     *
     * @param joinPoint /
     * @param sysLog    /
     * @author RenShiWei, ZhangYuKun
     * Date: 2020/12/14 17:38
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(ProceedingJoinPoint joinPoint, SysLog sysLog) {
        // 判断是否取消日志实例化
        if (ObjectUtils.isNotNull(sysLogConfig.isTest()) && !sysLogConfig.isTest()) {
            return;
        }
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
            log.error(StrUtil.format("【接口切面日志保存失败】操作人id：{}", SecurityUtils.getCurrentUserId()));
        }
    }

    /**
     * description: 分页查询日志信息
     *
     * @param criteria /
     * @param page     /
     * @return 日志的详细信息
     * @author ZhangYuKun
     * Date: 2021/1/14 20:14
     */
    @Override
    public IPage<SysLogDTO> queryAll(SysLogQueryCriteria criteria, IPage<SysLog> page) {
        IPage<SysLogBO> sysLogPage = sysLogMapper.queryLogDetailsListPage(buildSysLogQueryCriteria(criteria), page);
        List<SysLogDTO> sysLogDTOList = sysLogMapStruct.toDto(sysLogPage.getRecords());
        return PageUtil.toMapStructPage(sysLogPage, sysLogDTOList);
    }

    /**
     * description: 查询所有日志信息
     *
     * @param criteria /
     * @return 所有日志的详细信息
     * @author ZhangYuKun
     * Date: 2021/1/14 20:50
     */
    @Override
    public List<SysLogDTO> queryAll(SysLogQueryCriteria criteria) {
        List<SysLogBO> sysLogList = sysLogMapper.queryLogDetailsList(buildSysLogQueryCriteria(criteria));
        return sysLogMapStruct.toDto(sysLogList);
    }

    /**
     * description:查询异常详情
     *
     * @param id /
     * @return 异常日志的详细信息
     * @author ZhangYuKun
     * Date: 2021/1/15 21:12
     */
    @Override
    public SysLogDTO findByErrDetail(Long id) {
        SysLogBO sysLogBO = sysLogMapper.queryErrDetail(id);
        return sysLogMapStruct.toDto(sysLogBO);
    }

    /**
     * description:导出日志
     *
     * @param sysLogDTOList /
     * @param response      /
     * @author ZhangYuKun
     * Date: 2021/1/17 9:33
     */
    @Override
    public void download(List<SysLogDTO> sysLogDTOList, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SysLogDTO sysLogDTO : sysLogDTOList) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("操作用户名", sysLogDTO.getUsername());
            map.put("描述", sysLogDTO.getDescription());
            map.put("方法名", sysLogDTO.getMethod());
            map.put("参数", sysLogDTO.getParams());
            map.put("请求ip", sysLogDTO.getRequestIp());
            map.put("请求耗时（毫秒值）", sysLogDTO.getRequestTime());
            map.put("地址", sysLogDTO.getAddress());
            map.put("浏览器", sysLogDTO.getBrowser());
            map.put("详细异常", sysLogDTO.getExceptionDetail());
            map.put("创建时间", ObjectUtil.isNull(sysLogDTO.getCreateTime()) ? null :
                    LocalDateTimeUtil.format(sysLogDTO.getCreateTime()
                            , DatePattern.NORM_DATETIME_FORMATTER));
            list.add(map);
        }
        FileUtils.downloadExcel(list, response);
    }

    /**
     * description:删除所有错误日志
     *
     * @author ZhangYuKun
     * Date: 2021/1/17 9:35
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delAllByError() {
        sysLogMapper.deleteByLogType(SysLogEnum.ERROR.getLogType());
    }

    /**
     * description:删除所有INFO日志
     *
     * @author ZhangYuKun
     * Date: 2021/1/17 9:35
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delAllByInfo() {
        sysLogMapper.deleteByLogType(SysLogEnum.INFO.getLogType());
    }

    /**
     * description: 构建查询角色的LambdaQueryWrapper
     *
     * @param criteria /
     * @author ZhangYuKun
     * Date: 2021/1/14 20:12
     */
    public LambdaQueryWrapper<SysLog> buildSysLogQueryCriteria(SysLogQueryCriteria criteria) {
        LambdaQueryWrapper<SysLog> wrapper = new LambdaQueryWrapper<>();
        // 设置查询的日志类型
        wrapper.eq(SysLog::getLogType, criteria.getLogType());
        // 判断查询条件是否为空
        if (StrUtil.isNotEmpty(criteria.getBlurry())) {
            wrapper.and(i -> i.like(SysLog::getDescription, criteria.getBlurry()).or()
                    .like(SysLog::getAddress, criteria.getBlurry()).or()
                    .like(SysLog::getRequestIp, criteria.getBlurry()).or()
                    .like(SysLog::getMethod, criteria.getBlurry()).or()
                    .like(SysLog::getParams, criteria.getBlurry()));
        }
        // 判断是否添加创建时间范围条件
        if (ObjectUtil.isNotNull(criteria.getStartTime()) && ObjectUtil.isNotNull(criteria.getEndTime())) {
            wrapper.between(SysLog::getCreateTime, criteria.getStartTime(), criteria.getEndTime());
        }
        return wrapper;
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
                if (!StrUtil.isEmpty(requestParam.value())) {
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

