/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.device.balancedatarecords.service.impl;

import co.yixiang.modules.device.balancedatarecords.domain.DBalanceDataRecords;
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
import co.yixiang.modules.device.balancedatarecords.service.DBalanceDataRecordsService;
import co.yixiang.modules.device.balancedatarecords.service.dto.DBalanceDataRecordsDto;
import co.yixiang.modules.device.balancedatarecords.service.dto.DBalanceDataRecordsQueryCriteria;
import co.yixiang.modules.device.balancedatarecords.service.mapper.DBalanceDataRecordsMapper;
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

import java.util.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import co.yixiang.domain.PageResult;
/**
* @author jiansun
* @date 2023-03-04
*/
@Service
@AllArgsConstructor
//@CacheConfig(cacheNames = "dBalanceDataRecords")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class DBalanceDataRecordsServiceImpl extends BaseServiceImpl<DBalanceDataRecordsMapper, DBalanceDataRecords> implements DBalanceDataRecordsService {

    private final IGenerator generator;

    @Override
    //@Cacheable
    public PageResult<DBalanceDataRecordsDto> queryAll(DBalanceDataRecordsQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<DBalanceDataRecords> page = new PageInfo<>(queryAll(criteria));
        return generator.convertPageInfo(page,DBalanceDataRecordsDto.class);
    }


    @Override
    //@Cacheable
    public List<DBalanceDataRecords> queryAll(DBalanceDataRecordsQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(DBalanceDataRecords.class, criteria));
    }

    @Override
    public void saveEntity(DBalanceDataRecords entity, String imei) {

        //将数据发送到mqtt
        JSONObject msg = new JSONObject();
        msg.put("userId",entity.getUid());
        msg.put("bodyWeight",entity.getBodyWeight());
        msg.put("bodyBmi",entity.getBodyBmi());
        msg.put("time",new Date());
        msg.put("action","BALANCE");
        try {
            ServerMQTT.publishTerminalData(imei,msg);
        }catch (Exception e){
            e.printStackTrace();
        }

        baseMapper.insert(entity);
    }

    @Override
    public void download(List<DBalanceDataRecordsDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (DBalanceDataRecordsDto dBalanceDataRecords : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("用户id", dBalanceDataRecords.getUid());
            map.put("阻抗", dBalanceDataRecords.getImpedance());
            map.put("kg", dBalanceDataRecords.getBodyWeight());
            map.put("kg", dBalanceDataRecords.getBodyBmi());
            map.put("体脂率 %", dBalanceDataRecords.getBodyFatPercentage());
            map.put("皮下脂肪（%）", dBalanceDataRecords.getSubcutaneousFat());
            map.put("内脏脂肪等级", dBalanceDataRecords.getVisceralFatGrade());
            map.put("肌肉量 kg", dBalanceDataRecords.getMuscleMass());
            map.put("基础代谢 kcal", dBalanceDataRecords.getBasalMetabolism());
            map.put("骨量 kg", dBalanceDataRecords.getBoneMass());
            map.put("水分 %", dBalanceDataRecords.getWaterContent());
            map.put("身体年龄 岁", dBalanceDataRecords.getPhysicalAge());
            map.put("蛋白率 %", dBalanceDataRecords.getProteinPercentage());
            map.put("设备序列号", dBalanceDataRecords.getSn());
            map.put("创建时间", dBalanceDataRecords.getCreateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
