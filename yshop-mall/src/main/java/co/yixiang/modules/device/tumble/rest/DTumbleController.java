/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.device.tumble.rest;
import java.util.Arrays;
import co.yixiang.dozer.service.IGenerator;
import lombok.AllArgsConstructor;
import co.yixiang.modules.logging.aop.log.Log;
import co.yixiang.modules.device.tumble.domain.DTumble;
import co.yixiang.modules.device.tumble.service.DTumbleService;
import co.yixiang.modules.device.tumble.service.dto.DTumbleQueryCriteria;
import co.yixiang.modules.device.tumble.service.dto.DTumbleDto;
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
@Api(tags = "跌倒报警器管理")
@RestController
@RequestMapping("/api/dTumble")
public class DTumbleController {

    private final DTumbleService dTumbleService;
    private final IGenerator generator;


    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    //@PreAuthorize("@el.check('admin','dTumble:list')")
    public void download(HttpServletResponse response, DTumbleQueryCriteria criteria) throws IOException {
        dTumbleService.download(generator.convert(dTumbleService.queryAll(criteria), DTumbleDto.class), response);
    }

    @GetMapping
    @Log("查询跌倒报警器")
    @ApiOperation("查询跌倒报警器")
   // @PreAuthorize("@el.check('admin','dTumble:list')")
    public ResponseEntity<PageResult<DTumbleDto>> getDTumbles(DTumbleQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(dTumbleService.queryAll(criteria,pageable),HttpStatus.OK);
    }


    @GetMapping(value = "avaliableBind")
    @Log("查询可绑定的跌倒报警器")
    @ApiOperation("查询可绑定的跌倒报警器")
   // @PreAuthorize("@el.check('admin','dTumble:list')")
    public ResponseEntity<PageResult<DTumbleDto>> getDTumblesAvaliableBind(DTumbleQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity(dTumbleService.queryAllDTumbleBindAvailablePage(criteria,pageable),HttpStatus.OK);
    }



    @PostMapping
    @Log("新增跌倒报警器")
    @ApiOperation("新增跌倒报警器")
    //@PreAuthorize("@el.check('admin','dTumble:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody DTumble resources){
        resources.setCreateTime(new Date());
        return new ResponseEntity<>(dTumbleService.save(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改跌倒报警器")
    @ApiOperation("修改跌倒报警器")
    //@PreAuthorize("@el.check('admin','dTumble:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody DTumble resources){
        dTumbleService.updateById(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除跌倒报警器")
    @ApiOperation("删除跌倒报警器")
    //@PreAuthorize("@el.check('admin','dTumble:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Long[] ids) {
        Arrays.asList(ids).forEach(id->{
            dTumbleService.removeById(id);
        });
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
