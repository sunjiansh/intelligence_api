/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.servermanage.sosrecord.service.impl;

import co.yixiang.modules.servermanage.sosrecord.domain.SVipSosRecord;
import co.yixiang.common.service.impl.BaseServiceImpl;
import lombok.AllArgsConstructor;
import co.yixiang.dozer.service.IGenerator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import co.yixiang.common.utils.QueryHelpPlus;
import co.yixiang.utils.ValidationUtil;
import co.yixiang.utils.FileUtil;
import co.yixiang.modules.servermanage.sosrecord.service.SVipSosRecordService;
import co.yixiang.modules.servermanage.sosrecord.service.dto.SVipSosRecordDto;
import co.yixiang.modules.servermanage.sosrecord.service.dto.SVipSosRecordQueryCriteria;
import co.yixiang.modules.servermanage.sosrecord.service.mapper.SVipSosRecordMapper;
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
* @date 2023-02-11
*/
@Service
@AllArgsConstructor
//@CacheConfig(cacheNames = "sVipSosRecord")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SVipSosRecordServiceImpl extends BaseServiceImpl<SVipSosRecordMapper, SVipSosRecord> implements SVipSosRecordService {

    private final IGenerator generator;

    @Override
    //@Cacheable
    public PageResult<SVipSosRecordDto> queryAll(SVipSosRecordQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<SVipSosRecord> page = new PageInfo<>(queryAll(criteria));
        return generator.convertPageInfo(page,SVipSosRecordDto.class);
    }


    @Override
    //@Cacheable
    public List<SVipSosRecord> queryAll(SVipSosRecordQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(SVipSosRecord.class, criteria));
    }


    @Override
    public void download(List<SVipSosRecordDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SVipSosRecordDto sVipSosRecord : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("会员名称", sVipSosRecord.getMemberName());
            map.put("会员id", sVipSosRecord.getMemberId());
            map.put("会员手机号", sVipSosRecord.getMemberPhone());
            map.put("sos呼叫时间", sVipSosRecord.getSosTime());
            map.put("紧急联系号码", sVipSosRecord.getSosContact());
            map.put("会员截止日期", sVipSosRecord.getServiceEndTime());
            map.put("会员剩余天数", sVipSosRecord.getLastDays());
            map.put("创建时间", sVipSosRecord.getCreateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
