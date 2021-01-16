package marchsoft.modules.quartz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import marchsoft.base.BasicMapper;
import marchsoft.config.mybatisplus.MybatisRedisCache;
import marchsoft.modules.quartz.entity.QuartzJob;
import org.apache.ibatis.annotations.CacheNamespace;

/**
 * @author lixiangxiang
 * @description /
 * @date 2021/1/14 17:07
 */
@CacheNamespace(implementation = MybatisRedisCache.class, eviction = MybatisRedisCache.class)
public interface QuartzJobMapper extends  BasicMapper<QuartzJob> {


}
