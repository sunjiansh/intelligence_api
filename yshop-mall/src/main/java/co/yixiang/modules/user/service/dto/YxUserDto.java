/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yixiang.co

 */
package co.yixiang.modules.user.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
* @author hupeng
* @date 2020-05-12
*/
@Data
public class YxUserDto implements Serializable {

    /** 用户id */
    private Long uid;

    /** 用户账户(跟accout一样) */
    private String username;

    /** 用户密码（跟pwd） */
    private String password;


    /** 真实姓名 */
    private String realName;

    /** 生日 */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date birthday;

    /** 身份证号码 */
    private String cardId;

    /** 用户备注 */
    private String mark;

    /** 合伙人id */
    private Integer partnerId;

    /** 用户分组id */
    private Integer groupId;

    /** 用户昵称 */
    private String nickname = "";

    /** 用户头像 */
    private String avatar;

    /** 手机号码 */
    private String phone;

    /**
     * 性别 0：女 1：男  2：未知
     */
    private Integer sex;

    /** 添加时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

    /** 添加ip */
    private String addIp;


    /** 用户余额 */
    private BigDecimal nowMoney;

    /** 佣金金额 */
    private BigDecimal brokeragePrice;

    /** 用户剩余积分 */
    private BigDecimal integral;

    /** 连续签到天数 */
    private Integer signNum;

    /** 1为正常，0为禁止 */
    private Integer status;

    /** 等级 */
    private Integer level;

    /** 推广元id */
    private Long spreadUid;

    /** 推广员关联时间 */
    private Date spreadTime;

    /** 用户类型 */
    private String userType;

    /** 是否为推广员 */
    private Integer isPromoter;

    /** 用户购买次数 */
    private Integer payCount;

    /** 下级人数 */
    private Integer spreadCount;

    /** 详细地址 */
    private String addres;

    /** 管理员编号  */
    private Integer adminid;

    /** 用户登陆类型，h5,wechat,routine */
    private String loginType;


    /**
     * 绑定的智能手环IMEI号
     */
    private String imei;

    /**
     * 是否已同步手环信息到手环平台，0 未同步 1 已同步
     */
    private Integer watchBind;


    /**
     * 绑定的尿酸分析仪SN号
     */
    private String uricSn;

    /**
     * 是否已同步尿酸分析仪信息到手环平台，0 未同步 1 已同步
     */
    private Integer uricBind;

    /**
     * 紧急联系人电话号码，多个用英文“，”隔开
     */
    private String sosContact;


    /** 用户年龄 */
    private Integer age;

    /**
     * 会员服务开始日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date serviceStart;

    /**
     * 会员服务结束日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date serviceEnd;




}
