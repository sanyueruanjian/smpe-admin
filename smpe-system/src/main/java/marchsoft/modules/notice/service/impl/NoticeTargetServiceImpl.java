package marchsoft.modules.notice.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marchsoft.base.PageVO;
import marchsoft.exception.BadRequestException;
import marchsoft.modules.notice.entity.Notice;
import marchsoft.modules.notice.entity.NoticeTarget;
import marchsoft.modules.notice.entity.dto.NoticeQueryCriteria;
import marchsoft.modules.notice.entity.dto.NoticeTargetQueryCriteria;
import marchsoft.modules.notice.mapper.NoticeSendMapper;
import marchsoft.modules.notice.mapper.NoticeTargetMapper;
import marchsoft.modules.notice.service.INoticeTargetService;
import marchsoft.base.BasicServiceImpl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import marchsoft.utils.CacheKey;
import marchsoft.utils.RedisUtils;
import marchsoft.utils.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
public class NoticeTargetServiceImpl extends BasicServiceImpl<NoticeTargetMapper, NoticeTarget> implements INoticeTargetService {

    private final NoticeTargetMapper noticeTargetMapper;
    private final NoticeSendMapper noticeSendMapper;
    private final RedisUtils redisUtils;

    @Override
    public IPage<NoticeTarget> queryAll(NoticeTargetQueryCriteria criteria, PageVO pageVO) {
        IPage<NoticeTarget> noticeTarget = noticeTargetMapper.selectPage(pageVO.buildPage(), buildNoticeTargetQueryWrapper(criteria));
        return noticeTarget;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(NoticeTarget resources) {
        LambdaQueryWrapper<NoticeTarget> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(NoticeTarget::getContent, resources.getContent())
                .eq(NoticeTarget::getLink, resources.getLink());
        if (count(queryWrapper) > 0) {
            log.error(StrUtil.format("【创建通知内容】操作人id：{}，创建目标（名称）已存在 noticeTarget：{}", SecurityUtils.getCurrentUserId(),
                    resources));
            throw new BadRequestException("已存在：" + resources.getContent() + "  " + resources.getLink());
        }
        save(resources);
//        log.info(StrUtil.format("【创建通知内容成功】操作人id：{}，创建目标 noticeTarget：{}", SecurityUtils.getCurrentUserId(),
//                resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(NoticeTarget resources) {
        NoticeTarget noticeTarget = getById(resources.getId());
        if (ObjectUtil.isEmpty(noticeTarget)) {
            log.error(StrUtil.format("【修改通知内容失败】操作人id：{}，修改目标noticeTarget为空，目标noticeTargetId：{}", SecurityUtils.getCurrentUserId(),
                    resources.getId()));
            throw new BadRequestException("修改失败，当前数据id不存在");
        }
        LambdaQueryWrapper<NoticeTarget> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(NoticeTarget::getContent, resources.getContent())
                .eq(NoticeTarget::getLink, resources.getLink());
        NoticeTarget old = getOne(queryWrapper);
        if (ObjectUtil.isNotNull(old) && !old.getId().equals(resources.getId())) {
            log.error(StrUtil.format("【修改通知内容失败】操作人id：{}，修改目标已存在，修改目标noticeTarget：{}，存在目标通知内容：{}", SecurityUtils.getCurrentUserId(),
                    resources, old));
            throw new BadRequestException("已存在：" + resources.getContent() + "  " + resources.getLink());
        }
        updateById(resources);
        //清理缓存
        delCaches(resources.getId());
//        log.info(StrUtil.format("【修改通知内容成功】操作人id：{}，修改目标noticeTarget：{}", SecurityUtils.getCurrentUserId(),
//                resources));
    }

    @Override
    public void verification(Set<Long> ids) {
        if (noticeSendMapper.countByNoticeTargetIds(ids) > 0) {
            log.error(StrUtil.format("【删除通知内容失败】操作人id：{}，所选的通知内容：{}中存在已发消息！", SecurityUtils.getCurrentUserId(),
                    ids.toString()));
            throw new BadRequestException("所选的通知内容中存在已发送消息！");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        //清理缓存
        for (Long id : ids) {
            delCaches(id);
        }
        removeByIds(ids);
        log.info(StrUtil.format("【删除通知内容成功】操作人id：{}，删除目标noticeTargets：{}", SecurityUtils.getCurrentUserId(),
                ids.toString()));
    }

    /**
     * @author Wangmingcan
     * @date 2021-02-19 14:29
     * @param criteria
     * @return
     * @description
     */
    private LambdaQueryWrapper<NoticeTarget> buildNoticeTargetQueryWrapper(NoticeTargetQueryCriteria criteria) {
        LambdaQueryWrapper<NoticeTarget> wrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(criteria.getContent())) {
            wrapper.like(NoticeTarget::getContent, criteria.getContent());
        }
        if (StrUtil.isNotBlank(criteria.getLink())) {
            wrapper.like(NoticeTarget::getLink, criteria.getLink());
        }
        if (ObjectUtil.isNotNull(criteria.getStratTime())) {
            // 如果只有开始时间，就默认从开始到现在
            wrapper.between(NoticeTarget::getCreateTime, criteria.getStratTime(),
                    ObjectUtil.isNull(criteria.getEndTime()) ? LocalDateTime.now() : criteria.getEndTime());
        }
        wrapper.orderByDesc(NoticeTarget::getId);
        return wrapper;
    }

    /**
     * 清理缓存
     *
     * @param id /
     */
    private void delCaches(Long id) {
        redisUtils.del(CacheKey.NOTICE_TARGET_ID + id);
    }
}

