package marchsoft.modules.system.entity.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * description:DeptQueryCriteria
 *
 * @author RenShiWei
 * Date: 2020/11/24 17:26
 */
@Data
public class DeptQueryCriteria {

    /**
     * 部门名称
     */
    private String name;

    /**
     * 部门是否可用
     */
    private Boolean enabled;

    /**
     * 父部门id
     */
    private Long pid;


    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

}
