package co.yixiang.common.util;

import co.yixiang.utils.RedisUtils;
import co.yixiang.utils.SpringContextHolder;

/**
 * @author sunjian
 * @Date 2023/2/25
 * @license 版权所有，非经许可请勿使用本代码
 */
public class RedisContans {

    private static RedisUtils redisUtil = (RedisUtils) SpringContextHolder.getBean(RedisUtils.class);

    private static final String TERMINAL_CURRENT_USER_PREFIX="TERMINAL_CURRENT_USER_IMEI_";


    public static Long getTerminalCurrentUserId(String imei){
        return (Long)redisUtil.get(TERMINAL_CURRENT_USER_PREFIX+imei);
    }

    public static boolean setTerminalCurrentUserId(String imei,Long userId){
     return  redisUtil.set(TERMINAL_CURRENT_USER_PREFIX+imei,userId);
    }


}
