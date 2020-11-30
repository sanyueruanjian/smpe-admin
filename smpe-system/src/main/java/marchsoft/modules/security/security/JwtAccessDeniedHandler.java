package marchsoft.modules.security.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * description:用户没有访问权限是返回响应的处理类
 * 兼容统一响应格式
 *
 * @author RenShiWei
 * Date: 2020/8/3 15:13
 **/
@Slf4j
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    /**
     * description:权限不足的处理
     *
     * @author RenShiWei
     * Date: 2020/8/3 15:13
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        // 当用户在没有授权的情况下访问受保护的REST资源时，将调用此方法发送403响应（响应内层code403），默认到达后端的响应都为200
        response.sendError(HttpServletResponse.SC_FORBIDDEN, accessDeniedException.getMessage());
    }
}

