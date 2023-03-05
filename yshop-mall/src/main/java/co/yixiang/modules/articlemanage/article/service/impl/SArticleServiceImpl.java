/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.articlemanage.article.service.impl;

import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.utils.QueryHelpPlus;
import co.yixiang.domain.PageResult;
import co.yixiang.dozer.service.IGenerator;
import co.yixiang.modules.articlemanage.article.domain.SArticle;
import co.yixiang.modules.articlemanage.article.service.SArticleService;
import co.yixiang.modules.articlemanage.article.service.dto.SArticleDto;
import co.yixiang.modules.articlemanage.article.service.dto.SArticleQueryCriteria;
import co.yixiang.modules.articlemanage.article.service.mapper.SArticleMapper;
import co.yixiang.utils.FileUtil;
import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// 默认不使用缓存
//import org.springframework.cache.annotation.CacheConfig;
//import org.springframework.cache.annotation.CacheEvict;
//import org.springframework.cache.annotation.Cacheable;
/**
* @author jiansun
* @date 2023-02-15
*/
@Service
@AllArgsConstructor
//@CacheConfig(cacheNames = "sArticle")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SArticleServiceImpl extends BaseServiceImpl<SArticleMapper, SArticle> implements SArticleService {

    private final IGenerator generator;

    @Override
    //@Cacheable
    public PageResult<SArticleDto> queryAll(SArticleQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<SArticle> page = new PageInfo<>(queryAll(criteria));
        return generator.convertPageInfo(page,SArticleDto.class);
    }


    @Override
    //@Cacheable
    public List<SArticle> queryAll(SArticleQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(SArticle.class, criteria));
    }


    @Override
    public void download(List<SArticleDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SArticleDto sArticle : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("标签", sArticle.getLabel());
            map.put("文章标题", sArticle.getTitle());
            map.put("文章类型", sArticle.getType());
            map.put("正文", sArticle.getContent());
            map.put("创建时间", sArticle.getCreateTime());
            map.put("是否发布0 否 1 是", sArticle.getStatus());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
