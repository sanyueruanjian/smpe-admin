package marchsoft.entity.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 日志查询类
 *
 * @author Zheng Jie
 * @date 2019-6-4 09:23:07
 */
@Data
public class SysLogQueryCriteria {

    /** 根据description,address,requestIp,method,params模糊查询 */
    private String blurry;

    /** 日志类型 */
    private String logType;

    /** 开始时间 */
    private LocalDateTime startTime;

    /** 结束时间 */
    private LocalDateTime endTime;

}
