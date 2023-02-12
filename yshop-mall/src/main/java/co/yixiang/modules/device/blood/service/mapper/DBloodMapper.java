/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.blood.service.mapper;

import co.yixiang.common.mapper.CoreMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* @author jiansun
* @date 2023-01-10
*/
@Repository
public interface DBloodMapper extends CoreMapper<co.yixiang.modules.blood.domain.DBlood> {


    @Select("select a.bl_name,a.bl_id,a.brand,a.model,b.mid,b.id as id from d_blood a left join d_mdevice_device b on a.id=b.did where b.dtype='0' and b.mid=#{mid}")
    List<co.yixiang.modules.blood.service.dto.DBloodDto> findAllBindedDeviceByMid(@Param("mid") Long mid);


}
