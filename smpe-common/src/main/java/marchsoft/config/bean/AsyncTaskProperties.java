package marchsoft.config.bean;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * description:线程池配置属性类
 *
 * @author RenShiWei Date: 2020/7/12 21:58
 */
@Data
@Component
@ConfigurationProperties(prefix = "task.pool")
public class AsyncTaskProperties {

    /** 核心池大小 */
    private int corePoolSize;

    /** 线程池最大线程数 */
    private int maxPoolSize;

    /** 线程活跃时间（以秒为单位） */
    private int keepAliveSeconds;

    /** 阻塞队列容量 */
    private int queueCapacity;
}
