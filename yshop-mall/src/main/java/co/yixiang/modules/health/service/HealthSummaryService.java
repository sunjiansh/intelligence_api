package co.yixiang.modules.health.service;

import co.yixiang.modules.articlemanage.article.domain.SArticle;
import co.yixiang.modules.device.watchuricdatarecords.domain.DWatchUricDataRecords;
import co.yixiang.modules.device.watchuricdatarecords.service.dto.DWatchUricDataRecordsDto;
import org.apache.ibatis.annotations.Param;
import org.apache.xmlbeans.impl.xb.xsdschema.LocalSimpleType;

import java.util.List;
import java.util.Map;

/**
 * @author sunjian
 * @Date 2023/2/26
 * @license 版权所有，非经许可请勿使用本代码
 */
public interface HealthSummaryService  {


    DWatchUricDataRecordsDto getSleepRecordByDay(String day, Long uid);

    List<SArticle> getHealthArticlesForPage(Integer start,Integer pageSize);

    List<Map> getBloodPreasureByDay(String day, Long uid);

    List<Map> getHeartRateByDay(String day, Long uid);

    List<Map> getOxygenByDay(String day, Long uid);

    List<Map> getTemperatureByDay(String day, Long uid);

    List<Map> getWeightByDay(String day, Long uid);

    List<Map> getBloodSugarByDay(String day, Long uid);

    List<Map> getPulseRateByDay(String day, Long uid);

    List<Map> getEcgByDay(String day, Long uid);

    List<Map> getUricAcidByDay(String day, Long uid);

    List<Map> getFallDownByDay(String day, Long uid);


    List<Map> getUsersByDmainUnitImei(String imei);

}
