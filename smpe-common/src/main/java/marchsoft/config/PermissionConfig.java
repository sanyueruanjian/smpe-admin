package marchsoft.config;

import marchsoft.utils.SecurityUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户权限配置
 *
 * @author RenShiWei
 * Date: 2020/11/16 21:07
 */
@Service(value = "smpe")
public class PermissionConfig {

    /**
     * 权限检查，只要有权限其一即可通过安全认证
     *
     * @param permissions 权限str
     * @return boolean
     */
    public Boolean check(String... permissions) {
        // 获取当前用户的所有权限
        List<String> smpePermissions =
                SecurityUtils.getCurrentUser().getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        // 判断当前用户的所有权限是否包含接口上定义的权限
        return smpePermissions.contains("admin") || Arrays.stream(permissions).anyMatch(smpePermissions::contains);
    }
}
