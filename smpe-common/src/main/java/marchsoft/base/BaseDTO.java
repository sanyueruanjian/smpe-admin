package marchsoft.base;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author RenShiWei
 * Date: 2020/11/16 14:35
 * <p>
 * modify @RenShiWei 2020/11/23 description:时间数据类型 Timestamp -> LocalDateTime
 */
@Getter
@Setter
@ToString
public class BaseDTO implements Serializable {

    private static final long serialVersionUID = - 4289279747443003946L;

    /** 创建者 */
    @ApiModelProperty(hidden = true)
    private Long createBy;

    /** 更新者 */
    @ApiModelProperty(hidden = true)
    private Long updatedBy;

    /** 创建时间 */
    @ApiModelProperty(hidden = true)
    private LocalDateTime createTime;

    /** 更新时间 */
    @ApiModelProperty(hidden = true)
    private LocalDateTime updateTime;

}
