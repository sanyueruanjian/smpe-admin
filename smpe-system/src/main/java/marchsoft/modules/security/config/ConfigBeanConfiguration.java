//package marchsoft.modules.security.config;
//
//import marchsoft.modules.security.config.bean.LoginProperties;
//import org.springframework.boot.autoconfigure.security.SecurityProperties;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//
///**
// * description:配置文件转换Pojo类的 统一配置 类
// * <p>
// * 可以解决直接在配置类配置的警告问题
// *
// * @author RenShiWei
// * Date: 2020/11/17 11:38
// **/
//@Configuration
//public class ConfigBeanConfiguration {
//
//    @Bean
//    @ConfigurationProperties(prefix = "login", ignoreUnknownFields = true)
//    public LoginProperties loginProperties() {
//        return new LoginProperties();
//    }
//
//    /**
//     * description:配置SecurityProperties
//     * TODO 会出现bean被两次注入的问题(暂时添加@Primary注解解决）
//     *
//     * @author RenShiWei
//     * Date: 2020/11/17 15:36
//     */
//    @Bean
//    @Primary
//    @ConfigurationProperties(prefix = "jwt", ignoreUnknownFields = true)
//    public SecurityProperties securityProperties() {
//        return new SecurityProperties();
//    }
//
//}
