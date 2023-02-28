package co.yixiang.modules.health.service.mapper;

import co.yixiang.common.mapper.CoreMapper;
import co.yixiang.modules.articlemanage.article.domain.SArticle;
import co.yixiang.modules.device.watchuricdatarecords.domain.DWatchUricDataRecords;
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

        @Select("select * from view_sleep_records where userId=#{uid} and sleepDate = #{day} ")
        DWatchUricDataRecords querySleepRecordByDay(@Param("day") String day,@Param("uid") Long uid);


        @Select("select a.* from s_article a where  a.status=1 and  a.type='"+SArticle.ARTICLE_TYPE_HEALTH+"' order by a.create_time desc limit #{start},#{pageSize}  ")
        List<SArticle> queryHealthArticlesForPage(@Param("start") Integer start,@Param("pageSize") Integer pageSize);



        @Select("select * from view_blood_preasure_records where userId=#{uid} and pushDay = #{day} ")
        List<Map> queryBloodPreasureByDay(@Param("day") String day, @Param("uid") Long uid);


        @Select("select * from view_heart_rate_records where userId=#{uid} and pushDay = #{day} ")
        List<Map> queryHeartRateByDay(@Param("day") String day, @Param("uid") Long uid);

        @Select("select * from view_oxygen_records where userId=#{uid} and pushDay = #{day} ")
        List<Map> queryOxygenByDay(@Param("day") String day, @Param("uid") Long uid);


}
