/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.device.mainunit.service.impl;

import co.yixiang.modules.device.mainunit.domain.DMailunit;
import co.yixiang.common.service.impl.BaseServiceImpl;
import lombok.AllArgsConstructor;
import co.yixiang.dozer.service.IGenerator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import co.yixiang.common.utils.QueryHelpPlus;
import co.yixiang.utils.ValidationUtil;
import co.yixiang.utils.FileUtil;
import co.yixiang.modules.device.mainunit.service.DMailunitService;
import co.yixiang.modules.device.mainunit.service.dto.DMailunitDto;
import co.yixiang.modules.device.mainunit.service.dto.DMailunitQueryCriteria;
import co.yixiang.modules.device.mainunit.service.mapper.DMailunitMapper;
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
* @date 2023-01-11
*/
@Service
@AllArgsConstructor
//@CacheConfig(cacheNames = "dMailunit")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class DMailunitServiceImpl extends BaseServiceImpl<DMailunitMapper, DMailunit> implements DMailunitService {

    private final IGenerator generator;

    @Override
    //@Cacheable
    public PageResult<DMailunitDto> queryAll(DMailunitQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<DMailunit> page = new PageInfo<>(queryAll(criteria));
        return generator.convertPageInfo(page,DMailunitDto.class);
    }


    @Override
    //@Cacheable
    public List<DMailunit> queryAll(DMailunitQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(DMailunit.class, criteria));
    }


    @Override
    public void download(List<DMailunitDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (DMailunitDto dMailunit : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("名称", dMailunit.getName());
            map.put("imei", dMailunit.getImei());
            map.put("型号", dMailunit.getModel());
            map.put("序列号", dMailunit.getSn());
            map.put("品牌", dMailunit.getBrand());
            map.put("备注", dMailunit.getMark());
            map.put("创建时间", dMailunit.getCreateTime());
            map.put("是否激活", dMailunit.getIsActive());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
