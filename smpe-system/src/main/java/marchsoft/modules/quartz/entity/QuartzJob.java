package marchsoft.modules.quartz.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import marchsoft.base.BasicModel;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

public class QuartzJob extends BasicModel<QuartzJob> implements Serializable {
    public static final String JOB_KEY = "JOB_KEY";

    @ApiModelProperty(value = "ID")
    @TableId(value = "job_id")
    private Long id;

    @ApiModelProperty(value = "用于子任务唯一标识", hidden = true)
    private String uuid;

    @ApiModelProperty(value = "定时器名称")
    private String jobName;

    @NotBlank
    @ApiModelProperty(value = "Bean名称")
    private String beanName;

    @NotBlank
    @ApiModelProperty(value = "方法名称")
    private String methodName;

    @ApiModelProperty(value = "参数")
    private String params;

    @NotBlank
    @ApiModelProperty(value = "cron表达式")
    private String cronExpression;

    @ApiModelProperty(value = "状态，暂时或启动")
    private Boolean isPause = false;

    @ApiModelProperty(value = "负责人")
    private String personInCharge;

    @ApiModelProperty(value = "报警邮箱")
    private String email;

    @ApiModelProperty(value = "子任务")
    private String subTask;

    @ApiModelProperty(value = "失败后暂停")
    private Boolean pauseAfterFailure;

    @NotBlank
    @ApiModelProperty(value = "备注")
    private String description;

}
