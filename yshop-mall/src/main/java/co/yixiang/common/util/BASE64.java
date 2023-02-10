package co.yixiang.common.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * @author sunjian
 * @Date 2022/12/31
 *  BASE64加密解密
 * @license 版权所有，非经许可请勿使用本代码
 */
public class BASE64 {
    /**
     * BASE64解密
     *
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptBASE64(String key) throws Exception {
        return (new BASE64Decoder()).decodeBuffer(key);
    }

    /**
     * BASE64加密
     *
     * @param key
     * @return
     * @throws Exception
     */
    public static String encryptBASE64(byte[] key) throws Exception {
        return (new BASE64Encoder()).encodeBuffer(key);
    }

}
