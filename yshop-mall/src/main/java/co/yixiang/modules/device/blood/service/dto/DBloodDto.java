/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.blood.service.dto;

import lombok.Data;
import java.util.Date;
import java.io.Serializable;

/**
* @author jiansun
* @date 2023-01-10
*/
@Data
public class DBloodDto implements Serializable {

    private Long id;

    /** 蓝牙名称 */
    private String blName;

    /** 型号 */
    private String model;

    /** 蓝牙ID */
    private String blId;

    /** 品牌 */
    private String brand;

    /** 备注 */
    private String mark;

    /** 创建时间 */
    private Date createTime;

    /** 是否已激活 0 未激活 1 已激活 */
    private Integer isActive;

    private Long mid;
}
