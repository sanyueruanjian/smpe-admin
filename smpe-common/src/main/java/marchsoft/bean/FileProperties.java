package marchsoft.bean;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * TODO 文件上传相关需要再次进行改造
 *
 * @author Zheng Jie
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "file")
public class FileProperties {

    /** 文件上传路径 */
    private String path;

    /** 文件大小限制 */
    private Long maxSize;

    /** 头像大小限制 */
    private Long avatarMaxSize;

}
