/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.device.balance.service.impl;

import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.utils.QueryHelpPlus;
import co.yixiang.domain.PageResult;
import co.yixiang.dozer.service.IGenerator;
import co.yixiang.modules.device.balance.domain.DBalance;
import co.yixiang.modules.device.balance.domain.ViewDBalanceBindAvailable;
import co.yixiang.modules.device.balance.service.DBalanceService;
import co.yixiang.modules.device.balance.service.dto.DBalanceDto;
import co.yixiang.modules.device.balance.service.dto.DBalanceQueryCriteria;
import co.yixiang.modules.device.balance.service.mapper.DBalanceMapper;
import co.yixiang.modules.device.balance.service.mapper.ViewDBalanceBindAvailableMapper;
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
* @date 2023-01-11
*/
@Service
@AllArgsConstructor
//@CacheConfig(cacheNames = "dBalance")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class DBalanceServiceImpl extends BaseServiceImpl<DBalanceMapper, DBalance> implements DBalanceService {

    private final IGenerator generator;
    private final ViewDBalanceBindAvailableMapper viewDBalanceBindAvailableMapper;

    @Override
    //@Cacheable
    public PageResult<DBalanceDto> queryAll(DBalanceQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<DBalance> page = new PageInfo<>(queryAll(criteria));
        return generator.convertPageInfo(page,DBalanceDto.class);
    }

    @Override
    public PageResult<ViewDBalanceBindAvailable> queryAllDBlanceBindAvailablePage(DBalanceQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<ViewDBalanceBindAvailable> page = new PageInfo<>(viewDBalanceBindAvailableMapper.selectList(QueryHelpPlus.getPredicate(ViewDBalanceBindAvailable.class, criteria)));
        return generator.convertPageInfo(page,ViewDBalanceBindAvailable.class);
    }

    @Override
    //@Cacheable
    public List<DBalance> queryAll(DBalanceQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(DBalance.class, criteria));
    }


    @Override
    public List<DBalanceDto> queryAllBindedDeviceByMid(Long mid) {
        return baseMapper.findAllBindedDeviceByMid(mid);
    }

    @Override
    public void download(List<DBalanceDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (DBalanceDto dBalance : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("蓝牙名称", dBalance.getBlName());
            map.put("蓝牙ID", dBalance.getBlId());
            map.put("品牌", dBalance.getBrand());
            map.put("型号", dBalance.getModel());
            map.put("备注", dBalance.getMark());
            map.put("创建时间", dBalance.getCreateTime());
            map.put("是否激活", dBalance.getIsActive());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
