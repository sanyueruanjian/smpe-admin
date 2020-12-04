package marchsoft.config.mybatisplus;

import lombok.extern.slf4j.Slf4j;
import marchsoft.utils.SpringContextHolder;
import org.apache.ibatis.cache.Cache;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Mybatis缓存配置
 *
 * @author Wangmingcan
 * @date 2020/9/22 10:13
 */
@Slf4j
public class MybatisRedisCache implements Cache {

    /** 读写锁 */
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock(true);

    private final String redisBeanName = "redisTemplate";

    /** 这里使用了redis缓存，使用springboot自动注入 */
    private RedisTemplate<String, Object> redisTemplate;

    private String id;

    public MybatisRedisCache(final String id) {
        if (id == null) {
            throw new IllegalArgumentException("Cache instances require an ID");
        }
        this.id = id;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    @SuppressWarnings("all")
    public void putObject(Object key, Object value) {
        if (redisTemplate == null) {
            //由于启动期间注入失败，只能运行期间注入，这段代码可以删除
            redisTemplate = (RedisTemplate<String, Object>) SpringContextHolder.getBean(redisBeanName);
        }
        if (value != null) {
            if (key.toString().contains("selectPage")) {
                return;
            }
            redisTemplate.opsForValue().set(key.toString(), value);
        }
    }

    @Override
    @SuppressWarnings("all")
    public Object getObject(Object key) {
        if (redisTemplate == null) {
            //由于启动期间注入失败，只能运行期间注入，这段代码可以删除
            redisTemplate = (RedisTemplate<String, Object>) SpringContextHolder.getBean(redisBeanName);
        }
        try {
            if (key != null) {
                if (key.toString().contains("selectPage")) {
                    return null;
                }
                return redisTemplate.opsForValue().get(key.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("缓存出错 ");
        }
        return null;
    }

    @Override
    @SuppressWarnings("all")
    public Object removeObject(Object key) {
        if (redisTemplate == null) {
            //由于启动期间注入失败，只能运行期间注入，这段代码可以删除
            redisTemplate = (RedisTemplate<String, Object>) SpringContextHolder.getBean(redisBeanName);
        }
        if (key != null) {
            redisTemplate.delete(key.toString());
        }
        return null;
    }

    @Override
    @SuppressWarnings("all")
    public void clear() {
        log.debug("清空缓存");
        if (redisTemplate == null) {
            redisTemplate = (RedisTemplate<String, Object>) SpringContextHolder.getBean(redisBeanName);
        }
        Set<String> keys = redisTemplate.keys("*:" + this.id + "*");
        if (! CollectionUtils.isEmpty(keys)) {
            redisTemplate.delete(keys);
        }
    }

    @Override
    @SuppressWarnings("all")
    public int getSize() {
        if (redisTemplate == null) {
            //由于启动期间注入失败，只能运行期间注入，这段代码可以删除
            redisTemplate = (RedisTemplate<String, Object>) SpringContextHolder.getBean(redisBeanName);
        }
        Long size = redisTemplate.execute((RedisCallback<Long>) RedisServerCommands::dbSize);
        return size.intValue();
    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        return this.readWriteLock;
    }
}
