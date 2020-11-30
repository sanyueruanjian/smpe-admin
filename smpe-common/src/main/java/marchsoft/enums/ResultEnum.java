package marchsoft.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * description:统一响应信息枚举
 *
 * @author RenShiWei
 * Date: 2020/7/9 21:41
 **/
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ResultEnum {


    /**
     * 成功（默认返回状态码）
     */
    SUCCESS(0, "SUCCESS"),

    /**
     * 全局未知异常
     */
    SEVER_ERROR(500, "服务器异常,请重试"),

    /**
     * 请求失败（一般前端处理，不常用）
     */
    BAD_REQUEST(400, "请求失败"),

    /*
     * 登录、权限认证异常
     */
    LOGIN_EXPIRE(401, "未登录或当前登录状态过期"),
    IDENTITY_NOT_POW(403, "您的用户权限不足"),


    /*
       ====通用异常====
     */

    /*
        1001-1010 通用操作相关
     */
    OPERATION_FAIL(1001, "操作失败！"),
    SELECT_OPERATION_FAIL(1002, "查询操作失败！"),
    UPDATE_OPERATION_FAIL(1003, "更新操作失败！"),
    DELETE_OPERATION_FAIL(1004, "删除操作失败！"),
    INSERT_OPERATION_FAIL(1005, "新增操作失败！"),
    DATA_NOT_FOUND(1006, "查询数据不存在"),
    OPERATION_MIDDLE_FAIL(1007, "维护中间表失败！"),
    ALTER_DATA_NOT_EXIST(1008, "修改数据不存在！"),

    /*
        1011-1050 登录注册相关
     */
    LOGIN_FAIL(1011, "登录失败，账号或者密码错误"),
    LOGIN_FAIL_RELOGIN(1012, "登录失败，请重试"),
    LOGIN_FAIL_CODE(1013, "验证码错误"),
    NO_USER(1014, "用户不存在"),
    REGISTER_FAIL(1015, "注册失败，手机号已经存在"),
    NO_USER_PHONE(1016, "认证失败，手机号不存在"),
    PARAMS_NOT_NULL(1017, "请求参数不能为空"),
    LOGIN_INFO_NOT_FOUND(1018, "找不到当前登录的信息"),
    LOGIN_USER_INFO_NOT_FOUND(1019, "未能查到当前登录用户的信息"),
    COUNT_NOT_ENABLE(1020, "账号未激活"),

    /*
        1051-1070 短信业务相关
     */
    SMS_NOT_SEND(1051, "短信发送失败"),
    SMS_CODE_EXPIRE(1052, "短信验证码失效"),
    SMS_CODE_VERITY_FAIL(1053, "短信验证码验证失败"),

    /*
        1071-1100 文件、资源相关
     */
    FILE_OVERSTEP_SIZE(1071, "文件超出规定大小"),
    FILE_UPLOAD_FAIL(1072, "文件上传失败"),
    FILE_LOADING_FAIL(1073, "文件不存在，加载失败"),
    FILE_REQUEST_FAIL(1074, "文件类型不支持查看"),
    FILE_TYPE_IMAGE_FAIL(1075, "请上传图片类型的文件"),
    FILE_DOWNLOAD_FAIL_NOT_DATA(1076, "数据不存在，不能导出"),

    /*
        1101-1199 请求参数相关
     */
    PARAM_IS_INVALID(1101, "参数无效"),
    PARAM_IS_BLANK(1102, "参数为空"),
    PARAM_TYPE_BIND_ERROR(1103, "参数类型错误"),
    PARAM_NOT_COMPLETE(1104, "参数缺失"),
    PARAM_VERIFY_FAILURE(1105, "参数验证失败"),
    ENTITY_FILED_EXIT(1106, "实体/属性/参数值已存在"),
    ENTITY_FILED_NOT_EXIT(1107, "实体/属性/参数值不存在"),

    /*
       框架
     */
    PERMISSION_DENIED(1120, "权限不足"),
    USER_USERNAME_EXIST(1121, "当前用户名已存在"),
    USER_EMAIL_EXIST(1122, "当前用户邮箱已存在"),
    USER_NOT_EXIST(1123, "用户不存在"),
    ROLE_NAME_EXIST(1124, "角色名已存在"),



    /*
        -----------业务相关（2xxx）------------
     */


    /*
        第三方相关（3xxx）
     */
    /*
        3001-3020 微信公众号
     */
    WX_GZH_ACCESS_TOKEN_FAIL(3001, "微信公众号JSSDK获取access_token失败"),
    WX_GZH_JS_API_TICKET_FAIL(3002, "微信公众号JSSDK获取jsapi_ticket失败"),
    WX_GZH_SIGN_FAIL(3003, "微信公众号JSSDK获取SIGN失败"),
    WX_CODE_EMPTY(3004, "微信wxCode为空"),
    WX_CODE_OUTTIME(3005, "微信wxCode失效或不正确请重新获取"),

    ;
    private int code;

    private String msg;

}
