package marchsoft.modules.notice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import marchsoft.base.BasicModel;
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
@ApiModel(value="NoticeTarget对象", description="")
public class NoticeTarget extends BasicModel<NoticeTarget> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "通知的具体内容")
    private String content;

    @ApiModelProperty(value = "内容链接")
    private String link;

    @TableField("is_fanout")
    @ApiModelProperty(value = "1代表该消息发送给所有人，0代表发送给特定人群")
    private Integer fanout;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
