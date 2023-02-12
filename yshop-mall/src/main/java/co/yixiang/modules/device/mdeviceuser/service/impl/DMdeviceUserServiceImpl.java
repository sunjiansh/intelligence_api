/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.device.mdeviceuser.service.impl;

import co.yixiang.modules.device.apiservice.DWatchUricApiService;
import co.yixiang.modules.device.mdevicedevice.domain.DMdeviceDevice;
import co.yixiang.modules.device.mdevicedevice.service.mapper.DMdeviceDeviceMapper;
import co.yixiang.modules.device.mdeviceuser.domain.DMdeviceUser;
import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.modules.device.uric.domain.DUric;
import co.yixiang.modules.device.uric.service.mapper.DUricMapper;
import co.yixiang.modules.user.domain.YxUser;
import co.yixiang.utils.StringUtils;
import lombok.AllArgsConstructor;
import co.yixiang.dozer.service.IGenerator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import co.yixiang.common.utils.QueryHelpPlus;
import co.yixiang.utils.ValidationUtil;
import co.yixiang.utils.FileUtil;
import co.yixiang.modules.device.mdeviceuser.service.DMdeviceUserService;
import co.yixiang.modules.device.mdeviceuser.service.dto.DMdeviceUserDto;
import co.yixiang.modules.device.mdeviceuser.service.dto.DMdeviceUserQueryCriteria;
import co.yixiang.modules.device.mdeviceuser.service.mapper.DMdeviceUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
* @date 2023-01-14
*/
@Slf4j
@Service
@AllArgsConstructor
//@CacheConfig(cacheNames = "dMdeviceUser")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class DMdeviceUserServiceImpl extends BaseServiceImpl<DMdeviceUserMapper, DMdeviceUser> implements DMdeviceUserService {

    private final IGenerator generator;
    @Autowired
    private DMdeviceDeviceMapper dMdeviceDeviceMapper;
    @Autowired
    private DWatchUricApiService dWatchUricApiService;

    @Override
    //@Cacheable
    public PageResult<DMdeviceUserDto> queryAll(DMdeviceUserQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<DMdeviceUser> page = new PageInfo<>(queryAll(criteria));
        return generator.convertPageInfo(page,DMdeviceUserDto.class);
    }


    @Override
    //@Cacheable
    public List<DMdeviceUser> queryAll(DMdeviceUserQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(DMdeviceUser.class, criteria));
    }


    @Override
    public void download(List<DMdeviceUserDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (DMdeviceUserDto dMdeviceUser : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("用户ID", dMdeviceUser.getUid());
            map.put("主机ID", dMdeviceUser.getMid());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }


    @Override
    public void sycnBindUric2Users(Long mid, Long userId) {
        List<DUric> urics = dMdeviceDeviceMapper.queryUricDeviceByMainUnitId(mid);
        try {
            //先解绑该userId名下的尿酸分析仪
            dWatchUricApiService.unBindUricDevice(userId);
        }catch (Exception e){
            log.error(String.format("用户[{0}]解绑尿酸分析仪失败！", userId)+e.getMessage());
        }
        for(DUric dUric : urics){
            if(StringUtils.isNotEmpty(dUric.getSn())){
                try {
                    //重新绑定
                    dWatchUricApiService.bindUricDevice(userId,dUric.getSn());
                }catch (Exception e){
                    log.error(String.format("用户[{0}]绑定人员和尿酸分析仪{1}失败！", userId,dUric.getSn())+e.getMessage());
                }
            }
        }
    }


    @Override
    public void syncUnbindUricByUserId(Long userId) {
        try {
            //先解绑该userId名下的尿酸分析仪
            dWatchUricApiService.unBindUricDevice(userId);
        }catch (Exception e){
            log.error(String.format("用户[{0}]解绑尿酸分析仪失败！", userId)+e.getMessage());
        }
    }

    @Override
    public List<DMdeviceUserDto> queryAllBindedUsersByMid(Long mid) {
        return baseMapper.findAllBindedUsersByMid(mid);
    }
}
