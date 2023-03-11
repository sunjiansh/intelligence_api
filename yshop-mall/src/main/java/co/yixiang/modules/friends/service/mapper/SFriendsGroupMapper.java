/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.friends.service.mapper;

import co.yixiang.common.mapper.CoreMapper;
import co.yixiang.modules.friends.domain.SFriendsGroup;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
* @author jiansun
* @date 2023-02-23
*/
@Repository
public interface SFriendsGroupMapper extends CoreMapper<SFriendsGroup> {



    @Select("select a.id,a.label,b.real_name realName,b.phone,b.uid from s_friends_group a inner join yx_user b on a.friend_uid = b.uid where a.main_uid=#{uid}")
    List<Map> queryFriendListByMainId(@Param("uid") Long uid);

}
