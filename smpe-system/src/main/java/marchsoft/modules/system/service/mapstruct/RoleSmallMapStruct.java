package marchsoft.modules.system.service.mapstruct;

import marchsoft.base.BaseMapStruct;
import marchsoft.modules.system.entity.Role;
import marchsoft.modules.system.entity.dto.RoleSmallDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author Wangmingcan
 * @date 2020/8/18 8:39
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleSmallMapStruct extends BaseMapStruct<RoleSmallDTO, Role> {
}
