/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.watch.rest;
import java.util.Arrays;
import co.yixiang.dozer.service.IGenerator;
import co.yixiang.exception.BadRequestException;
import co.yixiang.modules.user.domain.YxUser;
import co.yixiang.modules.user.service.YxUserService;
import co.yixiang.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.AllArgsConstructor;
import co.yixiang.modules.logging.aop.log.Log;
import co.yixiang.modules.watch.domain.DWatch;
import co.yixiang.modules.watch.service.DWatchService;
import co.yixiang.modules.watch.service.dto.DWatchQueryCriteria;
import co.yixiang.modules.watch.service.dto.DWatchDto;
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
@Api(tags = "智能手环管理")
@RestController
@RequestMapping("/api/dWatch")
public class DWatchController {

    private final DWatchService dWatchService;
    private final IGenerator generator;
    private final YxUserService yxUserService;


    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    //@PreAuthorize("@el.check('admin','dWatch:list')")
    public void download(HttpServletResponse response, DWatchQueryCriteria criteria) throws IOException {
        dWatchService.download(generator.convert(dWatchService.queryAll(criteria), DWatchDto.class), response);
    }




    @GetMapping(value = "avaliableBind")
    @Log("查询可绑定的智能手环")
    @ApiOperation("查询可绑定的智能手环")
    //@PreAuthorize("@el.check('admin','dWatch:list')")
    public ResponseEntity<PageResult<DWatchDto>> getDWatchsAvaliableBind(DWatchQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(dWatchService.queryAll(criteria,pageable),HttpStatus.OK);
    }


    @GetMapping
    @Log("查询智能手环")
    @ApiOperation("查询智能手环")
    //@PreAuthorize("@el.check('admin','dWatch:list')")
    public ResponseEntity<PageResult<DWatchDto>> getDWatchs(DWatchQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(dWatchService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增智能手环")
    @ApiOperation("新增智能手环")
    //@PreAuthorize("@el.check('admin','dWatch:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody DWatch resources){
        resources.setCreateTime(new Date());
        return new ResponseEntity<>(dWatchService.save(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改智能手环")
    @ApiOperation("修改智能手环")
    //@PreAuthorize("@el.check('admin','dWatch:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody DWatch resources){

        DWatch existWatch = dWatchService.getById(resources.getId());
        if(StringUtils.isNotEmpty(resources.getImei())  &&  existWatch.getImei() != resources.getImei()){
                //IMEI发生改动要重置is_config
                resources.setIsConfig(0);
        }
        dWatchService.updateById(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除智能手环")
    @ApiOperation("删除智能手环")
    //@PreAuthorize("@el.check('admin','dWatch:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Long[] ids) {
        Arrays.asList(ids).forEach(id->{
            dWatchService.removeById(id);
        });
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @Log("初始化配置手表信息")
    @ApiOperation(value = "初始化配置手表信息")
   // @PutMapping(value = "/configWatch")
    public ResponseEntity configWatch(@RequestBody DWatch resources){
        try {
            //  TODO 1、api 同步配置信息
            // TODO 2、更新手表isConfig栏位为1
            if(StringUtils.isNotEmpty(resources.getImei())){
                yxUserService.syncWatchSleepConfig(resources.getImei());
            }
        }catch (Exception e){
            throw new BadRequestException("初始化失败！"+e.getMessage());
        }

        DWatch watch = new DWatch();
        watch.setIsConfig(1);
        watch.setId(resources.getId());
        dWatchService.updateById(watch);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


}
