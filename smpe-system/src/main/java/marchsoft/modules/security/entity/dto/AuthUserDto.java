package marchsoft.modules.security.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Wangmingcan
 * @date 2020年08月17日21:56:27
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthUserDto implements Serializable {

    private String username;

    private String password;

    private String code;

    private String uuid = "";

    @Override
    public String toString() {
        return "{username=" + username + ", password= ******}";
    }
}
