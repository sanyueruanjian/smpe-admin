package marchsoft.config.bean;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * description:Swagger配置，从yml读取
 *
 * @author RenShiWei
 * Date: 2020/11/16 16:55
 **/
@Data
@Configuration
@ConfigurationProperties(prefix = "swagger")
public class SwaggerProperties {

    /** 是否启用swagger */
    private Boolean enabled;

    /** 描述 */
    private String description;

    /** 标题 */
    private String title;

    /** 版本 */
    private String version;

    /** ip和host */
    private String swaggerHost;

}
