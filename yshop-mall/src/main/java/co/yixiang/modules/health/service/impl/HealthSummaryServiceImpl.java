package co.yixiang.modules.health.service.impl;

import co.yixiang.modules.articlemanage.article.domain.SArticle;
import co.yixiang.modules.device.apiservice.DWatchUricApiService;
import co.yixiang.modules.device.balancedatarecords.service.DBalanceDataRecordsService;
import co.yixiang.modules.device.bloodsugardatarecords.service.DBloodSugarDataRecordsService;
import co.yixiang.modules.device.ecgdatarecords.service.DEcgDataRecordsService;
import co.yixiang.modules.device.mqtt.ServerMQTT;
import co.yixiang.modules.device.tumbledatarecords.service.DTumbleDataRecordsService;
import co.yixiang.modules.device.watchuricdatarecords.service.DWatchUricDataRecordsService;
import co.yixiang.modules.device.watchuricdatarecords.service.dto.DWatchUricDataRecordsDto;
import co.yixiang.modules.health.service.HealthSummaryService;
import co.yixiang.modules.health.service.mapper.HealthSummaryMapper;
import co.yixiang.utils.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author sunjian
 * @Date 2023/2/26
 * @license 版权所有，非经许可请勿使用本代码
 */
@Slf4j
@Service
@AllArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class HealthSummaryServiceImpl  implements HealthSummaryService {

    private final HealthSummaryMapper healthSummaryMapper;
    private final DWatchUricApiService dWatchUricApiService;
    private final DWatchUricDataRecordsService dWatchUricDataRecordsService;
    private final DTumbleDataRecordsService dTumbleDataRecordsService;
    private final DBalanceDataRecordsService dBalanceDataRecordsService;
    private final DEcgDataRecordsService dEcgDataRecordsService;
    private final DBloodSugarDataRecordsService dBloodSugarDataRecordsService;


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
    public List<Map> getEcgDataRecords(Long uid) {
        return healthSummaryMapper.queryEcgDataRecords(uid);
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
    public List<Map<String,Object>> getUsersByDmainUnitImei(String imei) {
        List<Map<String,Object>> users =  healthSummaryMapper.queryUsersByDmainUnitImei(imei);
        if(users !=null && users.size()>0){
            for(int i = 0;i < users.size();i++){
                Map<String,Object> user = users.get(i);
                BigInteger uid = (BigInteger) user.get("uid");
                try {
                    JSONObject  result = dWatchUricApiService.getRiskStateById( uid.longValue());
                    if(result.getInteger("code") == 200){
                        user.put("healthInfo",result.getJSONObject("data"));
                    }
                }catch (Exception e){
                    log.error(e.getMessage());
                }
            }
        }
        return users;
    }


    @Override
    public Map<String,Map<String,Object>> getAndPushAllHealthRecordData(String imei,Long uid) {
        Map<String,Map<String,Object>>  result = Maps.newHashMap();

        Map<String,Object> sleepRecord = healthSummaryMapper.queryLatestSleepRecord(uid);
        if(sleepRecord != null)
        result.put("SLEEPRECORD",sleepRecord);

        Map<String,Object> bloodPreasure = healthSummaryMapper.queryLatestBloodPreasure(uid);
        if(bloodPreasure != null)
        result.put("BLOODPREASURE",bloodPreasure);

        Map<String,Object> heartRate = healthSummaryMapper.queryLatestHeartRate(uid);
        if(heartRate != null)
        result.put("HEARTRATE",heartRate);

        Map<String,Object> oxygen = healthSummaryMapper.queryLatestOxygen(uid);
        if(oxygen != null)
        result.put("OXYGEN",oxygen);

        Map<String,Object> temperature = healthSummaryMapper.queryLatestTemperature(uid);
        if(temperature != null)
        result.put("TEMPERATURE",temperature);

        Map<String,Object> weight = healthSummaryMapper.queryLatestWeight(uid);
        if(weight != null)
        result.put("WEIGHT",weight);

        Map<String,Object> bloodSugar = healthSummaryMapper.queryLatestBloodSugar(uid);
        if(bloodSugar != null)
        result.put("BLOODSUGAR",bloodSugar);

        Map<String,Object> pulseRate = healthSummaryMapper.queryLatestPulseRate(uid);
        if(pulseRate != null)
        result.put("PULSERATE",pulseRate);

        Map<String,Object> uricAcid = healthSummaryMapper.queryLatestUricAcid(uid);
        if(uricAcid != null)
        result.put("URICACID",uricAcid);

        Map<String,Object> fallDown = healthSummaryMapper.queryLatestFallDown(uid);
        if(fallDown != null)
        result.put("FALLDOWN",fallDown);

        Map<String,Object> ecg = healthSummaryMapper.queryLatestEcgDataRecords(uid);
        if(ecg != null)
        result.put("ECG",ecg);

        if(StringUtils.isNotEmpty(imei)){
            for (Map.Entry<String, Map<String,Object>> entry  : result.entrySet()) {
                //System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
                JSONObject msg = new JSONObject(entry.getValue());
                msg.put("action",entry.getKey());
                System.out.println(msg);
                try {
                    ServerMQTT.publishTerminalData(imei,msg);
                }catch (Exception e){
                    log.error("发送MQTT消息失败！"+e.getMessage());
                    //e.printStackTrace();
                }
            }
        }
        return result;
    }
}
