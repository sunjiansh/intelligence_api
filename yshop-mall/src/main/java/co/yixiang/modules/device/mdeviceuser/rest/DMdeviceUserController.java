/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.device.mdeviceuser.rest;
import java.util.Arrays;
import co.yixiang.dozer.service.IGenerator;
import co.yixiang.exception.BadRequestException;
import co.yixiang.exception.EntityExistException;
import co.yixiang.exception.ErrorRequestException;
import co.yixiang.modules.device.mainunit.domain.DMailunit;
import co.yixiang.modules.device.mainunit.service.DMailunitService;
import co.yixiang.modules.device.mdevicedevice.domain.DMdeviceDevice;
import co.yixiang.modules.device.mdevicedevice.service.DMdeviceDeviceService;
import co.yixiang.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.AllArgsConstructor;
import co.yixiang.modules.logging.aop.log.Log;
import co.yixiang.modules.device.mdeviceuser.domain.DMdeviceUser;
import co.yixiang.modules.device.mdeviceuser.service.DMdeviceUserService;
import co.yixiang.modules.device.mdeviceuser.service.dto.DMdeviceUserQueryCriteria;
import co.yixiang.modules.device.mdeviceuser.service.dto.DMdeviceUserDto;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import co.yixiang.domain.PageResult;
/**
* @author jiansun
* @date 2023-01-14
*/
@AllArgsConstructor
@Api(tags = "主机设备人员关联管理")
@RestController
@RequestMapping("/api/dMdeviceUser")
public class DMdeviceUserController {

    private final DMdeviceUserService dMdeviceUserService;
    private final IGenerator generator;
    private final DMailunitService mainunitService;


    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    //@PreAuthorize("@el.check('admin','dMdeviceUser:list')")
    public void download(HttpServletResponse response, DMdeviceUserQueryCriteria criteria) throws IOException {
        dMdeviceUserService.download(generator.convert(dMdeviceUserService.queryAll(criteria), DMdeviceUserDto.class), response);
    }

    @GetMapping
    @Log("查询主机设备人员关联")
    @ApiOperation("查询主机设备人员关联")
  //  @PreAuthorize("@el.check('admin','dMdeviceUser:list')")
    public ResponseEntity<PageResult<DMdeviceUserDto>> getDMdeviceUsers(DMdeviceUserQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(dMdeviceUserService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @GetMapping(value = "/{mid}")
    @Log("根据主机设备ID查询主机设备人员关联")
    @ApiOperation("根据主机设备ID查询主机设备人员关联")
    //@PreAuthorize("@el.check('admin','dMdeviceUser:list')")
    public ResponseEntity<PageResult<DMdeviceUserDto>> getDMdeviceUsersByMid(@PathVariable(name = "mid") Long mid){
        //List<DMdeviceUser> result = dMdeviceUserService.list(new LambdaQueryWrapper<DMdeviceUser>().eq(DMdeviceUser::getMid, mid));
        List<DMdeviceUserDto> result = dMdeviceUserService.queryAllBindedUsersByMid(mid);
        return new ResponseEntity(result,HttpStatus.OK);
    }


    @PostMapping
    @Log("新增主机设备人员关联")
    @ApiOperation("新增主机设备人员关联")
    //@PreAuthorize("@el.check('admin','dMdeviceUser:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody DMdeviceUser resources) throws Exception{
        DMdeviceUser existUser = dMdeviceUserService.getOne(new LambdaQueryWrapper<DMdeviceUser>().eq(DMdeviceUser::getUid,resources.getUid()));
        if(existUser != null){
            Long mid = existUser.getMid();
            DMailunit mainunit =   mainunitService.getOne(new LambdaQueryWrapper<DMailunit>().eq(DMailunit::getId,mid));
            throw  new BadRequestException("该用户已绑定到主机号(IMEI):["+mainunit.getImei()+"],请勿重复绑定！");
        }

        //同步人员和尿酸分析仪绑定到手环平台
        //dMdeviceUserService.sycnBindUric2Users(resources.getMid(),resources.getUid());
        return new ResponseEntity<>(dMdeviceUserService.save(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改主机设备人员关联")
    @ApiOperation("修改主机设备人员关联")
    //@PreAuthorize("@el.check('admin','dMdeviceUser:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody DMdeviceUser resources){
        dMdeviceUserService.updateById(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除主机设备人员关联")
    @ApiOperation("删除主机设备人员关联")
    //@PreAuthorize("@el.check('admin','dMdeviceUser:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Long[] ids) {
        Arrays.asList(ids).forEach(id->{
           // DMdeviceUser  dMdeviceUser = dMdeviceUserService.getById(id);
            //接绑定用户已绑定的尿酸分析仪
            //dMdeviceUserService.syncUnbingUricByUserId(dMdeviceUser.getUid());
            dMdeviceUserService.removeById(id);
        });

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
