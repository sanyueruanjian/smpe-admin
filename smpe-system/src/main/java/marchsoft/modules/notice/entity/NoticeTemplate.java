package marchsoft.modules.notice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
@ApiModel(value="NoticeTemplate对象", description="")
public class NoticeTemplate extends BasicModel<NoticeTemplate> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "模板标题")
    private String title;

    @ApiModelProperty(value = "模板内容")
    private String content;

    @ApiModelProperty(value = "通知类型like,reply,recommand,notification")
    private String type;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
