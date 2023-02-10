/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.device.tumbledatarecords.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
* @author jiansun
* @date 2023-02-02
*/
@Data
public class DTumbleDataRecordsDto implements Serializable {

    private Long id;

    /** 报文 */
    private String content;

    /** 指令 */
    private String cmd;

    /** 数据 */
    private String data;

    /** 指令名称 */
    private String cmdName;

    /** 创建时间 */
    private Date createTime;

    /** 数据上报时间 */
    private Date pushTime;
}
