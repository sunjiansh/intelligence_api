/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.device.balance.rest;
import java.util.Arrays;
import co.yixiang.dozer.service.IGenerator;
import lombok.AllArgsConstructor;
import co.yixiang.modules.logging.aop.log.Log;
import co.yixiang.modules.device.balance.domain.DBalance;
import co.yixiang.modules.device.balance.service.DBalanceService;
import co.yixiang.modules.device.balance.service.dto.DBalanceQueryCriteria;
import co.yixiang.modules.device.balance.service.dto.DBalanceDto;
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
@Api(tags = "体脂秤管理")
@RestController
@RequestMapping("/api/dBalance")
public class DBalanceController {

    private final DBalanceService dBalanceService;
    private final IGenerator generator;


    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    //@PreAuthorize("@el.check('admin','dBalance:list')")
    public void download(HttpServletResponse response, DBalanceQueryCriteria criteria) throws IOException {
        dBalanceService.download(generator.convert(dBalanceService.queryAll(criteria), DBalanceDto.class), response);
    }

    @GetMapping
    @Log("查询体脂秤")
    @ApiOperation("查询体脂秤")
    //@PreAuthorize("@el.check('admin','dBalance:list')")
    public ResponseEntity<PageResult<DBalanceDto>> getDBalances(DBalanceQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(dBalanceService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @GetMapping(value = "avaliableBind")
    @Log("查询可绑定的体脂秤")
    @ApiOperation("查询可绑定的体脂秤")
    //@PreAuthorize("@el.check('admin','dBalance:list')")
    public ResponseEntity<PageResult<DBalanceDto>> getDBalancesAvaliableBind(DBalanceQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity(dBalanceService.queryAllDBlanceBindAvailablePage(criteria,pageable),HttpStatus.OK);
    }



    @PostMapping
    @Log("新增体脂秤")
    @ApiOperation("新增体脂秤")
    //@PreAuthorize("@el.check('admin','dBalance:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody DBalance resources){
        resources.setCreateTime(new Date());
        return new ResponseEntity<>(dBalanceService.save(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改体脂秤")
    @ApiOperation("修改体脂秤")
    //@PreAuthorize("@el.check('admin','dBalance:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody DBalance resources){
        dBalanceService.updateById(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除体脂秤")
    @ApiOperation("删除体脂秤")
    //@PreAuthorize("@el.check('admin','dBalance:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Long[] ids) {
        Arrays.asList(ids).forEach(id->{
            dBalanceService.removeById(id);
        });
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
