/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.device.mdevicedevice.domain;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.validation.constraints.*;
import co.yixiang.domain.BaseDomain;

/**
* @author jiansun
* @date 2023-01-14
*/
@Data
@TableName("d_mdevice_device")
public class DMdeviceDevice  {

    public static final String DTYPE_BLOOD = "0";//血糖仪
    public static final String DTYPE_TUMBLE = "1";//跌倒报警器
    public static final String DTYPE_BALANCE = "2";//体脂秤
    public static final String DTYPE_BLOOD_FAT= "3";//血脂仪
    public static final String DTYPE_ECG= "4";//心电图


    @TableId
    private Long id;

    /** 主机ID */
    @NotNull
    private Long mid;

    /** 其他设备ID */
    @NotNull
    private Long did;

    /** 设备类型 */
    @NotBlank
    private String dtype;


    public void copy(DMdeviceDevice source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
