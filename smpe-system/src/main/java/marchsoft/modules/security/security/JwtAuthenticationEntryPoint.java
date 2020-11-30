package marchsoft.modules.security.security;

import lombok.extern.slf4j.Slf4j;
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
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException == null ? "Unauthorized" :
                authException.getMessage());
    }

}

