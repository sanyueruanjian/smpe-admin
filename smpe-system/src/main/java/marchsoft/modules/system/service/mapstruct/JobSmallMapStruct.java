package marchsoft.modules.system.service.mapstruct;

import marchsoft.base.BaseMapStruct;
import marchsoft.modules.system.entity.Job;
import marchsoft.modules.system.entity.dto.JobSmallDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author Wangmingcan
 * @date 2020/8/18 8:38
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface JobSmallMapStruct extends BaseMapStruct<JobSmallDTO, Job> {
}
