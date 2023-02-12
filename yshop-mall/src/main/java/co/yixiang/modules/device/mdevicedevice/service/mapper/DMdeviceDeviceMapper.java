/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.device.mdevicedevice.service.mapper;

import co.yixiang.common.mapper.CoreMapper;
import co.yixiang.modules.device.mdevicedevice.domain.DMdeviceDevice;
import co.yixiang.modules.device.uric.domain.DUric;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* @author jiansun
* @date 2023-01-14
*/
@Repository
public interface DMdeviceDeviceMapper extends CoreMapper<DMdeviceDevice> {


    @Select("select a.* from d_uric a inner join d_mdevice_device b on a.id = b.did where b.dtype=3 and b.mid = #{mid}")
    List<DUric> queryUricDeviceByMainUnitId(@Param("mid") Long mid);

}
