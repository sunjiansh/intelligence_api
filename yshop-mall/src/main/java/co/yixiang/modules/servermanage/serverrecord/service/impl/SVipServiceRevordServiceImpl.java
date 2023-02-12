/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.servermanage.serverrecord.service.impl;

import co.yixiang.modules.servermanage.serverrecord.domain.SVipServiceRevord;
import co.yixiang.common.service.impl.BaseServiceImpl;
import lombok.AllArgsConstructor;
import co.yixiang.dozer.service.IGenerator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import co.yixiang.common.utils.QueryHelpPlus;
import co.yixiang.utils.ValidationUtil;
import co.yixiang.utils.FileUtil;
import co.yixiang.modules.servermanage.serverrecord.service.SVipServiceRevordService;
import co.yixiang.modules.servermanage.serverrecord.service.dto.SVipServiceRevordDto;
import co.yixiang.modules.servermanage.serverrecord.service.dto.SVipServiceRevordQueryCriteria;
import co.yixiang.modules.servermanage.serverrecord.service.mapper.SVipServiceRevordMapper;
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
//@CacheConfig(cacheNames = "sVipServiceRevord")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SVipServiceRevordServiceImpl extends BaseServiceImpl<SVipServiceRevordMapper, SVipServiceRevord> implements SVipServiceRevordService {

    private final IGenerator generator;

    @Override
    //@Cacheable
    public PageResult<SVipServiceRevordDto> queryAll(SVipServiceRevordQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<SVipServiceRevord> page = new PageInfo<>(queryAll(criteria));
        return generator.convertPageInfo(page,SVipServiceRevordDto.class);
    }


    @Override
    //@Cacheable
    public List<SVipServiceRevord> queryAll(SVipServiceRevordQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(SVipServiceRevord.class, criteria));
    }


    @Override
    public void download(List<SVipServiceRevordDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SVipServiceRevordDto sVipServiceRevord : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("客服人员", sVipServiceRevord.getServerPerson());
            map.put("问题类型", sVipServiceRevord.getQuestionType());
            map.put("处理记录", sVipServiceRevord.getHandleRecord());
            map.put("服务开始时间", sVipServiceRevord.getServerStartTime());
            map.put("服务结束书简", sVipServiceRevord.getServerEndTime());
            map.put("创建时间", sVipServiceRevord.getCreateTime());
            map.put("被服务人员id", sVipServiceRevord.getMemberId());
            map.put("被服务人员名称", sVipServiceRevord.getMemberName());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
