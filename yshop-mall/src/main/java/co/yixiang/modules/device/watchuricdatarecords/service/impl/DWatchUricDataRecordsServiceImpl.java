/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.device.watchuricdatarecords.service.impl;

import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.utils.QueryHelpPlus;
import co.yixiang.domain.PageResult;
import co.yixiang.dozer.service.IGenerator;
import co.yixiang.modules.device.apiservice.DWatchUricApiService;
import co.yixiang.modules.device.uric.service.DUricService;
import co.yixiang.modules.device.watchuricdatarecords.domain.DWatchUricDataRecords;
import co.yixiang.modules.device.watchuricdatarecords.service.DWatchUricDataRecordsService;
import co.yixiang.modules.device.watchuricdatarecords.service.dto.DWatchUricDataRecordsDto;
import co.yixiang.modules.device.watchuricdatarecords.service.dto.DWatchUricDataRecordsQueryCriteria;
import co.yixiang.modules.device.watchuricdatarecords.service.mapper.DWatchUricDataRecordsMapper;
import co.yixiang.modules.user.service.mapper.UserMapper;
import co.yixiang.utils.FileUtil;
import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// 默认不使用缓存
//import org.springframework.cache.annotation.CacheConfig;
//import org.springframework.cache.annotation.CacheEvict;
//import org.springframework.cache.annotation.Cacheable;

/**
* @author jiansun
* @date 2023-01-29
*/
@Slf4j
@Service
@AllArgsConstructor
//@CacheConfig(cacheNames = "dWatchUricDataRecords")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class DWatchUricDataRecordsServiceImpl extends BaseServiceImpl<DWatchUricDataRecordsMapper, DWatchUricDataRecords> implements DWatchUricDataRecordsService {

    private final IGenerator generator;
    private final DWatchUricDataRecordsMapper dWatchUricDataRecordsMapper;
    private final  UserMapper yxUserMapper;
    private final DWatchUricApiService dWatchUricApiService;
    private final co.yixiang.modules.watch.service.DWatchService dWatchService;
    private final DUricService dUricService;


    @Override
    //@Cacheable
    public PageResult<DWatchUricDataRecordsDto> queryAll(DWatchUricDataRecordsQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<DWatchUricDataRecords> page = new PageInfo<>(queryAll(criteria));
        return generator.convertPageInfo(page,DWatchUricDataRecordsDto.class);
    }


    @Override
    //@Cacheable
    public List<DWatchUricDataRecords> queryAll(DWatchUricDataRecordsQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(DWatchUricDataRecords.class, criteria));
    }


    @Override
    public List<Map> getMapLocationRecordsByImei(String day,String imei) {
        return baseMapper.queryMapLocationRecordsByImei(day,imei);
    }

    @Override
    public List<Map> getMapLocationRecordsByUid(String day, Long uid) {
        return baseMapper.queryMapLocationRecordsByUid(day,uid);
    }

    @Override
    public void download(List<DWatchUricDataRecordsDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (DWatchUricDataRecordsDto dWatchUricDataRecords : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("指令", dWatchUricDataRecords.getCmd());
            map.put("指令名称", dWatchUricDataRecords.getCmdName());
            map.put("电量", dWatchUricDataRecords.getElectric());
            map.put("步数", dWatchUricDataRecords.getStep());
            map.put("卡路里", dWatchUricDataRecords.getCalories());
            map.put("舒张压", dWatchUricDataRecords.getDbp());
            map.put("收缩压", dWatchUricDataRecords.getSbp());
            map.put("心率", dWatchUricDataRecords.getHeartRate());
            map.put("睡眠总时长，单位：秒", dWatchUricDataRecords.getAllSleepTime());
            map.put("睡眠日期,日期格式是yyyy-MM-dd", dWatchUricDataRecords.getSleepDate());
            map.put("浅睡时长，单位：秒", dWatchUricDataRecords.getLowSleepTime());
            map.put("深睡时长，单位：秒", dWatchUricDataRecords.getDeepSleepTime());
            map.put("睡眠数据曲线", dWatchUricDataRecords.getSleepLine());
            map.put("睡眠质量[0-4]级", dWatchUricDataRecords.getSleepQuality());
            map.put("睡眠中起床次数", dWatchUricDataRecords.getWakeCount());
            map.put("入睡时间戳", dWatchUricDataRecords.getSleepDown());
            map.put("出睡时间戳", dWatchUricDataRecords.getSleepUp());
            map.put("血氧", dWatchUricDataRecords.getOxygen());
            map.put("体温", dWatchUricDataRecords.getTemperature());
            map.put("经纬度", dWatchUricDataRecords.getLocation());
            map.put("定位描述", dWatchUricDataRecords.getDesc());
            map.put("定位类型", dWatchUricDataRecords.getType());
            map.put("围栏通知描述用户id", dWatchUricDataRecords.getUid());
            map.put("围栏通知  1：进围栏 2：出围栏", dWatchUricDataRecords.getTag());
            map.put("围栏的唯一标识", dWatchUricDataRecords.getDuid());
            map.put("血糖仪数据记录时间", dWatchUricDataRecords.getRecordTime());
            map.put("糖化血红蛋白（mmol/l）", dWatchUricDataRecords.getGhb());
            map.put("尿酸值（mmol/l）", dWatchUricDataRecords.getUricAcid());
            map.put("血酮值（mmol/l）", dWatchUricDataRecords.getKetone());
            map.put("数据上传时间", dWatchUricDataRecords.getPushTime());
            map.put("数据记录时间", dWatchUricDataRecords.getCreateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

}
