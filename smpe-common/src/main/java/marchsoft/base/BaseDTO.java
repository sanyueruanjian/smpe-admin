package marchsoft.base;

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

    /** 创建者 */
    private String createBy;

    /** 更新者 */
    private String updatedBy;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;

}
