/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.device.mdeviceuser.service.dto;

import lombok.Data;
import java.io.Serializable;

/**
* @author jiansun
* @date 2023-01-14
*/
@Data
public class DMdeviceUserDto implements Serializable {

    private Long id;

    /** 用户ID */
    private Long uid;

    /** 主机ID */
    private Long mid;

    private String phone;

    private String realName;

}
