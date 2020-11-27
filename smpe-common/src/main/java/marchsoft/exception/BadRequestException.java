package marchsoft.exception;

import lombok.Getter;
import marchsoft.enums.ResultEnum;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * description:通用请求异常
 *
 * @author RenShiWei
 * Date: 2020/7/9 22:09
 **/
@Getter
public class BadRequestException extends RuntimeException {

    private Integer status = BAD_REQUEST.value();

    public BadRequestException(String msg) {
        super(msg);
    }

    public BadRequestException(Integer status, String msg) {
        super(msg);
        this.status = status;
    }

    public BadRequestException(ResultEnum resultEnum) {
        super(resultEnum.getMsg());
        this.status = resultEnum.getCode();
    }

}
