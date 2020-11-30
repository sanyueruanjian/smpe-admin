package marchsoft.modules.system.entity.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * description
 *
 * @author RenShiWei
 * Date: 2020/11/24 17:26
 */
@Data
@NoArgsConstructor
public class JobSmallDTO implements Serializable {

    private Long id;

    private String name;
}
