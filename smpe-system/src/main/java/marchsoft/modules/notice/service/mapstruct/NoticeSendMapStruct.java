package marchsoft.modules.notice.service.mapstruct;

import marchsoft.base.BaseMapStruct;
import marchsoft.modules.notice.entity.bo.NoticeSendBO;
import marchsoft.modules.notice.entity.dto.NoticeSendDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author Wangmingcan
 * @date 2021/2/23 9:16
 * @description
 */
@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NoticeSendMapStruct extends BaseMapStruct<NoticeSendDTO, NoticeSendBO> {

}
