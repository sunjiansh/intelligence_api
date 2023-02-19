/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.monotormanage.alarmrecord.rest;

import co.yixiang.domain.PageResult;
import co.yixiang.dozer.service.IGenerator;
import co.yixiang.modules.logging.aop.log.Log;
import co.yixiang.modules.monotormanage.alarmrecord.domain.SAlarmReccord;
import co.yixiang.modules.monotormanage.alarmrecord.service.SAlarmReccordService;
import co.yixiang.modules.monotormanage.alarmrecord.service.dto.SAlarmReccordDto;
import co.yixiang.modules.monotormanage.alarmrecord.service.dto.SAlarmReccordQueryCriteria;
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

/**
* @author jiansun
* @date 2023-02-13
*/
@AllArgsConstructor
@Api(tags = "报警记录管理")
@RestController
@RequestMapping("/api/sAlarmReccord")
public class SAlarmReccordController {

    private final SAlarmReccordService sAlarmReccordService;
    private final IGenerator generator;


    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
   // @PreAuthorize("@el.check('admin','sAlarmReccord:list')")
    public void download(HttpServletResponse response, SAlarmReccordQueryCriteria criteria) throws IOException {
        sAlarmReccordService.download(generator.convert(sAlarmReccordService.queryAll(criteria), SAlarmReccordDto.class), response);
    }

    @GetMapping
    @Log("查询报警记录")
    @ApiOperation("查询报警记录")
   // @PreAuthorize("@el.check('admin','sAlarmReccord:list')")
    public ResponseEntity<PageResult<SAlarmReccordDto>> getSAlarmReccords(SAlarmReccordQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(sAlarmReccordService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增报警记录")
    @ApiOperation("新增报警记录")
  //  @PreAuthorize("@el.check('admin','sAlarmReccord:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody SAlarmReccord resources){
        return new ResponseEntity<>(sAlarmReccordService.save(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改报警记录")
    @ApiOperation("修改报警记录")
  //  @PreAuthorize("@el.check('admin','sAlarmReccord:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody SAlarmReccord resources){
        sAlarmReccordService.updateById(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除报警记录")
    @ApiOperation("删除报警记录")
   // @PreAuthorize("@el.check('admin','sAlarmReccord:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Long[] ids) {
        Arrays.asList(ids).forEach(id->{
            sAlarmReccordService.removeById(id);
        });
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
