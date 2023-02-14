/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.monotormanage.alarmrecord.service.dto;

import lombok.Data;
import java.util.Date;
import java.io.Serializable;

/**
* @author jiansun
* @date 2023-02-13
*/
@Data
public class SAlarmReccordDto implements Serializable {

    private Long id;

    /** 报警类型 */
    private Integer alarmType;

    /** 报警内容 */
    private String content;

    /** 手机号 */
    private String phone;

    /** 创建时间 */
    private Date createTime;

    /** 手环imei */
    private String imei;

    /** 会员姓名 */
    private String menberName;

    /** 会员姓id */
    private Long memberId;
}
