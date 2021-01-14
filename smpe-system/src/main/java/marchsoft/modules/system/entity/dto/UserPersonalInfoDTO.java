package marchsoft.modules.system.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * description:用户个人信息
 *
 * @author RenShiWei
 * Date: 2020/11/25 10:58
 **/
@Data
public class UserPersonalInfoDTO {

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "性别")
    private Boolean gender;

    @ApiModelProperty(value = "手机号码")
    private String phone;

}

