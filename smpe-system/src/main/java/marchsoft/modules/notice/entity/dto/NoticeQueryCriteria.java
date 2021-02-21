package marchsoft.modules.notice.entity.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Wangmingcan
 * @date 2021/2/19 11:05
 * @description
 */
@Data
public class NoticeQueryCriteria {

    /**
     * 通知模板标题
     */
    private String title;

    /**
     * 通知模板内容
     */
    private String content;

    /**
     * 通知模板类型
     */
    private String type;

    /**
     * 开始时间
     */
    private LocalDateTime stratTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

}
