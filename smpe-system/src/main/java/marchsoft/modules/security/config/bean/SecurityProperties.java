package marchsoft.modules.security.config.bean;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * description:Jwt参数配置
 * 读取yml中对jwt的配置，注入到以下属性当中
 *
 * @author RenShiWei
 * Date: 2020/8/3 14:56
 **/
@Data
@ConfigurationProperties(prefix = "jwt")
@Configuration
public class SecurityProperties {

    /** Request Headers ： Authorization */
    private String header;

    /** 令牌前缀，最后留个空格 Bearer */
    private String tokenStartWith;

    /** 必须使用最少88位的Base64对该令牌进行编码 */
    private String base64Secret;

    /** 令牌过期时间 此处单位/毫秒 */
    private Long tokenValidityInSeconds;

    /** 在线用户 key，根据 key 查询 redis 中在线用户的数据 */
    private String onlineKey;

    /** 验证码 key */
    private String codeKey;

    /** token 续期检查 */
    private Long detect;

    /** 续期时间 */
    private Long renew;

    /** 重写get方法，加入空格，防止空格造成影响 */
    public String getTokenStartWith() {
        return tokenStartWith + " ";
    }
}
