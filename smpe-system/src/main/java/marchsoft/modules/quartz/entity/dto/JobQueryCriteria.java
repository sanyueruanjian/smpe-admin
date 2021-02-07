package marchsoft.modules.quartz.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author lixiangxiang
 * @description /
 * @date 2021/1/18 19:58
 */
@Data
public class JobQueryCriteria {

    @ApiModelProperty(value = "任务名称")
    private String jobName;

    @ApiModelProperty(value = "是否成功")
    private Boolean isSuccess;

    @ApiModelProperty(value = "结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "开始时间")
    private LocalDateTime startTime;

}
