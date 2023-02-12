/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.device.mdevicedevice.rest;

import co.yixiang.domain.PageResult;
import co.yixiang.dozer.service.IGenerator;
import co.yixiang.exception.BadRequestException;
import co.yixiang.modules.device.balance.service.DBalanceService;
import co.yixiang.modules.device.balance.service.dto.DBalanceDto;
import co.yixiang.modules.device.ecg.service.DEcgService;
import co.yixiang.modules.device.ecg.service.dto.DEcgDto;
import co.yixiang.modules.device.mainunit.domain.DMailunit;
import co.yixiang.modules.device.mainunit.service.DMailunitService;
import co.yixiang.modules.device.mdevicedevice.domain.DMdeviceDevice;
import co.yixiang.modules.device.mdevicedevice.service.DMdeviceDeviceService;
import co.yixiang.modules.device.mdevicedevice.service.dto.DMdeviceDeviceDto;
import co.yixiang.modules.device.mdevicedevice.service.dto.DMdeviceDeviceQueryCriteria;
import co.yixiang.modules.device.tumble.service.DTumbleService;
import co.yixiang.modules.device.tumble.service.dto.DTumbleDto;
import co.yixiang.modules.logging.aop.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
/**
* @author jiansun
* @date 2023-01-14
*/
@AllArgsConstructor
@Api(tags = "主机设备与其他守备关联表管理")
@RestController
@RequestMapping("/api/dMdeviceDevice")
public class DMdeviceDeviceController {

    private final DMdeviceDeviceService dMdeviceDeviceService;
    private final IGenerator generator;
    private final DMailunitService mainunitService;

    private final co.yixiang.modules.blood.service.DBloodService dBloodService;
    private final DBalanceService dBalanceService;
    private final DTumbleService dTumbleService;
    private final DEcgService dEcgService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    //@PreAuthorize("@el.check('admin','dMdeviceDevice:list')")
    public void download(HttpServletResponse response, DMdeviceDeviceQueryCriteria criteria) throws IOException {
        dMdeviceDeviceService.download(generator.convert(dMdeviceDeviceService.queryAll(criteria), DMdeviceDeviceDto.class), response);
    }

    @GetMapping
    @Log("查询主机设备与其他守备关联表")
    @ApiOperation("查询主机设备与其他守备关联表")
    //@PreAuthorize("@el.check('admin','dMdeviceDevice:list')")
    public ResponseEntity<PageResult<DMdeviceDeviceDto>> getDMdeviceDevices(DMdeviceDeviceQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(dMdeviceDeviceService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @GetMapping(value = "/{mid}")
    @Log("根据主机设备ID查询主机设备与其他设备关联表")
    @ApiOperation("根据主机设备ID查询主机设备与其他设备关联表")
    //@PreAuthorize("@el.check('admin','dMdeviceDevice:list')")
    public ResponseEntity<PageResult<DMdeviceDeviceDto>> getDMdeviceDevicesByMid(@PathVariable(name = "mid") Long mid){
       List<DMdeviceDevice>  result = dMdeviceDeviceService.list(new LambdaQueryWrapper<DMdeviceDevice>().eq(DMdeviceDevice::getMid, mid));
        return new ResponseEntity(result,HttpStatus.OK);
    }


    @GetMapping(value = "/{mid}/{deviceType}")
    @Log("根据主机设备ID和设备类型查询主机设备与其他设备关联表")
    @ApiOperation("根据主机设备ID和设备类型查询主机设备与其他设备关联表")
    //@PreAuthorize("@el.check('admin','dMdeviceDevice:list')")
    public ResponseEntity<PageResult<DMdeviceDeviceDto>> getDMdeviceDevicesByMidAndDeviceType(@PathVariable(name = "mid") Long mid,@PathVariable(name = "deviceType") String deviceType){
    //    List<DMdeviceDevice>  xxx = dMdeviceDeviceService.list(new LambdaQueryWrapper<DMdeviceDevice>().eq(DMdeviceDevice::getMid, mid).eq(DMdeviceDevice::getDtype, deviceType));
        switch (deviceType){
            case "0":
                List<co.yixiang.modules.blood.service.dto.DBloodDto> result0  =  dBloodService.queryAllBindedDeviceByMid(mid);
                return new ResponseEntity(result0,HttpStatus.OK);
            case "1":
                List<DTumbleDto> result1 = dTumbleService.queryAllBindedDeviceByMid(mid);
                return new ResponseEntity(result1,HttpStatus.OK);
            case "2":
                List<DBalanceDto> result2 = dBalanceService.queryAllBindedDeviceByMid(mid);
                return new ResponseEntity(result2,HttpStatus.OK);
            case "3":
                return new ResponseEntity(null,HttpStatus.OK);
            case "4":
                List<DEcgDto> result3 = dEcgService.queryAllBindedDeviceByMid(mid);
                return new ResponseEntity(result3,HttpStatus.OK);
                default: return new ResponseEntity(null,HttpStatus.OK);
        }
    }



    @PostMapping
    @Log("新增主机设备与其他守备关联表")
    @ApiOperation("新增主机设备与其他守备关联表")
    //@PreAuthorize("@el.check('admin','dMdeviceDevice:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody DMdeviceDevice resources){
        DMdeviceDevice existDevice = dMdeviceDeviceService.getOne(new LambdaQueryWrapper<DMdeviceDevice>().eq(DMdeviceDevice::getDid,resources.getDid()).eq(DMdeviceDevice::getDtype,resources.getDtype()));
        if(existDevice != null){
            Long mid = existDevice.getMid();
            DMailunit mainunit =   mainunitService.getOne(new LambdaQueryWrapper<DMailunit>().eq(DMailunit::getId,mid));
            throw  new BadRequestException("该设备已绑定到主机号(IMEi):["+mainunit.getImei()+"],请勿重复绑定！");
        }

        //同步尿酸分析仪和人员绑定到手环平台（如果dtype则设备类型是尿酸分析仪）
//        if(StringUtils.equals(resources.getDtype(),"3")){
//            dMdeviceDeviceService.sycnBindUric2Users(resources.getMid(),resources.getDid());
//        }

        return new ResponseEntity<>(dMdeviceDeviceService.save(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改主机设备与其他守备关联表")
    @ApiOperation("修改主机设备与其他守备关联表")
    //@PreAuthorize("@el.check('admin','dMdeviceDevice:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody DMdeviceDevice resources){
        dMdeviceDeviceService.updateById(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除主机设备与其他守备关联表")
    @ApiOperation("删除主机设备与其他守备关联表")
    //@PreAuthorize("@el.check('admin','dMdeviceDevice:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Long[] ids) {
        Arrays.asList(ids).forEach(id->{
            dMdeviceDeviceService.removeById(id);
        });
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
