package marchsoft.modules.system.entity.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * description:角色查询参数构造
 *
 * @author RenShiWei
 * Date: 2020/11/26 15:39
 */
@Data
public class RoleQueryCriteria {

    /** 模糊查询参数（角色名、描述） */
    private String blurry;

    private LocalDateTime startTime;

    private LocalDateTime endTime;
}
