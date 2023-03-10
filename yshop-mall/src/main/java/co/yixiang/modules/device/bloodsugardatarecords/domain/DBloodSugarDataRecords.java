/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.device.bloodsugardatarecords.domain;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.validation.constraints.*;
import java.util.Date;
import co.yixiang.domain.BaseDomain;

/**
* @author jiansun
* @date 2023-03-04
*/
@Data
@TableName("d_blood_sugar_data_records")
public class DBloodSugarDataRecords {
    @TableId
    private Long id;

    /** 用户id */
    @NotNull
    private Long uid;

    /** 血糖 */
    private String bloodSugar;

    /** 血氧饱和度 */
    private String bloodOxygenSaturation;

    /** 血红蛋白浓度 */
    private String hemoglobinConcentration;

    /** 血流速度，3位整数 */
    private String bloodFlowVelocity;

    /** 环境温度 */
    private String environmentTemperature;

    /** 环境湿度 */
    private String environmentHumidity;

    /** 体表温度 */
    private String bodyTemperature;

    /** 体表湿度 */
    private String bodySurfaceHumidity;

    /** 脉搏数，3位整数 */
    private String pulseRate;

    /** 电量状态（%） */
    private String battery;


    /** 设备序列号 */
    private String sn;

    @TableField(fill= FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;


    public void copy(DBloodSugarDataRecords source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
