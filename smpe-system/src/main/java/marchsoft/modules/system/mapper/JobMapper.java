package marchsoft.modules.system.mapper;

import marchsoft.base.BasicMapper;
import marchsoft.config.mybatisplus.MybatisRedisCache;
import marchsoft.modules.system.entity.Job;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Set;

/**
 * <p>
 * 岗位 Mapper 接口
 * </p>
 *
 * @author Wangmingcan
 * @since 2020-08-17
 */
@Component
@CacheConfig(cacheNames = "job")
public interface JobMapper extends BasicMapper<Job> {


    /**
     * @param id 用户id
     * @return Set<Job>
     * @author Wangmingcan
     * @date 2020-08-23 15:44
     * description 通过用户id和关联表users_jobs查询该用户的所有工作。
     */
    @Select("SELECT j.id, j.name, j.enabled, j.job_sort, j.create_by, j.update_by, j.create_time, j.update_time " +
            "FROM sys_job j, sys_users_jobs uj WHERE j.id = uj.job_id AND uj.user_id = ${id} AND j.is_deleted=0")
    @Cacheable(key = "'user:' + #p0")
    Set<Job> findByUserId(Long id);

    @Cacheable(key = "'id:' + #p0")
    Job selectById(Long id);

    /**
     * description:删除用户维护用户岗位中间表
     *
     * @param userId 用户id
     * @return 操作条数
     * @author RenShiWei
     * Date: 2020/11/25 9:28
     */
    @Delete("DELETE FROM sys_users_jobs WHERE user_id=#{userId}")
    Integer delUserAtJob(Long userId);
}
