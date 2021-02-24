package marchsoft.modules.notice.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import marchsoft.modules.notice.entity.Notice;
import marchsoft.base.IBasicService;
import marchsoft.modules.notice.entity.dto.NoticeDTO;
import marchsoft.modules.notice.entity.dto.NoticeQueryCriteria;

import java.util.Set;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangmingcan
 * @since 2021-02-19
 */
public interface INoticeService extends IBasicService<Notice> {

    /**
     * @author Wangmingcan
     * @date 2021-02-19 11:15
     * @param criteria
     * @param page
     * @return IPage<NoticeDTO>
     * @description 查询消息模板
     */
    IPage<NoticeDTO> queryAll(NoticeQueryCriteria criteria, IPage<Notice> page);

    /**
     * @author Wangmingcan
     * @date 2021-02-22 11:06
     * @param resources
     * @return
     * @description 发送通知
     */
    void send(NoticeDTO resources);

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
