package marchsoft.modules.security.rest;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.wf.captcha.base.Captcha;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marchsoft.annotation.rest.AnonymousDeleteMapping;
import marchsoft.annotation.rest.AnonymousGetMapping;
import marchsoft.annotation.rest.AnonymousPostMapping;
import marchsoft.config.bean.RsaProperties;
import marchsoft.enums.ResultEnum;
import marchsoft.exception.BadRequestException;
import marchsoft.modules.security.config.bean.LoginCodeEnum;
import marchsoft.modules.security.config.bean.LoginProperties;
import marchsoft.modules.security.config.bean.SecurityProperties;
import marchsoft.modules.security.security.TokenProvider;
import marchsoft.modules.security.service.OnlineUserService;
import marchsoft.modules.security.service.dto.AuthUserDto;
import marchsoft.modules.security.service.dto.JwtUserDto;
import marchsoft.modules.system.service.IUserService;
import marchsoft.response.Result;
import marchsoft.utils.RedisUtils;
import marchsoft.utils.RsaUtils;
import marchsoft.utils.SecurityUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Wangmingcan
 * @date 2020-08-23
 * 授权、根据token获取用户详细信息
 */
@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Api(tags = "系统：系统授权接口")
public class AuthorizationController {

    private final SecurityProperties properties;
    private final RedisUtils redisUtils;
    private final OnlineUserService onlineUserService;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final LoginProperties loginProperties;
    private final IUserService userService;

    @ApiOperation("登录授权")
    @AnonymousPostMapping(value = "/login")
    public Result<Map<String, Object>> login(@Validated @RequestBody AuthUserDto authUser,
                                             HttpServletRequest request) throws Exception {
        // 密码解密
        String password = RsaUtils.decryptByPrivateKey(RsaProperties.privateKey, authUser.getPassword());
        // 查询验证码
        String code = (String) redisUtils.get(authUser.getUuid());
        // 清除验证码
        redisUtils.del(authUser.getUuid());
        if (StrUtil.isBlank(code)) {
            throw new BadRequestException("验证码不存在或已过期");
        }
        if (StrUtil.isBlank(authUser.getCode()) || ! authUser.getCode().equalsIgnoreCase(code)) {
            throw new BadRequestException("验证码错误");
        }
        // modify @RenShiWei 2020/11/23 description:生成token（username->id）
        Long userId = userService.findUserIdByName(authUser.getUsername());
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userId, password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // 生成令牌
        final JwtUserDto jwtUserDto = (JwtUserDto) authentication.getPrincipal();
        String token = tokenProvider.createToken(authentication, jwtUserDto.getUser().getId());
        // 保存在线信息
        onlineUserService.save(jwtUserDto, token, request);
        // 返回 token 与 用户信息
        Map<String, Object> authInfo = new HashMap<String, Object>(2) {{
            put("token", properties.getTokenStartWith() + token);
            put("user", jwtUserDto);
        }};
        if (loginProperties.isSingleLogin()) {
            //踢掉之前已经登录的token
            onlineUserService.checkLoginOnUser(authUser.getUsername(), token);
        }
        return Result.success(authInfo);
    }

    @ApiOperation("获取用户信息")
    @GetMapping(value = "/info")
    public Result<UserDetails> getUserInfo() {
        UserDetails currentUser = SecurityUtils.getCurrentUser();
        if (ObjectUtil.isEmpty(currentUser)) {
            throw new BadRequestException(ResultEnum.LOGIN_USER_INFO_NOT_FOUND);
        }
        return Result.success(currentUser);
    }

    @ApiOperation("获取验证码")
    @AnonymousGetMapping(value = "/code")
    public Result<Map<String, Object>> getCode() {
        // 获取运算的结果
        Captcha captcha = loginProperties.getCaptcha();
        String uuid = properties.getCodeKey() + IdUtil.simpleUUID();
        //当验证码类型为 arithmetic时且长度 >= 2 时，captcha.text()的结果有几率为浮点型
        String captchaValue = captcha.text();
        if (captcha.getCharType() - 1 == LoginCodeEnum.arithmetic.ordinal() & captchaValue.contains(".")) {
            captchaValue = captchaValue.split("\\.")[0];
        }
        // 保存
        redisUtils.set(uuid, captchaValue, loginProperties.getLoginCode().getExpiration(), TimeUnit.MINUTES);
        log.info("登录图片验证码结果:" + captchaValue);
        // 验证码信息
        Map<String, Object> imgResult = new HashMap<String, Object>(2) {{
            put("img", captcha.toBase64());
            put("uuid", uuid);
        }};
        return Result.success(imgResult);
    }

    @ApiOperation("退出登录")
    @AnonymousDeleteMapping(value = "/logout")
    public Result<Void> logout(HttpServletRequest request) {
        onlineUserService.logout(tokenProvider.getToken(request));
        return Result.success();
    }

    @ApiOperation("测试登录授权获取token，（传账号密码即可）")
    @AnonymousPostMapping(value = "/testLogin")
    public Result<Map<String, Object>> testLogin(@Validated @RequestBody AuthUserDto authUser,
                                                 HttpServletRequest request) throws Exception {
        String password = authUser.getPassword();
        // modify @RenShiWei 2020/11/23 description:生成token（username->id）
        Long userId = userService.findUserIdByName(authUser.getUsername());
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userId, password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // 生成令牌
        final JwtUserDto jwtUserDto = (JwtUserDto) authentication.getPrincipal();
        String token = tokenProvider.createToken(authentication, jwtUserDto.getUser().getId());
        // 保存在线信息
        onlineUserService.save(jwtUserDto, token, request);
        // 返回 token 与 用户信息
        Map<String, Object> authInfo = new HashMap<String, Object>(2) {{
            put("token", properties.getTokenStartWith() + token);
            put("user", jwtUserDto);
        }};
        if (loginProperties.isSingleLogin()) {
            //踢掉之前已经登录的token
            onlineUserService.checkLoginOnUser(authUser.getUsername(), token);
        }
        return Result.success(authInfo);
    }

}
