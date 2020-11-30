package marchsoft.modules.system.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * description
 *
 * @author RenShiWei
 * Date: 2020/11/24 17:26
 */
@Data
public class MenuQueryCriteria {

    @ApiModelProperty(value = "模糊查询参数，菜单标题or菜单组件or菜单权限")
    private String blurry;

    @ApiModelProperty(value = "开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "父级id")
    private Long pid;
}
