package co.yixiang.modules.health.service.mapper;

import co.yixiang.common.mapper.CoreMapper;
import co.yixiang.modules.articlemanage.article.domain.SArticle;
import co.yixiang.modules.device.watchuricdatarecords.service.dto.DWatchUricDataRecordsDto;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author sunjian
 * @Date 2023/2/26
 * @license 版权所有，非经许可请勿使用本代码
 */
@Repository
public interface HealthSummaryMapper  extends CoreMapper {


        @Select("select a.* from s_article a where  a.status=1 and  a.type="+SArticle.ARTICLE_TYPE_HEALTH+" order by a.create_time desc limit #{start},#{pageSize}  ")
        List<SArticle> queryHealthArticlesForPage(@Param("start") Integer start,@Param("pageSize") Integer pageSize);


        //睡眠数据
        @Select("select * from view_sleep_records where userId=#{uid} and sleepDate = #{day} ")
        DWatchUricDataRecordsDto querySleepRecordByDay(@Param("day") String day, @Param("uid") Long uid);

        //血压数据
        @Select("select * from view_blood_preasure_records where userId=#{uid} and pushDay = #{day} ")
        List<Map> queryBloodPreasureByDay(@Param("day") String day, @Param("uid") Long uid);

        //心率数据
        @Select("select * from view_heart_rate_records where userId=#{uid} and pushDay = #{day} ")
        List<Map> queryHeartRateByDay(@Param("day") String day, @Param("uid") Long uid);

        //血氧数据
        @Select("select * from view_oxygen_records where userId=#{uid} and pushDay = #{day} ")
        List<Map> queryOxygenByDay(@Param("day") String day, @Param("uid") Long uid);


        //体温数据
        @Select("select * from view_temperature_records where userId=#{uid} and pushDay = #{day} ")
        List<Map> queryTemperatureByDay(@Param("day") String day, @Param("uid") Long uid);


        //体脂秤数据
        @Select("select * from view_balance_records where userId=#{uid} and pushDay = #{day} ")
        List<Map> queryWeightByDay(@Param("day") String day, @Param("uid") Long uid);


        // 血糖数据
        @Select("select * from view_blood_sugar_records where userId=#{uid} and pushDay = #{day} ")
        List<Map> queryBloodSugarByDay(@Param("day") String day, @Param("uid") Long uid);

        //脉搏数据
        @Select("select * from view_pulse_rate_records where userId=#{uid} and pushDay = #{day} ")
        List<Map> queryPulseRateByDay(@Param("day") String day, @Param("uid") Long uid);


        //尿酸数据
        @Select("select * from view_uric_acid_records where userId=#{uid} and pushDay = #{day} ")
        List<Map> queryUricAcidByDay(@Param("day") String day, @Param("uid") Long uid);


        //跌倒数据
        @Select("select * from view_fall_down_records where userId=#{uid} and pushDay = #{day} ")
        List<Map> queryFallDownByDay(@Param("day") String day, @Param("uid") Long uid);


        // 心电图数据
        @Select("select * from view_ecg_data_records where userId=#{uid} limit 20 ")
        List<Map> queryEcgDataRecords(@Param("uid") Long uid);

        /**
         * 根据主机IMEI查询绑定的用户
         * @param imei
         * @return
         */
        @Select("select a.uid,a.real_name realName,a.phone,a.avatar,a.sex,a.age,a.sos_contact as sos from yx_user a inner join d_mdevice_user b on a.uid = b.uid inner join d_mailunit c on c.id = b.mid where a.status=1 and c.imei=#{imei} ")
        List<Map<String,Object>> queryUsersByDmainUnitImei(@Param("imei") String imei);






        //睡眠数据
        @Select("select * from view_sleep_records where userId=#{uid} order by pushTime desc  limit 1 ")
        Map<String,Object> queryLatestSleepRecord(@Param("uid") Long uid);

        //血压数据
        @Select("select * from view_blood_preasure_records where userId=#{uid} order by pushTime desc limit 1 ")
        Map<String,Object> queryLatestBloodPreasure( @Param("uid") Long uid);

        //心率数据
        @Select("select * from view_heart_rate_records where userId=#{uid} order by pushTime desc limit 1 ")
        Map<String,Object> queryLatestHeartRate(@Param("uid") Long uid);

        //血氧数据
        @Select("select * from view_oxygen_records where userId=#{uid} order by pushTime desc  limit 1 ")
        Map<String,Object> queryLatestOxygen( @Param("uid") Long uid);


        //体温数据
        @Select("select * from view_temperature_records where userId=#{uid} order by pushTime desc limit 1 ")
        Map<String,Object> queryLatestTemperature(@Param("uid") Long uid);


        //体脂秤数据
        @Select("select * from view_balance_records where userId=#{uid} order by pushTime desc limit 1 ")
        Map<String,Object> queryLatestWeight( @Param("uid") Long uid);


        // 血糖数据
        @Select("select * from view_blood_sugar_records where userId=#{uid} order by pushTime desc limit 1 ")
        Map<String,Object> queryLatestBloodSugar(@Param("uid") Long uid);

        //脉搏数据
        @Select("select * from view_pulse_rate_records where userId=#{uid} order by pushTime desc  limit 1 ")
        Map<String,Object> queryLatestPulseRate(@Param("uid") Long uid);


        //尿酸数据
        @Select("select * from view_uric_acid_records where userId=#{uid} order by pushTime desc limit 1 ")
        Map<String,Object> queryLatestUricAcid( @Param("uid") Long uid);


        //跌倒数据
        @Select("select * from view_fall_down_records where userId=#{uid} order by pushTime desc limit 1 ")
        Map<String,Object> queryLatestFallDown( @Param("uid") Long uid);


        // 心电图数据
        @Select("select * from view_ecg_data_records where userId=#{uid} limit 1 ")
        Map<String,Object> queryLatestEcgDataRecords(@Param("uid") Long uid);







}
