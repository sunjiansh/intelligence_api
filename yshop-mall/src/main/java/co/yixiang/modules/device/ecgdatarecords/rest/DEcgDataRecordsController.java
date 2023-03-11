/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.device.ecgdatarecords.rest;
import java.util.Arrays;
import co.yixiang.dozer.service.IGenerator;
import lombok.AllArgsConstructor;
import co.yixiang.modules.logging.aop.log.Log;
import co.yixiang.modules.device.ecgdatarecords.domain.DEcgDataRecords;
import co.yixiang.modules.device.ecgdatarecords.service.DEcgDataRecordsService;
import co.yixiang.modules.device.ecgdatarecords.service.dto.DEcgDataRecordsQueryCriteria;
import co.yixiang.modules.device.ecgdatarecords.service.dto.DEcgDataRecordsDto;
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
* @date 2023-03-06
*/
@AllArgsConstructor
@Api(tags = "心电图测量数据上报管理")
@RestController
@RequestMapping("/api/dEcgDataRecords")
public class DEcgDataRecordsController {

    private final DEcgDataRecordsService dEcgDataRecordsService;
    private final IGenerator generator;


    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    //@PreAuthorize("@el.check('admin','dEcgDataRecords:list')")
    public void download(HttpServletResponse response, DEcgDataRecordsQueryCriteria criteria) throws IOException {
        dEcgDataRecordsService.download(generator.convert(dEcgDataRecordsService.queryAll(criteria), DEcgDataRecordsDto.class), response);
    }

    @GetMapping
    @Log("查询心电图测量数据上报")
    @ApiOperation("查询心电图测量数据上报")
  //  @PreAuthorize("@el.check('admin','dEcgDataRecords:list')")
    public ResponseEntity<PageResult<DEcgDataRecordsDto>> getDEcgDataRecordss(DEcgDataRecordsQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(dEcgDataRecordsService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增心电图测量数据上报")
    @ApiOperation("新增心电图测量数据上报")
   // @PreAuthorize("@el.check('admin','dEcgDataRecords:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody DEcgDataRecords resources){
        return new ResponseEntity<>(dEcgDataRecordsService.save(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改心电图测量数据上报")
    @ApiOperation("修改心电图测量数据上报")
  //  @PreAuthorize("@el.check('admin','dEcgDataRecords:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody DEcgDataRecords resources){
        dEcgDataRecordsService.updateById(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除心电图测量数据上报")
    @ApiOperation("删除心电图测量数据上报")
  //  @PreAuthorize("@el.check('admin','dEcgDataRecords:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Long[] ids) {
        Arrays.asList(ids).forEach(id->{
            dEcgDataRecordsService.removeById(id);
        });
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
