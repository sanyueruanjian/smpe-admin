package marchsoft.modules.notice.mapper;

import marchsoft.modules.notice.entity.NoticeTemplate;
import marchsoft.base.BasicMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;

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
public interface NoticeTemplateMapper extends BasicMapper<NoticeTemplate> {

    @Cacheable(key = "'template_id:' + #p0")
    NoticeTemplate selectById(Long id);

}
