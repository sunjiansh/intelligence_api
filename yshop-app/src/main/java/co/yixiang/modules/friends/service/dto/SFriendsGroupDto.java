/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.friends.service.dto;

import lombok.Data;

import java.io.Serializable;

/**
* @author jiansun
* @date 2023-02-23
*/
@Data
public class SFriendsGroupDto implements Serializable {

    private Long id;

    /** 发起人uid */
    private Long mainUid;

    /** 朋友uid */
    private Long friendUid;

    /** 朋友称呼 */
    private String label;

    private String phone;

    private String captcha;

}
