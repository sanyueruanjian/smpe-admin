package marchsoft.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import marchsoft.enums.ResultEnum;

/**
 * description:对返回前端数据进行封装
 *
 * @author RenShiWei
 * Date: 2020/7/9 22:09
 **/
@Data
@AllArgsConstructor
public class Result<T> {

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 状态信息说明
     */
    private String message;

    /**
     * 返回的数据
     */
    private T data;

    /**
     * description: 成功，只返回状态码和msg，没有返回数据
     *
     * @return 只返回状态码和msg，没有返回数据
     * @author RenShiWei
     * Date: 2020/7/10 19:57
     */
    public static <E> Result<E> success() {
        return new Result<>(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(), null);
    }

    /**
     * description: 成功，封装返回数据
     *
     * @param data 返回数据
     * @return 封装返回的数据
     * @author RenShiWei
     * Date: 2020/7/10 19:57
     */
    public static <E> Result<E> success(E data) {
        return new Result<>(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(), data);
    }


    /**
     * description: 接口调用出现异常，返回枚举异常信息，无数据
     *
     * @return 枚举异常信息, 无数据
     * @author RenShiWei
     * Date: 2020/7/10 19:57
     */
    public static <E> Result<E> error(ResultEnum responseEnum) {
        return new Result<>(responseEnum.getCode(), responseEnum.getMsg(), null);
    }

    /**
     * description: 接口调用出现异常，返回枚举中自定义的状态码及异常信息
     *
     * @param errorCode 异常状态码
     * @param errorMsg  异常信息
     * @return 异常信息，无数据
     * @author RenShiWei
     * Date: 2020/7/10 19:57
     */
    public static <E> Result<E> error(Integer errorCode, String errorMsg) {
        return new Result<>(errorCode, errorMsg, null);
    }

    /**
     * description: 接口调用出现异常，返回默认400的状态码
     *
     * @param errorMsg 异常信息
     * @return 异常信息，无数据
     * @author RenShiWei
     * Date: 2020/7/10 19:57
     */
    public static <E> Result<E> error(String errorMsg) {
        return new Result<>(ResultEnum.BAD_REQUEST.getCode(), errorMsg, null);
    }

}

