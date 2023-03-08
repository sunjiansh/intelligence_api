/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.watch.rest;

import co.yixiang.api.ApiResult;
import co.yixiang.common.bean.LocalUser;
import co.yixiang.common.interceptor.AuthCheck;
import co.yixiang.common.util.HexUtil;
import co.yixiang.dozer.service.IGenerator;
import co.yixiang.modules.device.apiservice.DWatchUricApiService;
import co.yixiang.modules.device.watchuricdatarecords.service.DWatchUricDataRecordsService;
import co.yixiang.modules.logging.aop.log.AppLog;
import co.yixiang.modules.user.domain.YxUser;
import co.yixiang.modules.user.service.YxUserService;
import co.yixiang.modules.watch.domain.DWatch;
import co.yixiang.modules.watch.service.DWatchService;
import co.yixiang.utils.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author jiansun
 * @date 2023-01-10
 */
@Slf4j
@RestController
@Api(value = "智能手环管理", tags = "智能手环管理")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/dWatch")
public class DWatchController {

    private final DWatchService dWatchService;
    private final IGenerator generator;
    private final YxUserService yxUserService;
    private final DWatchUricApiService dWatchUricApiService;
    private final DWatchUricDataRecordsService dWatchUricDataRecordsService;





    @AuthCheck
    @GetMapping
    @AppLog("查询智能手环")
    @ApiOperation("查询智能手环")
    public ApiResult<List<DWatch>> getDWatchs(){
        YxUser user = LocalUser.getUser();
        List<DWatch> list = dWatchService.list(new LambdaQueryWrapper<DWatch>().eq(DWatch::getImei,user.getImei()));
        return  ApiResult.ok(list);
    }


    @AuthCheck
    @GetMapping(value = "/{id}")
    @AppLog("查询智能手环")
    @ApiOperation("查询智能手环")
    public ApiResult<DWatch> getDWatchById(@PathVariable("id") Long id){
        DWatch dWatch = dWatchService.getById(id);
        return  ApiResult.ok(dWatch);
    }


    @AuthCheck
    @PutMapping(value = "/configHr")
    @AppLog("设置心率、血压、血氧、血糖上传频率(单位秒,连续上传时最小时间不小于 300 秒，最大不超过 65535)0：关闭 1：单次上传")
    @ApiOperation("设置心率、血压、血氧、血糖上传频率(单位秒,连续上传时最小时间不小于 300 秒，最大不超过 65535)0：关闭 1：单次上传")
    public ApiResult<Boolean> configHr( @RequestBody DWatch resources){
        DWatch existWatch = dWatchService.getById(resources.getId());

        try {
            JSONObject result  =  dWatchUricApiService.configWatchHr(resources.getImei(),resources.getBldstart());
            if(result.getInteger("code") != 200){
                log.error(result.getString("msg"));
                throw new RuntimeException(result.getString("msg"));
            }
        }catch (Exception e){
            log.error("设置心率、血压、血氧、血糖上传频率失败！"+e.getMessage());
            throw new RuntimeException("调用智能手环平台配置设置心率、血压、血氧、血糖上传频率接口失败！"+e.getMessage());
        }

        dWatchService.updateById(resources);
        return ApiResult.ok();
    }


    @AuthCheck
    @PutMapping(value = "/configTemperature")
    @AppLog("设置体温上传频率(单位秒,连续上传时最小时间不小于 300 秒，最大不超过 65535)0：关闭 1：单次上传")
    @ApiOperation("设置体温上传频率(单位秒,连续上传时最小时间不小于 300 秒，最大不超过 65535)0：关闭 1：单次上传")
    public ApiResult<Boolean> configTemperature( @RequestBody DWatch resources){
        DWatch existWatch = dWatchService.getById(resources.getId());

        try {
            JSONObject result  =  dWatchUricApiService.configWatchTemperature(resources.getImei(),resources.getWdstart());
            if(result.getInteger("code") != 200){
                log.error(result.getString("msg"));
                throw new RuntimeException(result.getString("msg"));
            }
        }catch (Exception e){
            log.error("设置体温上传频率失败！"+e.getMessage());
            throw new RuntimeException("调用智能手环平台配置设置体温上传频率接口失败！"+e.getMessage());
        }

        dWatchService.updateById(resources);
        return ApiResult.ok();
    }


    @AuthCheck
    @PutMapping(value = "/configLocation")
    @AppLog("设置上报位置的时间间隔，单位是秒，此上传间隔针对手表处于运动状态时，手表静止时不传位置数据")
    @ApiOperation("设置上报位置的时间间隔，单位是秒，此上传间隔针对手表处于运动状态时，手表静止时不传位置数据")
    public ApiResult<Boolean> configLocation( @RequestBody DWatch resources){
        DWatch existWatch = dWatchService.getById(resources.getId());

        try {
            JSONObject result  =  dWatchUricApiService.configWatchLocation(resources.getImei(),resources.getLocation());
            if(result.getInteger("code") != 200){
                log.error(result.getString("msg"));
                throw new RuntimeException(result.getString("msg"));
            }
        }catch (Exception e){
            log.error("设置定位上传频率失败！"+e.getMessage());
            throw new RuntimeException("调用智能手环平台配置设置定位上传频率接口失败！"+e.getMessage());
        }

        dWatchService.updateById(resources);
        return ApiResult.ok();
    }

    @AuthCheck
    @PutMapping(value = "/configSleep")
    @AppLog("设置上睡眠数据上报")
    @ApiOperation("设置上睡眠数据上报")
    public ApiResult<Boolean> configSleep( @RequestBody DWatch resources){
        DWatch existWatch = dWatchService.getById(resources.getId());
        try {

            if(StringUtils.isEmpty(resources.getSleepTimeStartStr())){
                throw new RuntimeException("就寝时间不能为空！");
            }
            if(StringUtils.isEmpty(resources.getSleepTimeEndStr())){
                throw new RuntimeException("起床时间不能为空！");
            }
            String startTime = resources.getSleepTimeStartStr().replace(":","");
            String endTime = resources.getSleepTimeEndStr().replace(":","");

            JSONObject result  =  dWatchUricApiService.configWatchSleep(resources.getImei(),startTime,endTime);
            if(result.getInteger("code") != 200){
                log.error(result.getString("msg"));
                throw new RuntimeException(result.getString("msg"));
            }
        }catch (Exception e){
            log.error("设置睡眠监测时间失败！"+e.getMessage());
            throw new RuntimeException("调用智能手环平台配置设置睡眠监测时间接口失败！"+e.getMessage());
        }
        dWatchService.updateById(resources);
        return ApiResult.ok();
    }



    @AuthCheck
    @PutMapping(value = "/configSOS")
    @AppLog("设置sos号码，多个号码以英文逗号分割，最多三个")
    @ApiOperation("设置sos号码，多个号码以英文逗号分割，最多三个")
    public ApiResult<Boolean> configSOS( @RequestBody DWatch resources){
        DWatch existWatch = dWatchService.getById(resources.getId());

        try {
            JSONObject result  =  dWatchUricApiService.configWatchSOSInfo(resources.getImei(),resources.getSos());
            if(result.getInteger("code") != 200){
                log.error(result.getString("msg"));
                throw new RuntimeException(result.getString("msg"));
            }
        }catch (Exception e){
            log.error("设置SOS号码失败！"+e.getMessage());
            throw new RuntimeException("调用智能手环平台配置设置SOS号码接口失败！"+e.getMessage());
        }


        try {
            YxUser user = yxUserService.getOne(new LambdaQueryWrapper<YxUser>().eq(YxUser::getImei,existWatch.getImei()));
            user.setSosContact(resources.getSos());
            yxUserService.updateById(user);
        }catch (Exception e){
            log.error("同步SOS号码到用户失败！"+e.getMessage());
            throw new RuntimeException("同步SOS号码到用户失败！"+e.getMessage());
        }

        dWatchService.updateById(resources);
        return ApiResult.ok();
    }





    @AuthCheck
    @PutMapping(value = "/configContacts")
    @AppLog("设置通讯录")
    @ApiOperation("设置通讯录")
    public ApiResult<Boolean> configContacts( @RequestBody DWatch resources){
     //   DWatch existWatch = dWatchService.getById(resources.getId());
        JSONArray array = new JSONArray();
        if(StringUtils.isNotEmpty(resources.getWhiteListStr())){
            String[] arr = resources.getWhiteListStr().split(",");
            for(String co : arr){
                String[] items = co.split("\\|");
                String name = items[0];
                String phone = items[1];
                String newname = HexUtil.getUnicodeName(name);
                String newco = newname+"|"+phone;
                array.add(newco);
            }
        }else{
            array.add("");
        }
        try {
            JSONObject result  =  dWatchUricApiService.configWatchContacts(resources.getImei(),array);
            if(result.getInteger("code") != 200){
                log.error(result.getString("msg"));
                throw new RuntimeException(result.getString("msg"));
            }
        }catch (Exception e){
            log.error("设置通讯录失败！"+e.getMessage());
            throw new RuntimeException("调用智能手环平台配置设置通讯录接口失败！"+e.getMessage());
        }


        dWatchService.updateById(resources);
        return ApiResult.ok();
    }

    @AuthCheck
    @GetMapping(value = "/getMyHistoryTrace")
    @AppLog("查看历史足迹")
    @ApiOperation("查看历史足迹")
    public ApiResult<List<Map>> getMyHistoryTrace(@RequestParam("day") Date  day){
        YxUser user = LocalUser.getUser();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dayStr = simpleDateFormat.format(day);
        List<Map> result = dWatchUricDataRecordsService.getMapLocationRecordsByUid(dayStr,user.getUid());
        return ApiResult.ok(result);
    }





}
