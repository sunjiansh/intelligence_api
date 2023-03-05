/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.device.balancedatarecords.rest;
import java.util.Arrays;
import co.yixiang.dozer.service.IGenerator;
import lombok.AllArgsConstructor;
import co.yixiang.modules.logging.aop.log.Log;
import co.yixiang.modules.device.balancedatarecords.domain.DBalanceDataRecords;
import co.yixiang.modules.device.balancedatarecords.service.DBalanceDataRecordsService;
import co.yixiang.modules.device.balancedatarecords.service.dto.DBalanceDataRecordsQueryCriteria;
import co.yixiang.modules.device.balancedatarecords.service.dto.DBalanceDataRecordsDto;
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
@Api(tags = "体脂秤数据上报接口管理")
@RestController
@RequestMapping("/api/dBalanceDataRecords")
public class DBalanceDataRecordsController {

    private final DBalanceDataRecordsService dBalanceDataRecordsService;
    private final IGenerator generator;


    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    //@PreAuthorize("@el.check('admin','dBalanceDataRecords:list')")
    public void download(HttpServletResponse response, DBalanceDataRecordsQueryCriteria criteria) throws IOException {
        dBalanceDataRecordsService.download(generator.convert(dBalanceDataRecordsService.queryAll(criteria), DBalanceDataRecordsDto.class), response);
    }

    @GetMapping
    @Log("查询体脂秤数据上报接口")
    @ApiOperation("查询体脂秤数据上报接口")
    //@PreAuthorize("@el.check('admin','dBalanceDataRecords:list')")
    public ResponseEntity<PageResult<DBalanceDataRecordsDto>> getDBalanceDataRecordss(DBalanceDataRecordsQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(dBalanceDataRecordsService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增体脂秤数据上报接口")
    @ApiOperation("新增体脂秤数据上报接口")
    //@PreAuthorize("@el.check('admin','dBalanceDataRecords:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody DBalanceDataRecords resources){
        return new ResponseEntity<>(dBalanceDataRecordsService.save(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改体脂秤数据上报接口")
    @ApiOperation("修改体脂秤数据上报接口")
    //@PreAuthorize("@el.check('admin','dBalanceDataRecords:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody DBalanceDataRecords resources){
        dBalanceDataRecordsService.updateById(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除体脂秤数据上报接口")
    @ApiOperation("删除体脂秤数据上报接口")
    //@PreAuthorize("@el.check('admin','dBalanceDataRecords:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Long[] ids) {
        Arrays.asList(ids).forEach(id->{
            dBalanceDataRecordsService.removeById(id);
        });
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
