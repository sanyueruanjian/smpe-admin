package marchsoft.modules.notice.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import marchsoft.base.PageVO;
import marchsoft.modules.notice.entity.Notice;
import marchsoft.modules.notice.entity.NoticeSend;
import marchsoft.modules.notice.entity.NoticeTarget;
import marchsoft.modules.notice.entity.bo.NoticeSendBO;
import marchsoft.modules.notice.entity.dto.NoticeQueryCriteria;
import marchsoft.modules.notice.entity.dto.NoticeSendQueryCriteria;
import marchsoft.modules.notice.mapper.NoticeSendMapper;
import marchsoft.modules.notice.service.INoticeSendService;
import marchsoft.base.BasicServiceImpl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
* <p>
*  服务实现类
* </p>
*
* @author wangmingcan
* @since 2021-02-19
*/
@Service
@RequiredArgsConstructor
public class NoticeSendServiceImpl extends BasicServiceImpl<NoticeSendMapper, NoticeSend> implements INoticeSendService {

    private final NoticeSendMapper noticeSendMapper;
    private final RabbitTemplate rabbitTemplate;

    @Override
    public IPage<NoticeSendBO> queryAll(NoticeSendQueryCriteria criteria, PageVO pageVO) {
        IPage<NoticeSendBO> noticeSendPage = noticeSendMapper.queryPage(pageVO.buildPage(), buildNoticeSendQueryWrapper(criteria));
        return noticeSendPage;
    }

    @Override
    public void send(NoticeSend resources) {
        rabbitTemplate.convertAndSend("notification", resources);
    }

    /**
     * @author Wangmingcan
     * @date 2021-02-19 14:29
     * @param criteria
     * @return
     * @description
     */
    private LambdaQueryWrapper<NoticeSend> buildNoticeSendQueryWrapper(NoticeSendQueryCriteria criteria) {
        LambdaQueryWrapper<NoticeSend> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(NoticeSend::getRead, criteria.getRead());
        if (StrUtil.isNotBlank(criteria.getType())) {
            // 默认使用Like匹配
            wrapper.like(NoticeSend::getType, criteria.getType());
        }
        if (criteria.getUserId() != null) {
            wrapper.like(NoticeSend::getUserId, criteria.getUserId());
        }
        if (ObjectUtil.isNotNull(criteria.getStratTime())) {
            // 如果只有开始时间，就默认从开始到现在
            wrapper.between(NoticeSend::getCreateTime, criteria.getStratTime(),
                    ObjectUtil.isNull(criteria.getEndTime()) ? LocalDateTime.now() : criteria.getEndTime());
        }
        return wrapper;
    }
}

