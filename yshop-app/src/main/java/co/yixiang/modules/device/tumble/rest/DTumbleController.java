/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.device.tumble.rest;

import co.yixiang.api.ApiResult;
import co.yixiang.api.YshopException;
import co.yixiang.common.aop.NoRepeatSubmit;
import co.yixiang.common.bean.LocalUser;
import co.yixiang.common.interceptor.AuthCheck;
import co.yixiang.dozer.service.IGenerator;
import co.yixiang.modules.device.apiservice.DTumbleApiService;
import co.yixiang.modules.device.tumble.domain.DTumble;
import co.yixiang.modules.device.tumble.service.DTumbleService;
import co.yixiang.modules.logging.aop.log.AppLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
* @author jiansun
* @date 2023-01-11
*/
@Slf4j
@RestController
@Api(value = "跌倒报警器管理", tags = "跌倒报警器管理")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/dTumble")
public class DTumbleController {

    private final DTumbleService dTumbleService;
    private final DTumbleApiService dTumbleApiService;
    private final IGenerator generator;



    @AuthCheck
    @GetMapping
    @AppLog("查询跌倒报警器")
    @ApiOperation("查询跌倒报警器")
    public ApiResult<List<DTumble>> getDTumbles(){
        Long uid = LocalUser.getUser().getUid();
        List<DTumble> list = dTumbleService.list();
        return  ApiResult.ok(list);
    }

    @AuthCheck
    @GetMapping(value = "/{id}")
    @AppLog("根据ID查询跌倒报警器")
    @ApiOperation("根据ID查询跌倒报警器")
    public ApiResult<DTumble> getDTumbleById(@PathVariable("id") Long id){
        DTumble dTumble = dTumbleService.getById(id);
        return  ApiResult.ok(dTumble);
    }


    @AuthCheck
    @PostMapping
    @NoRepeatSubmit
    @AppLog("新增跌倒报警器")
    @ApiOperation("新增跌倒报警器")
    public ApiResult<Boolean> create(@Validated @RequestBody DTumble resources){
        resources.setCreateTime(new Date());
        dTumbleService.save(resources);
        return  ApiResult.ok();
    }

    @AuthCheck
    @PutMapping
    @AppLog("修改跌倒报警器")
    @ApiOperation("修改跌倒报警器")
    public ApiResult<Boolean> update(@Validated @RequestBody DTumble resources){
        DTumble dTumble = dTumbleService.getById(resources.getId());
        dTumble.setHeight(resources.getHeight());

        if(dTumble.getDeviceId() == null){
            throw new YshopException("设备的deviceId为空，等设备自动同步信息后再试！");
        }
        if(dTumble.getDeviceId() == null){
            throw new YshopException("设备的produceId为空，等设备自动同步信息后再试！");
        }
        if(dTumble.getHeight() == null){
            throw new YshopException("设置的高度值不能为空！");
        }
        if(dTumble.getHeight() > 300 || dTumble.getHeight() < 200){
            throw new YshopException("设置的高度值不合法，取值范围 200-300");
        }

        try {
            DTumbleApiService.configHeight(dTumble.getDeviceId(),dTumble.getProductId(),dTumble.getHeight());
        }catch (Exception e){
            throw new YshopException(e.getMessage());
        }
        dTumbleService.updateById(dTumble);
        return  ApiResult.ok();
    }

    @AuthCheck
    @AppLog("删除跌倒报警器")
    @ApiOperation("删除跌倒报警器")
    @PostMapping(value = "/delete")
    public ApiResult<Boolean> deleteAll(@RequestBody Long[] ids) {
        Arrays.asList(ids).forEach(id->{
            dTumbleService.removeById(id);
        });
        return  ApiResult.ok();
    }
}
