package marchsoft.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author ZhangYuKun
 * @date 2021/1/22 9:48
 */
@Component
@Getter
public class SysLogConfig {
    @Value("${spring.logging.test}")
    private boolean test;
}
