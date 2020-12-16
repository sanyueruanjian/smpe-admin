package marchsoft.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import lombok.extern.slf4j.Slf4j;
import marchsoft.enums.DataScopeEnum;
import marchsoft.enums.ResultEnum;
import marchsoft.exception.BadRequestException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

/**
 * 获取当前登录的用户
 *
 * @author RenShiWei
 * Date: 2020/11/16 21:07
 */
@Slf4j
public class SecurityUtils {

    /**
     * 获取当前登录的用户
     * 这里的UserDetails其实就是JwtUserDto，但是没有引入system模块，不能直接转
     *
     * @return UserDetails
     */
    public static UserDetails getCurrentUser() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new BadRequestException(ResultEnum.LOGIN_EXPIRE);
        }
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            UserDetailsService userDetailsService = SpringContextHolder.getBean(UserDetailsService.class);
            return userDetailsService.loadUserByUsername(userDetails.getUsername());
        }
        throw new BadRequestException(ResultEnum.LOGIN_INFO_NOT_FOUND);
    }

    /**
     * 获取系统用户名称
     *
     * @return 系统用户名称
     */
    public static String getCurrentUsername() {
        return getCurrentUser().getUsername();
    }

    /**
     * description:从token中获取当前用户id 或者 查询数据库
     * modify @RenShiWei 2020/11/23 description: HuTool-Json -> FastJson 处理
     *
     * @return 当前用户id
     * @author RenShiWei
     * Date: 2020/11/23 20:16
     */
    public static Long getCurrentUserId() {
        UserDetails userDetails = getCurrentUser();
        return JSON.parseObject(JSON.toJSONString(userDetails)).getJSONObject("user").getObject("id", Long.class);
    }

    /**
     * description:从token中获取当前用户id
     *
     * @return 当前用户id
     * @author RenShiWei
     * Date: 2020/11/23 20:16
     */
    public static Long getCurrentUserIdByToken() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new BadRequestException(ResultEnum.LOGIN_EXPIRE);
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String principal = userDetails.getUsername();
        return Long.parseLong(principal);
    }

    /**
     * 获取当前用户的数据权限集合
     *
     * @return /
     */
    public static List<Long> getCurrentUserDataScope() {
        UserDetails userDetails = getCurrentUser();
        JSONArray jsonArray = JSON.parseObject(JSON.toJSONString(userDetails)).getJSONArray(
                "dataScopes");
        return jsonArray.toJavaList(Long.class);
    }

    /**
     * 获取数据权限级别
     * TODO:@RenShiWei 2020/11/23 description:需要进行修改和测试
     *
     * @return 级别
     */
    public static String getDataScopeType() {
        List<Long> dataScopes = getCurrentUserDataScope();
        if (dataScopes.size() != 0) {
            return "";
        }
        return DataScopeEnum.ALL.getValue();
    }

    /**
     * description:获取当前登录的用户(无异常处理只在特殊情况下使用)
     *
     * @return UserDetails
     * @author RenShiWei
     * Date: 2020/12/5 16:00
     */
    public static UserDetails getCurrentUserNoThrow() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            UserDetailsService userDetailsService = SpringContextHolder.getBean(UserDetailsService.class);
            return userDetailsService.loadUserByUsername(userDetails.getUsername());
        }
        return null;
    }

    /**
     * description:获取系统用户名称(无异常处理只在特殊情况下使用)
     *
     * @return /
     * @author RenShiWei
     * Date: 2020/12/5 16:01
     */
    public static String getCurrentUsernameNoThrow() {
        if (getCurrentUserNoThrow() == null) {
            return null;
        }
        return getCurrentUserNoThrow().getUsername();
    }

    public static Long getCurrentUserIdThrow() {
        if (getCurrentUserNoThrow() == null) {
            return 0L;
        }
        UserDetails userDetails = getCurrentUser();
        return JSON.parseObject(JSON.toJSONString(userDetails)).getJSONObject("user").getObject("id", Long.class);
    }


}
