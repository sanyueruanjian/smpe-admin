package marchsoft.modules.system.entity.bo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import marchsoft.modules.system.entity.Dept;
import marchsoft.modules.system.entity.Job;
import marchsoft.modules.system.entity.Role;
import marchsoft.modules.system.entity.User;

import java.util.Set;

/**
 * description:
 *
 * @author RenShiWei
 * Date: 2020/11/24 20:32
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class UserBO extends User {

    private static final long serialVersionUID = 5209955359940119094L;

    private Dept dept;

    private Set<Role> roles;

    private Set<Job> jobs;


}

