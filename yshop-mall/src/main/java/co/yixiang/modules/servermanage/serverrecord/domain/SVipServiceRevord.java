/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.servermanage.serverrecord.domain;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.validation.constraints.*;
import java.util.Date;
import co.yixiang.domain.BaseDomain;

/**
* @author jiansun
* @date 2023-02-11
*/
@Data
@TableName("s_vip_service_revord")
public class SVipServiceRevord  {
    @TableId
    private Long id;

    /** 客服人员 */
    @NotBlank
    private String serverPerson;

    /** 问题类型 */
    private String questionType;

    /** 处理记录 */
    private String handleRecord;

    /** 服务开始时间 */
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date serverStartTime;

    /** 服务结束书简 */
   // @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date serverEndTime;


    /** 被服务人员id */
    @NotNull
    private Long memberId;

    /** 被服务人员名称 */
    @NotBlank
    private String memberName;


    @TableField(fill= FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;


    public void copy(SVipServiceRevord source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
