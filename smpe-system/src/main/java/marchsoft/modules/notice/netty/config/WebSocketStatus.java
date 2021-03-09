package marchsoft.modules.notice.netty.config;

/**
 * @author Wangmingcan
 * @date 2021/2/21 16:24
 * @description
 */
public enum WebSocketStatus {

    /*
     * 前台检测连接
     */
    PING(0, "前台检测连接"),
    /*
     * 聊天消息
     */
    MESSAGE(1, "聊天消息"),
    /*
     * 对方已离开
     */
    LEAVE(2, "对方已离开");

    private Integer code;

    private String message;

    private WebSocketStatus(Integer code, String message){
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
