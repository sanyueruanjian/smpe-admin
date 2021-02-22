package marchsoft.modules.notice.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import marchsoft.base.PageVO;
import marchsoft.modules.notice.entity.Notice;
import marchsoft.modules.notice.entity.NoticeSend;
import marchsoft.base.IBasicService;
import marchsoft.modules.notice.entity.bo.NoticeSendBO;
import marchsoft.modules.notice.entity.dto.NoticeQueryCriteria;
import marchsoft.modules.notice.entity.dto.NoticeSendQueryCriteria;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangmingcan
 * @since 2021-02-19
 */
public interface INoticeSendService extends IBasicService<NoticeSend> {

    /**
     * @author Wangmingcan
     * @date 2021-02-19 11:15
     * @param criteria
     * @param pageVO
     * @return IPage<Notice>
     * @description 查询消息模板
     */
    IPage<NoticeSendBO> queryAll(NoticeSendQueryCriteria criteria, PageVO pageVO);

    /**
     * @author Wangmingcan
     * @date 2021-02-22 11:06
     * @param resources
     * @return
     * @description 发送通知
     */
    void send(NoticeSend resources);

}
