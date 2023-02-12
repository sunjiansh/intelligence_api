/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.device.uric.service.mapper;

import co.yixiang.common.mapper.CoreMapper;
import co.yixiang.modules.device.tumble.domain.DTumble;
import co.yixiang.modules.device.uric.domain.DUric;
import co.yixiang.modules.device.uric.service.dto.DUricDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* @author jiansun
* @date 2023-01-11
*/
@Repository
public interface DUricMapper extends CoreMapper<DUric> {

    @Select("select a.name,a.imei,a.model,a.sn,a.brand,b.mid,b.id as id from d_uric a left join d_mdevice_device b on a.id=b.did where b.dtype='3' and b.mid=#{mid}")
    List<DUricDto> findAllBindedDeviceByMid(@Param("mid") Long mid);

}
