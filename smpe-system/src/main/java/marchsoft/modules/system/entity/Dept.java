package marchsoft.modules.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import marchsoft.base.BasicModel;

import java.io.Serializable;
import java.util.Objects;

/**
 * <p>
 * 部门
 * </p>
 *
 * @author Wangmingcan
 * @since 2020-08-17
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "Dept对象", description = "部门")
@TableName("sys_dept")
public class Dept extends BasicModel<Dept> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "上级部门")
    private Long pid;

    @ApiModelProperty(value = "子部门数目")
    private Integer subCount;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "排序")
    private Integer deptSort;

    @ApiModelProperty(value = "状态")
    private Boolean enabled;

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
        Dept dept = (Dept) o;
        return Objects.equals(id, dept.id) &&
                Objects.equals(name, dept.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

}


