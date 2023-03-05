/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.device.bloodsugardatarecords.service.impl;

import co.yixiang.modules.device.bloodsugardatarecords.domain.DBloodSugarDataRecords;
import co.yixiang.common.service.impl.BaseServiceImpl;
import lombok.AllArgsConstructor;
import co.yixiang.dozer.service.IGenerator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import co.yixiang.common.utils.QueryHelpPlus;
import co.yixiang.utils.ValidationUtil;
import co.yixiang.utils.FileUtil;
import co.yixiang.modules.device.bloodsugardatarecords.service.DBloodSugarDataRecordsService;
import co.yixiang.modules.device.bloodsugardatarecords.service.dto.DBloodSugarDataRecordsDto;
import co.yixiang.modules.device.bloodsugardatarecords.service.dto.DBloodSugarDataRecordsQueryCriteria;
import co.yixiang.modules.device.bloodsugardatarecords.service.mapper.DBloodSugarDataRecordsMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
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
* @date 2023-03-04
*/
@Service
@AllArgsConstructor
//@CacheConfig(cacheNames = "dBloodSugarDataRecords")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class DBloodSugarDataRecordsServiceImpl extends BaseServiceImpl<DBloodSugarDataRecordsMapper, DBloodSugarDataRecords> implements DBloodSugarDataRecordsService {

    private final IGenerator generator;

    @Override
    //@Cacheable
    public PageResult<DBloodSugarDataRecordsDto> queryAll(DBloodSugarDataRecordsQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<DBloodSugarDataRecords> page = new PageInfo<>(queryAll(criteria));
        return generator.convertPageInfo(page,DBloodSugarDataRecordsDto.class);
    }


    @Override
    //@Cacheable
    public List<DBloodSugarDataRecords> queryAll(DBloodSugarDataRecordsQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(DBloodSugarDataRecords.class, criteria));
    }


    @Override
    public void download(List<DBloodSugarDataRecordsDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (DBloodSugarDataRecordsDto dBloodSugarDataRecords : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("用户id", dBloodSugarDataRecords.getUid());
            map.put("血糖", dBloodSugarDataRecords.getBloodSugar());
            map.put("血氧饱和度", dBloodSugarDataRecords.getBloodOxygenSaturation());
            map.put("血红蛋白浓度", dBloodSugarDataRecords.getHemoglobinConcentration());
            map.put("血流速度，3位整数", dBloodSugarDataRecords.getBloodFlowVelocity());
            map.put("环境温度", dBloodSugarDataRecords.getEnvironmentTemperature());
            map.put("环境湿度", dBloodSugarDataRecords.getEnvironmentHumidity());
            map.put("体表温度", dBloodSugarDataRecords.getBodyTemperature());
            map.put("体表湿度", dBloodSugarDataRecords.getBodySurfaceHumidity());
            map.put("脉搏数，3位整数", dBloodSugarDataRecords.getPulseRate());
            map.put("电量状态（%）", dBloodSugarDataRecords.getBattery());
            map.put("创建时间", dBloodSugarDataRecords.getCreateTime());
            map.put("设备序列号", dBloodSugarDataRecords.getSn());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
