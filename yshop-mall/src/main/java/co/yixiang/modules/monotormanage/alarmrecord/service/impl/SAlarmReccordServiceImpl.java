/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.monotormanage.alarmrecord.service.impl;

import co.yixiang.modules.monotormanage.alarmrecord.domain.SAlarmReccord;
import co.yixiang.common.service.impl.BaseServiceImpl;
import lombok.AllArgsConstructor;
import co.yixiang.dozer.service.IGenerator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import co.yixiang.common.utils.QueryHelpPlus;
import co.yixiang.utils.ValidationUtil;
import co.yixiang.utils.FileUtil;
import co.yixiang.modules.monotormanage.alarmrecord.service.SAlarmReccordService;
import co.yixiang.modules.monotormanage.alarmrecord.service.dto.SAlarmReccordDto;
import co.yixiang.modules.monotormanage.alarmrecord.service.dto.SAlarmReccordQueryCriteria;
import co.yixiang.modules.monotormanage.alarmrecord.service.mapper.SAlarmReccordMapper;
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
* @date 2023-02-13
*/
@Service
@AllArgsConstructor
//@CacheConfig(cacheNames = "sAlarmReccord")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SAlarmReccordServiceImpl extends BaseServiceImpl<SAlarmReccordMapper, SAlarmReccord> implements SAlarmReccordService {

    private final IGenerator generator;

    @Override
    //@Cacheable
    public PageResult<SAlarmReccordDto> queryAll(SAlarmReccordQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<SAlarmReccord> page = new PageInfo<>(queryAll(criteria));
        return generator.convertPageInfo(page,SAlarmReccordDto.class);
    }


    @Override
    //@Cacheable
    public List<SAlarmReccord> queryAll(SAlarmReccordQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(SAlarmReccord.class, criteria));
    }


    @Override
    public void download(List<SAlarmReccordDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SAlarmReccordDto sAlarmReccord : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("报警类型", sAlarmReccord.getAlarmType());
            map.put("报警内容", sAlarmReccord.getContent());
            map.put("手机号", sAlarmReccord.getPhone());
            map.put("创建时间", sAlarmReccord.getCreateTime());
            map.put("手环imei", sAlarmReccord.getImei());
            map.put("会员姓名", sAlarmReccord.getMenberName());
            map.put("会员姓id", sAlarmReccord.getMemberId());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
