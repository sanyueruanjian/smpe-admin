package marchsoft.modules.system.entity.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Zheng Jie
 * 公共查询类
 */
@Data
public class MenuQueryCriteria {

    private String blurry;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

//    private Boolean pidIsNull;

    private Long pid;
}
