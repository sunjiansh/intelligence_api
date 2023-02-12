/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.servermanage.serverrecord.service.dto;

import lombok.Data;
import java.util.Date;
import java.io.Serializable;

/**
* @author jiansun
* @date 2023-02-11
*/
@Data
public class SVipServiceRevordDto implements Serializable {

    private Long id;

    /** 客服人员 */
    private String serverPerson;

    /** 问题类型 */
    private String questionType;

    /** 处理记录 */
    private String handleRecord;

    /** 服务开始时间 */
    private Date serverStartTime;

    /** 服务结束书简 */
    private Date serverEndTime;

    /** 创建时间 */
    private Date createTime;

    /** 被服务人员id */
    private Long memberId;

    /** 被服务人员名称 */
    private String memberName;
}
