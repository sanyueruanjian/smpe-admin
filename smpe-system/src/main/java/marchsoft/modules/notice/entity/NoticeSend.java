package marchsoft.modules.notice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import marchsoft.base.BasicModel;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author wangmingcan
 * @since 2021-02-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value="NoticeSend对象", description="")
public class NoticeSend extends BasicModel<NoticeSend> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "消息模板id")
    private Long noticeId;

    @ApiModelProperty(value = "消息内容id")
    private Long targetId;

    @ApiModelProperty(value = "接收者id，0为全部")
    private Long userId;

    @ApiModelProperty(value = "通知类型like,reply,recommand,notification")
    private String type;

    @ApiModelProperty(value = "1已读，0未读")
    @TableField("is_read")
    private Integer read;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
