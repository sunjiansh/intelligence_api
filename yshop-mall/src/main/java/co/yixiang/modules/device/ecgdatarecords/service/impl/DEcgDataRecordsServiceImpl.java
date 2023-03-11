/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.device.ecgdatarecords.service.impl;

import co.yixiang.modules.device.ecgdatarecords.domain.DEcgDataRecords;
import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.modules.device.mqtt.ServerMQTT;
import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import co.yixiang.dozer.service.IGenerator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import co.yixiang.common.utils.QueryHelpPlus;
import co.yixiang.utils.ValidationUtil;
import co.yixiang.utils.FileUtil;
import co.yixiang.modules.device.ecgdatarecords.service.DEcgDataRecordsService;
import co.yixiang.modules.device.ecgdatarecords.service.dto.DEcgDataRecordsDto;
import co.yixiang.modules.device.ecgdatarecords.service.dto.DEcgDataRecordsQueryCriteria;
import co.yixiang.modules.device.ecgdatarecords.service.mapper.DEcgDataRecordsMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
// 默认不使用缓存
//import org.springframework.cache.annotation.CacheConfig;
//import org.springframework.cache.annotation.CacheEvict;
//import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import co.yixiang.domain.PageResult;
/**
* @author jiansun
* @date 2023-03-06
*/
@Service
@AllArgsConstructor
//@CacheConfig(cacheNames = "dEcgDataRecords")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class DEcgDataRecordsServiceImpl extends BaseServiceImpl<DEcgDataRecordsMapper, DEcgDataRecords> implements DEcgDataRecordsService {

    private final IGenerator generator;

    @Override
    //@Cacheable
    public PageResult<DEcgDataRecordsDto> queryAll(DEcgDataRecordsQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<DEcgDataRecords> page = new PageInfo<>(queryAll(criteria));
        return generator.convertPageInfo(page,DEcgDataRecordsDto.class);
    }


    @Override
    //@Cacheable
    public List<DEcgDataRecords> queryAll(DEcgDataRecordsQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(DEcgDataRecords.class, criteria));
    }


    @Override
    public void download(List<DEcgDataRecordsDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (DEcgDataRecordsDto dEcgDataRecords : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("用户id", dEcgDataRecords.getUid());
            map.put("平均心率", dEcgDataRecords.getAverageHeartRate());
            map.put("ai报告地址", dEcgDataRecords.getReportUrl());
            map.put(" createTime",  dEcgDataRecords.getCreateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }


    @Override
    public void saveEntity(DEcgDataRecords entity, String imei) {
        //将数据发送到mqtt
        JSONObject msg = new JSONObject();
        msg.put("userId",entity.getUid());
        msg.put("averageHeartRate",entity.getAverageHeartRate());
        msg.put("time",new Date());
        msg.put("action","ECGHR");
        try {
            ServerMQTT.publishTerminalData(imei,msg);
        }catch (Exception e){
            e.printStackTrace();
        }

        baseMapper.insert(entity);
    }
}
