package marchsoft.modules.notice.entity.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Wangmingcan
 * @date 2021/2/19 16:00
 * @description
 */
@Data
public class NoticeTargetQueryCriteria {

    /**
     * 通知内容
     */
    private String content;

    /**
     * 通知链接
     */
    private String link;

    /**
     * 开始时间
     */
    private LocalDateTime stratTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

}
