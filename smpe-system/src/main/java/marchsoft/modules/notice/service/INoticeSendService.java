package marchsoft.modules.notice.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import marchsoft.base.PageVO;
import marchsoft.modules.notice.entity.Notice;
import marchsoft.modules.notice.entity.NoticeSend;
import marchsoft.base.IBasicService;
import marchsoft.modules.notice.entity.bo.NoticeSendBO;
import marchsoft.modules.notice.entity.dto.NoticeQueryCriteria;
import marchsoft.modules.notice.entity.dto.NoticeSendDTO;
import marchsoft.modules.notice.entity.dto.NoticeSendQueryCriteria;

import java.util.List;
import java.util.Set;

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
     * @param page
     * @return IPage<Notice>
     * @description 查询消息模板
     */
    IPage<NoticeSendDTO> queryAll(NoticeSendQueryCriteria criteria, IPage<NoticeSend> page);

    /**
     * @author Wangmingcan
     * @date 2021-02-22 11:06
     * @param resources
     * @return
     * @description 发送通知
     */
    void send(NoticeSendDTO resources);

    /**
     * @author Wangmingcan
     * @date 2021-02-23 10:57
     * @param ids
     * @return void
     * @description 更新已读
     */
    void update(Set<Long> ids);

    /**
     * @author Wangmingcan
     * @date 2021-02-23 11:08
     * @param ids
     * @return void
     * @description 删除已发通知
     */
    void delete(Set<Long> ids);

}
