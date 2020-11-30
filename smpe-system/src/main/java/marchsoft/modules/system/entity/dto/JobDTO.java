package marchsoft.modules.system.entity.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import marchsoft.base.BaseDTO;

import java.io.Serializable;

/**
 * description:JobDTO
 *
 * @author RenShiWei
 * Date: 2020/11/24 17:26
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class JobDTO extends BaseDTO implements Serializable {

    private Long id;

    private Integer jobSort;

    private String name;

    private Boolean enabled;

    public JobDTO(String name, Boolean enabled) {
        this.name = name;
        this.enabled = enabled;
    }
}
