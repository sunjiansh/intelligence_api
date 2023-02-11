/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.user.rest;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.PhoneUtil;
import cn.hutool.crypto.SecureUtil;
import co.yixiang.api.ApiResult;
import co.yixiang.constant.ShopConstants;
import co.yixiang.enums.AppFromEnum;
import co.yixiang.exception.BadRequestException;
import co.yixiang.modules.device.apiservice.DWatchUricApiService;
import co.yixiang.modules.device.mdeviceuser.service.DMdeviceUserService;
import co.yixiang.modules.logging.aop.log.Log;
import co.yixiang.modules.aop.ForbidSubmit;
import co.yixiang.modules.user.domain.YxUser;
import co.yixiang.modules.user.service.YxUserService;
import co.yixiang.modules.user.service.dto.UserMoneyDto;
import co.yixiang.modules.user.service.dto.YxUserQueryCriteria;
import co.yixiang.utils.IpUtil;
import co.yixiang.utils.StringUtils;
import co.yixiang.utils.ValidationUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
* @author hupeng
* @date 2019-10-06
*/
@Slf4j
@Api(tags = "商城:会员管理")
@RestController
@RequestMapping("api")
public class MemberController {

    private final YxUserService yxUserService;
    @Autowired
    private DWatchUricApiService dWatchUricApiService;
    @Autowired
    private DMdeviceUserService dMdeviceUserService;

    public MemberController(YxUserService yxUserService) {
        this.yxUserService = yxUserService;
    }

    @Log("查看下级")
    @ApiOperation(value = "查看下级")
    @PostMapping(value = "/yxUser/spread")
    @PreAuthorize("hasAnyRole('admin','YXUSER_ALL','YXUSER_EDIT')")
    public ResponseEntity getSpread(@RequestBody YxUserQueryCriteria criteria){
        return new ResponseEntity<>(yxUserService.querySpread(criteria.getUid(),criteria.getGrade()),
                HttpStatus.OK);
    }

    @Log("查询用户")
    @ApiOperation(value = "查询用户")
    @GetMapping(value = "/yxUser")
    @PreAuthorize("hasAnyRole('admin','YXUSER_ALL','YXUSER_SELECT')")
    public ResponseEntity getYxUsers(YxUserQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(yxUserService.queryAll(criteria,pageable),HttpStatus.OK);
    }
    @Log("查询可绑定用户")
    @ApiOperation(value = "查询可绑定用户")
    @GetMapping(value = "/yxUser/bindAvailable")
    public ResponseEntity getYxUsersBindAvailable(YxUserQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(yxUserService.queryAllBindAvailablePage(criteria,pageable),HttpStatus.OK);
    }


    @Log("新增用户")
    @ApiOperation(value = "新增用户")
    @PostMapping(value = "/yxUser")
    @PreAuthorize("hasAnyRole('admin','YXUSER_ALL','YXUSER_ADD')")
    public ResponseEntity add(@Validated @RequestBody YxUser resources){


        if (ObjectUtil.isEmpty(resources.getPhone())) {
            throw new BadRequestException("手机号不能为空");
        }

        if (!ValidationUtil.isPhoneNumber(resources.getPhone())) {
            throw new BadRequestException("手机号码格式不合法" );
        }

        YxUser yxUser = yxUserService.getOne(Wrappers.<YxUser>lambdaQuery()
              //  .eq(YxUser::getIsDel,0)
                .eq(YxUser::getPhone,resources.getPhone()),false);
        if (ObjectUtil.isNotNull(yxUser)) {
            throw new BadRequestException("该手机号用户已存在");
        }

        if(StringUtils.isNotEmpty(resources.getImei())){
            YxUser existUser = yxUserService.getOne(new LambdaQueryWrapper<YxUser>().eq(YxUser::getImei,resources.getImei()));
            if(existUser != null){
                throw  new BadRequestException("该手环已绑定到用户:["+existUser.getPhone()+"],请勿重复绑定！");
            }
        }

        if(StringUtils.isNotEmpty(resources.getUricSn())){
            YxUser existUser = yxUserService.getOne(new LambdaQueryWrapper<YxUser>().eq(YxUser::getUricSn,resources.getUricSn()));
            if(existUser != null){
                throw  new BadRequestException("该尿酸分析仪已绑定到用户:["+existUser.getPhone()+"],请勿重复绑定！");
            }
        }


        String ip = IpUtil.getLocalIP();
        YxUser user = YxUser.builder()
                .username(resources.getPhone())
                .nickname(resources.getPhone())
                .realName(resources.getRealName())
                .password(SecureUtil.md5(ShopConstants.YSHOP_DEFAULT_PWD))
                .phone(resources.getPhone())
                .avatar(ShopConstants.YSHOP_DEFAULT_AVATAR)
                .addIp(ip)
                .lastIp(ip)
                .mark(resources.getMark())
                .userType(AppFromEnum.PC.getValue())
                .imei(resources.getImei())
                .sex(resources.getSex())
                .birthday(resources.getBirthday())
                .serviceStart(resources.getServiceStart())
                .serviceEnd(resources.getServiceEnd())
                .build();

        yxUserService.save(user);
        try {
            yxUserService.syncNewUserInfo2WatchUric(user);
        }catch (Exception e){
            throw new BadRequestException("同步失败！"+e.getMessage());
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("修改用户")
    @ApiOperation(value = "修改用户")
    @PutMapping(value = "/yxUser")
    @PreAuthorize("hasAnyRole('admin','YXUSER_ALL','YXUSER_EDIT')")
    public ResponseEntity update(@Validated @RequestBody YxUser resources){
        if(StringUtils.isNotEmpty(resources.getImei())){
            YxUser existUser = yxUserService.getOne(new LambdaQueryWrapper<YxUser>().eq(YxUser::getImei,resources.getImei()).ne(YxUser::getUid,resources.getUid()));
            if(existUser != null){
                throw  new BadRequestException("该手环已绑定到用户:["+existUser.getPhone()+"],请勿重复绑定！");
            }
        }

        if(StringUtils.isNotEmpty(resources.getUricSn())){
            YxUser existUser = yxUserService.getOne(new LambdaQueryWrapper<YxUser>().eq(YxUser::getUricSn,resources.getUricSn()).ne(YxUser::getUid,resources.getUid()));
            if(existUser != null){
                throw  new BadRequestException("该尿酸分析仪已绑定到用户:["+existUser.getPhone()+"],请勿重复绑定！");
            }
        }

        //同步用户信息
        try {
            yxUserService.syncUpdateUserInfo2WatchUric(resources);
        }catch (Exception e){
            throw new BadRequestException("同步失败！"+e.getMessage());
        }
        yxUserService.saveOrUpdate(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ForbidSubmit
    @Log("删除用户")
    @ApiOperation(value = "删除用户")
    @DeleteMapping(value = "/yxUser/{uid}")
    @PreAuthorize("hasAnyRole('admin','YXUSER_ALL','YXUSER_DELETE')")
    public ResponseEntity delete(@PathVariable Long uid){
        //yxUserService.removeById(uid);

        try {
            //解绑手环信息
            yxUserService.syncDelUserWatchBindInfo(uid);
            //解绑尿酸分析仪信息
            yxUserService.syncUnbindUricByUserId(uid);
        }catch (Exception e){
            throw new BadRequestException("同步失败！"+e.getMessage());
        }

        yxUserService.deleteByUid(uid);

        return new ResponseEntity(HttpStatus.OK);
    }

    @ForbidSubmit
    @ApiOperation(value = "用户禁用启用")
    @PostMapping(value = "/yxUser/onStatus/{id}")
    public ResponseEntity onStatus(@PathVariable Long id,@RequestBody String jsonStr){
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        Integer status = jsonObject.getInteger("status");
        yxUserService.onStatus(id,status);
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(value = "修改余额")
    @PostMapping(value = "/yxUser/money")
    @PreAuthorize("hasAnyRole('admin','YXUSER_ALL','YXUSER_EDIT')")
    public ResponseEntity updatePrice(@Validated @RequestBody UserMoneyDto param){
        yxUserService.updateMoney(param);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


    @ApiOperation(value = "同步用户手环信息到手环平台")
    @PostMapping(value = "/yxUser/syncWatchBindInfo")
    public ResponseEntity syncWatchBindInfo(@RequestBody YxUser resources){
        if (ObjectUtil.isEmpty(resources.getUid())) {
            throw new BadRequestException("用户Id不能为空");
        }
        if (ObjectUtil.isEmpty(resources.getImei())) {
            throw new BadRequestException("IMEI不能为空");
        }

        try {
            yxUserService.syncWatchBindInfo(resources.getUid(),resources.getImei());
        }catch (Exception e){
            throw new BadRequestException("同步失败！"+e.getMessage());
        }

        try {
            yxUserService.syncWatchSOSConfig(resources.getImei(),resources.getSosContact());
        }catch (Exception e){
            throw new BadRequestException("同步失败！"+e.getMessage());
        }

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


    @ApiOperation(value = "同步用户尿酸分析仪信息到手环平台")
    @PostMapping(value = "/yxUser/syncUricBindInfo")
    public ResponseEntity syncUricBindInfo(@RequestBody YxUser resources){
        if (ObjectUtil.isEmpty(resources.getUid())) {
            throw new BadRequestException("用户Id不能为空");
        }
        if (ObjectUtil.isEmpty(resources.getUricSn())) {
            throw new BadRequestException("尿酸分析仪SN不能为空");
        }
        //同步尿酸分析仪信息
        try {
            yxUserService.syncUricBindInfo(resources.getUid(),resources.getUricSn());
        }catch (Exception e){
            throw new BadRequestException("同步失败！"+e.getMessage());
        }

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }



}
