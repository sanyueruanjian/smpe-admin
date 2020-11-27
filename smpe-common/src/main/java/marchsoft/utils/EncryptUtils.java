package marchsoft.utils;

import cn.hutool.core.codec.Base64Decoder;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.DES;
import lombok.Data;

/**
 * description 加密解密
 * 基于hutool，混入逻辑处理
 *
 * @author lixiaoshan
 * Date: 2020-09-26 15:29
 */
@Data
public class EncryptUtils {

    /**
     * 密钥
     */
    private final static CharSequence SECRET_KEY = "Kj0m6nPyIJ4=";

    /**
     * 偏移量
     */
    private final static Integer OFF_SET = 10;

    /**
     * 偏移插入字符
     */
    private final static String INSERT_CHAR = "p";

    /**
     * description 加密
     *
     * @param info 需要加密的数据
     * @return 加密后的字符串
     * @author lixiaoshan
     * @date 13:57 2020/9/29
     **/
    public static String desEncrypt(String info) {
        byte[] key = Base64Decoder.decode(SECRET_KEY);
        DES des = SecureUtil.des(key);
        String encrypt = des.encryptHex(info);
        StringBuilder encryptBuffer = new StringBuilder(encrypt);
        return encryptBuffer.reverse().insert(OFF_SET, INSERT_CHAR).toString();
    }

    /**
     * description 解密
     *
     * @param encrypt 需要解密的字符串
     * @return 解密后的字符串
     * @author lixiaoshan
     * @date 13:57 2020/9/29
     **/
    public static String desDecrypt(String encrypt) {
        StringBuilder encryptBuffer = new StringBuilder(encrypt);
        String offSetEncrypt = encryptBuffer.replace(OFF_SET, OFF_SET + 1, "").reverse().toString();
        byte[] key = Base64Decoder.decode(SECRET_KEY);
        DES des = SecureUtil.des(key);
        return des.decryptStr(offSetEncrypt);
    }

}
