package marchsoft.modules.system.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Set;

/**
 * description:
 *
 * @author RenShiWei
 * Date: 2020/11/24 20:46
 **/
@Data
@ApiModel
public class UserInsertOrUpdateDTO {

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "部门id")
    private Long deptId;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "性别")
    private Boolean gender;

    @ApiModelProperty(value = "手机号码")
    private String phone;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "头像地址")
    private String avatarName;

    @ApiModelProperty(value = "头像真实路径")
    private String avatarPath;

    @ApiModelProperty(value = "密码", hidden = true)
    private String password;

    @ApiModelProperty(value = "状态：1启用、0禁用")
    private Boolean enabled;

    @ApiModelProperty(value = "岗位id集合")
    private Set<Long> jobs;

    @ApiModelProperty(value = "角色id集合")
    private Set<Long> roles;

}

