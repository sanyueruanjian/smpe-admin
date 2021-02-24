package marchsoft.modules.notice.entity.bo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import marchsoft.modules.notice.entity.NoticeTemplate;
import marchsoft.modules.notice.entity.Notice;
import marchsoft.modules.notice.entity.NoticeTarget;
import marchsoft.modules.system.entity.bo.UserBO;

/**
 * @author Wangmingcan
 * @date 2021/2/19 10:49
 * @description
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class NoticeBO extends Notice {

    private NoticeTemplate noticeTemplate;

    private NoticeTarget noticeTarget;

    private UserBO user;

}
