package marchsoft.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * description:获取 HttpServletRequest
 *
 * @author RenShiWei
 * Date: 2020/12/14 17:25
 */
public class RequestHolder {

    /**
     * description:获取 HttpServletRequest
     *
     * @return /
     * @author RenShiWei
     * Date: 2020/12/14 17:25
     */
    public static HttpServletRequest getHttpServletRequest() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    }

}
