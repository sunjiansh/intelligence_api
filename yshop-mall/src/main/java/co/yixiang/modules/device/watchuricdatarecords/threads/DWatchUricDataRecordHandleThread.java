package co.yixiang.modules.device.watchuricdatarecords.threads;

import co.yixiang.common.enums.WatchUricCmdEnum;
import co.yixiang.modules.device.apiservice.DWatchUricApiService;
import co.yixiang.modules.device.mqtt.ServerMQTT;
import co.yixiang.modules.device.watchuricdatarecords.domain.DWatchUricDataRecords;
import co.yixiang.modules.device.watchuricdatarecords.service.mapper.DWatchUricDataRecordsMapper;
import co.yixiang.modules.user.domain.YxUser;
import co.yixiang.modules.user.service.YxUserService;
import co.yixiang.modules.user.service.mapper.UserMapper;
import co.yixiang.utils.SpringContextHolder;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * @author 38294
 * @Date 2023/2/4
 * @license 版权所有，非经许可请勿使用本代码
 */
@Slf4j
public class DWatchUricDataRecordHandleThread implements Runnable {

	private DWatchUricDataRecordsMapper dWatchUricDataRecordsMapper = (DWatchUricDataRecordsMapper) SpringContextHolder.getBean(DWatchUricDataRecordsMapper.class);
	private UserMapper yxUserMapper = (UserMapper)SpringContextHolder.getBean(UserMapper.class);
	private YxUserService yxUserService =  (YxUserService)SpringContextHolder.getBean(YxUserService.class);
	private  DWatchUricApiService dWatchUricApiService = (DWatchUricApiService)SpringContextHolder.getBean(DWatchUricApiService.class);



	private JSONObject json;

	public DWatchUricDataRecordHandleThread(JSONObject json){
		this.json = json;
	}

	public void run() {
		log.info("收到手环或尿酸分析仪数据，开始处理=======");
		try {
			handleWatchAndUricReportData(json);
		}catch (Exception e){
			log.error(e.getMessage());
			e.printStackTrace();
		}
		log.info("手环或尿酸分析仪数据处理完成=======");
	}

	public void handleWatchAndUricReportData(JSONObject json) throws Exception {
		String imei = json.getString("imei");
		String deviceModel = json.getString("deviceModel");//1 是L18产品
		String cmd = json.getString("cmd");// CZW06 测体温 CZW07 定位
		Date pushTimes = new Date(json.getLongValue("pushTimestamp"));

		//TODO 1、根据IMEI验证终端设备是否有权限上传数据
		//YxUser user= yxUserMapper.queryUserByImei(imei);

		YxUser user = yxUserService.getOne(new LambdaQueryWrapper<YxUser>().eq(YxUser::getImei,imei).or().eq(YxUser::getUricSn,imei));
		//该设备没有绑定用户，或者账号处于锁定状态，无需记录上传的数据
		if(user ==null || user.getStatus()==0){
			return;
		}

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
			//TODO 4、绑定尿酸分析仪，要放在主设备维护那里做绑定
			try {
				JSONObject bind2Result  = dWatchUricApiService.bindUricDevice(user.getUid(),imei);
				if(bind2Result.getInteger("code") != 200){
					log.error(bind2Result.getString("msg"));
				}
			}catch (Exception e){
				log.error("调用智能手环平台绑定尿酸分析仪接口失败！"+e.getMessage());
			}
		}else{//智能手环
			mainUnitImei = yxUserMapper.fineMainUnitImeiByWatchImei(imei);
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


		//将数据发送到mqtt
		JSONObject msg = new JSONObject();
		msg.put("userId",entity.getUserId());
		msg.put("time",entity.getPushTime());
		msg.put("allSleepTime",entity.getAllSleepTime());
		msg.put("action","SLEEP");
		try {
			ServerMQTT.publishTerminalData(mainUnitImei,msg);
		}catch (Exception e){
			e.printStackTrace();
		}




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

		//将数据发送到mqtt
		JSONObject msg = new JSONObject();
		msg.put("userId",entity.getUserId());
		msg.put("uricAcid",entity.getUricAcid());
		msg.put("time",entity.getPushTime());
		msg.put("action","URICACID");
		try {
			ServerMQTT.publishTerminalData(mainUnitImei,msg);
		}catch (Exception e){
			e.printStackTrace();
		}

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
