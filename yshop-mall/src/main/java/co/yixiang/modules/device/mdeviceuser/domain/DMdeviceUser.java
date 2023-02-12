/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.device.mdeviceuser.domain;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.validation.constraints.*;
import co.yixiang.domain.BaseDomain;

import java.util.Date;

/**
* @author jiansun
* @date 2023-01-14
*/
@Data
@TableName("d_mdevice_user")
public class DMdeviceUser  {
    @TableId
    private Long id;

    /** 用户ID */
    @NotNull
    private Long uid;

    /** 主机ID */
    @NotNull
    private Long mid;


    public void copy(DMdeviceUser source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
