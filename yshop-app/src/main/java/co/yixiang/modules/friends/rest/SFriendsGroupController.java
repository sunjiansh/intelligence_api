/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.friends.rest;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import co.yixiang.api.ApiResult;
import co.yixiang.api.YshopException;
import co.yixiang.common.bean.LocalUser;
import co.yixiang.common.interceptor.AuthCheck;
import co.yixiang.common.util.SmsUtils;
import co.yixiang.constant.ShopConstants;
import co.yixiang.domain.PageResult;
import co.yixiang.dozer.service.IGenerator;
import co.yixiang.modules.friends.domain.SFriendsGroup;
import co.yixiang.modules.friends.service.SFriendsGroupService;
import co.yixiang.modules.friends.service.dto.SFriendsGroupDto;
import co.yixiang.modules.friends.service.dto.SFriendsGroupQueryCriteria;
import co.yixiang.modules.logging.aop.log.AppLog;
import co.yixiang.modules.user.domain.YxUser;
import co.yixiang.modules.user.service.YxUserService;
import co.yixiang.utils.RedisUtils;
import co.yixiang.utils.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.exceptions.ClientException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sun.util.locale.LocaleUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
* @author jiansun
* @date 2023-02-23
*/
@Slf4j
@RestController
@Api(value = "亲友群组管理", tags = "亲友群组管理")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/sFriendsGroup")
public class SFriendsGroupController {

    private final SFriendsGroupService sFriendsGroupService;
    private final IGenerator generator;
    private final YxUserService yxUserService;
    private final RedisUtils redisUtil;


    public static final String CODE_INVITE_PREFIX = "code_invite_";


    @AuthCheck
    @GetMapping(value = "/list")
    @AppLog("查询亲友群组")
    @ApiOperation("查询亲友群组")
    public ApiResult<List<Map>> getSFriendsGroups(){
        YxUser user = LocalUser.getUser();
       // List<SFriendsGroup> list = sFriendsGroupService.list(new LambdaQueryWrapper<SFriendsGroup>().eq(SFriendsGroup::getMainUid,user.getUid()));
        List<Map> list  =sFriendsGroupService.getriendListByMainId(user.getUid());
        return ApiResult.ok(list);
    }


    @AuthCheck
    @PostMapping(value = "/add")
    @AppLog("新增亲友群组")
    @ApiOperation("新增亲友群组")
    public ApiResult<String> create(@Validated @RequestBody SFriendsGroupDto resources){

        YxUser locaUser = LocalUser.getUser();


        Object codeObj = redisUtil.get(CODE_INVITE_PREFIX+ resources.getPhone());
        if(codeObj == null){
            return ApiResult.fail("请先获取验证码");
        }
        String code = codeObj.toString();
        if (!StrUtil.equals(code, resources.getCaptcha())) {
            return ApiResult.fail("验证码错误");
        }

        YxUser friend = yxUserService.getOne(new LambdaQueryWrapper<YxUser>().eq(YxUser::getPhone,resources.getPhone()));
        if (friend == null) {
            return ApiResult.fail("该用户不存在");
        }

        if(locaUser.getUid() == friend.getUid()){
            return ApiResult.fail("不能添加自己为好友");
        }

        SFriendsGroup existSFriendsGroup = sFriendsGroupService.getOne(new LambdaQueryWrapper<SFriendsGroup>().eq(SFriendsGroup::getFriendUid,friend.getUid()).eq(SFriendsGroup::getMainUid,locaUser.getUid()));
        if(existSFriendsGroup != null){
            return ApiResult.fail("您已添加过该好友");
        }


        YxUser user = yxUserService.getOne(new LambdaQueryWrapper<YxUser>().eq(YxUser::getPhone,resources.getPhone()));

        SFriendsGroup sFriendsGroup = new SFriendsGroup();
        sFriendsGroup.setMainUid(locaUser.getUid());
        sFriendsGroup.setFriendUid(user.getUid());
        sFriendsGroup.setLabel(resources.getLabel());
        sFriendsGroupService.save(sFriendsGroup);
        return ApiResult.ok("添加成功");
    }

    @AuthCheck
    @PutMapping(value = "/update")
    @AppLog("修改亲友群组")
    @ApiOperation("修改亲友群组")
    public ApiResult<Boolean> update(@Validated @RequestBody SFriendsGroup resources){
        sFriendsGroupService.updateById(resources);
        return ApiResult.ok();
    }


//    @AuthCheck
//    @PostMapping(value = "/check/{mainUid}/{phone}")
//    @AppLog("检查用户是否存在")
//    @ApiOperation("检查用户是否存在")
//    public ApiResult<Boolean> check(@RequestParam("phone") String phone,@RequestParam("mainUid") Long mainUid) {
//
//        YxUser user = yxUserService.getOne(new LambdaQueryWrapper<YxUser>().eq(YxUser::getPhone,phone));
//        if (user == null) {
//            throw new YshopException("该用户不存在");
//        }
//
//        SFriendsGroup sFriendsGroup = sFriendsGroupService.getOne(new LambdaQueryWrapper<SFriendsGroup>().eq(SFriendsGroup::getFriendUid,user.getUid()).eq(SFriendsGroup::getMainUid,mainUid));
//        if(sFriendsGroup != null){
//            throw new YshopException("该用户已添加过好友");
//        }
//
//        return ApiResult.ok();
//    }


    @AuthCheck
    @GetMapping(value = "/sendSMS")
    @AppLog("发送短信验证码")
    @ApiOperation("发送短信验证码")
    public ApiResult<String> sendSMS(@RequestParam("phone") String phone) {
        YxUser locaUser = LocalUser.getUser();

        YxUser user = yxUserService.getOne(new LambdaQueryWrapper<YxUser>().eq(YxUser::getPhone,phone));
        if (user == null) {
            return ApiResult.fail("该用户不存在");
        }

        if(locaUser.getUid() == user.getUid()){
            return ApiResult.fail("不能添加自己为好友");
        }

        SFriendsGroup sFriendsGroup = sFriendsGroupService.getOne(new LambdaQueryWrapper<SFriendsGroup>().eq(SFriendsGroup::getFriendUid,user.getUid()).eq(SFriendsGroup::getMainUid,locaUser.getUid()));
        if(sFriendsGroup != null){
            return ApiResult.fail("您已添加过该好友");
        }

        String codeKey = CODE_INVITE_PREFIX + phone;
        if (ObjectUtil.isNotNull(redisUtil.get(codeKey))) {
            return ApiResult.fail("已发送过验证码");//短信功能正常后开放
        }
        String code = RandomUtil.randomNumbers(ShopConstants.YSHOP_SMS_SIZE);
        //redis存储
        redisUtil.set(codeKey, code, ShopConstants.YSHOP_SMS_REDIS_TIME);

        //发送阿里云短信
        JSONObject json = new JSONObject();
        json.put("code",code);
        try {
            SmsUtils.sendSms(phone,json.toJSONString());
        } catch (ClientException e) {
            redisUtil.del(codeKey);
            e.printStackTrace();
            return ApiResult.fail("发送失败："+e.getErrMsg());
        }

        return ApiResult.ok("发送成功，请注意查收");
    }



    @AuthCheck
    @AppLog("删除亲友群组")
    @ApiOperation("删除亲友群组")
    @PostMapping(value = "/delete")
    public ApiResult<Boolean> deleteAll(@RequestBody Long[] ids) {
        Arrays.asList(ids).forEach(id->{
            sFriendsGroupService.removeById(id);
        });
        return ApiResult.ok();
    }
}
