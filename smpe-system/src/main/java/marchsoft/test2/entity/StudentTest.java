package marchsoft.test2.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 学生表（主要用于测试）
 * </p>
 *
 * @author RenShiWei
 * @since 2020-12-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="StudentTest对象", description="学生表（主要用于测试）")
public class StudentTest extends Model<StudentTest> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id
")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "学号")
    private String number;

    @ApiModelProperty(value = "性别（0为男，1位女）")
    private Boolean sex;

    @ApiModelProperty(value = "年龄")
    private Integer age;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private String createBy;

    private String updateBy;

    @ApiModelProperty(value = "是否开启（0不开启，1开启）")
    private Boolean enable;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
