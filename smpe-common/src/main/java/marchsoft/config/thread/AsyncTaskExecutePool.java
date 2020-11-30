package marchsoft.config.thread;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marchsoft.config.bean.AsyncTaskProperties;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;


/**
 * description:异步任务线程池装配类
 *
 * @author RenShiWei
 * Date: 2020/7/12 21:58
 * '@EnableAsync' 开启异步线程，
 * 重写AsyncConfigurer的方法：使用@Async标注方法执行异步任务，每次都要添加注解，可以重写spring默认线程池的方式使用的时候，
 * 只需要加@Async注解就可以，不用去声明线程池类。
 */
@Slf4j
@Configuration
@EnableAsync
@RequiredArgsConstructor
public class AsyncTaskExecutePool implements AsyncConfigurer {

    /** 注入配置类 */
    private final AsyncTaskProperties config;

    /**
     * description: 设置线程池的参数配置
     *
     * @author RenShiWei
     * Date: 2020/8/11 15:44
     */
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 核心线程池大小
        executor.setCorePoolSize(config.getCorePoolSize());
        // 最大线程数
        executor.setMaxPoolSize(config.getMaxPoolSize());
        // 队列容量
        executor.setQueueCapacity(config.getQueueCapacity());
        // 活跃时间
        executor.setKeepAliveSeconds(config.getKeepAliveSeconds());
        // 线程名字前缀
        executor.setThreadNamePrefix("el-async-");
        // setRejectedExecutionHandler：当pool已经达到max size的时候，如何处理新任务
        // CallerRunsPolicy：不在新线程中执行任务，而是由调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

    /**
     * description: 异步任务的异常处理
     *
     * @author RenShiWei
     * Date: 2020/8/11 15:40
     */
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (throwable, method, objects) -> {
            log.error("====" + throwable.getMessage() + "====", throwable);
            log.error("exception method:" + method.getName());
        };
    }

}
