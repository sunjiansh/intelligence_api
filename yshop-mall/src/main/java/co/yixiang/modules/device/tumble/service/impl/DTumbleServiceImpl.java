/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.device.tumble.service.impl;

import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.utils.QueryHelpPlus;
import co.yixiang.domain.PageResult;
import co.yixiang.dozer.service.IGenerator;
import co.yixiang.modules.device.tumble.domain.DTumble;
import co.yixiang.modules.device.tumble.domain.ViewDTumbleBindAvailable;
import co.yixiang.modules.device.tumble.service.DTumbleService;
import co.yixiang.modules.device.tumble.service.dto.DTumbleDto;
import co.yixiang.modules.device.tumble.service.dto.DTumbleQueryCriteria;
import co.yixiang.modules.device.tumble.service.mapper.DTumbleMapper;
import co.yixiang.modules.device.tumble.service.mapper.ViewDTumbleBindAvailableMapper;
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
//@CacheConfig(cacheNames = "dTumble")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class DTumbleServiceImpl extends BaseServiceImpl<DTumbleMapper, DTumble> implements DTumbleService {

    private final IGenerator generator;
    private final ViewDTumbleBindAvailableMapper viewDTumbleBindAvailableMapper;

    @Override
    //@Cacheable
    public PageResult<DTumbleDto> queryAll(DTumbleQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<DTumble> page = new PageInfo<>(queryAll(criteria));
        return generator.convertPageInfo(page,DTumbleDto.class);
    }


    @Override
    //@Cacheable
    public List<DTumble> queryAll(DTumbleQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(DTumble.class, criteria));
    }

    @Override
    public PageResult<ViewDTumbleBindAvailable> queryAllDTumbleBindAvailablePage(DTumbleQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<ViewDTumbleBindAvailable> page = new PageInfo<>(viewDTumbleBindAvailableMapper.selectList(QueryHelpPlus.getPredicate(ViewDTumbleBindAvailable.class, criteria)));
        return generator.convertPageInfo(page,ViewDTumbleBindAvailable.class);
    }

    @Override
    public List<DTumbleDto> queryAllBindedDeviceByMid(Long mid) {
        return baseMapper.findAllBindedDeviceByMid(mid);
    }

    @Override
    public void download(List<DTumbleDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (DTumbleDto dTumble : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("名称", dTumble.getName());
            map.put("imei", dTumble.getImei());
            map.put("型号", dTumble.getModel());
            map.put("序列号", dTumble.getSn());
            map.put("品牌", dTumble.getBrand());
            map.put("备注", dTumble.getMark());
            map.put("创建时间", dTumble.getCreateTime());
            map.put("是否激活", dTumble.getIsActive());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
