/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.device.watchuricdatarecords.service.impl;

import co.yixiang.common.enums.WatchUricCmdEnum;
import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.utils.QueryHelpPlus;
import co.yixiang.domain.PageResult;
import co.yixiang.dozer.service.IGenerator;
import co.yixiang.modules.device.apiservice.DWatchUricApiService;
import co.yixiang.modules.device.mqtt.ServerMQTT;
import co.yixiang.modules.device.uric.domain.DUric;
import co.yixiang.modules.device.uric.service.DUricService;
import co.yixiang.modules.device.watchuricdatarecords.domain.DWatchUricDataRecords;
import co.yixiang.modules.device.watchuricdatarecords.service.DWatchUricDataRecordsService;
import co.yixiang.modules.device.watchuricdatarecords.service.dto.DWatchUricDataRecordsDto;
import co.yixiang.modules.device.watchuricdatarecords.service.dto.DWatchUricDataRecordsQueryCriteria;
import co.yixiang.modules.device.watchuricdatarecords.service.mapper.DWatchUricDataRecordsMapper;
import co.yixiang.modules.user.domain.YxUser;
import co.yixiang.modules.user.service.mapper.UserMapper;
import co.yixiang.modules.watch.domain.DWatch;
import co.yixiang.utils.FileUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

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

    @Override
    public void handleWatchAndUricReportData(JSONObject json) throws Exception {
        String imei = json.getString("imei");
        String deviceModel = json.getString("deviceModel");//1 是L18产品
        String cmd = json.getString("cmd");// CZW06 测体温 CZW07 定位
        Date pushTimes = new Date(json.getLongValue("pushTimestamp"));

        //TODO 1、根据IMEI验证终端设备是否有权限上传数据
        YxUser user= yxUserMapper.queryUserByImei(imei);
        if(user ==null){
            //该设备没有绑定用户，无需记录上传的数据
            return;
        }

        //TODO 2、同步人员账号信息和设备绑定信息到智能手环平台
//        try {
//            JSONObject result  = dWatchUricApiService.syncUserInfo(user);
//            if(result.getInteger("code") != 200){
//                log.error(result.getString("msg"));
//            }
//        }catch (Exception e){
//            log.error("调用智能手环平台同步人员信息接口失败！"+e.getMessage());
//        }



        //主机的IMEI
        String  mainUnitImei = "";


        WatchUricCmdEnum wcmd = WatchUricCmdEnum.getByValue(cmd);
        DWatchUricDataRecords entity = new DWatchUricDataRecords();
        entity.setUserId(user.getUid());
        entity.setImei(imei);
        entity.setDeviceModel(deviceModel);
        entity.setCmd(cmd);
        entity.setCmdName(wcmd.getDesc());
        entity.setCreateTime(new Date());
        entity.setPushTime(pushTimes);

        //尿酸分析仪
        if(WatchUricCmdEnum.CZW31.getValue().equals(cmd) || WatchUricCmdEnum.CZW32.equals(cmd) || WatchUricCmdEnum.CZW33.getValue().equals(cmd)){
            mainUnitImei = yxUserMapper.findMainUnitImeiByUrinSn(imei);

            DUric dUric = dUricService.getOne(new LambdaQueryWrapper<DUric>().eq(DUric::getImei,imei));
            if(dUric == null){
                log.error("该尿酸分析仪没有注册到本平台，不予上报数据！IMIE:"+imei);
                return;
            }
            if(dUric.getIsActive() == 0){
                log.error("该尿酸分析仪处于非激活状态，不予上报数据！IMIE:"+imei);
                return;
            }

            //TODO 4、绑定尿酸分析仪，要放在主设备维护那里做绑定
//            try {
//                JSONObject bind2Result  = dWatchUricApiService.bindUricDevice(user.getUid(),imei);
//                if(bind2Result.getInteger("code") != 200){
//                    log.error(bind2Result.getString("msg"));
//                }
//            }catch (Exception e){
//                log.error("调用智能手环平台绑定尿酸分析仪接口失败！"+e.getMessage());
//            }
        }else{//智能手环
            mainUnitImei = yxUserMapper.fineMainUnitImeiByWatchImei(imei);
            co.yixiang.modules.watch.domain.DWatch watch = dWatchService.getOne(new LambdaQueryWrapper<DWatch>().eq(DWatch::getImei,imei));

            //TODO 3、绑定手环信息(已经再用户管理那绑定好)
//            try {
//                JSONObject bindResult  = dWatchUricApiService.bindWatch(user.getUid(),imei);
//                if(bindResult.getInteger("code") != 200){
//                    log.error(bindResult.getString("msg"));
//                }
//            }catch (Exception e){
//                log.error("调用智能手环平台绑定手环接口失败！"+e.getMessage());
//            }
        }




        switch (wcmd){
            case CZW01://自动上传电量、步数信息
               saveElectricStepInfo(entity,json,mainUnitImei);
                        break;
            case CZW02://手动测量爱体检
                saveBpInfo(entity,json,mainUnitImei);
                        break;
            case CZW03://手动上传心率
                saveHrInfo(entity,json,mainUnitImei);
                break;
            case CZW04://自动上传睡眠信息
                saveSleepInfo(entity,json,mainUnitImei);
                break;
            case CZW05://手动测量血氧
                saveAutoBpInfo(entity,json,mainUnitImei);
                break;
            case CZW06://手动测量体温
                saveTemperatureInfo(entity,json,mainUnitImei);
                break;
            case CZW07://自动上传位置信息
                saveLocationInfo(entity,json,mainUnitImei);
                break;
            case CZW30://自动上传围栏通知
                saveWLInfo(entity,json,mainUnitImei);
                break;
            case CZW31://手动测量血红蛋白
                saveXTangInfo(entity,json,mainUnitImei);
                break;
            case CZW32://手动测量尿酸值
                saveUricAcidInfo(entity,json,mainUnitImei);
                break;
            case CZW33://手动测量尿酸值血酮值
                saveXTongInfo(entity,json,mainUnitImei);
                break;
        }
    }

    /**
     *  save 步数，电量
     * @param entity
     */
    private void saveElectricStepInfo(DWatchUricDataRecords entity,JSONObject json,String  mainUnitImei){
        //0 为自动测量数据
        entity.setMeasureMode(0);
        entity.setElectric(json.getString("electric"));
        entity.setStep(json.getString("step"));
        entity.setCalories(json.getLong("calories"));
        dWatchUricDataRecordsMapper.insert(entity);
    }
    /**
     *  save 心率，高压，低压
     * @param entity
     */
    private void saveBpInfo(DWatchUricDataRecords entity,JSONObject json,String  mainUnitImei){
        //1 为手动测量数据
        entity.setMeasureMode(1);
        entity.setDbp(json.getDouble("dbp"));
        entity.setSbp(json.getDouble("sbp"));
        entity.setHeartRate(json.getDouble("heartRate"));

//        //TODO 将血压数据上报到手环平台
//        try {
//            JSONObject uploadResult  = DWatchUricApiService.batchUploadBpInfo(entity);
//            if(uploadResult.getInteger("code") != 200){
//                log.error(uploadResult.getString("msg"));
//            }
//        }catch (Exception e){
//            log.error(entity.getImei()+"上报血压数据到平台失败，原因："+e.getMessage());
//        }
//
//        //TODO 将心率数据上报到手环平台
//        try {
//            JSONObject uploadResult  = DWatchUricApiService.batchUploadHeart(entity);
//            if(uploadResult.getInteger("code") != 200){
//                log.error(uploadResult.getString("msg"));
//            }
//        }catch (Exception e){
//            log.error(entity.getImei()+"上报心率数据到平台失败，原因："+e.getMessage());
//        }


        //将数据发送到mqtt
        JSONObject msg = new JSONObject();
        msg.put("userId",entity.getUserId());
        msg.put("time",entity.getPushTime());
        msg.put("dbp",entity.getDbp());
        msg.put("sbp",entity.getSbp());
        msg.put("heartRate",entity.getHeartRate());
        msg.put("action","PHYSICAL");
        try {
            ServerMQTT.publishTerminalData(mainUnitImei,msg);
        }catch (Exception e){
            e.printStackTrace();
        }
        dWatchUricDataRecordsMapper.insert(entity);
    }
    /**
     *  save 心率
     * @param entity
     */
    private void saveHrInfo(DWatchUricDataRecords entity,JSONObject json,String  mainUnitImei){
        //1 为手动测量数据
        entity.setMeasureMode(1);
        entity.setHeartRate(json.getDouble("heartRate"));
        dWatchUricDataRecordsMapper.insert(entity);
    }
    /**
     *  save 睡眠
     * @param entity
     */
    private void saveSleepInfo(DWatchUricDataRecords entity,JSONObject json,String  mainUnitImei){
        //0 为自动测量数据
        entity.setMeasureMode(0);
        entity.setAllSleepTime(json.getLong("allSleepTime"));
        entity.setSleepDate(json.getString("sleepDate"));
        entity.setLowSleepTime(json.getLong("lowSleepTime"));
        entity.setDeepSleepTime(json.getLong("deepSleepTime"));
        entity.setSleepLine(json.getString("sleepLine"));
        entity.setSleepQuality(json.getInteger("sleepQuality"));
        entity.setWakeCount(json.getInteger("wakeCount"));
        entity.setSleepDown(json.getLong("sleepDown"));
        entity.setSleepUp(json.getLong("sleepUp"));
        dWatchUricDataRecordsMapper.insert(entity);

    }
    /**
     *  save 体温，高压，低压，心率，血氧
     * @param entity
     */
    private void saveAutoBpInfo(DWatchUricDataRecords entity,JSONObject json,String  mainUnitImei){
        //1 为手动测量数据
        entity.setMeasureMode(1);
        entity.setDbp(json.getDouble("dbp"));
        entity.setSbp(json.getDouble("sbp"));
        entity.setHeartRate(json.getDouble("heartRate"));
        entity.setOxygen(json.getDouble("oxygen"));
        entity.setTemperature(json.getString("temperature"));


//        //TODO 将血压数据上报到手环平台
//        try {
//            JSONObject uploadResult  = DWatchUricApiService.batchUploadBpInfo(entity);
//            if(uploadResult.getInteger("code") != 200){
//                log.error(uploadResult.getString("msg"));
//            }
//        }catch (Exception e){
//            log.error(entity.getImei()+"上报血压数据到平台失败，原因："+e.getMessage());
//        }
//
//        //TODO 将心率数据上报到手环平台
//        try {
//            JSONObject uploadResult  = DWatchUricApiService.batchUploadHeart(entity);
//            if(uploadResult.getInteger("code") != 200){
//                log.error(uploadResult.getString("msg"));
//            }
//        }catch (Exception e){
//            log.error(entity.getImei()+"上报心率数据到平台失败，原因："+e.getMessage());
//        }
//
//
//        //TODO 将血氧数据上报到手环平台
//        try {
//            JSONObject uploadResult  = DWatchUricApiService.batchUploadSpo2(entity);
//            if(uploadResult.getInteger("code") != 200){
//                log.error(uploadResult.getString("msg"));
//            }
//        }catch (Exception e){
//            log.error(entity.getImei()+"上报血氧数据到平台失败，原因："+e.getMessage());
//        }
//
//        //TODO 将体温数据上报到手环平台
//        try {
//            JSONObject uploadResult  = DWatchUricApiService.batchUploadTemperature(entity);
//            if(uploadResult.getInteger("code") != 200){
//                log.error(uploadResult.getString("msg"));
//            }
//        }catch (Exception e){
//            log.error(entity.getImei()+"上报体温数据到平台失败，原因："+e.getMessage());
//        }


        //将数据发送到mqtt
        JSONObject msg = new JSONObject();
        msg.put("userId",entity.getUserId());
        msg.put("time",entity.getPushTime());
        msg.put("dbp",entity.getDbp());
        msg.put("sbp",entity.getSbp());
        msg.put("heartRate",entity.getHeartRate());
        msg.put("oxygen",entity.getOxygen());
        msg.put("temperature",entity.getTemperature());
        msg.put("action","OXYGEN");
        try {
            ServerMQTT.publishTerminalData(mainUnitImei,msg);
        }catch (Exception e){
            e.printStackTrace();
        }

        dWatchUricDataRecordsMapper.insert(entity);
    }
    /**
     *  save 体温
     * @param entity
     */
    private void saveTemperatureInfo(DWatchUricDataRecords entity,JSONObject json,String  mainUnitImei){
        //1 为手动测量数据
        entity.setMeasureMode(1);
        entity.setTemperature(json.getString("temperature"));

//        //TODO 将体温数据上报到手环平台
//        try {
//            JSONObject uploadResult  = DWatchUricApiService.batchUploadTemperature(entity);
//            if(uploadResult.getInteger("code") != 200){
//                log.error(uploadResult.getString("msg"));
//            }
//        }catch (Exception e){
//            log.error(entity.getImei()+"上报体温数据到平台失败，原因："+e.getMessage());
//        }

        //将数据发送到mqtt
        JSONObject msg = new JSONObject();
        msg.put("userId",entity.getUserId());
        msg.put("temperature",entity.getTemperature());
        msg.put("time",entity.getPushTime());
        msg.put("action","TEMPERATURE");
        try {
            ServerMQTT.publishTerminalData(mainUnitImei,msg);
        }catch (Exception e){
            e.printStackTrace();
        }

        dWatchUricDataRecordsMapper.insert(entity);
    }
    /**
     *  save 经纬度
     * @param entity
     */
    private void saveLocationInfo(DWatchUricDataRecords entity,JSONObject json,String  mainUnitImei){
            //0 为自动测量数据
            entity.setMeasureMode(0);
            entity.setLocation(json.getString("location"));
            entity.setLocationDesc(json.getString("desc"));
            entity.setType(json.getString("type"));
            dWatchUricDataRecordsMapper.insert(entity);
    }
    /**
     *  save 围栏通知
     * @param entity
     */
    private void saveWLInfo(DWatchUricDataRecords entity,JSONObject json,String  mainUnitImei){
        //0 为自动测量数据
        entity.setMeasureMode(0);
        entity.setUid(json.getString("uid"));
        entity.setTag(json.getString("tag"));
        entity.setDuid(json.getString("duid"));
        dWatchUricDataRecordsMapper.insert(entity);
    }
    /**
     *  save 血糖仪血糖数据
     * @param entity
     */
    private void saveXTangInfo(DWatchUricDataRecords entity,JSONObject json,String  mainUnitImei){
        //1 为手动测量数据
        entity.setMeasureMode(1);
        entity.setGhb(json.getDouble("ghb"));
        entity.setRecordTime(json.getLong("recordTime"));


//        //TODO 将血糖数据上报到手环平台
//        try {
//            JSONObject uploadResult  = DWatchUricApiService.batchUploadBs(entity);
//            if(uploadResult.getInteger("code") != 200){
//                log.error(uploadResult.getString("msg"));
//            }
//        }catch (Exception e){
//            log.error(entity.getImei()+"上报血糖数据到平台失败，原因："+e.getMessage());
//        }

        dWatchUricDataRecordsMapper.insert(entity);
    }
    /**
     *  save 血糖仪尿酸数据
     * @param entity
     */
    private void saveUricAcidInfo(DWatchUricDataRecords entity,JSONObject json,String  mainUnitImei){
        //1 为手动测量数据
        entity.setMeasureMode(1);
        entity.setUricAcid(json.getDouble("uricAcid"));
        entity.setRecordTime(json.getLong("recordTime"));
        dWatchUricDataRecordsMapper.insert(entity);
    }
    /**
     *  save 血糖仪血酮数据
     * @param entity
     */
    private void saveXTongInfo(DWatchUricDataRecords entity,JSONObject json,String  mainUnitImei){
        //1 为手动测量数据
        entity.setMeasureMode(1);
        entity.setKetone(json.getDouble("ketone"));
        entity.setRecordTime(json.getLong("recordTime"));
        dWatchUricDataRecordsMapper.insert(entity);
    }




}
