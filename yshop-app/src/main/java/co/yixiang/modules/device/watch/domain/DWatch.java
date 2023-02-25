/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.watch.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
* @author jiansun
* @date 2023-01-10
*/
@Data
@TableName("d_watch")
public class DWatch  {
    @TableId
    private Long id;

    /** 名称 */
    private String name;

    /** imei */
    @NotBlank
    private String imei;

    /** 型号 */
    private String model;

    /** 序列号 */
    private String sn;

    /** 品牌 */
    private String brand;

    /** 备注 */
    private String mark;


    /** 是否已激活 0 未激活 1 已激活 */
    private Integer isActive;

    /**
     * 是否已初始化配置到手环平台  0 未初始化 1 已初始化
     */
    private Integer isConfig;

    /**
     * 上报位置的时间间隔，单位是秒，此上传间隔针对手表处于运动状态时，手表静止时不传位置数据
     */
    private Integer location;
    /**
     * 心率、血压、血氧、血糖上传频率(单位秒,连续上传时最小时间不小于 300 秒，最大不超过 65535)0：关闭 1：单次上传
     */
    private Integer bldstart;
    /**
     * 体温上传频率(单位秒,连续上传时最小时间不小于 300 秒，最大不超过 65535)0：关闭 1：单次上传
     */
    private Integer wdstart;
    /**
     * sos号码，多个号码以英文逗号分割，最多三个
     */
    private String sos;
    /**
     * 睡眠开关 0：关闭 1：打开
     */
    private Integer sleepSwitch;
    /**
     *睡眠测量开始时间 格式：HH:mm
     */
    private String sleepTimeStartStr;
    /**
     *睡眠测量截至时间 格式：HH:mm
     */
    private String sleepTimeEndStr;
    /**
     * 高压预警值
     */
    private Double calibrateSbp;
    /**
     * 低压值预警值
     */
    private Double calibrateDbp;

    /**
     * 一组联系人，使用|分隔名称和电话号码，名称使用UNICODE编码直接下发byte，多组之间使用逗号分隔，未填写的可为空，但位置仍需保留";"白名单无更新，通过覆盖来设置
     *      示例：""whiteList"":[""D3590D54|189*****192"",""D3590D54|189*****192""]
     *      移除所有白名单列表：""whiteList"":[""""]
     */
    private String whiteListStr;

    /**
     * 高温预警
     */
    private Double temperatureHeight;

    /**
     * 低温预警
     */
    private Double temperatureLow;


    /**
     * 最高心率
     */
    private Double hrHeight;

    /**
     * 最低心率
     */
    private Double hrLow;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;


    public void copy(DWatch source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
