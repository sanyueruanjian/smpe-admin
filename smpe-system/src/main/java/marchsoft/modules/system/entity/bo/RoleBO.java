package marchsoft.modules.system.entity.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import marchsoft.modules.system.entity.Dept;
import marchsoft.modules.system.entity.Menu;
import marchsoft.modules.system.entity.Role;

import java.util.Set;

/**
 * description:
 *
 * @author RenShiWei
 * Date: 2020/11/26 9:13
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel
public class RoleBO extends Role {

    @ApiModelProperty("角色对应的菜单集合")
    private Set<Menu> menus;

    @ApiModelProperty("角色对应的部门集合")
    private Set<Dept> depts;

}

