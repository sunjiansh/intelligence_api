/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.device.bloodsugardatarecords.rest;
import java.util.Arrays;
import co.yixiang.dozer.service.IGenerator;
import lombok.AllArgsConstructor;
import co.yixiang.modules.logging.aop.log.Log;
import co.yixiang.modules.device.bloodsugardatarecords.domain.DBloodSugarDataRecords;
import co.yixiang.modules.device.bloodsugardatarecords.service.DBloodSugarDataRecordsService;
import co.yixiang.modules.device.bloodsugardatarecords.service.dto.DBloodSugarDataRecordsQueryCriteria;
import co.yixiang.modules.device.bloodsugardatarecords.service.dto.DBloodSugarDataRecordsDto;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import co.yixiang.domain.PageResult;
/**
* @author jiansun
* @date 2023-03-04
*/
@AllArgsConstructor
@Api(tags = "血糖仪数据上报接口管理")
@RestController
@RequestMapping("/api/dBloodSugarDataRecords")
public class DBloodSugarDataRecordsController {

    private final DBloodSugarDataRecordsService dBloodSugarDataRecordsService;
    private final IGenerator generator;


    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    //@PreAuthorize("@el.check('admin','dBloodSugarDataRecords:list')")
    public void download(HttpServletResponse response, DBloodSugarDataRecordsQueryCriteria criteria) throws IOException {
        dBloodSugarDataRecordsService.download(generator.convert(dBloodSugarDataRecordsService.queryAll(criteria), DBloodSugarDataRecordsDto.class), response);
    }

    @GetMapping
    @Log("查询血糖仪数据上报接口")
    @ApiOperation("查询血糖仪数据上报接口")
    //@PreAuthorize("@el.check('admin','dBloodSugarDataRecords:list')")
    public ResponseEntity<PageResult<DBloodSugarDataRecordsDto>> getDBloodSugarDataRecordss(DBloodSugarDataRecordsQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(dBloodSugarDataRecordsService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增血糖仪数据上报接口")
    @ApiOperation("新增血糖仪数据上报接口")
    //@PreAuthorize("@el.check('admin','dBloodSugarDataRecords:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody DBloodSugarDataRecords resources){
        return new ResponseEntity<>(dBloodSugarDataRecordsService.save(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改血糖仪数据上报接口")
    @ApiOperation("修改血糖仪数据上报接口")
    //@PreAuthorize("@el.check('admin','dBloodSugarDataRecords:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody DBloodSugarDataRecords resources){
        dBloodSugarDataRecordsService.updateById(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除血糖仪数据上报接口")
    @ApiOperation("删除血糖仪数据上报接口")
   // @PreAuthorize("@el.check('admin','dBloodSugarDataRecords:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Long[] ids) {
        Arrays.asList(ids).forEach(id->{
            dBloodSugarDataRecordsService.removeById(id);
        });
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
