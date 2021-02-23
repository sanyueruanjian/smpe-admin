package marchsoft.modules.notice.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marchsoft.base.PageVO;
import marchsoft.exception.BadRequestException;
import marchsoft.modules.notice.entity.Notice;
import marchsoft.modules.notice.entity.NoticeSend;
import marchsoft.modules.notice.entity.NoticeTarget;
import marchsoft.modules.notice.entity.bo.NoticeSendBO;
import marchsoft.modules.notice.entity.dto.NoticeQueryCriteria;
import marchsoft.modules.notice.entity.dto.NoticeSendDTO;
import marchsoft.modules.notice.entity.dto.NoticeSendQueryCriteria;
import marchsoft.modules.notice.mapper.NoticeSendMapper;
import marchsoft.modules.notice.service.INoticeSendService;
import marchsoft.base.BasicServiceImpl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import marchsoft.modules.notice.service.mapstruct.NoticeSendMapStruct;
import marchsoft.utils.PageUtil;
import marchsoft.utils.SecurityUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
* <p>
*  服务实现类
* </p>
*
* @author wangmingcan
* @since 2021-02-19
*/
@Service
@Slf4j
@RequiredArgsConstructor
public class NoticeSendServiceImpl extends BasicServiceImpl<NoticeSendMapper, NoticeSend> implements INoticeSendService {

    private final NoticeSendMapper noticeSendMapper;
    private final RabbitTemplate rabbitTemplate;
    private final NoticeSendMapStruct noticeSendMapStruct;

    @Override
    public IPage<NoticeSendDTO> queryAll(NoticeSendQueryCriteria criteria, IPage<NoticeSend> page) {
        IPage<NoticeSendBO> noticeSendPage = noticeSendMapper.queryPage(page, buildNoticeSendQueryWrapper(criteria));
        List<NoticeSendDTO> noticeSendDTOs = noticeSendMapStruct.toDto(noticeSendPage.getRecords());
        return PageUtil.toMapStructPage(noticeSendPage, noticeSendDTOs);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void send(NoticeSendDTO resources) {
        //有待优化逻辑
        for (Long id : resources.getUserIds()) {
            NoticeSend noticeSend = new NoticeSend();
            noticeSend.setRead(0);
            noticeSend.setNoticeId(resources.getNoticeId());
            noticeSend.setTargetId(resources.getTargetId());
            noticeSend.setType(resources.getType());
            noticeSend.setUserId(id);
            rabbitTemplate.convertAndSend("notification", noticeSend);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Set<Long> ids) {
        LambdaUpdateWrapper<NoticeSend> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(NoticeSend::getRead, 1);
        updateWrapper.in(NoticeSend::getId, ids);
        if (!update(updateWrapper)) {
            throw new BadRequestException("没有更新影响");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        removeByIds(ids);
//        log.info(StrUtil.format("【删除已发通知成功】操作人id：{}，删除目标noticeSends：{}", SecurityUtils.getCurrentUserId(),
//                ids.toString()));
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
        if (criteria.getUserId() != null) {
            wrapper.like(NoticeSend::getUserId, criteria.getUserId());
        }else {
            throw new BadRequestException("查询通知用户id不能为空");
        }
        if (criteria.getRead() != null) {
            wrapper.eq(NoticeSend::getRead, criteria.getRead());
        }
        if (StrUtil.isNotBlank(criteria.getType())) {
            // 默认使用Like匹配
            wrapper.like(NoticeSend::getType, criteria.getType());
        }
        if (ObjectUtil.isNotNull(criteria.getStratTime())) {
            // 如果只有开始时间，就默认从开始到现在
            wrapper.between(NoticeSend::getCreateTime, criteria.getStratTime(),
                    ObjectUtil.isNull(criteria.getEndTime()) ? LocalDateTime.now() : criteria.getEndTime());
        }
        wrapper.orderByDesc(NoticeSend::getId);
        return wrapper;
    }
}

