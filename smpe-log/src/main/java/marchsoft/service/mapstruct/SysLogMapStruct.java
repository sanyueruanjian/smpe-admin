package marchsoft.service.mapstruct;

import marchsoft.base.BaseMapStruct;
import marchsoft.entity.SysLog;
import marchsoft.entity.bo.SysLogBO;
import marchsoft.entity.dto.SysLogDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author ZhangYuKun
 * @date 2021/1/14
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SysLogMapStruct extends BaseMapStruct<SysLogDTO, SysLogBO> {
}
