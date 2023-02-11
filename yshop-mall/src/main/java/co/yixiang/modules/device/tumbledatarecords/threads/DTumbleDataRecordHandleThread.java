package co.yixiang.modules.device.tumbledatarecords.threads;

import co.yixiang.common.enums.TumbleCmdEnum;
import co.yixiang.common.util.APPdataUtil;
import co.yixiang.common.util.BASE64;
import co.yixiang.modules.device.mqtt.ServerMQTT;
import co.yixiang.modules.device.tumble.service.mapper.DTumbleMapper;
import co.yixiang.modules.device.tumbledatarecords.domain.DTumbleDataRecords;
import co.yixiang.modules.device.tumbledatarecords.service.mapper.DTumbleDataRecordsMapper;
import co.yixiang.modules.servermanage.sosrecord.domain.SVipSosRecord;
import co.yixiang.modules.servermanage.sosrecord.service.SVipSosRecordService;
import co.yixiang.modules.user.domain.YxUser;
import co.yixiang.modules.user.service.mapper.UserMapper;
import co.yixiang.utils.DateUtils;
import co.yixiang.utils.SpringContextHolder;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author sunjian
 * @Date 2023/2/6
 * @license 版权所有，非经许可请勿使用本代码
 */
@Slf4j
public class DTumbleDataRecordHandleThread implements Runnable{

    private DTumbleDataRecordsMapper dTumbleDataRecordsMapper = (DTumbleDataRecordsMapper) SpringContextHolder.getBean(DTumbleDataRecordsMapper.class);
    private UserMapper yxUserMapper = (UserMapper) SpringContextHolder.getBean(UserMapper.class);
    private DTumbleMapper dTumbleMapper =  (DTumbleMapper) SpringContextHolder.getBean(DTumbleMapper.class);
    private SVipSosRecordService sVipSosRecordService = (SVipSosRecordService) SpringContextHolder.getBean(SVipSosRecordService.class);

    private JSONObject json;

    public DTumbleDataRecordHandleThread(JSONObject json){
        this.json = json;
    }

    @Override
    public void run() {


        log.info("收到跌倒报警器数据，开始处理=======");
        try {
            handleTumbleReportData(json);
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
        }
        log.info("跌倒报警器数据处理完成=======");
    }



    public void handleTumbleReportData(JSONObject json) throws Exception {
        String productId = json.getString("productId");
        String imei = json.getString("IMEI") == null ? json.getString("imei") :  json.getString("IMEI");
        String deviceId = json.getString("deviceId");
        String protocol = json.getString("protocol");
        String messageType = json.getString("messageType");
        JSONObject payload = json.getJSONObject("payload");
        String serviceId = json.getString("serviceId");
        String tenantId = json.getString("tenantId");
        Date timestamp = json.getDate("timestamp");


        if(payload == null){
            return;
        }
        String APPdata = new String(BASE64.decryptBASE64(payload.getString("APPdata")));
        if(!APPdata.startsWith(TumbleCmdEnum.CONTENT_HEAD)){
            return;
        }


        //更新productId、deviceId、serviceId、tenantId信息到 跌倒报警器设备维护表
        try {
            dTumbleMapper.updateParamByImei(productId,deviceId,serviceId,tenantId,imei);
        }catch (Exception e){
            log.error("更新跌倒报警器设备维护表失败！"+e.getMessage());
        }

        //TODO 1、根据IMEI验证终端设备是否有权限上传数据
        List<YxUser> users = yxUserMapper.queryUsersByTumbleImei(imei);
        if(users ==null ){
            //该设备没有绑定用户，无需记录上传的数据
            return;
        }
        String  mainUnitImei = yxUserMapper.findMainUnitImeiByTumbleImei(imei);
        //TODO 2、用users和redis中主机当前登陆人匹配，匹配上谁。本次上传的数据就算是谁的
        Long userId = 1L;



        //TODO 3、根据IMEI验证终端设备是否有权限上传数据


        String cmd = APPdataUtil.getCmd(APPdata);
        String data = APPdataUtil.getData(APPdata);

        //log.error("xxxx");

        TumbleCmdEnum wcmd = TumbleCmdEnum.getByValue(cmd);

        DTumbleDataRecords records = new DTumbleDataRecords();
        records.setCmd(cmd);
        records.setCmdName(wcmd.getDesc());
        records.setData(data);
        records.setAppData(APPdata);
        records.setContent(json.toJSONString());
        records.setCreateTime(new Date());
        records.setProductId(productId);
        records.setImei(imei);
        records.setDeviceId(deviceId);
        records.setProtocol(protocol);
        records.setMessageType(messageType);
        records.setPayload(payload.toJSONString());
        records.setServiceId(serviceId);
        records.setTenantId(tenantId);
        records.setPushTime(timestamp);
        records.setUserId(userId);

        switch (wcmd){
            case ALARM://报警
                saveAlarmInfo(records,mainUnitImei);
                break;
            case HEART_BEAT://心跳
                saveHbInfo(records,mainUnitImei);
                break;
            case REGISTER:
                saveRegisterInfo(records,mainUnitImei);
                break;
        }
    }


    private void saveRegisterInfo(DTumbleDataRecords records, String mainUnitImei){
        APPdataUtil.parseRegisterData(records,records.getAppData());

        //更新imsi、iccid信息到 跌倒报警器设备维护表
        try {
            dTumbleMapper.updateIccidByImei(records.getImsi(),records.getIccid(),records.getImei());
        }catch (Exception e){
            log.error("更新跌倒报警器设备维护表失败！"+e.getMessage());
        }

        dTumbleDataRecordsMapper.insert(records);
    }

    private void saveAlarmInfo(DTumbleDataRecords records, String mainUnitImei ){
        APPdataUtil.parseAlarmData(records,records.getAppData());


        if("1".equals(records.getIsPersonAlarm())){
            //将数据发送到mqtt
            JSONObject msg = new JSONObject();
            msg.put("userId",records.getUserId());
            msg.put("time",records.getPushTime());
            msg.put("content","有人");
            msg.put("action","ALARM");
            try {
                ServerMQTT.publishTerminalData(mainUnitImei,msg);
            }catch (Exception e){
                e.printStackTrace();
            }

            //TODO 保存一条报警信息,这个逻辑应该放在SOS报警的地方
            YxUser user  = yxUserMapper.selectById(records.getUserId());
            SVipSosRecord sVipSosRecord = new SVipSosRecord();
            Date now = new Date();
            sVipSosRecord.setMemberId(user.getUid());
            sVipSosRecord.setMemberName(user.getRealName());
            sVipSosRecord.setMemberPhone(user.getPhone());
            sVipSosRecord.setSosTime(now);
            sVipSosRecord.setServiceEndTime(user.getServiceEnd());
            sVipSosRecord.setSosContact(user.getSosContact());
            if(user.getServiceEnd() != null){
                if(now.before(user.getServiceEnd())){
                    int days = DateUtils.differentDaysByMillisecond(now,user.getServiceEnd());
                    sVipSosRecord.setLastDays(days > 0?days:0);
                }else{
                    sVipSosRecord.setLastDays(0);
                }
            }
            try {
                sVipSosRecordService.save(sVipSosRecord);
            }catch (Exception e){
                log.error("保存SOS报警记录失败！"+e.getMessage());
            }

        }





      if("1".equals(records.getFallDownAlarm())){
          //将数据发送到mqtt
          JSONObject msg = new JSONObject();
          msg.put("userId",records.getUserId());
          msg.put("time",records.getPushTime());
          msg.put("content","有人跌倒");
          msg.put("action","ALARM");
          try {
              ServerMQTT.publishTerminalData(mainUnitImei,msg);
          }catch (Exception e){
              e.printStackTrace();
          }
      }

        dTumbleDataRecordsMapper.insert(records);
    }

    private void saveHbInfo(DTumbleDataRecords records, String mainUnitImei ){
        APPdataUtil.parseHeartBeatData(records,records.getAppData());
        dTumbleDataRecordsMapper.insert(records);
    }

}
