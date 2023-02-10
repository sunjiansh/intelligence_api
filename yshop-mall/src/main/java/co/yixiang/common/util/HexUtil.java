package co.yixiang.common.util;

import co.yixiang.utils.StringUtils;

import java.math.BigInteger;
import java.util.Arrays;

/**
 * @author sunjian
 * @Date 2023/2/2
 * @license 版权所有，非经许可请勿使用本代码
 */
public class HexUtil {
    /**
     * byte数组转int
     * @param bytes
     * @return
     */
    public static int bytes2Int(byte[] bytes) {
        // 根据byte的位数
        int result = 0x00000000;
        for (int i = 0; i < bytes.length; i++) {
            result = result ^ ((bytes[i] & 0x000000ff) << (8 * (bytes.length - i - 1)));
        }
        return result;
    }

    public static int bytes2Int(byte a, byte b) {
        return a << 8 & 0x0000ff00 ^ b & 0x000000ff;
    }

    /**
     * byte数组 转换成 16进制大写字符串
     */
    public static String bytes2Hex(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }

        StringBuilder hex = new StringBuilder();

        for (byte b : bytes) {
            hex.append(Integer.toHexString(b & 0x00000ff)); // 将低八位取出来
        }

        return hex.toString().toUpperCase();
    }

    /**
     * 16进制字符串 转换为对应的 byte数组
     */
    public static byte[] hex2Bytes(String hex) {
        if (hex == null || hex.length() == 0) {
            return null;
        }

        char[] hexChars = hex.toCharArray();
        byte[] bytes = new byte[hexChars.length / 2];   // 如果 hex 中的字符不是偶数个, 则忽略最后一个

        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt("" + hexChars[i * 2] + hexChars[i * 2 + 1], 16);
        }

        return bytes;
    }

    /**
     * 将16进制字符转换为二进制字符
     * @param hexString
     * @return
     */
    public static String hexString2binaryString(String hexString) {
        //16进制转10进制
        BigInteger sint = new BigInteger(hexString, 16);
        //10进制转2进制
        String result = sint.toString(2);
        //字符串反转
        return new StringBuilder(result).reverse().toString();
    }

/**
 * 二进制转成十六进制
 */
    public static String binary2HexString(String binary){
        if(binary == null || (binary.length() % 8 != 0)){
            return "";
        }
        StringBuilder hex = new StringBuilder();
        int temp;
        for(int i = 0;i < binary.length();i += 4){
            temp = 0;
            for(int j = 0;j < 4;j++){
                temp += Integer.parseInt(binary.substring(i + j,i + j + 1)) << (4 - j -1);
            }
            hex.append(Integer.toHexString(temp));
        }
        return hex.toString();
    }


    /**
     * 十六进制转二进制
     * @param hex
     * @return
     */
    public static  String hexToBinaryString(String hex){
        if(hex == null || hex.length() % 2 != 0){
            return "";
        }
        StringBuilder binary = new StringBuilder();
        String temp;
        for(int i = 0;i < hex.length();i++){
            temp = "0000" +Integer.toBinaryString(Integer.parseInt(hex.substring(i,i + 1),16));
            binary.append(temp.substring(temp.length() - 4));
        }
        return binary.toString();
    }


    /**
     * 高低字节转换
     * @param hex
     * @return
     */
    public static String reverseHex(String hex) {
        char[] charArray = hex.toCharArray();
        int length = charArray.length;
        int times = length / 2;
        for (int c1i = 0; c1i < times; c1i += 2) {
            int c2i = c1i + 1;
            char c1 = charArray[c1i];
            char c2 = charArray[c2i];
            int c3i = length - c1i - 2;
            int c4i = length - c1i - 1;
            charArray[c1i] = charArray[c3i];
            charArray[c2i] = charArray[c4i];
            charArray[c3i] = c1;
            charArray[c4i] = c2;
        }
        return new String(charArray);
    }


    /**
     * 十六进制转十进制
     * @param content
     * @return
     */
    public static int hexToDec(String content){
        int number=Integer.parseInt(content, 16);
        return number;
    }


    /**
     * 十进制转十六进制
     * @param num
     * @return
     */
    public static String decToHex(Integer num){
        String hex = Integer.toHexString(num);
        return hex;
    }

    /**
     * 字符串s左侧加0补齐长度至length
     * @param s
     * @param length
     * @return
     */
    public static String padLeft(String s, int length) {
        byte[] bs = new byte[length];
        byte[] ss = s.getBytes();
        Arrays.fill(bs, (byte) (48 & 0xff));
        System.arraycopy(ss, 0, bs,length - ss.length, ss.length);
        return new String(bs);
    }

    /**
     * 取字符的最低位校验和
     * @param data
     * @return
     */
    public static String makeChecksum(String data) {
        if (StringUtils.isEmpty(data))
        {
            return "";
        }

        int iTotal = 0;
        int iLen = data.length();
        int iNum = 0;

        while (iNum < iLen)
        {
            String s = data.substring(iNum, iNum + 2);
           // System.out.println(s);
            iTotal += Integer.parseInt(s, 16);
            iNum = iNum + 2;
        }

        /**
         * 用256求余最大是255，即16进制的FF
         */
        int iMod = iTotal % 256;
        String sHex = Integer.toHexString(iMod);
        iLen = sHex.length();
        //如果不够校验位的长度，补0,这里用的是两位校验
        if (iLen < 2)
        {
            sHex = "0" + sHex;
        }
        return sHex;
    }

}
