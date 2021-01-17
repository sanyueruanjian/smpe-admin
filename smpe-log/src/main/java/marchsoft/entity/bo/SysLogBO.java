package marchsoft.entity.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import marchsoft.entity.SysLog;

/**
 * @author ZhangYuKun
 * @date 2021/1/15 16:28
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel
public class SysLogBO extends SysLog {

    @ApiModelProperty("日志操作对应的用户名")
    private String username;
}
