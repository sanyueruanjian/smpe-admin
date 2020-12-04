package marchsoft.modules.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import marchsoft.base.BasicModel;

import java.io.Serializable;
import java.util.Objects;

/**
 * <p>
 * 角色表
 * </p>
 *
 * @author Wangmingcan
 * @since 2020-08-17
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "Role对象", description = "角色表")
@TableName("sys_role")
public class Role extends BasicModel<Role> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "role_id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "角色级别")
    private Integer level;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "数据权限")
    private String dataScope;

//    @ApiModelProperty(value = "创建者")
//    @TableField(fill = FieldFill.INSERT)
//    private String createBy;
//
//    @ApiModelProperty(value = "更新者")
//    @TableField(fill = FieldFill.INSERT_UPDATE)
//    private String updateBy;
//
//    @ApiModelProperty(value = "创建日期")
//    @TableField(fill = FieldFill.INSERT)
//    private LocalDateTime createTime;
//
//    @ApiModelProperty(value = "更新时间")
//    @TableField(fill = FieldFill.INSERT_UPDATE)
//    private LocalDateTime updateTime;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Role role = (Role) o;
        return Objects.equals(id, role.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
