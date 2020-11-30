package marchsoft.modules.security.security;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marchsoft.modules.security.config.bean.SecurityProperties;
import marchsoft.utils.RedisUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * description: token生产者（提供者）
 * jjwt github地址：https://github.com/jwtk/jjwt
 *
 * @author RenShiWei
 * Date: 2020/11/18 10:54
 * <p>
 * 'InitializingBean' 初始化bean执行相应的方法
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider implements InitializingBean {

    private final SecurityProperties properties;
    private final RedisUtils redisUtils;
    /** 标识声明(claims) 声明是JWT的“主体”，包含JWT创建者希望提供给JWT收件人的信息。 */
    public static final String AUTHORITIES_KEY = "auth";
    private JwtParser jwtParser;
    private JwtBuilder jwtBuilder;

    /**
     * description:初始化时执行的方法，源于InitializingBean
     *
     * @author RenShiWei
     * Date: 2020/11/18 11:40
     */
    @Override
    public void afterPropertiesSet() {
        Key key = generateKey();
        jwtParser = Jwts.parserBuilder()
                .setSigningKey(key)
                .build();
        jwtBuilder = Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS512);
    }

    /**
     * description: SecretKey 根据 SECRET 的编码方式解码后得到：
     * Base64 编码：SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretString));
     * Base64URL 编码：SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretString));
     * 未编码：SecretKey key = Keys.hmacShaKeyFor(secretString.getBytes(StandardCharsets.UTF_8));
     * <p>
     * 也可采用key使用的是HMAC-SHA-256加密算法创建密钥
     * Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
     *
     * @return SecretKey 签名密钥
     * @author RenShiWei
     * Date: 2020/11/18 11:40
     */
    private Key generateKey() {
        byte[] keyBytes = Decoders.BASE64.decode(properties.getBase64Secret());
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 根据用户id创建Token 设置永不过期，
     * Token 的时间有效性转到Redis维护
     * <p>
     * modify @RenShiWei 2020/11/21 description:修改根据userId生成token（之前为根据username生成）
     *
     * @param authentication /
     * @return /
     */
    public String createToken(Authentication authentication, Long userId) {
        //获取权限列表
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return jwtBuilder
                // 加入ID确保生成的 Token 都不一致
                .setId(IdUtil.simpleUUID())
                // 声明中加入权限信息，方便从token中取出
                .claim(AUTHORITIES_KEY, authorities)
                //设置生成token的主题（依据什么生成token）
                .setSubject(userId.toString())
                .compact();
    }

    /**
     * 依据Token 获取鉴权信息
     *
     * @param token /
     * @return /
     */
    Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);

        Object authoritiesStr = claims.get(AUTHORITIES_KEY);
        Collection<? extends GrantedAuthority> authorities =
                ObjectUtil.isNotEmpty(authoritiesStr) ?
                        Arrays.stream(authoritiesStr.toString().split(","))
                                .map(SimpleGrantedAuthority::new)
                                .collect(Collectors.toList()) : Collections.emptyList();
        User principal = new User(claims.getSubject(), "******", authorities);
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    /**
     * description: 根据token获取声明信息
     *
     * @param token /
     * @return token中的声明信息
     * @author RenShiWei
     * Date: 2020/11/18 11:46
     */
    public Claims getClaims(String token) {
        return jwtParser
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * @param token 需要检查的token
     */
    public void checkRenewal(String token) {
        // 判断是否续期token,计算token的过期时间
        long time = redisUtils.getExpire(properties.getOnlineKey() + token) * 1000;
        Date expireDate = DateUtil.offset(new Date(), DateField.MILLISECOND, (int) time);
        // 判断当前时间与过期时间的时间差
        long differ = expireDate.getTime() - System.currentTimeMillis();
        // 如果在续期检查的范围内，则续期
        if (differ <= properties.getDetect()) {
            long renew = time + properties.getRenew();
            redisUtils.expire(properties.getOnlineKey() + token, renew, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * description: 从请求中获取token
     *
     * @param request 请求
     * @return token
     * @author RenShiWei
     * Date: 2020/11/18 11:49
     */
    public String getToken(HttpServletRequest request) {
        final String requestHeader = request.getHeader(properties.getHeader());
        if (requestHeader != null && requestHeader.startsWith(properties.getTokenStartWith())) {
            // @RenShiWei 2020/11/18 description:将固定值7，修改为从配置文件的字符串长度+1（截取token前缀）
            return requestHeader.substring(properties.getTokenStartWith().length() + 1);
        }
        return null;
    }

}
