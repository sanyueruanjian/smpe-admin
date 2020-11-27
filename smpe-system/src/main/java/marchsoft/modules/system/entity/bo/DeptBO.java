package marchsoft.modules.system.entity.bo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import marchsoft.modules.system.entity.Dept;
import marchsoft.modules.system.entity.Role;

import java.util.Set;

/**
 * description: 部门Bo
 *
 * @author liuxingxing
 * @date 2020.11.26 16:10
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DeptBO extends Dept {
    private Set<Role> roles;
}
