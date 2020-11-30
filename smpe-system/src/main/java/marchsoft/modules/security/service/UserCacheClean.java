package marchsoft.modules.security.service;

import lombok.RequiredArgsConstructor;
import marchsoft.utils.RedisUtils;
import marchsoft.utils.StringUtils;
import org.springframework.stereotype.Component;

/**
 * 用于清理 用户登录信息缓存，为防止Spring循环依赖与安全考虑 ，单独构成工具类
 *
 * @author Wangmingcan
 * @date 2020年08月17日21:56:27
 */
@Component
@RequiredArgsConstructor
public class UserCacheClean {

    private final RedisUtils redisUtils;

    /**
     * 清理特定用户缓存信息<br>
     * 用户信息变更时
     *
     * @param userName /
     */
    public void cleanUserCache(String userName) {
        if (StringUtils.isNotEmpty(userName)) {
            redisUtils.del(userName);
        }
    }

}
