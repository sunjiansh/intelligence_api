/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.device.uric.rest;
import java.util.Arrays;
import co.yixiang.dozer.service.IGenerator;
import lombok.AllArgsConstructor;
import co.yixiang.modules.logging.aop.log.Log;
import co.yixiang.modules.device.uric.domain.DUric;
import co.yixiang.modules.device.uric.service.DUricService;
import co.yixiang.modules.device.uric.service.dto.DUricQueryCriteria;
import co.yixiang.modules.device.uric.service.dto.DUricDto;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import java.util.Date;
import javax.servlet.http.HttpServletResponse;
import co.yixiang.domain.PageResult;
/**
* @author jiansun
* @date 2023-01-11
*/
@AllArgsConstructor
@Api(tags = "尿酸分析仪管理")
@RestController
@RequestMapping("/api/dUric")
public class DUricController {

    private final DUricService dUricService;
    private final IGenerator generator;


    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    //@PreAuthorize("@el.check('admin','dUric:list')")
    public void download(HttpServletResponse response, DUricQueryCriteria criteria) throws IOException {
        dUricService.download(generator.convert(dUricService.queryAll(criteria), DUricDto.class), response);
    }

    @GetMapping
    @Log("查询尿酸分析仪")
    @ApiOperation("查询尿酸分析仪")
    //@PreAuthorize("@el.check('admin','dUric:list')")
    public ResponseEntity<PageResult<DUricDto>> getDUrics(DUricQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(dUricService.queryAll(criteria,pageable),HttpStatus.OK);
    }


    @GetMapping(value = "avaliableBind")
    @Log("查询可绑定的尿酸分析仪")
    @ApiOperation("查询可绑定的尿酸分析仪")
    //@PreAuthorize("@el.check('admin','dUric:list')")
    public ResponseEntity<PageResult<DUricDto>> getDUricsAvaliableBind(DUricQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(dUricService.queryAll(criteria,pageable),HttpStatus.OK);
    }


    @PostMapping
    @Log("新增尿酸分析仪")
    @ApiOperation("新增尿酸分析仪")
   // @PreAuthorize("@el.check('admin','dUric:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody DUric resources){
        resources.setCreateTime(new Date());
        return new ResponseEntity<>(dUricService.save(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改尿酸分析仪")
    @ApiOperation("修改尿酸分析仪")
    //@PreAuthorize("@el.check('admin','dUric:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody DUric resources){
        dUricService.updateById(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除尿酸分析仪")
    @ApiOperation("删除尿酸分析仪")
   // @PreAuthorize("@el.check('admin','dUric:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Long[] ids) {
        Arrays.asList(ids).forEach(id->{
            dUricService.removeById(id);
        });
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
