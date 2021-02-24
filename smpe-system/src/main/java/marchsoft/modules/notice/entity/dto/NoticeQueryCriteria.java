package marchsoft.modules.notice.entity.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Wangmingcan
 * @date 2021/2/19 16:20
 * @description
 */
@Data
public class NoticeQueryCriteria {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 通知类型
     */
    private String type;

    /**
     * 是否已读，1已读，0未读
     */
    private Integer read = 0;

    /**
     * 开始时间
     */
    private LocalDateTime stratTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

}
