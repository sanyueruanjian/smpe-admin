package marchsoft.aspect;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * description:配置mybatis的切面
 *
 * @author liuxingxing
 * @date 2020.12.05 09:48
 */
@Slf4j
@Aspect
@Component
public class MybatisAspectj {

    /**
     * description:配置切面织入点
     *
     * @author liuxingxing
     * Date: 2020/12/5 10:35
     */
    @Pointcut("execution(public * com.baomidou.mybatisplus.core.mapper.BaseMapper.selectOne(..))")
    public void selectOneAspect() {}

    /**
     * description:在执行BaseMapper的selectOne方法前的操作<P>
     * 添加"LIMIT 1"限制，解决selectOne报错的问题
     *
     * @author liuxingxing
     * Date: 2020/12/5 10:35
     */
    @Before("selectOneAspect()")
    public void beforeSelect(JoinPoint point) {
        Object arg = point.getArgs()[0];
        if (arg instanceof AbstractWrapper) {
            //强转参数类型
            AbstractWrapper<?, ?, ?> abstractWrapper = Convert.convert(AbstractWrapper.class, arg);
            log.info("BaseMapper的selectOne设置切面成功");
            abstractWrapper.last("LIMIT 1");
        }
    }

}
