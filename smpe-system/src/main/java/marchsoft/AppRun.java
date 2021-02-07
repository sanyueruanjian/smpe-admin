package marchsoft;

import io.swagger.annotations.Api;
import marchsoft.annotation.rest.AnonymousGetMapping;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RestController;

/**
 * description:启动类
 *
 * @author RenShiWei
 * Date: 2020/11/16 10:25
 * <p>
 * '@Api(hidden = true)'     //在swagger中隐藏该类资源
 * '@EnableTransactionManagement' //开启事务支持，之后可以使用@Transactiona
 **/

@RestController
@Api(hidden = true)
@SpringBootApplication
@EnableTransactionManagement(proxyTargetClass = true)
@MapperScan({
        "marchsoft.modules.system.mapper",
        "marchsoft.modules.quartz.mapper",
        "marchsoft.mapper"
})
public class AppRun {

    public static void main(String[] args) {
        SpringApplication.run(AppRun.class, args);
    }

    /**
     * 访问首页提示
     *
     * @return /
     */
    @AnonymousGetMapping("/")
    public String index() {
        return "Backend service started successfully";
    }

}

