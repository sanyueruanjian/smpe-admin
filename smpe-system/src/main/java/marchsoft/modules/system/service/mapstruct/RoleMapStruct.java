package marchsoft.modules.system.service.mapstruct;

import marchsoft.base.BaseMapStruct;
import marchsoft.modules.system.entity.bo.RoleBO;
import marchsoft.modules.system.entity.dto.RoleDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author Wangmingcan
 * @date 2020/8/18 8:38
 */
@Mapper(componentModel = "spring", uses = {MenuMapStruct.class, DeptMapStruct.class}, unmappedTargetPolicy =
        ReportingPolicy.IGNORE)
public interface RoleMapStruct extends BaseMapStruct<RoleDTO, RoleBO> {
}
