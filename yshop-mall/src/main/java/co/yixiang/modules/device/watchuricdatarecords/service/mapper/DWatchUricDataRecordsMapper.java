/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.device.watchuricdatarecords.service.mapper;

import co.yixiang.common.mapper.CoreMapper;
import co.yixiang.modules.device.watchuricdatarecords.domain.DWatchUricDataRecords;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
* @author jiansun
* @date 2023-01-29
*/
@Repository
public interface DWatchUricDataRecordsMapper extends CoreMapper<DWatchUricDataRecords> {



    @Select("select * from view_map_location_records where imei = #{imei} and push_day= #{day} ")
    public List<Map> queryMapLocationRecordsByImei(@Param("day") String day,@Param("imei") String imei);


    @Select("select * from view_map_location_records where user_id = #{uid} and push_day= #{day} ")
    public List<Map> queryMapLocationRecordsByUid(@Param("day") String day,@Param("uid") Long uid);

}
