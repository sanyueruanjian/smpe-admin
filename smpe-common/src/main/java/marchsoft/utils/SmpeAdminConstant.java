package marchsoft.utils;

/**
 * description: 常用静态常量
 *
 * @author RenShiWei
 * Date: 2020/8/3 21:07
 **/
public class SmpeAdminConstant {

    public static final String RESET_PASS = "重置密码";

    public static final String RESET_MAIL = "重置邮箱";

    /**
     * 用于IP定位转换
     */
    static final String REGION = "内网IP|内网IP";

    /**
     * Url相关常量
     */
    public static class Url {
        // IP归属地查询
        public static final String IP_URL = "http://whois.pconline.com.cn/ipJson.jsp?ip=%s&json=true";
    }

}
