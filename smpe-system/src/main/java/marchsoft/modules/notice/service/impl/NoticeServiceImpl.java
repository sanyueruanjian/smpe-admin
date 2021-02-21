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
import marchsoft.modules.notice.entity.dto.NoticeQueryCriteria;
import marchsoft.modules.notice.mapper.NoticeMapper;
import marchsoft.modules.notice.mapper.NoticeSendMapper;
import marchsoft.modules.notice.service.INoticeService;
import marchsoft.base.BasicServiceImpl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import marchsoft.utils.CacheKey;
import marchsoft.utils.RedisUtils;
import marchsoft.utils.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
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
    private final NoticeSendMapper noticeSendMapper;
    private final RedisUtils redisUtils;

    @Override
    public IPage<Notice> queryAll(NoticeQueryCriteria criteria, PageVO pageVO) {
        IPage<Notice> noticePage = noticeMapper.selectPage(pageVO.buildPage(), buildNoticeQueryWrapper(criteria));
        return noticePage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(Notice resources) {
        LambdaQueryWrapper<Notice> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Notice::getTitle, resources.getTitle())
                    .eq(Notice::getContent, resources.getContent());
        if (count(queryWrapper) > 0) {
            log.error(StrUtil.format("【创建通知模板失败】操作人id：{}，创建目标（名称）已存在 notice：{}", SecurityUtils.getCurrentUserId(),
                    resources));
            throw new BadRequestException("已存在：" + resources.getTitle() + "  " + resources.getContent());
        }
        save(resources);
//        log.info(StrUtil.format("【创建通知模板成功】操作人id：{}，创建目标 notice：{}", SecurityUtils.getCurrentUserId(),
//                resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Notice resources) {
        Notice notice = getById(resources.getId());
        if (ObjectUtil.isEmpty(notice)) {
            log.error(StrUtil.format("【修改通知模板失败】操作人id：{}，修改目标notice为空，目标noticeId：{}", SecurityUtils.getCurrentUserId(),
                    resources.getId()));
            throw new BadRequestException("修改失败，当前数据id不存在");
        }
        LambdaQueryWrapper<Notice> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Notice::getTitle, resources.getTitle())
                .eq(Notice::getContent, resources.getContent());
        Notice old = getOne(queryWrapper);
        if (ObjectUtil.isNotNull(old) && !old.getId().equals(resources.getId())) {
            log.error(StrUtil.format("【修改通知模板失败】操作人id：{}，修改目标已存在，修改目标notice：{}，存在目标通知模板：{}", SecurityUtils.getCurrentUserId(),
                    resources, old));
            throw new BadRequestException("已存在：" + resources.getTitle() + "  " + resources.getContent());
        }
        updateById(resources);
        //清理缓存
        delCaches(resources.getId());
//        log.info(StrUtil.format("【修改通知模板成功】操作人id：{}，修改目标notice：{}", SecurityUtils.getCurrentUserId(),
//                resources));
    }

    @Override
    public void verification(Set<Long> ids) {
        if (noticeSendMapper.countByNoticeIds(ids) > 0) {
            log.error(StrUtil.format("【删除通知模板失败】操作人id：{}，所选的通知模板：{}中存在已发消息！", SecurityUtils.getCurrentUserId(),
                    ids.toString()));
            throw new BadRequestException("所选的通知模板中存在已发送消息！");
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
        log.info(StrUtil.format("【删除通知模板成功】操作人id：{}，删除目标notices：{}", SecurityUtils.getCurrentUserId(),
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
        if (StrUtil.isNotBlank(criteria.getType())) {
            // 默认使用Like匹配
            wrapper.like(Notice::getType, criteria.getType());
        }
        if (StrUtil.isNotBlank(criteria.getTitle())) {
            wrapper.like(Notice::getTitle, criteria.getTitle());
        }
        if (StrUtil.isNotBlank(criteria.getContent())) {
            wrapper.like(Notice::getContent, criteria.getContent());
        }
        if (ObjectUtil.isNotNull(criteria.getStratTime())) {
            // 如果只有开始时间，就默认从开始到现在
            wrapper.between(Notice::getCreateTime, criteria.getStratTime(),
                    ObjectUtil.isNull(criteria.getEndTime()) ? LocalDateTime.now() : criteria.getEndTime());
        }
        return wrapper;
    }

    /**
     * 清理缓存
     *
     * @param id /
     */
    private void delCaches(Long id) {
        redisUtils.del(CacheKey.NOTICE_ID + id);
    }
}

