/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.monotormanage.geography.service.impl;

import co.yixiang.modules.monotormanage.geography.domain.SGeography;
import co.yixiang.common.service.impl.BaseServiceImpl;
import lombok.AllArgsConstructor;
import co.yixiang.dozer.service.IGenerator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import co.yixiang.common.utils.QueryHelpPlus;
import co.yixiang.utils.ValidationUtil;
import co.yixiang.utils.FileUtil;
import co.yixiang.modules.monotormanage.geography.service.SGeographyService;
import co.yixiang.modules.monotormanage.geography.service.dto.SGeographyDto;
import co.yixiang.modules.monotormanage.geography.service.dto.SGeographyQueryCriteria;
import co.yixiang.modules.monotormanage.geography.service.mapper.SGeographyMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
// 默认不使用缓存
//import org.springframework.cache.annotation.CacheConfig;
//import org.springframework.cache.annotation.CacheEvict;
//import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import co.yixiang.domain.PageResult;
/**
* @author jiansun
* @date 2023-02-12
*/
@Service
@AllArgsConstructor
//@CacheConfig(cacheNames = "sGeography")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SGeographyServiceImpl extends BaseServiceImpl<SGeographyMapper, SGeography> implements SGeographyService {

    private final IGenerator generator;

    @Override
    //@Cacheable
    public PageResult<SGeographyDto> queryAll(SGeographyQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<SGeography> page = new PageInfo<>(queryAll(criteria));
        return generator.convertPageInfo(page,SGeographyDto.class);
    }


    @Override
    //@Cacheable
    public List<SGeography> queryAll(SGeographyQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(SGeography.class, criteria));
    }


    @Override
    public void download(List<SGeographyDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SGeographyDto sGeography : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("手环IMEI", sGeography.getImei());
            map.put("围栏名称", sGeography.getName());
            map.put("围栏地址", sGeography.getAddress());
            map.put("围栏半径", sGeography.getRegionRange());
            map.put("经度", sGeography.getLon());
            map.put("纬度", sGeography.getLat());
            map.put("创建时间", sGeography.getCreateTime());
            map.put("更新时间", sGeography.getUpdateTime());
            map.put("会员ID", sGeography.getMemberId());
            map.put("会员人员名称", sGeography.getMemberName());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
