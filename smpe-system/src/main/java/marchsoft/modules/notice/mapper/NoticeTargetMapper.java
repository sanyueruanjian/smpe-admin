package marchsoft.modules.notice.mapper;

import marchsoft.modules.notice.entity.Notice;
import marchsoft.modules.notice.entity.NoticeTarget;
import marchsoft.base.BasicMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
* <p>
*  Mapper 接口
* </p>
*
* @author wangmingcan
* @since 2021-02-19
*/
@Mapper
@CacheConfig(cacheNames = "notice")
public interface NoticeTargetMapper extends BasicMapper<NoticeTarget> {

    @Cacheable(key = "'target_id:' + #p0")
    NoticeTarget selectById(Long id);

}
