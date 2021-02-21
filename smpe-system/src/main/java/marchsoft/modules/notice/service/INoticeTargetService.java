package marchsoft.modules.notice.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import marchsoft.base.PageVO;
import marchsoft.modules.notice.entity.Notice;
import marchsoft.modules.notice.entity.NoticeTarget;
import marchsoft.base.IBasicService;
import marchsoft.modules.notice.entity.dto.NoticeTargetQueryCriteria;

import java.util.Set;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangmingcan
 * @since 2021-02-19
 */
public interface INoticeTargetService extends IBasicService<NoticeTarget> {

    /**
     * @author Wangmingcan
     * @date 2021-02-19 16:02
     * @param criteria
     * @param pageVO
     * @return
     * @description
     */
    IPage<NoticeTarget> queryAll(NoticeTargetQueryCriteria criteria, PageVO pageVO);

    /**
     * @author Wangmingcan
     * @date 15:27
     * @param resources
     * @return void
     * @description 新增通知内容
     */
    void create(NoticeTarget resources);

    /**
     * @author Wangmingcan
     * @date 2021-02-21 15:33
     * @param resources
     * @return void
     * @description 修改通知内容
     */
    void update(NoticeTarget resources);

    /**
     * @author Wangmingcan
     * @date 2021-02-21 15:44
     * @param ids
     * @return
     * @description 验证通知内容是否被关联
     */
    void verification(Set<Long> ids);

    /**
     * @author Wangmingcan
     * @date 2021-02-21 15:44
     * @param ids
     * @return
     * @description 删除通知内容
     */
    void delete(Set<Long> ids);

}
