package marchsoft.modules.system.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import marchsoft.base.BaseDTO;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * description
 *
 * @author RenShiWei
 * Date: 2020/11/24 17:26
 */
@Data
public class UserDTO extends BaseDTO implements Serializable {

    private static final long serialVersionUID = 3061801174078023207L;

    private Long id;

    private Set<RoleSmallDTO> roles;

    private Set<JobSmallDTO> jobs;

    private DeptSmallDTO dept;

    private Long deptId;

    private String username;

    private String nickName;

    private String email;

    private String phone;

    private String gender;

    private String avatarName;

    private String avatarPath;

    @JsonIgnore
    private String password;

    private Boolean enabled;

    @JsonIgnore
    private Boolean isAdmin = false;

    private LocalDateTime pwdResetTime;
}
