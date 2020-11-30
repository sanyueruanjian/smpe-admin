package marchsoft.modules.system.entity.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * description:JobQueryCriteria
 *
 * @author RenShiWei
 * Date: 2020/11/24 17:26
 */
@Data
@NoArgsConstructor
public class JobQueryCriteria {

    /**
     * 岗位名称
     */
    private String name;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 开始时间
     */
    private LocalDateTime stratTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;
}
