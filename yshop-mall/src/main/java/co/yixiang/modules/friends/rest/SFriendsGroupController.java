/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.friends.rest;
import java.util.Arrays;
import co.yixiang.dozer.service.IGenerator;
import lombok.AllArgsConstructor;
import co.yixiang.modules.logging.aop.log.Log;
import co.yixiang.modules.friends.domain.SFriendsGroup;
import co.yixiang.modules.friends.service.SFriendsGroupService;
import co.yixiang.modules.friends.service.dto.SFriendsGroupQueryCriteria;
import co.yixiang.modules.friends.service.dto.SFriendsGroupDto;
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
* @date 2023-02-23
*/
@AllArgsConstructor
@Api(tags = "亲友群组管理")
@RestController
@RequestMapping("/api/sFriendsGroup")
public class SFriendsGroupController {

    private final SFriendsGroupService sFriendsGroupService;
    private final IGenerator generator;


    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    //@PreAuthorize("@el.check('admin','sFriendsGroup:list')")
    public void download(HttpServletResponse response, SFriendsGroupQueryCriteria criteria) throws IOException {
        sFriendsGroupService.download(generator.convert(sFriendsGroupService.queryAll(criteria), SFriendsGroupDto.class), response);
    }

    @GetMapping
    @Log("查询亲友群组")
    @ApiOperation("查询亲友群组")
    //@PreAuthorize("@el.check('admin','sFriendsGroup:list')")
    public ResponseEntity<PageResult<SFriendsGroupDto>> getSFriendsGroups(SFriendsGroupQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(sFriendsGroupService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增亲友群组")
    @ApiOperation("新增亲友群组")
    //@PreAuthorize("@el.check('admin','sFriendsGroup:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody SFriendsGroup resources){
        return new ResponseEntity<>(sFriendsGroupService.save(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改亲友群组")
    @ApiOperation("修改亲友群组")
    //@PreAuthorize("@el.check('admin','sFriendsGroup:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody SFriendsGroup resources){
        sFriendsGroupService.updateById(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除亲友群组")
    @ApiOperation("删除亲友群组")
    //@PreAuthorize("@el.check('admin','sFriendsGroup:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Long[] ids) {
        Arrays.asList(ids).forEach(id->{
            sFriendsGroupService.removeById(id);
        });
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
