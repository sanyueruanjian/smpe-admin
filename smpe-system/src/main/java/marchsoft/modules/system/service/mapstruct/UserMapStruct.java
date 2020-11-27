package marchsoft.modules.system.service.mapstruct;

import marchsoft.base.BaseMapStruct;
import marchsoft.modules.system.entity.bo.UserBO;
import marchsoft.modules.system.entity.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author Wangmingcan
 * @date 2020/8/18 8:39
 */
@Mapper(componentModel = "spring",
        uses = {RoleMapStruct.class, DeptMapStruct.class, JobMapStruct.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapStruct extends BaseMapStruct<UserDTO, UserBO> {

}
