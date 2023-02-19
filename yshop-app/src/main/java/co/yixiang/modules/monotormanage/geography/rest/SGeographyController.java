/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.monotormanage.geography.rest;

import co.yixiang.api.ApiResult;
import co.yixiang.api.YshopException;
import co.yixiang.common.aop.NoRepeatSubmit;
import co.yixiang.common.bean.LocalUser;
import co.yixiang.common.interceptor.AuthCheck;
import co.yixiang.dozer.service.IGenerator;
import co.yixiang.modules.logging.aop.log.AppLog;
import co.yixiang.modules.monotormanage.geography.domain.SGeography;
import co.yixiang.modules.monotormanage.geography.service.SGeographyService;
import co.yixiang.modules.user.domain.YxUser;
import co.yixiang.modules.user.service.YxUserService;
import co.yixiang.utils.location.GetTencentLocationVO;
import co.yixiang.utils.location.LocationUtils;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
* @author jiansun
* @date 2023-02-12
*/
@Slf4j
@RestController
@Api(value = "电子围栏管理", tags = "电子围栏管理")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/sGeography")
public class SGeographyController {

    private final SGeographyService sGeographyService;
    private final IGenerator generator;
    private final YxUserService yxUserService;


    @AuthCheck
    @GetMapping
    @AppLog("查询电子围栏管理")
    @ApiOperation("查询电子围栏管理")
    public ApiResult<List<SGeography>> getSGeographys(){
        Long uid = LocalUser.getUser().getUid();
        List<SGeography> list = sGeographyService.list(new LambdaQueryWrapper<SGeography>().eq(SGeography::getMemberId,uid));
        return  ApiResult.ok(list);
    }

    @AuthCheck
    @GetMapping(value = "/{id}")
    @AppLog("根据ID查询电子围栏")
    @ApiOperation("根据ID查询电子围栏")
    public ApiResult<SGeography> getSGeographyById(@PathVariable("id") Long id){
        //Long uid = LocalUser.getUser().getUid();
        SGeography geography = sGeographyService.getById(id);
        return  ApiResult.ok(geography);
    }


    @AuthCheck
    @PostMapping
    @NoRepeatSubmit
    @AppLog("新增电子围栏管理")
    @ApiOperation("新增电子围栏管理")
    public ApiResult<Boolean> create(@Validated @RequestBody SGeography resources){

        YxUser user = LocalUser.getUser();
        resources.setMemberId(user.getUid());
        resources.setMemberName(user.getRealName());
        resources.setImei(user.getImei());
        resources.setUpdateTime(new Date());
        sGeographyService.save(resources);
        return  ApiResult.ok();
    }

    @AuthCheck
    @PutMapping
    @AppLog("修改电子围栏管理")
    @ApiOperation("修改电子围栏管理")
    public ApiResult<Boolean> update(@Validated @RequestBody SGeography resources){
        sGeographyService.updateById(resources);
        return  ApiResult.ok();
    }

    @AuthCheck
    @AppLog("删除电子围栏管理")
    @ApiOperation("删除电子围栏管理")
    @PostMapping(value = "/delete")
    public ApiResult<Boolean> deleteAll(@RequestBody Long[] ids) {
        Arrays.asList(ids).forEach(id->{
            sGeographyService.removeById(id);
        });
        return  ApiResult.ok();
    }



    @AuthCheck
    @AppLog("根据坐标反查地址")
    @ApiOperation("根据坐标反查地址")
    @PostMapping(value = "/address")
    public ApiResult<Object> address(HttpServletResponse response, @RequestBody JSONObject jsonObject) {
           // JSONObject jsonObject = JSON.parseObject(jsonStr);
            double lat = jsonObject.getDouble("lat");
            double lng = jsonObject.getDouble("lng");
         JSONObject result = LocationUtils.getAddress(lat,lng);
         return  ApiResult.ok(result);
    }


    @AuthCheck
    @AppLog("根据地址查坐标")
    @ApiOperation("根据地址查坐标")
    @PostMapping(value = "/latLng")
    public ApiResult<Object> latLng(HttpServletResponse response,@RequestBody JSONObject jsonObject) {
        String address = jsonObject.getString("address");
        GetTencentLocationVO result = LocationUtils.getLocation(address);
        if(result.getStatus()!=0){
            new YshopException(result.getMessage());
        }
        return ApiResult.ok(result);
    }



}
