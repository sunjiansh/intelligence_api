/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.friends.service.impl;

import co.yixiang.modules.friends.domain.SFriendsGroup;
import co.yixiang.common.service.impl.BaseServiceImpl;
import lombok.AllArgsConstructor;
import co.yixiang.dozer.service.IGenerator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import co.yixiang.common.utils.QueryHelpPlus;
import co.yixiang.utils.ValidationUtil;
import co.yixiang.utils.FileUtil;
import co.yixiang.modules.friends.service.SFriendsGroupService;
import co.yixiang.modules.friends.service.dto.SFriendsGroupDto;
import co.yixiang.modules.friends.service.dto.SFriendsGroupQueryCriteria;
import co.yixiang.modules.friends.service.mapper.SFriendsGroupMapper;
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
* @date 2023-02-23
*/
@Service
@AllArgsConstructor
//@CacheConfig(cacheNames = "sFriendsGroup")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SFriendsGroupServiceImpl extends BaseServiceImpl<SFriendsGroupMapper, SFriendsGroup> implements SFriendsGroupService {

    private final IGenerator generator;

    @Override
    //@Cacheable
    public PageResult<SFriendsGroupDto> queryAll(SFriendsGroupQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<SFriendsGroup> page = new PageInfo<>(queryAll(criteria));
        return generator.convertPageInfo(page,SFriendsGroupDto.class);
    }


    @Override
    //@Cacheable
    public List<SFriendsGroup> queryAll(SFriendsGroupQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(SFriendsGroup.class, criteria));
    }


    @Override
    public void download(List<SFriendsGroupDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SFriendsGroupDto sFriendsGroup : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("发起人uid", sFriendsGroup.getMainUid());
            map.put("朋友uid", sFriendsGroup.getFriendUid());
            map.put("朋友称呼", sFriendsGroup.getLabel());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public List<Map> getriendListByMainId(Long uid) {
        return baseMapper.queryFriendListByMainId(uid);
    }
}
