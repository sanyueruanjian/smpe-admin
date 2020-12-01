package marchsoft.modules.system.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Set;

/**
 * description:角色新增/修改列表
 *
 * @author RenShiWei
 * Date: 2020/11/26 17:24
 **/
@Data
public class RoleInsertOrUpdateDTO {

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

    @ApiModelProperty(value = "菜单集合")
    private Set<Long> menuIds;

    @ApiModelProperty(value = "部门集合")
    private Set<Long> depts;

}

