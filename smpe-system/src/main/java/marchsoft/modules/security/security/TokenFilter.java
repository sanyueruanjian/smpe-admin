package marchsoft.modules.security.security;

import cn.hutool.core.util.StrUtil;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marchsoft.modules.security.config.bean.SecurityProperties;
import marchsoft.modules.security.service.OnlineUserService;
import marchsoft.modules.security.service.UserCacheClean;
import marchsoft.modules.security.service.dto.OnlineUserDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Objects;

/**
 * description:Token过滤器
 *
 * @author RenShiWei
 * Date: 2020/11/18 9:42
 **/
@RequiredArgsConstructor
@Slf4j
public class TokenFilter extends GenericFilterBean {

    /** Token配置 */
    private final TokenProvider tokenProvider;

    /** JWT配置 */
    private final SecurityProperties properties;

    /** 用户在线业务处理 */
    private final OnlineUserService onlineUserService;

    /** 用户缓存清理工具 */
    private final UserCacheClean userCacheClean;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String token = resolveToken(httpServletRequest);
        // 对于 Token 为空的不需要去查 Redis
        if (StrUtil.isNotBlank(token)) {
            OnlineUserDto onlineUserDto = null;
            boolean cleanUserCache = false;
            try {
                //从redis中获取一条用户信息
                onlineUserDto = onlineUserService.getOne(properties.getOnlineKey() + token);
            } catch (ExpiredJwtException e) {
                log.error(e.getMessage());
                cleanUserCache = true;
            } finally {
                //出现异常或者用户信息为空时，清除用户缓存
                // modify @RenShiWei 2020/11/24 description:用户信息缓存在map中（java内存） ——> redis
                if (cleanUserCache || Objects.isNull(onlineUserDto)) {
                    userCacheClean.cleanUserCache(String.valueOf(tokenProvider.getClaims(token).get(TokenProvider.AUTHORITIES_KEY)));
                }
            }
            // token合法
            if (onlineUserDto != null && StringUtils.hasText(token)) {
                //获取token认证信息
                Authentication authentication = tokenProvider.getAuthentication(token);
                //信息存储到security上下文
                SecurityContextHolder.getContext().setAuthentication(authentication);
                // Token 续期
                tokenProvider.checkRenewal(token);
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    /**
     * 初步检测Token
     *
     * @param request /
     * @return /
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(properties.getHeader());
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(properties.getTokenStartWith())) {
            // 去掉令牌前缀
            return bearerToken.replace(properties.getTokenStartWith(), "");
        } else {
            //未携带token会进行记录
            log.debug("非法Token：{}", bearerToken);
        }
        return null;
    }
}
