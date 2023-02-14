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
import java.util.List;
import co.yixiang.annotation.Query;

/**
* @author jiansun
* @date 2023-02-13
*/
@Data
public class SAlarmReccordQueryCriteria{

    /** 精确 */
    @Query
    private Integer alarmType;

    /** 模糊 */
    @Query(type = Query.Type.INNER_LIKE)
    private String phone;

    /** 精确 */
    @Query
    private String imei;

    /** 模糊 */
    @Query(type = Query.Type.INNER_LIKE)
    private String menberName;
    /** BETWEEN */
    @Query(type = Query.Type.BETWEEN)
    private List<Date> createTime;
}
