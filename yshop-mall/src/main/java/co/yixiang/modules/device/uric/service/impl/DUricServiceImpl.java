/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.device.uric.service.impl;

import co.yixiang.modules.device.uric.domain.DUric;
import co.yixiang.common.service.impl.BaseServiceImpl;
import lombok.AllArgsConstructor;
import co.yixiang.dozer.service.IGenerator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import co.yixiang.common.utils.QueryHelpPlus;
import co.yixiang.utils.ValidationUtil;
import co.yixiang.utils.FileUtil;
import co.yixiang.modules.device.uric.service.DUricService;
import co.yixiang.modules.device.uric.service.dto.DUricDto;
import co.yixiang.modules.device.uric.service.dto.DUricQueryCriteria;
import co.yixiang.modules.device.uric.service.mapper.DUricMapper;
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
//@CacheConfig(cacheNames = "dUric")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class DUricServiceImpl extends BaseServiceImpl<DUricMapper, DUric> implements DUricService {

    private final IGenerator generator;

    @Override
    //@Cacheable
    public PageResult<DUricDto> queryAll(DUricQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<DUric> page = new PageInfo<>(queryAll(criteria));
        return generator.convertPageInfo(page,DUricDto.class);
    }


    @Override
    //@Cacheable
    public List<DUric> queryAll(DUricQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(DUric.class, criteria));
    }

    @Override
    public List<DUricDto> queryAllBindedDeviceByMid(Long mid) {
        return baseMapper.findAllBindedDeviceByMid(mid);
    }

    @Override
    public void download(List<DUricDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (DUricDto dUric : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("名称", dUric.getName());
            map.put("imei", dUric.getImei());
            map.put("型号", dUric.getModel());
            map.put("序列号", dUric.getSn());
            map.put("品牌", dUric.getBrand());
            map.put("备注", dUric.getMark());
            map.put("创建时间", dUric.getCreateTime());
            map.put("是否激活", dUric.getIsActive());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
