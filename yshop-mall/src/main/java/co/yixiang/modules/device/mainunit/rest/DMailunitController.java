/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.device.mainunit.rest;
import java.util.Arrays;
import co.yixiang.dozer.service.IGenerator;
import lombok.AllArgsConstructor;
import co.yixiang.modules.logging.aop.log.Log;
import co.yixiang.modules.device.mainunit.domain.DMailunit;
import co.yixiang.modules.device.mainunit.service.DMailunitService;
import co.yixiang.modules.device.mainunit.service.dto.DMailunitQueryCriteria;
import co.yixiang.modules.device.mainunit.service.dto.DMailunitDto;
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
* @date 2023-01-11
*/
@AllArgsConstructor
@Api(tags = "主机设备管理")
@RestController
@RequestMapping("/api/dMailunit")
public class DMailunitController {

    private final DMailunitService dMailunitService;
    private final IGenerator generator;


    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    //@PreAuthorize("@el.check('admin','dMailunit:list')")
    public void download(HttpServletResponse response, DMailunitQueryCriteria criteria) throws IOException {
        dMailunitService.download(generator.convert(dMailunitService.queryAll(criteria), DMailunitDto.class), response);
    }

    @GetMapping
    @Log("查询主机设备")
    @ApiOperation("查询主机设备")
    //@PreAuthorize("@el.check('admin','dMailunit:list')")
    public ResponseEntity<PageResult<DMailunitDto>> getDMailunits(DMailunitQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(dMailunitService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增主机设备")
    @ApiOperation("新增主机设备")
    //@PreAuthorize("@el.check('admin','dMailunit:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody DMailunit resources){
        return new ResponseEntity<>(dMailunitService.save(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改主机设备")
    @ApiOperation("修改主机设备")
    //@PreAuthorize("@el.check('admin','dMailunit:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody DMailunit resources){
        dMailunitService.updateById(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除主机设备")
    @ApiOperation("删除主机设备")
    //@PreAuthorize("@el.check('admin','dMailunit:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Long[] ids) {
        Arrays.asList(ids).forEach(id->{
            dMailunitService.removeById(id);
        });
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
