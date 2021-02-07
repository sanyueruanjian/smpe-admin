package marchsoft.modules.quartz.mapper;

import marchsoft.base.BasicMapper;
import marchsoft.config.mybatisplus.MybatisRedisCache;
import marchsoft.modules.quartz.entity.QuartzJob;
import org.apache.ibatis.annotations.CacheNamespace;
import org.springframework.cache.annotation.CacheConfig;

/**
 * @author lixiangxiang
 * @description /
 * @date 2021/1/14 17:07
 */
public interface QuartzJobMapper extends BasicMapper<QuartzJob> {

}
