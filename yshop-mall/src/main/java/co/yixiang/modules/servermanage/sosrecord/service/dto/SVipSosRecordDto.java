/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.servermanage.sosrecord.service.dto;

import lombok.Data;
import java.util.Date;
import java.io.Serializable;

/**
* @author jiansun
* @date 2023-02-11
*/
@Data
public class SVipSosRecordDto implements Serializable {

    private Long id;

    /** 会员名称 */
    private String memberName;

    /** 会员id */
    private Long memberId;

    /** 会员手机号 */
    private String memberPhone;

    /** sos呼叫时间 */
    private Date sosTime;

    /** 紧急联系号码 */
    private String sosContact;

    /** 会员截止日期 */
    private Date serviceEndTime;

    /** 会员剩余天数 */
    private Integer lastDays;

    /** 创建时间 */
    private Date createTime;
}
