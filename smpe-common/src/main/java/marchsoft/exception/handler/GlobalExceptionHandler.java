package marchsoft.exception.handler;

import lombok.extern.slf4j.Slf4j;
import marchsoft.enums.ResultEnum;
import marchsoft.exception.BadRequestException;
import marchsoft.response.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
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
    public ResponseEntity<Result<String>> handleException(Throwable e) {
        // 打印堆栈信息
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Result.error(ResultEnum.SEVER_ERROR.getCode(), ResultEnum.SEVER_ERROR.getMsg()));
    }

    /**
     * 功能描述：处理自定义异常
     *
     * @param e 自定义异常
     * @return restful风格的异常信息
     * @author RenShiWei
     * Date: 2020/4/13 22:18
     */
    @ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity<Result<String>> badRequestException(BadRequestException e) {
        log.error(e.getMessage(), e);
        //默认到后端的请求，状态码都为200，自定义的异常由封装的code去控制
        return ResponseEntity.status(HttpStatus.OK).body(Result.error(e.getMessage()));
    }

    /**
     * description: security的角色权限不足异常
     *
     * @param e 权限不足异常
     * @return 200状态码 403自定义code
     * @author RenShiWei
     * Date: 2020/8/7 19:52
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Result<String>> handleAccessDeniedException(AccessDeniedException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Result.error(ResultEnum.IDENTITY_NOT_POW.getCode(),
                ResultEnum.IDENTITY_NOT_POW.getMsg()));
    }

    /**
     * description:处理所有接口数据验证异常
     *
     * @param e 接口数据验证异常
     * @return 统一异常结果处理
     * @author RenShiWei
     * Date: 2020/7/10 11:33
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Result<String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        String[] str = Objects.requireNonNull(e.getBindingResult().getAllErrors().get(0).getCodes())[1].split("\\.");
        String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        String msg = "不能为空";
        if (msg.equals(message)) {
            message = str[1] + ":" + message;
        }
        return ResponseEntity.status(HttpStatus.OK).body(Result.error(ResultEnum.PARAM_VERIFY_FAILURE.getCode(),
                message));
    }

}
