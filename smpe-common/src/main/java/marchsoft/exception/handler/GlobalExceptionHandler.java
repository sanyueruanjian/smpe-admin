package marchsoft.exception.handler;

import lombok.extern.slf4j.Slf4j;
import marchsoft.enums.ResultEnum;
import marchsoft.exception.BadRequestException;
import marchsoft.response.Result;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

/**
 * description:全局异常处理
 *
 * @author RenShiWei
 * Date: 2020/7/9 22:09
 **/
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 功能描述：处理所有不可知的异常
     *
     * @param e 异常 Throwable(异常的根类)
     * @return 异常对象信息
     * @author RenShiWei
     * Date: 2020/7/10 10:54
     */
    @ExceptionHandler(Throwable.class)
    public Result<Void> handleException(Throwable e) {
        // 打印堆栈信息
        log.error(e.getMessage(), e);
        return Result.error(ResultEnum.SEVER_ERROR.getCode(), e.getMessage());
    }

    /**
     * 处理自定义异常
     */
    @ExceptionHandler(value = BadRequestException.class)
    public Result<Void> badRequestException(BadRequestException e) {
        log.error(e.getMessage(), e);
        return Result.error(e.getStatus(), e.getMessage());
    }

    /**
     * 处理所有接口数据验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        String[] str = Objects.requireNonNull(e.getBindingResult().getAllErrors().get(0).getCodes())[1].split("\\.");
        String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        String msg = "不能为空";
        if (msg.equals(message)) {
            message = str[1] + ":" + message;
        }
        return Result.error(ResultEnum.PARAM_VERIFY_FAILURE.getCode(), message);
    }

}
