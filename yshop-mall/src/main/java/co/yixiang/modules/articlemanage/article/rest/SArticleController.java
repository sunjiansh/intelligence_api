/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.articlemanage.article.rest;
import java.util.Arrays;
import co.yixiang.dozer.service.IGenerator;
import lombok.AllArgsConstructor;
import co.yixiang.modules.logging.aop.log.Log;
import co.yixiang.modules.articlemanage.article.domain.SArticle;
import co.yixiang.modules.articlemanage.article.service.SArticleService;
import co.yixiang.modules.articlemanage.article.service.dto.SArticleQueryCriteria;
import co.yixiang.modules.articlemanage.article.service.dto.SArticleDto;
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
* @date 2023-02-15
*/
@AllArgsConstructor
@Api(tags = "文章管理管理")
@RestController
@RequestMapping("/api/sArticle")
public class SArticleController {

    private final SArticleService sArticleService;
    private final IGenerator generator;


    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    //@PreAuthorize("@el.check('admin','sArticle:list')")
    public void download(HttpServletResponse response, SArticleQueryCriteria criteria) throws IOException {
        sArticleService.download(generator.convert(sArticleService.queryAll(criteria), SArticleDto.class), response);
    }

    @GetMapping(value = "/info/{id}")
    @Log("根据ID查询文章")
    @ApiOperation("根据ID查询文章")
    public ResponseEntity<SArticle> info(@PathVariable("id") Long id){
        return new ResponseEntity<SArticle>(sArticleService.getById(id),HttpStatus.OK);
    }

    @GetMapping
    @Log("查询文章管理")
    @ApiOperation("查询文章管理")
    //@PreAuthorize("@el.check('admin','sArticle:list')")
    public ResponseEntity<PageResult<SArticleDto>> getSArticles(SArticleQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(sArticleService.queryAll(criteria,pageable),HttpStatus.OK);
    }


    @PostMapping
    @Log("新增文章管理")
    @ApiOperation("新增文章管理")
    //@PreAuthorize("@el.check('admin','sArticle:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody SArticle resources){
        return new ResponseEntity<>(sArticleService.save(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改文章管理")
    @ApiOperation("修改文章管理")
    //@PreAuthorize("@el.check('admin','sArticle:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody SArticle resources){
        sArticleService.updateById(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除文章管理")
    @ApiOperation("删除文章管理")
    //@PreAuthorize("@el.check('admin','sArticle:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Long[] ids) {
        Arrays.asList(ids).forEach(id->{
            sArticleService.removeById(id);
        });
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
