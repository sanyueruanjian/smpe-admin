package marchsoft.modules.system.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Set;

/**
 * Description：
 *
 * @author jiaoqianjin
 * Date: 2020/11/28 16:34
 **/
@Data
public class RoleMenuUpdateDTO {

    @ApiModelProperty(value = "角色ID")
    private Long id;

    @ApiModelProperty(value = "菜单ID集合")
    private Set<Long> menus;
}
