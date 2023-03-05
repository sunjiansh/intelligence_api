/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.device.tumbledatarecords.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
* @author jiansun
* @date 2023-02-02
*/
@Data
@TableName("d_tumble_data_records")
public class DTumbleDataRecords  {
    @TableId
    private Long id;

    /** 报文 */
    private String content;

    /** 指令 */
    private String cmd;

    /** 数据 */
    private String data;

    /** 指令名称 */
    private String cmdName;


    /** 数据上报时间 */
    private Date pushTime;

    private String productId;

    private String imei;

    private String deviceId;

    private String protocol;

    private String messageType;

    private String payload;

    private String serviceId;

    private String tenantId;

    private Long userId;

    /**
     *有人 报警
     * 开关0：关闭， 1：开启
     */
   private String isPersonSwitch;
    /**
     *无人报警
     * 开关0：关闭， 1：开启
     */
   private String noPersonSwitch;
    /**
     *跌倒报警
     * 开关0：关闭， 1：开启
     */
    private String  fallDownSwitch;
    /**
     *驻留报警
     * 开关0：关闭， 1：开启
     */
    private String  lingerSwitch;
    /**
     *驻留报警0:未报警 1：报警
     */
    private String  lingerAlarm;
    /**
     *跌倒报警 0:未报警 1：跌倒报警
     */
    private String  fallDownAlarm;
    /**
     *有人报警 0:未报警 1：有人报警
     */
    private String isPersonAlarm;
    /**
     *无人报警 0:未报警 1：无人报警
     */
    private String noPersonAlarm;
    /**
     *无人/有人 0：无人 1：有人(当前环境状态提示)
     */
    private String existPerson;
    /**
     *故障
     * 0:设备正常
     *  1:设备故障
     */
    private String isError;
    /**
     *无人报警时长,单位：小时
     */
    private String paramNoPersonTime;
    /**
     *驻留时长，单位：分钟
     */
    private String paramLingerTime;
    /**
     *安装高度,单位：cm(安装高度（200~300cm)
     */
    private String paramHight;
    /**
     *电池电量:单位 MV,FFFF 表示无效， 设备不能检测电池电量,AAAA 表示设备外部供电
     */
    private String   paramBattery;
    /**
     *跌倒报警时长05=5秒(跌倒报警时长参数说明（5~180s）)
     */
    private String paramFallDownTime;

    /**
     * appdata
     */
    private String appData;

    private String imsi;

    private String iccid;




    @TableField(fill= FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;


    public void copy(DTumbleDataRecords source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
