package marchsoft.modules.notice.entity.bo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import marchsoft.modules.notice.entity.Notice;
import marchsoft.modules.notice.entity.NoticeSend;
import marchsoft.modules.notice.entity.NoticeTarget;
import marchsoft.modules.system.entity.User;
import marchsoft.modules.system.entity.bo.UserBO;

/**
 * @author Wangmingcan
 * @date 2021/2/19 10:49
 * @description
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class NoticeSendBO extends NoticeSend {

    private Notice notice;

    private NoticeTarget noticeTarget;

    private UserBO user;

}
