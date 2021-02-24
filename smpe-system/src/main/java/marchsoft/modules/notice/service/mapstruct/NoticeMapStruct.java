package marchsoft.modules.notice.service.mapstruct;

import marchsoft.base.BaseMapStruct;
import marchsoft.modules.notice.entity.bo.NoticeBO;
import marchsoft.modules.notice.entity.dto.NoticeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author Wangmingcan
 * @date 2021/2/23 9:16
 * @description
 */
@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NoticeMapStruct extends BaseMapStruct<NoticeDTO, NoticeBO> {

}
