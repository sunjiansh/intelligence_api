/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.watch.service.impl;

import co.yixiang.modules.watch.domain.DWatch;
import co.yixiang.common.service.impl.BaseServiceImpl;
import lombok.AllArgsConstructor;
import co.yixiang.dozer.service.IGenerator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import co.yixiang.common.utils.QueryHelpPlus;
import co.yixiang.utils.ValidationUtil;
import co.yixiang.utils.FileUtil;
import co.yixiang.modules.watch.service.DWatchService;
import co.yixiang.modules.watch.service.dto.DWatchDto;
import co.yixiang.modules.watch.service.dto.DWatchQueryCriteria;
import co.yixiang.modules.watch.service.mapper.DWatchMapper;
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
* @date 2023-01-10
*/
@Service
@AllArgsConstructor
//@CacheConfig(cacheNames = "dWatch")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class DWatchServiceImpl extends BaseServiceImpl<DWatchMapper, DWatch> implements DWatchService {

    private final IGenerator generator;

    @Override
    //@Cacheable
    public PageResult<DWatchDto> queryAll(DWatchQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<DWatch> page = new PageInfo<>(queryAll(criteria));
        return generator.convertPageInfo(page,DWatchDto.class);
    }


    @Override
    //@Cacheable
    public List<DWatch> queryAll(DWatchQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(DWatch.class, criteria));
    }


    @Override
    public void download(List<DWatchDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (DWatchDto dWatch : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("名称", dWatch.getName());
            map.put("imei", dWatch.getImei());
            map.put("型号", dWatch.getModel());
            map.put("序列号", dWatch.getSn());
            map.put("品牌", dWatch.getBrand());
            map.put("备注", dWatch.getMark());
            map.put("创建时间", dWatch.getCreateTime());
            map.put("是否激活", dWatch.getIsActive());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
