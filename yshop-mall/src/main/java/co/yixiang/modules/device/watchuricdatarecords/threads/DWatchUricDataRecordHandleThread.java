package co.yixiang.modules.device.watchuricdatarecords.threads;

import co.yixiang.common.enums.WatchUricCmdEnum;
import co.yixiang.common.util.GeographyCalc;
import co.yixiang.modules.device.apiservice.DWatchUricApiService;
import co.yixiang.modules.device.mqtt.ServerMQTT;
import co.yixiang.modules.device.uric.domain.DUric;
import co.yixiang.modules.device.uric.service.DUricService;
import co.yixiang.modules.device.watchuricdatarecords.domain.DWatchUricDataRecords;
import co.yixiang.modules.device.watchuricdatarecords.service.mapper.DWatchUricDataRecordsMapper;
import co.yixiang.modules.monotormanage.alarmrecord.domain.SAlarmReccord;
import co.yixiang.modules.monotormanage.alarmrecord.service.SAlarmReccordService;
import co.yixiang.modules.monotormanage.geography.domain.SGeography;
import co.yixiang.modules.monotormanage.geography.service.SGeographyService;
import co.yixiang.modules.user.domain.YxUser;
import co.yixiang.modules.user.service.YxUserService;
import co.yixiang.modules.user.service.mapper.UserMapper;
import co.yixiang.modules.watch.domain.DWatch;
import co.yixiang.utils.SpringContextHolder;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
	private SAlarmReccordService sAlarmReccordService = (SAlarmReccordService)SpringContextHolder.getBean(SAlarmReccordService.class);
	private SGeographyService sGeographyService = (SGeographyService)SpringContextHolder.getBean(SGeographyService.class);
	private final co.yixiang.modules.watch.service.DWatchService dWatchService = SpringContextHolder.getBean(co.yixiang.modules.watch.service.DWatchService.class);
	private final DUricService dUricService = SpringContextHolder.getBean(DUricService.class);





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

		//该设备没有绑定用户，无需记录上传的数据
		if(user ==null){
			log.error("该设备没有绑定用户，不予上报数据！IMIE:"+imei);
			return;
		}

		//该用户账号处于锁定状态，无需记录上传的数据
		if(user.getStatus()==0){
			//该设备没有绑定用户，无需记录上传的数据
			log.error("该用户账号处于锁定状态，不予上报数据！phone:"+user.getPhone());
			return;
		}

		//TODO check 用户是否在会员有效期内
		Date now = new Date();
		if(!(now.before(user.getServiceEnd()) && now.after(user.getServiceStart()))){
			log.error("该用户在不在会员有效期，不予上报数据！phone:"+user.getPhone());
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
//			try {
//				JSONObject bind2Result  = dWatchUricApiService.bindUricDevice(user.getUid(),imei);
//				if(bind2Result.getInteger("code") != 200){
//					log.error(bind2Result.getString("msg"));
//				}
//			}catch (Exception e){
//				log.error("调用智能手环平台绑定尿酸分析仪接口失败！"+e.getMessage());
//			}
		}else{//智能手环
			mainUnitImei = yxUserMapper.fineMainUnitImeiByWatchImei(imei);

			co.yixiang.modules.watch.domain.DWatch watch = dWatchService.getOne(new LambdaQueryWrapper<co.yixiang.modules.watch.domain.DWatch>().eq(co.yixiang.modules.watch.domain.DWatch::getImei,imei));
			if(watch == null){
				log.error("该手环没有注册到本平台，不予上报数据！IMIE:"+imei);
				return;
			}
			if(watch.getIsActive() == 0){
				log.error("该手环处于非激活状态，不予上报数据！IMIE:"+imei);
				return;
			}

		}

		switch (wcmd){
			case CZW01://自动上传电量、步数信息
				saveElectricStepInfo(entity,json,mainUnitImei);
				break;
			case CZW02://手动测量爱体检
				saveBpInfo(entity,json,mainUnitImei);
				handleBoldAlarmInfo(entity);
				handleHrAlarmInfo(entity);
				break;
			case CZW03://手动上传心率
				saveHrInfo(entity,json,mainUnitImei);
				handleHrAlarmInfo(entity);
				break;
			case CZW04://自动上传睡眠信息
				saveSleepInfo(entity,json,mainUnitImei);
				break;
			case CZW05://手动测量血氧
				saveAutoBpInfo(entity,json,mainUnitImei);
				break;
			case CZW06://手动测量体温
				saveTemperatureInfo(entity,json,mainUnitImei);
				handleTemperatureAlarmInfo(entity);
				break;
			case CZW07://自动上传位置信息
				saveLocationInfo(entity,json,mainUnitImei);
				handleGeographyAlarmInfo(entity);
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
		String[] lo = json.getString("location").split(",");
		entity.setLat(Double.parseDouble(lo[1]));
		entity.setLon(Double.parseDouble(lo[0]));
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

	/**
	 * 处理电子围栏报警信息
	 * @param entity
	 */
	private void handleGeographyAlarmInfo(DWatchUricDataRecords entity){
			String location =	entity.getLocation();
			String[] latLng = location.split(",");
			Double lon = Double.parseDouble(latLng[0]);
		    Double lat = Double.parseDouble(latLng[1]);
		    YxUser user = yxUserMapper.selectById(entity.getUserId());
			List<SGeography> nets = sGeographyService.list(new LambdaQueryWrapper<SGeography>().eq(SGeography::getMemberId,entity.getUserId()));
			if(nets != null && nets.size() > 0){
				Set<Boolean> results = new HashSet<>();
				StringBuilder content = new StringBuilder();
				content.append("用户脱离围栏：");
				nets.stream().forEach(sGeography -> content.append(sGeography.getName()).append(","));
				for(SGeography sGeography :nets){
					boolean isIncIrcle = GeographyCalc.isInCircle(lat,lon,sGeography.getLat(),sGeography.getLon(),sGeography.getRegionRange());
					results.add(isIncIrcle);
				}
				//遍历每个围栏，如果都不在这几个围栏中则报警
				if(!results.contains(true)){
					SAlarmReccord alarmReccord = new SAlarmReccord();
					alarmReccord.setMemberId(user.getUid());
					alarmReccord.setMenberName(user.getRealName());
					alarmReccord.setPhone(user.getPhone());
					alarmReccord.setAlarmType(SAlarmReccord.ALARM_TYPE_GEO);//报警类型1 围栏报警 2 风险分级
					alarmReccord.setImei(entity.getImei());
					alarmReccord.setContent(content.toString());
					alarmReccord.setCreateTime(new Date());
					sAlarmReccordService.save(alarmReccord);
				}
			}
	}

	/**
	 * 处理体温报警信息
	 * @param entity
	 */
	private void handleTemperatureAlarmInfo(DWatchUricDataRecords entity){
		co.yixiang.modules.watch.domain.DWatch watch = dWatchService.getOne(new LambdaQueryWrapper<DWatch>().eq(DWatch::getImei,entity.getImei()));
		if(Double.parseDouble(entity.getTemperature()) > watch.getTemperatureHeight()/10 || Double.parseDouble(entity.getTemperature()) < watch.getTemperatureLow()/10){
			YxUser user = yxUserMapper.selectById(entity.getUserId());
			SAlarmReccord alarmReccord = new SAlarmReccord();
			String content = String.format("体温异常，最高预警值%s，最低预警值%s，当前测量值%s",watch.getTemperatureHeight()/10,watch.getTemperatureLow()/10,entity.getTemperature() );
			alarmReccord.setMemberId(user.getUid());
			alarmReccord.setMenberName(user.getRealName());
			alarmReccord.setPhone(user.getPhone());
			alarmReccord.setAlarmType(SAlarmReccord.ALARM_TYPE_TEMP);
			alarmReccord.setImei(entity.getImei());
			alarmReccord.setContent(content);
			alarmReccord.setCreateTime(new Date());
			sAlarmReccordService.save(alarmReccord);
		}
	}

	/**
	 * 处理心率报警信息
	 * @param entity
	 */
	private void handleHrAlarmInfo(DWatchUricDataRecords entity){
		co.yixiang.modules.watch.domain.DWatch watch = dWatchService.getOne(new LambdaQueryWrapper<DWatch>().eq(DWatch::getImei,entity.getImei()));
		if(entity.getHeartRate() > watch.getHrHeight() || entity.getHeartRate() < watch.getHrLow()){
			YxUser user = yxUserMapper.selectById(entity.getUserId());
			SAlarmReccord alarmReccord = new SAlarmReccord();
			String content = String.format("心率异常，最高预警值%s，最低预警值%s，当前测量值%s",watch.getHrHeight(),watch.getHrLow(),entity.getHeartRate() );
			alarmReccord.setMemberId(user.getUid());
			alarmReccord.setMenberName(user.getRealName());
			alarmReccord.setPhone(user.getPhone());
			alarmReccord.setAlarmType(SAlarmReccord.ALARM_TYPE_HR);
			alarmReccord.setImei(entity.getImei());
			alarmReccord.setContent(content);
			alarmReccord.setCreateTime(new Date());
			sAlarmReccordService.save(alarmReccord);
		}
	}


	/**
	 * 处理血压报警信息
	 * @param entity
	 */
	private void handleBoldAlarmInfo(DWatchUricDataRecords entity){
		co.yixiang.modules.watch.domain.DWatch watch = dWatchService.getOne(new LambdaQueryWrapper<DWatch>().eq(DWatch::getImei,entity.getImei()));
		if(entity.getSbp() > watch.getCalibrateSbp() || entity.getDbp() < watch.getCalibrateDbp()){
			YxUser user = yxUserMapper.selectById(entity.getUserId());
			SAlarmReccord alarmReccord = new SAlarmReccord();
			String content = String.format("血压异常，舒张压预警值%s，收缩压预警值%s，当前测量值%s",watch.getCalibrateDbp(),watch.getCalibrateSbp(),entity.getDbp()+"-"+entity.getSbp() );
			alarmReccord.setMemberId(user.getUid());
			alarmReccord.setMenberName(user.getRealName());
			alarmReccord.setPhone(user.getPhone());
			alarmReccord.setAlarmType(SAlarmReccord.ALARM_TYPE_BOLD);
			alarmReccord.setImei(entity.getImei());
			alarmReccord.setContent(content);
			alarmReccord.setCreateTime(new Date());
			sAlarmReccordService.save(alarmReccord);
		}
	}



}
