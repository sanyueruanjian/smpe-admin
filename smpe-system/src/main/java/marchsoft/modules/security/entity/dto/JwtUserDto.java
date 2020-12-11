package marchsoft.modules.security.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import marchsoft.modules.system.entity.dto.UserDTO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Wangmingcan
 * @date 2020年08月17日21:56:27
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class JwtUserDto implements UserDetails, Serializable {


    private UserDTO user;

    private List<Long> dataScopes;

    @JsonIgnore
    private List<GrantedAuthority> authorities;

    /**
     * description:获取角色权限信息
     *
     * @return 权限信息
     * @author RenShiWei
     * Date: 2020/11/18 12:11
     */
    public Set<String> getRoles() {
        return authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
    }

    public void setRoles(Set<String> roles) {
        authorities =
                roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return user.getUsername();
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return user.getEnabled();
    }
}
