package co.yixiang.modules.health.service.impl;

import co.yixiang.modules.articlemanage.article.domain.SArticle;
import co.yixiang.modules.device.watchuricdatarecords.domain.DWatchUricDataRecords;
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
    public DWatchUricDataRecords getSleepRecordByDay(String day, Long uid) {
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
}
