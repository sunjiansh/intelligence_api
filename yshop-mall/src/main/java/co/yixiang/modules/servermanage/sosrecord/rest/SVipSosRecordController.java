/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.servermanage.sosrecord.rest;
import java.util.Arrays;
import co.yixiang.dozer.service.IGenerator;
import lombok.AllArgsConstructor;
import co.yixiang.modules.logging.aop.log.Log;
import co.yixiang.modules.servermanage.sosrecord.domain.SVipSosRecord;
import co.yixiang.modules.servermanage.sosrecord.service.SVipSosRecordService;
import co.yixiang.modules.servermanage.sosrecord.service.dto.SVipSosRecordQueryCriteria;
import co.yixiang.modules.servermanage.sosrecord.service.dto.SVipSosRecordDto;
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
* @date 2023-02-11
*/
@AllArgsConstructor
@Api(tags = "会员服务管理-SOS记录管理")
@RestController
@RequestMapping("/api/sVipSosRecord")
public class SVipSosRecordController {

    private final SVipSosRecordService sVipSosRecordService;
    private final IGenerator generator;


    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('admin','sVipSosRecord:list')")
    public void download(HttpServletResponse response, SVipSosRecordQueryCriteria criteria) throws IOException {
        sVipSosRecordService.download(generator.convert(sVipSosRecordService.queryAll(criteria), SVipSosRecordDto.class), response);
    }

    @GetMapping
    @Log("查询会员服务管理-SOS记录")
    @ApiOperation("查询会员服务管理-SOS记录")
    @PreAuthorize("@el.check('admin','sVipSosRecord:list')")
    public ResponseEntity<PageResult<SVipSosRecordDto>> getSVipSosRecords(SVipSosRecordQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(sVipSosRecordService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增会员服务管理-SOS记录")
    @ApiOperation("新增会员服务管理-SOS记录")
    @PreAuthorize("@el.check('admin','sVipSosRecord:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody SVipSosRecord resources){
        return new ResponseEntity<>(sVipSosRecordService.save(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改会员服务管理-SOS记录")
    @ApiOperation("修改会员服务管理-SOS记录")
    @PreAuthorize("@el.check('admin','sVipSosRecord:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody SVipSosRecord resources){
        sVipSosRecordService.updateById(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除会员服务管理-SOS记录")
    @ApiOperation("删除会员服务管理-SOS记录")
    @PreAuthorize("@el.check('admin','sVipSosRecord:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Long[] ids) {
        Arrays.asList(ids).forEach(id->{
            sVipSosRecordService.removeById(id);
        });
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
