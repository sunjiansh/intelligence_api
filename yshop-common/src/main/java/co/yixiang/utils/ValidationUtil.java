/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yixiang.co

 */
package co.yixiang.utils;

import cn.hutool.core.util.ObjectUtil;
import co.yixiang.exception.BadRequestException;
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 验证工具
 * @author Zheng Jie
 * @date 2018-11-23
 */
public class ValidationUtil{

    /**
     * 验证空
     */
    public static void isNull(Object obj, String entity, String parameter , Object value){
        if(ObjectUtil.isNull(obj)){
            String msg = entity + " 不存在: "+ parameter +" is "+ value;
            throw new BadRequestException(msg);
        }
    }

    /**
     * 验证是否为邮箱
     */
    public static boolean isEmail(String email) {
        return new EmailValidator().isValid(email, null);
    }

    /**
     * 验证手机号是否合法
     * @return
     */
    public static boolean isPhoneNumber(String number){
        String regExp = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
        Pattern  p = Pattern.compile(regExp);
        Matcher m = p.matcher(number);
        return m.matches();
    }




}
