/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.device.balancedatarecords.domain;
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
@TableName("d_balance_data_records")
public class DBalanceDataRecords  {
    @TableId
    private Long id;

    /** 用户id */
    @NotNull
    private Long uid;

    /** 阻抗 */
    private String impedance;

    /** kg */
    private Float bodyWeight;

    /** kg */
    private Float bodyBmi;

    /** 体脂率 % */
    private Float bodyFatPercentage;

    /** 皮下脂肪（%） */
    private Float subcutaneousFat;

    /** 内脏脂肪等级 */
    private Float visceralFatGrade;

    /** 肌肉量 kg */
    private Float muscleMass;

    /** 基础代谢 kcal */
    private Float basalMetabolism;

    /** 骨量 kg */
    private Float boneMass;

    /** 水分 % */
    private Float waterContent;

    /** 身体年龄 岁 */
    private Float physicalAge;

    /** 蛋白率 % */
    private Float proteinPercentage;

    /** 设备序列号 */
    private String sn;

    @TableField(fill= FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

    public void copy(DBalanceDataRecords source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
