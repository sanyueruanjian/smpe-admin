package marchsoft.entity.dto;

import lombok.Data;
import marchsoft.base.BaseDTO;

import java.io.Serializable;

/**
 * @author ZhangYuKun
 * @date 2021/1/14
 */
@Data
public class SysLogDTO extends BaseDTO implements Serializable {

    private Long id;

    private String username;

    private String description;

    private Integer logType;

    private String method;

    private String params;

    private Long requestTime;

    private String requestIp;

    private String address;

    private String browser;

    private String exceptionDetail;

}
