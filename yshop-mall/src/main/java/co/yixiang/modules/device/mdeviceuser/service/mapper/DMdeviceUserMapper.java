/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.device.mdeviceuser.service.mapper;

import co.yixiang.common.mapper.CoreMapper;
import co.yixiang.modules.device.mdeviceuser.domain.DMdeviceUser;
import co.yixiang.modules.device.mdeviceuser.service.dto.DMdeviceUserDto;
import co.yixiang.modules.user.domain.YxUser;
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
public interface DMdeviceUserMapper extends CoreMapper<DMdeviceUser> {



    @Select("select a.* from yx_user a inner join d_mdevice_user b on a.uid = b.uid where b.mid = #{mid}")
    List<YxUser> queryUserByMainUnitId(@Param("mid") Long mid);



    @Select("select a.real_name,a.phone,b.mid,b.id as id from yx_user a left join d_mdevice_user b on a.uid=b.uid where b.mid=#{mid}")
    List<DMdeviceUserDto> findAllBindedUsersByMid(@Param("mid") Long mid);

}
