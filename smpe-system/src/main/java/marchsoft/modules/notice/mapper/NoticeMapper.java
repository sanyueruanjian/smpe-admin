package marchsoft.modules.notice.mapper;

import marchsoft.modules.notice.entity.Notice;
import marchsoft.base.BasicMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.io.Serializable;

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
public interface NoticeMapper extends BasicMapper<Notice> {

    @Cacheable(key = "'id:' + #p0")
    Notice selectById(Long id);

}
