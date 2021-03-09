package marchsoft.modules.notice.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import marchsoft.base.BaseDTO;
import marchsoft.modules.notice.entity.NoticeTemplate;
import marchsoft.modules.notice.entity.NoticeTarget;

import java.io.Serializable;
import java.util.List;

/**
 * @author Wangmingcan
 * @date 2021/2/19 9:40
 * @description
 */

@Data
public class NoticeDTO extends BaseDTO implements Serializable {

    @ApiModelProperty(hidden = true)
    private Long id;

    @ApiModelProperty(value = "消息模板id")
    private Long templateId;

    @ApiModelProperty(value = "消息内容id")
    private Long targetId;

    @ApiModelProperty(value = "接收者id集合")
    private List<Long> userIds;

    @ApiModelProperty(value = "通知类型like,reply,recommand,notification")
    private String type;

    @ApiModelProperty(hidden = true)
    private NoticeTemplate noticeTemplate;

    @ApiModelProperty(hidden = true)
    private NoticeTarget noticeTarget;

    @ApiModelProperty(hidden = true)
    private Integer read;

}
