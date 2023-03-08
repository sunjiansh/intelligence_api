package co.yixiang.modules.terminal.rest;

import co.yixiang.api.ApiResult;
import co.yixiang.api.YshopException;
import co.yixiang.common.util.RedisContans;
import co.yixiang.modules.device.balance.service.DBalanceService;
import co.yixiang.modules.device.balance.service.dto.DBalanceDto;
import co.yixiang.modules.device.balancedatarecords.domain.DBalanceDataRecords;
import co.yixiang.modules.device.balancedatarecords.service.DBalanceDataRecordsService;
import co.yixiang.modules.device.bloodsugardatarecords.domain.DBloodSugarDataRecords;
import co.yixiang.modules.device.bloodsugardatarecords.service.DBloodSugarDataRecordsService;
import co.yixiang.modules.device.ecg.service.DEcgService;
import co.yixiang.modules.device.ecg.service.dto.DEcgDto;
import co.yixiang.modules.device.ecgdatarecords.domain.DEcgDataRecords;
import co.yixiang.modules.device.ecgdatarecords.service.DEcgDataRecordsService;
import co.yixiang.modules.device.mainunit.domain.DMailunit;
import co.yixiang.modules.device.mainunit.service.DMailunitService;
import co.yixiang.modules.device.watchuricdatarecords.service.dto.DWatchUricDataRecordsDto;
import co.yixiang.modules.health.service.HealthSummaryService;
import co.yixiang.modules.logging.aop.log.AppLog;
import co.yixiang.utils.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author sunjian
 * @Date 2023/3/.
 * @license 版权所有，非经许可请勿使用本代码
 */
@Slf4j
@RestController()
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api(value = "终端主机接口", tags = "终端主机接口")
@RequestMapping("/terminal")
public class TerminalController {


    private final HealthSummaryService healthSummaryService;
    private final DMailunitService dMailunitService;
    private final DBalanceService dBalanceService;
    private final co.yixiang.modules.blood.service.DBloodService dBloodService;
    private final DEcgService dEcgService;
    private final DBalanceDataRecordsService dBalanceDataRecordsService;
    private final DBloodSugarDataRecordsService dBloodSugarDataRecordsService;
    private final DEcgDataRecordsService dEcgDataRecordsService;





    @GetMapping(value = "/getSleepDataByDay")
    @AppLog("按天查询睡眠数据")
    @ApiOperation("按天查询睡眠数据")
    public ApiResult<DWatchUricDataRecordsDto> getSleepDataByDay(@RequestParam("day") Date day, @RequestParam("uid") Long uid){
        //Long uid = LocalUser.getUser().getUid();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dayStr = simpleDateFormat.format(day);
        DWatchUricDataRecordsDto record = healthSummaryService.getSleepRecordByDay(dayStr,uid);
        return  ApiResult.ok(record);
    }


    @GetMapping(value = "/getBloodPreasureByDay")
    @AppLog("按天查询血压数据")
    @ApiOperation("按天查询血压数据")
    public ApiResult<List<Map>> getBloodPreasureByDay(@RequestParam("day") Date day, @RequestParam("uid") Long uid){
        //Long uid = LocalUser.getUser().getUid();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dayStr = simpleDateFormat.format(day);
        List<Map> list = healthSummaryService.getBloodPreasureByDay(dayStr,uid);
        return  ApiResult.ok(list);
    }


    @GetMapping(value = "/getHeartRateByDay")
    @AppLog("按天查询心率数据")
    @ApiOperation("按天查询心率数据")
    public ApiResult<List<Map>> getHeartRateByDay(@RequestParam("day") Date day, @RequestParam("uid") Long uid){
        //Long uid = LocalUser.getUser().getUid();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dayStr = simpleDateFormat.format(day);
        List<Map> list = healthSummaryService.getHeartRateByDay(dayStr,uid);
        return  ApiResult.ok(list);
    }


    @GetMapping(value = "/getOxygenByDay")
    @AppLog("按天查询血氧数据")
    @ApiOperation("按天查询血氧数据")
    public ApiResult<List<Map>> getOxygenByDay(@RequestParam("day") Date day, @RequestParam("uid") Long uid){
        //Long uid = LocalUser.getUser().getUid();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dayStr = simpleDateFormat.format(day);
        List<Map> list = healthSummaryService.getOxygenByDay(dayStr,uid);
        return  ApiResult.ok(list);
    }


    @GetMapping(value = "/getTemperatureByDay")
    @AppLog("按天查询体温数据")
    @ApiOperation("按天查询体温数据")
    public ApiResult<List<Map>> getTemperatureByDay(@RequestParam("day") Date day, @RequestParam("uid") Long uid){
        //Long uid = LocalUser.getUser().getUid();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dayStr = simpleDateFormat.format(day);
        List<Map> list = healthSummaryService.getTemperatureByDay(dayStr,uid);
        return  ApiResult.ok(list);
    }


    @GetMapping(value = "/getWeightByDay")
    @AppLog("按天查询体重数据")
    @ApiOperation("按天查询体重数据")
    public ApiResult<List<Map>> getWeightByDay(@RequestParam("day") Date day, @RequestParam("uid") Long uid){
        //Long uid = LocalUser.getUser().getUid();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dayStr = simpleDateFormat.format(day);
        List<Map> list = healthSummaryService.getWeightByDay(dayStr,uid);
        return  ApiResult.ok(list);
    }


    @GetMapping(value = "/getBloodSugarByDay")
    @AppLog("按天查询血糖数据")
    @ApiOperation("按天查询血糖数据")
    public ApiResult<List<Map>> getBloodSugarByDay(@RequestParam("day") Date day, @RequestParam("uid") Long uid){
        //Long uid = LocalUser.getUser().getUid();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dayStr = simpleDateFormat.format(day);
        List<Map> list = healthSummaryService.getBloodSugarByDay(dayStr,uid);
        return  ApiResult.ok(list);
    }

    @GetMapping(value = "/getPulseRateByDay")
    @AppLog("按天查询脉搏数据")
    @ApiOperation("按天查询脉搏数据")
    public ApiResult<List<Map>> getPulseRateByDay(@RequestParam("day") Date day, @RequestParam("uid") Long uid){
        //Long uid = LocalUser.getUser().getUid();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dayStr = simpleDateFormat.format(day);
        List<Map> list = healthSummaryService.getPulseRateByDay(dayStr,uid);
        return  ApiResult.ok(list);
    }



    @GetMapping(value = "/getUricAcidByDay")
    @AppLog("按天查尿酸数据")
    @ApiOperation("按天查尿酸数据")
    public ApiResult<List<Map>> getUricAcidByDay(@RequestParam("day") Date day, @RequestParam("uid") Long uid){
        //Long uid = LocalUser.getUser().getUid();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dayStr = simpleDateFormat.format(day);
        List<Map> list = healthSummaryService.getUricAcidByDay(dayStr,uid);
        return  ApiResult.ok(list);
    }

    @GetMapping(value = "/getFallDownByDay")
    @AppLog("按天查跌倒数据")
    @ApiOperation("按天查跌倒数据")
    public ApiResult<List<Map>> getFallDownByDay(@RequestParam("day") Date day, @RequestParam("uid") Long uid){
        //Long uid = LocalUser.getUser().getUid();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dayStr = simpleDateFormat.format(day);
        List<Map> list = healthSummaryService.getFallDownByDay(dayStr,uid);
        return  ApiResult.ok(list);
    }


    @GetMapping(value = "/getEcgDataRecords")
    @AppLog("查询心电图测量记录数据")
    @ApiOperation("查询心电图测量记录数据")
    public ApiResult<List<Map>> getEcgDataRecords(@RequestParam("uid") Long uid){
        //Long uid = LocalUser.getUser().getUid();
        List<Map> list = healthSummaryService.getEcgDataRecords(uid);
        return  ApiResult.ok(list);
    }




    @GetMapping(value = "/test")
    public ApiResult<Long> test(){

        Long x = System.currentTimeMillis();
//        if(0==0){
//
//            throw new YshopException("参数非法");
//        }
        return  ApiResult.ok(x);
    }


    @GetMapping(value = "/getUsers")
    @AppLog("查询该主机绑定的可登录用户")
    @ApiOperation("查询该主机绑定的可登录用户")
    public ApiResult<List<Map<String,Object>>> getUsers(@RequestParam("imei") String imei){
        if(StringUtils.isEmpty(imei)){
            throw new YshopException("imei不能为空");
        }
        List<Map<String,Object>> users = healthSummaryService.getUsersByDmainUnitImei(imei);
        return  ApiResult.ok(users);
    }

    @PostMapping(value = "/changeCurrentUser")
    @AppLog("切换主机当前端登录人")
    @ApiOperation("切换主机当前端登录人")
    public ApiResult<Boolean> changeCurrentUser(@RequestParam("imei") String imei,@RequestParam("uid") Long uid){
        if(StringUtils.isEmpty(imei)){
            throw new YshopException("imei不能为空");
        }
        if(null == uid){
            throw new YshopException("uid不能为空");
        }
        RedisContans.setTerminalCurrentUserId(imei,uid);

        //System.out.println(RedisContans.getTerminalCurrentUserId(imei));

        //通过MQTT 给这台主机推送最新的健康测量数据
        healthSummaryService.getAndPushAllHealthRecordData(imei,uid);

        return  ApiResult.ok();
    }



    @GetMapping(value = "/getDevices")
    @AppLog("查询这台主机可连接的其他设备")
    @ApiOperation("查询这台主机可连接的其他设备")
    public ApiResult<JSONObject> getDevices(@RequestParam("imei") String imei){
        if(StringUtils.isEmpty(imei)){
            throw new YshopException("url地址中imei不能为空");
        }
        DMailunit m = dMailunitService.getOne(new LambdaQueryWrapper<DMailunit>().eq(DMailunit::getImei,imei));
        if(m == null){
            throw new YshopException("该主机设备不存在,请联系平台管理员");
        }

        List<co.yixiang.modules.blood.service.dto.DBloodDto> bloodDeviceList  =  dBloodService.queryAllBindedDeviceByMid(m.getId());
        List<DBalanceDto> blalanceDeviceList = dBalanceService.queryAllBindedDeviceByMid(m.getId());
        List<DEcgDto> ecgDeviceList = dEcgService.queryAllBindedDeviceByMid(m.getId());
        JSONObject result = new JSONObject();

        result.put("bloodDeviceList",bloodDeviceList);
        result.put("blalanceDeviceList",bloodDeviceList);
        result.put("ecgDeviceList",bloodDeviceList);

        return  ApiResult.ok(result);
    }







    @PostMapping(value = "/saveBloodSugarData/{imei}")
    @AppLog("保存血糖仪检测结果")
    @ApiOperation("保存血糖仪检测结果")
    public ApiResult<String> saveBloodSugarData(@PathVariable("imei") String imei,@Validated @RequestBody DBloodSugarDataRecords resources){
        try {
            dBloodSugarDataRecordsService.saveEntity(resources,imei);

            //通过MQTT 给这台主机推送最新的健康测量数据
            healthSummaryService.getAndPushAllHealthRecordData(imei,resources.getUid());

        }catch (Exception e){
            return ApiResult.fail(e.getMessage());
        }
        return  ApiResult.ok("保存成功");
    }



    @PostMapping(value = "/saveBalanceData/{imei}")
    @AppLog("保存体脂秤检测结果")
    @ApiOperation("保存体脂秤检测结果")
    public ApiResult<String> saveBalanceData(@PathVariable("imei") String imei,@Validated @RequestBody DBalanceDataRecords resources){
        try {
            dBalanceDataRecordsService.saveEntity(resources,imei);

            //通过MQTT 给这台主机推送最新的健康测量数据
            healthSummaryService.getAndPushAllHealthRecordData(imei,resources.getUid());

        }catch (Exception e){
            return ApiResult.fail(e.getMessage());
        }
        return  ApiResult.ok("保存成功");
    }



    @PostMapping(value = "/saveEcgData/{imei}")
    @AppLog("保存心电图检测结果")
    @ApiOperation("保存心电图检测结果")
    public ApiResult<String> saveEcgData(@PathVariable("imei") String imei,@Validated @RequestBody DEcgDataRecords resources){
        try {
          dEcgDataRecordsService.saveEntity(resources,imei);

            //通过MQTT 给这台主机推送最新的健康测量数据
            healthSummaryService.getAndPushAllHealthRecordData(imei,resources.getUid());

        }catch (Exception e){
            return ApiResult.fail(e.getMessage());
        }
        return  ApiResult.ok("保存成功");
    }






}
