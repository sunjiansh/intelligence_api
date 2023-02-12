/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.blood.rest;
import java.util.Arrays;
import co.yixiang.dozer.service.IGenerator;
import co.yixiang.modules.device.blood.domain.ViewDBloodBindAvailable;
import lombok.AllArgsConstructor;
import co.yixiang.modules.logging.aop.log.Log;
import co.yixiang.modules.blood.domain.DBlood;
import co.yixiang.modules.blood.service.DBloodService;
import co.yixiang.modules.blood.service.dto.DBloodQueryCriteria;
import co.yixiang.modules.blood.service.dto.DBloodDto;
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
* @date 2023-01-10
*/
@AllArgsConstructor
@Api(tags = "无创血糖仪管理")
@RestController
@RequestMapping("/api/dBlood")
public class DBloodController {

    private final DBloodService dBloodService;
    private final IGenerator generator;


    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    //@PreAuthorize("@el.check('admin','dBlood:list')")
    public void download(HttpServletResponse response, DBloodQueryCriteria criteria) throws IOException {
        dBloodService.download(generator.convert(dBloodService.queryAll(criteria), DBloodDto.class), response);
    }

    @GetMapping
    @Log("查询无创血糖仪")
    @ApiOperation("查询无创血糖仪")
    //@PreAuthorize("@el.check('admin','dBlood:list')")
    public ResponseEntity<PageResult<DBloodDto>> getDBloods(DBloodQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(dBloodService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @GetMapping(value = "avaliableBind")
    @Log("查询可绑定的无创血糖仪")
    @ApiOperation("查询可绑定的无创血糖仪")
    //@PreAuthorize("@el.check('admin','dBlood:list')")
    public ResponseEntity<PageResult<DBloodDto>> getDBloodsAvaliableBind(DBloodQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity(dBloodService.queryAllDBloodBindAvailablePage(criteria,pageable),HttpStatus.OK);
    }


    @PostMapping
    @Log("新增无创血糖仪")
    @ApiOperation("新增无创血糖仪")
    //@PreAuthorize("@el.check('admin','dBlood:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody DBlood resources){
        resources.setCreateTime(new Date());
        return new ResponseEntity<>(dBloodService.save(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改无创血糖仪")
    @ApiOperation("修改无创血糖仪")
    //@PreAuthorize("@el.check('admin','dBlood:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody DBlood resources){
        dBloodService.updateById(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除无创血糖仪")
    @ApiOperation("删除无创血糖仪")
    //@PreAuthorize("@el.check('admin','dBlood:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Long[] ids) {
        Arrays.asList(ids).forEach(id->{
            dBloodService.removeById(id);
        });
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
