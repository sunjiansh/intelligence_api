/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.monotormanage.geography.service.dto;

import lombok.Data;
import java.util.Date;
import java.io.Serializable;

/**
* @author jiansun
* @date 2023-02-12
*/
@Data
public class SGeographyDto implements Serializable {

    private Long id;

    /** 手环IMEI */
    private String imei;

    /** 围栏名称 */
    private String name;

    /** 围栏地址 */
    private String address;

    /** 围栏半径 */
    private Integer regionRange;

    /** 经度 */
    private Double lon;

    /** 纬度 */
    private Double lat;

    /** 创建时间 */
    private Date createTime;

    /** 更新时间 */
    private Date updateTime;

    /** 会员ID */
    private Long memberId;

    /** 会员人员名称 */
    private String memberName;
}
