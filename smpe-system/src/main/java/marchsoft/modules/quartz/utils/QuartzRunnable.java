package marchsoft.modules.quartz.utils;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import marchsoft.exception.BadRequestException;
import marchsoft.utils.SpringContextHolder;
import marchsoft.utils.StringUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * @author lixiangxiang
 * @description 执行定时任务
 * @date 2021/1/15 9:57
 */
@Slf4j
public class QuartzRunnable implements Callable<Object> {

    private final Object target;
    private final Method method;
    private final String params;

    QuartzRunnable(String beanName,String methodName,String  params) throws NoSuchMethodException, ClassNotFoundException {
        //获取到bean对象
        this.target = SpringContextHolder.getBean(beanName);
        //获取到参数
        this.params = params;
        //如果参数不为空
        if(StringUtils.isNotBlank(params)) {
            //反射获取到方法 两个参数 分别是方法名和参数类型
            this.method = target.getClass().getDeclaredMethod(methodName,String.class);
        }else {
            this.method = target.getClass().getDeclaredMethod(methodName);
        }
    }

    /***
     * description: 线程回调函数 反射执行方法
     *
     * @author: lixiangxiang
     * @return java.lang.Object
     * @date 2021/1/15 11:14
     */
    @Override
    public Object call() throws Exception {
        ReflectionUtils.makeAccessible(method);
        if(StringUtils.isNotBlank(params)) {
            method.invoke(target,params);
        }else {
            method.invoke(target);
        }
        return null;
    }
}
