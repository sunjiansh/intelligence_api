package co.yixiang.modules.health.service.impl;

import co.yixiang.modules.articlemanage.article.domain.SArticle;
import co.yixiang.modules.device.watchuricdatarecords.service.dto.DWatchUricDataRecordsDto;
import co.yixiang.modules.health.service.HealthSummaryService;
import co.yixiang.modules.health.service.mapper.HealthSummaryMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author sunjian
 * @Date 2023/2/26
 * @license 版权所有，非经许可请勿使用本代码
 */
@Service
@AllArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class HealthSummaryServiceImpl  implements HealthSummaryService {

    private final HealthSummaryMapper healthSummaryMapper;

    @Override
    public DWatchUricDataRecordsDto getSleepRecordByDay(String day, Long uid) {
        return healthSummaryMapper.querySleepRecordByDay(day,uid);
    }

    @Override
    public List<SArticle> getHealthArticlesForPage(Integer start, Integer pageSize) {
        return healthSummaryMapper.queryHealthArticlesForPage(start,pageSize);
    }

    @Override
    public List<Map> getBloodPreasureByDay(String day, Long uid) {
        return healthSummaryMapper.queryBloodPreasureByDay(day,uid);
    }

    @Override
    public List<Map> getHeartRateByDay(String day, Long uid) {
        return healthSummaryMapper.queryHeartRateByDay(day,uid);
    }


    @Override
    public List<Map> getOxygenByDay(String day, Long uid) {
        return healthSummaryMapper.queryOxygenByDay(day,uid);
    }


    @Override
    public List<Map> getTemperatureByDay(String day, Long uid) {
        return healthSummaryMapper.queryTemperatureByDay(day,uid);
    }

    @Override
    public List<Map> getWeightByDay(String day, Long uid) {
        return healthSummaryMapper.queryWeightByDay(day,uid);
    }


    @Override
    public List<Map> getBloodSugarByDay(String day, Long uid) {
        return healthSummaryMapper.queryBloodSugarByDay(day,uid);
    }

    @Override
    public List<Map> getPulseRateByDay(String day, Long uid) {
        return healthSummaryMapper.queryPulseRateByDay(day,uid);
    }

    @Override
    public List<Map> getEcgByDay(String day, Long uid) {
        return healthSummaryMapper.queryEcgByDay(day,uid);
    }

    @Override
    public List<Map> getUricAcidByDay(String day, Long uid) {
        return healthSummaryMapper.queryUricAcidByDay(day,uid);
    }

    @Override
    public List<Map> getFallDownByDay(String day, Long uid) {
        return healthSummaryMapper.queryFallDownByDay(day,uid);
    }

    @Override
    public List<Map> getUsersByDmainUnitImei(String imei) {
        return healthSummaryMapper.queryUsersByDmainUnitImei(imei);
    }
}
