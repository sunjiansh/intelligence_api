/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.blood.service.impl;

import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.utils.QueryHelpPlus;
import co.yixiang.domain.PageResult;
import co.yixiang.dozer.service.IGenerator;
import co.yixiang.modules.blood.domain.DBlood;
import co.yixiang.modules.blood.service.DBloodService;
import co.yixiang.modules.blood.service.dto.DBloodDto;
import co.yixiang.modules.blood.service.dto.DBloodQueryCriteria;
import co.yixiang.modules.blood.service.mapper.DBloodMapper;
import co.yixiang.modules.device.blood.domain.ViewDBloodBindAvailable;
import co.yixiang.modules.device.blood.service.mapper.ViewDBloodBindAvailableMapper;
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
* @date 2023-01-10
*/
@Service
@AllArgsConstructor
//@CacheConfig(cacheNames = "dBlood")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class DBloodServiceImpl extends BaseServiceImpl<DBloodMapper, DBlood> implements DBloodService {

    private final IGenerator generator;
    private final ViewDBloodBindAvailableMapper viewDBloodBindAvailableMapper;

    @Override
    //@Cacheable
    public PageResult<DBloodDto> queryAll(DBloodQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<DBlood> page = new PageInfo<>(queryAll(criteria));
        return generator.convertPageInfo(page,DBloodDto.class);
    }

    @Override
    public PageResult<ViewDBloodBindAvailable> queryAllDBloodBindAvailablePage(DBloodQueryCriteria criteria, Pageable pageable) {
         getPage(pageable);
         PageInfo<ViewDBloodBindAvailable> page = new PageInfo<>(viewDBloodBindAvailableMapper.selectList(QueryHelpPlus.getPredicate(ViewDBloodBindAvailable.class, criteria)));
        return generator.convertPageInfo(page,ViewDBloodBindAvailable.class);
    }

    @Override
    //@Cacheable
    public List<DBlood> queryAll(DBloodQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(DBlood.class, criteria));
    }

    @Override
    public List<DBloodDto> queryAllBindedDeviceByMid(Long mid) {
        return baseMapper.findAllBindedDeviceByMid(mid);
    }

    @Override
    public void download(List<DBloodDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (DBloodDto dBlood : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("蓝牙名称", dBlood.getBlName());
            map.put("型号", dBlood.getModel());
            map.put("蓝牙ID", dBlood.getBlId());
            map.put("品牌", dBlood.getBrand());
            map.put("备注", dBlood.getMark());
            map.put("创建时间", dBlood.getCreateTime());
            map.put("是否激活", dBlood.getIsActive());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
