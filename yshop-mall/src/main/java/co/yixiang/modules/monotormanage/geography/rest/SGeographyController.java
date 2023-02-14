/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.monotormanage.geography.rest;
import java.util.Arrays;

import ch.qos.logback.core.util.LocationUtil;
import co.yixiang.dozer.service.IGenerator;
import co.yixiang.exception.BadRequestException;
import co.yixiang.modules.device.watchuricdatarecords.domain.ViewDeviceLocation;
import co.yixiang.modules.user.domain.YxUser;
import co.yixiang.modules.user.service.YxUserService;
import co.yixiang.utils.location.GetTencentLocationVO;
import co.yixiang.utils.location.LocationUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.AllArgsConstructor;
import co.yixiang.modules.logging.aop.log.Log;
import co.yixiang.modules.monotormanage.geography.domain.SGeography;
import co.yixiang.modules.monotormanage.geography.service.SGeographyService;
import co.yixiang.modules.monotormanage.geography.service.dto.SGeographyQueryCriteria;
import co.yixiang.modules.monotormanage.geography.service.dto.SGeographyDto;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import co.yixiang.domain.PageResult;
/**
* @author jiansun
* @date 2023-02-12
*/
@AllArgsConstructor
@Api(tags = "电子围栏管理管理")
@RestController
@RequestMapping("/api/sGeography")
public class SGeographyController {

    private final SGeographyService sGeographyService;
    private final IGenerator generator;
    private final YxUserService yxUserService;


    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
  //  @PreAuthorize("@el.check('admin','sGeography:list')")
    public void download(HttpServletResponse response, SGeographyQueryCriteria criteria) throws IOException {
        sGeographyService.download(generator.convert(sGeographyService.queryAll(criteria), SGeographyDto.class), response);
    }

    @GetMapping
    @Log("查询电子围栏管理")
    @ApiOperation("查询电子围栏管理")
  //  @PreAuthorize("@el.check('admin','sGeography:list')")
    public ResponseEntity<PageResult<SGeographyDto>> getSGeographys(SGeographyQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(sGeographyService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增电子围栏管理")
    @ApiOperation("新增电子围栏管理")
   // @PreAuthorize("@el.check('admin','sGeography:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody SGeography resources){
        resources.setUpdateTime(new Date());
        return new ResponseEntity<>(sGeographyService.save(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改电子围栏管理")
    @ApiOperation("修改电子围栏管理")
   // @PreAuthorize("@el.check('admin','sGeography:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody SGeography resources){
        sGeographyService.updateById(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除电子围栏管理")
    @ApiOperation("删除电子围栏管理")
    //@PreAuthorize("@el.check('admin','sGeography:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Long[] ids) {
        Arrays.asList(ids).forEach(id->{
            sGeographyService.removeById(id);
        });
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @Log("根据坐标反查地址")
    @ApiOperation("根据坐标反查地址")
    @PostMapping(value = "/address")
    public ResponseEntity<Object> address(HttpServletResponse response, @RequestBody JSONObject jsonObject) {
           // JSONObject jsonObject = JSON.parseObject(jsonStr);
            double lat = jsonObject.getDouble("lat");
            double lng = jsonObject.getDouble("lng");
         JSONObject result = LocationUtils.getAddress(lat,lng);
         return new ResponseEntity(result,HttpStatus.OK);
    }

    @Log("根据地址查坐标")
    @ApiOperation("根据地址查坐标")
    @PostMapping(value = "/latLng")
    public ResponseEntity<Object> latLng(HttpServletResponse response,@RequestBody JSONObject jsonObject) {
        String address = jsonObject.getString("address");
        GetTencentLocationVO result = LocationUtils.getLocation(address);
        if(result.getStatus()!=0){
            new BadRequestException(result.getMessage());
        }
        return new ResponseEntity(result.getResult(),HttpStatus.OK);
    }


    @GetMapping(value = "/getDeviceLocationList")
    @Log("查询设备定位信息列表")
    @ApiOperation("查询设备定位信息列表")
    public ResponseEntity<Object> getDeviceLocationList(){
        List<ViewDeviceLocation> list = sGeographyService.queryDeviceLocationList();
        return new ResponseEntity<>(list,HttpStatus.OK);
    }

    @GetMapping(value = "/getUserInfoByImei/{imei}")
    @Log("根据IMEI查询设备定位信息")
    @ApiOperation("根据IMEI查询设备定位信息")
    public ResponseEntity<Object> getUserInfoByImei(@PathVariable("imei") String imei){
        YxUser user = yxUserService.getOne(new LambdaQueryWrapper<YxUser>().eq(YxUser::getImei,imei));
        return new ResponseEntity<>(user,HttpStatus.OK);
    }



}
