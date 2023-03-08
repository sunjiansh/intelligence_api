/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.device.tumbledatarecords.service.impl;

import co.yixiang.common.enums.TumbleCmdEnum;
import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.util.APPdataUtil;
import co.yixiang.common.util.BASE64;
import co.yixiang.common.util.RedisContans;
import co.yixiang.common.utils.QueryHelpPlus;
import co.yixiang.domain.PageResult;
import co.yixiang.dozer.service.IGenerator;
import co.yixiang.modules.device.mqtt.ServerMQTT;
import co.yixiang.modules.device.tumbledatarecords.domain.DTumbleDataRecords;
import co.yixiang.modules.device.tumbledatarecords.service.DTumbleDataRecordsService;
import co.yixiang.modules.device.tumbledatarecords.service.dto.DTumbleDataRecordsDto;
import co.yixiang.modules.device.tumbledatarecords.service.dto.DTumbleDataRecordsQueryCriteria;
import co.yixiang.modules.device.tumbledatarecords.service.mapper.DTumbleDataRecordsMapper;
import co.yixiang.modules.user.domain.YxUser;
import co.yixiang.modules.user.service.mapper.UserMapper;
import co.yixiang.utils.FileUtil;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

// 默认不使用缓存
//import org.springframework.cache.annotation.CacheConfig;
//import org.springframework.cache.annotation.CacheEvict;
//import org.springframework.cache.annotation.Cacheable;

/**
* @author jiansun
* @date 2023-02-02
*/
@Slf4j
@Service
@AllArgsConstructor
//@CacheConfig(cacheNames = "dTumbleDataRecords")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class DTumbleDataRecordsServiceImpl extends BaseServiceImpl<DTumbleDataRecordsMapper, DTumbleDataRecords> implements DTumbleDataRecordsService {

    private final IGenerator generator;
    private final UserMapper yxUserMapper;

    @Override
    //@Cacheable
    public PageResult<DTumbleDataRecordsDto> queryAll(DTumbleDataRecordsQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<DTumbleDataRecords> page = new PageInfo<>(queryAll(criteria));
        return generator.convertPageInfo(page,DTumbleDataRecordsDto.class);
    }


    @Override
    //@Cacheable
    public List<DTumbleDataRecords> queryAll(DTumbleDataRecordsQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(DTumbleDataRecords.class, criteria));
    }


    @Override
    public void download(List<DTumbleDataRecordsDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (DTumbleDataRecordsDto dTumbleDataRecords : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("报文", dTumbleDataRecords.getContent());
            map.put("指令", dTumbleDataRecords.getCmd());
            map.put("数据", dTumbleDataRecords.getData());
            map.put("指令名称", dTumbleDataRecords.getCmdName());
            map.put("创建时间", dTumbleDataRecords.getCreateTime());
            map.put("数据上报时间", dTumbleDataRecords.getPushTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

}
