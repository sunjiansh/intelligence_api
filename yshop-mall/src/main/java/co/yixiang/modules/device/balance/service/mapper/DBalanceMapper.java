/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.device.balance.service.mapper;

import co.yixiang.common.mapper.CoreMapper;
import co.yixiang.modules.device.balance.domain.DBalance;
import co.yixiang.modules.device.balance.service.dto.DBalanceDto;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* @author jiansun
* @date 2023-01-11
*/
@Repository
public interface DBalanceMapper extends CoreMapper<DBalance> {

    @Select("select a.bl_name,a.bl_id,a.brand,a.model,b.mid,b.id as id from d_balance a left join d_mdevice_device b on a.id=b.did where b.dtype='2' and b.mid=#{mid}")
    List<DBalanceDto> findAllBindedDeviceByMid(@Param("mid") Long mid);

}
