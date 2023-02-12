/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.device.ecg.service.impl;

import co.yixiang.modules.device.balance.domain.ViewDBalanceBindAvailable;
import co.yixiang.modules.device.ecg.domain.DEcg;
import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.modules.device.ecg.domain.ViewDEcgBindAvailable;
import co.yixiang.modules.device.ecg.service.mapper.ViewDEcgBindAvailableMapper;
import lombok.AllArgsConstructor;
import co.yixiang.dozer.service.IGenerator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import co.yixiang.common.utils.QueryHelpPlus;
import co.yixiang.utils.ValidationUtil;
import co.yixiang.utils.FileUtil;
import co.yixiang.modules.device.ecg.service.DEcgService;
import co.yixiang.modules.device.ecg.service.dto.DEcgDto;
import co.yixiang.modules.device.ecg.service.dto.DEcgQueryCriteria;
import co.yixiang.modules.device.ecg.service.mapper.DEcgMapper;
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
//@CacheConfig(cacheNames = "dEcg")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class DEcgServiceImpl extends BaseServiceImpl<DEcgMapper, DEcg> implements DEcgService {

    private final IGenerator generator;
    private final ViewDEcgBindAvailableMapper viewDEcgBindAvailableMapper;

    @Override
    //@Cacheable
    public PageResult<DEcgDto> queryAll(DEcgQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<DEcg> page = new PageInfo<>(queryAll(criteria));
        return generator.convertPageInfo(page,DEcgDto.class);
    }

    @Override
    public PageResult<ViewDEcgBindAvailable> queryAllDEcgBindAvailablePage(DEcgQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<ViewDEcgBindAvailable> page = new PageInfo<>(viewDEcgBindAvailableMapper.selectList(QueryHelpPlus.getPredicate(ViewDEcgBindAvailable.class, criteria)));
        return generator.convertPageInfo(page,ViewDEcgBindAvailable.class);
    }

    @Override
    //@Cacheable
    public List<DEcg> queryAll(DEcgQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(DEcg.class, criteria));
    }

    @Override
    public List<DEcgDto> queryAllBindedDeviceByMid(Long mid) {
        return baseMapper.findAllBindedDeviceByMid(mid);
    }


    @Override
    public void download(List<DEcgDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (DEcgDto dEcg : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("名称", dEcg.getName());
            map.put("型号", dEcg.getModel());
            map.put("序列号", dEcg.getSn());
            map.put("品牌", dEcg.getBrand());
            map.put("备注", dEcg.getMark());
            map.put("创建时间", dEcg.getCreateTime());
            map.put("是否激活", dEcg.getIsActive());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
