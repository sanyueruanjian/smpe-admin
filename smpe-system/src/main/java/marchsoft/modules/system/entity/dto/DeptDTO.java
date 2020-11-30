package marchsoft.modules.system.entity.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import marchsoft.base.BaseDTO;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * description:DeptDTO
 *
 * @author RenShiWei
 * Date: 2020/11/24 17:26
 */
@Data
public class DeptDTO extends BaseDTO implements Serializable {

    private Long id;

    private String name;

    private Boolean enabled;

    private Integer deptSort;

    /**
     * 如果为空此字段不返回
     */
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private List<DeptDTO> children;

    private Long pid;

    private Integer subCount;

    public Boolean getHasChildren() {
        return subCount > 0;
    }

    public Boolean getLeaf() {
        return subCount <= 0;
    }

    public String getLabel() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DeptDTO deptDto = (DeptDTO) o;
        return Objects.equals(id, deptDto.id) &&
                Objects.equals(name, deptDto.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
