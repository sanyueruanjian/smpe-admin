package marchsoft.modules.notice.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marchsoft.exception.BadRequestException;
import marchsoft.modules.notice.entity.Notice;
import marchsoft.modules.notice.entity.bo.NoticeBO;
import marchsoft.modules.notice.entity.dto.NoticeDTO;
import marchsoft.modules.notice.entity.dto.NoticeQueryCriteria;
import marchsoft.modules.notice.mapper.NoticeMapper;
import marchsoft.modules.notice.service.INoticeService;
import marchsoft.base.BasicServiceImpl;
import marchsoft.modules.notice.service.mapstruct.NoticeMapStruct;
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
public class NoticeServiceImpl extends BasicServiceImpl<NoticeMapper, Notice> implements INoticeService {

    private final NoticeMapper noticeMapper;
    private final RabbitTemplate rabbitTemplate;
    private final NoticeMapStruct noticeMapStruct;

    @Override
    public IPage<NoticeDTO> queryAll(NoticeQueryCriteria criteria, IPage<Notice> page) {
        IPage<NoticeBO> noticePage = noticeMapper.queryPage(page, buildNoticeQueryWrapper(criteria));
        List<NoticeDTO> noticeDTOS = noticeMapStruct.toDto(noticePage.getRecords());
        return PageUtil.toMapStructPage(noticePage, noticeDTOS);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void send(NoticeDTO resources) {
        //有待优化逻辑
        for (Long id : resources.getUserIds()) {
            Notice notice = new Notice();
            notice.setRead(0);
            notice.setTemplateId(resources.getTemplateId());
            notice.setTargetId(resources.getTargetId());
            notice.setType(resources.getType());
            notice.setUserId(id);
            rabbitTemplate.convertAndSend("notification", notice);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Set<Long> ids) {
        LambdaUpdateWrapper<Notice> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(Notice::getRead, 1);
        updateWrapper.in(Notice::getId, ids);
        if (!update(updateWrapper)) {
            throw new BadRequestException("没有更新影响");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        removeByIds(ids);
        log.info(StrUtil.format("【删除已发通知成功】操作人id：{}，删除目标notices：{}", SecurityUtils.getCurrentUserId(),
                ids.toString()));
    }

    /**
     * @author Wangmingcan
     * @date 2021-02-19 14:29
     * @param criteria
     * @return
     * @description
     */
    private LambdaQueryWrapper<Notice> buildNoticeQueryWrapper(NoticeQueryCriteria criteria) {
        LambdaQueryWrapper<Notice> wrapper = new LambdaQueryWrapper<>();
        if (criteria.getUserId() != null) {
            wrapper.like(Notice::getUserId, criteria.getUserId());
        }else {
            throw new BadRequestException("查询通知用户id不能为空");
        }
        if (criteria.getRead() != null) {
            wrapper.eq(Notice::getRead, criteria.getRead());
        }
        if (StrUtil.isNotBlank(criteria.getType())) {
            // 默认使用Like匹配
            wrapper.like(Notice::getType, criteria.getType());
        }
        if (ObjectUtil.isNotNull(criteria.getStratTime())) {
            // 如果只有开始时间，就默认从开始到现在
            wrapper.between(Notice::getCreateTime, criteria.getStratTime(),
                    ObjectUtil.isNull(criteria.getEndTime()) ? LocalDateTime.now() : criteria.getEndTime());
        }
        wrapper.orderByDesc(Notice::getId);
        return wrapper;
    }
}

