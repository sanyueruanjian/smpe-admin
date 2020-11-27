package marchsoft.modules.system.entity.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Zheng Jie
 * @date 2019-03-25
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
     * 父部门id是否为空
     */
    private Boolean pidIsNull;

    /**
     * 开始时间
     */
    private LocalDateTime stratTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

}
