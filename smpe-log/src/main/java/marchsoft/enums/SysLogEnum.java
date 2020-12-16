package marchsoft.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * description:日志枚举
 *
 * @author RenShiWei
 * Date: 2020/12/14 17:57
 **/
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum SysLogEnum {

    /*
        日志枚举
     */
    INFO(0, "INFO"),
    DEBUG(1, "DEBUG"),
    WARN(2, "WARN"),
    ERROR(3, "ERROR"),


    ;
    private int logType;

    private String logTypeMsg;

}
