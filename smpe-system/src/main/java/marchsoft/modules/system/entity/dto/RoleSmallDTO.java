package marchsoft.modules.system.entity.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * description
 *
 * @author RenShiWei
 * Date: 2020/11/24 17:26
 */
@Data
public class RoleSmallDTO implements Serializable {

    private Long id;

    private String name;

    private Integer level;

    private String dataScope;
}
