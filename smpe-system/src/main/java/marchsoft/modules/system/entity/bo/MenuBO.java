package marchsoft.modules.system.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import marchsoft.modules.system.entity.Menu;
import marchsoft.modules.system.entity.Role;

import java.util.Set;

/**
 * Description：
 *
 * @author jiaoqianjin
 * Date: 2020/11/26 8:53
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class MenuBO extends Menu {

    @ApiModelProperty(value = "角色集合")
    private Set<Role> roles;
}
