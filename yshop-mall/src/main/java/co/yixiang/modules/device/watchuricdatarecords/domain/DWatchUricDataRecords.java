/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.device.watchuricdatarecords.domain;

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
* @date 2023-01-29
*/
@Data
@TableName("d_watch_uric_data_records")
public class DWatchUricDataRecords  {
    @TableId
    private Long id;

    /** 指令 */
    private String cmd;

    /** 指令名称 */
    private String cmdName;

    /** 电量 */
    private String electric;

    /** 步数 */
    private String step;

    /** 卡路里 */
    private Long calories;

    /** 舒张压 */
    private Double dbp;

    /** 收缩压 */
    private Double sbp;

    /** 心率 */
    private Double heartRate;

    /** 睡眠总时长，单位：秒 */
    private Long allSleepTime;

    /** 睡眠日期,日期格式是yyyy-MM-dd */
    private String sleepDate;

    /** 浅睡时长，单位：秒 */
    private Long lowSleepTime;

    /** 深睡时长，单位：秒 */
    private Long deepSleepTime;

    /** 睡眠数据曲线 */
    private String sleepLine;

    /** 睡眠质量[0-4]级 */
    private Integer sleepQuality;

    /** 睡眠中起床次数 */
    private Integer wakeCount;

    /** 入睡时间戳 */
    private Long sleepDown;

    /** 出睡时间戳 */
    private Long sleepUp;

    /** 血氧 */
    private Double oxygen;

    /** 体温 */
    private String temperature;

    /** 经纬度 */
    private String location;

    /** 定位描述 */
    private String locationDesc;

    /** 定位类型 */
    private String type;

    /** 围栏通知描述用户id */
    private String uid;

    /** 围栏通知  1：进围栏 2：出围栏 */
    private String tag;

    /** 围栏的唯一标识 */
    private String duid;

    /** 血糖仪数据记录时间 */
    private Long recordTime;

    /** 糖化血红蛋白（mmol/l） */
    private Double ghb;

    /** 尿酸值（mmol/l） */
    private Double uricAcid;

    /** 血酮值（mmol/l） */
    private Double ketone;

    /** 数据上传时间 */
    private Date pushTime;

    /** 数据创建时间时间 */
    @TableField(fill= FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;


    private String imei;

    private String deviceModel;

    private Long userId;

    //测量方式：0自动测量，1手动测量，2手动输入
    private Integer measureMode;

    //血糖、尿酸检测 1：空腹测量 2：餐后2小时测量 3：餐前测量 4：其它测量时间
    private Integer timePeriod;


    public void copy(DWatchUricDataRecords source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
