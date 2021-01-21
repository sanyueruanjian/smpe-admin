package marchsoft.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author lixiangxiang
 * @description 异常工具
 * @date 2021/1/20 16:50
 */
public class ThrowableUtil {

    /**
     * 获取异常信息
     */
    public static String getStackTrace(Throwable throwable){
        StringWriter sw = new StringWriter();
        try (PrintWriter pw = new PrintWriter(sw)) {
            throwable.printStackTrace(pw);
            return sw.toString();
        }
    }

}
