package marchsoft.modules.system.entity.dto;

import lombok.Data;
import marchsoft.base.BaseDTO;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

/**
 * description
 *
 * @author RenShiWei
 * Date: 2020/11/24 17:26
 */
@Data
public class RoleDTO extends BaseDTO implements Serializable {

    private Long id;

    private Set<MenuDTO> menus;

    private Set<DeptDTO> depts;

    private String name;

    private String dataScope;

    private Integer level;

    private String description;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RoleDTO roleDto = (RoleDTO) o;
        return Objects.equals(id, roleDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
