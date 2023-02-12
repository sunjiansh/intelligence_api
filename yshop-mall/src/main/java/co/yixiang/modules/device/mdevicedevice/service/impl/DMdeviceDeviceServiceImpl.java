/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.device.mdevicedevice.service.impl;

import co.yixiang.modules.device.apiservice.DWatchUricApiService;
import co.yixiang.modules.device.mdevicedevice.domain.DMdeviceDevice;
import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.modules.device.mdeviceuser.service.mapper.DMdeviceUserMapper;
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
import co.yixiang.modules.device.mdevicedevice.service.DMdeviceDeviceService;
import co.yixiang.modules.device.mdevicedevice.service.dto.DMdeviceDeviceDto;
import co.yixiang.modules.device.mdevicedevice.service.dto.DMdeviceDeviceQueryCriteria;
import co.yixiang.modules.device.mdevicedevice.service.mapper.DMdeviceDeviceMapper;
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
//@CacheConfig(cacheNames = "dMdeviceDevice")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class DMdeviceDeviceServiceImpl extends BaseServiceImpl<DMdeviceDeviceMapper, DMdeviceDevice> implements DMdeviceDeviceService {

    private final IGenerator generator;
    @Autowired
    private DMdeviceUserMapper dMdeviceUserMapper;
    @Autowired
    private DUricMapper dUricMapper;
    @Autowired
    private DWatchUricApiService dWatchUricApiService;

    @Override
    //@Cacheable
    public PageResult<DMdeviceDeviceDto> queryAll(DMdeviceDeviceQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<DMdeviceDevice> page = new PageInfo<>(queryAll(criteria));
        return generator.convertPageInfo(page,DMdeviceDeviceDto.class);
    }


    @Override
    //@Cacheable
    public List<DMdeviceDevice> queryAll(DMdeviceDeviceQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(DMdeviceDevice.class, criteria));
    }


    @Override
    public void download(List<DMdeviceDeviceDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (DMdeviceDeviceDto dMdeviceDevice : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("主机ID", dMdeviceDevice.getMid());
            map.put("其他设备ID", dMdeviceDevice.getDid());
            map.put("设备类型", dMdeviceDevice.getDtype());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }


    @Override
    public void sycnBindUric2Users(Long mid, Long uricId) {
        List<YxUser> users = dMdeviceUserMapper.queryUserByMainUnitId(mid);
        DUric dUric = dUricMapper.selectById(uricId);
        for(YxUser user : users){
            if(StringUtils.isNotEmpty(dUric.getSn())){
                try {
                    //先解绑userId名下的尿酸分析仪
                    dWatchUricApiService.unBindUricDevice(user.getUid());
                    //再重新绑定
                    dWatchUricApiService.bindUricDevice(user.getUid(),dUric.getSn());
                }catch (Exception e){
                    log.error(String.format("用户[{0}]绑定人员和尿酸分析仪{1}仪失败！", user.getUid(),dUric.getSn())+e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

}
