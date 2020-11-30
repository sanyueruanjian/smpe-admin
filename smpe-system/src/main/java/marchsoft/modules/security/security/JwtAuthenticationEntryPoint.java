package marchsoft.modules.security.security;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import marchsoft.enums.ResultEnum;
import marchsoft.response.Result;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * description:认证失败处理类
 *
 * @author RenShiWei
 * Date: 2020/8/3 15:15
 **/
@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * 功能描述： 认证失败的凭证处理方法
     *
     * @author RenShiWei
     * Date: 2020/8/3 15:15
     */
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        // 当用户尝试访问安全的REST资源而不提供任何凭据时，将调用此方法发送401 响应
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException==null?"Unauthorized":authException.getMessage());
         /*
            因为我们使用的REST API,所以我们认为到达后台的请求都是正常的，
            所以返回的HTTP状态码都是200，用接口返回的code来确定请求是否正常。
         */
        // 当用户尝试访问安全的REST资源而不提供任何凭据时，将调用此方法发送401 响应
//        response.setStatus(200);
//        response.setCharacterEncoding("UTF-8");
//        response.setContentType("application/json; charset=utf-8");
//        if (authException == null) {
//            response.getWriter().write(JSON.toJSONString(Result.error(ResultEnum.LOGIN_EXPIRE.getCode(),
//                    "Unauthorized")));
//        } else {
//            response.getWriter().write(JSON.toJSONString(Result.error(ResultEnum.LOGIN_EXPIRE.getCode(),
//                    authException.getMessage())));
//        }
//        log.error("未登录或当前登录状态过期");
    }

}

