/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.device.ecg.rest;
import java.util.Arrays;
import co.yixiang.dozer.service.IGenerator;
import lombok.AllArgsConstructor;
import co.yixiang.modules.logging.aop.log.Log;
import co.yixiang.modules.device.ecg.domain.DEcg;
import co.yixiang.modules.device.ecg.service.DEcgService;
import co.yixiang.modules.device.ecg.service.dto.DEcgQueryCriteria;
import co.yixiang.modules.device.ecg.service.dto.DEcgDto;
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
@Api(tags = "心电图检测仪管理")
@RestController
@RequestMapping("/api/dEcg")
public class DEcgController {

    private final DEcgService dEcgService;
    private final IGenerator generator;


    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    //@PreAuthorize("@el.check('admin','dEcg:list')")
    public void download(HttpServletResponse response, DEcgQueryCriteria criteria) throws IOException {
        dEcgService.download(generator.convert(dEcgService.queryAll(criteria), DEcgDto.class), response);
    }

    @GetMapping
    @Log("查询心电图检测仪")
    @ApiOperation("查询心电图检测仪")
    //@PreAuthorize("@el.check('admin','dEcg:list')")
    public ResponseEntity<PageResult<DEcgDto>> getDEcgs(DEcgQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(dEcgService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @GetMapping(value = "avaliableBind")
    @Log("查询可绑定的心电图检测仪")
    @ApiOperation("查询可绑定的心电图检测仪")
    //@PreAuthorize("@el.check('admin','dEcg:list')")
    public ResponseEntity<PageResult<DEcgDto>> getDEcgsAvaliableBind(DEcgQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity(dEcgService.queryAllDEcgBindAvailablePage(criteria,pageable),HttpStatus.OK);
    }



    @PostMapping
    @Log("新增心电图检测仪")
    @ApiOperation("新增心电图检测仪")
    //@PreAuthorize("@el.check('admin','dEcg:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody DEcg resources){
        resources.setCreateTime(new Date());
        return new ResponseEntity<>(dEcgService.save(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改心电图检测仪")
    @ApiOperation("修改心电图检测仪")
    //@PreAuthorize("@el.check('admin','dEcg:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody DEcg resources){
        dEcgService.updateById(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除心电图检测仪")
    @ApiOperation("删除心电图检测仪")
    //@PreAuthorize("@el.check('admin','dEcg:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Long[] ids) {
        Arrays.asList(ids).forEach(id->{
            dEcgService.removeById(id);
        });
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
