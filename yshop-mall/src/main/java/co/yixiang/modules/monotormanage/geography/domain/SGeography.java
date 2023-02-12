/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.monotormanage.geography.domain;
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
* @date 2023-02-12
*/
@Data
@TableName("s_geography")
public class SGeography  {
    @TableId
    private Long id;

    /** 手环IMEI */
    private String imei;

    /** 围栏名称 */
    private String name;

    /** 围栏地址 */
    private String address;

    /** 围栏半径 */
    private Integer regionRange;

    /** 经度 */
    private Double lon;

    /** 纬度 */
    private Double lat;



    /** 会员ID */
    private Long memberId;

    /** 会员人员名称 */
    private String memberName;




    @TableField(fill= FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

    @TableField(fill= FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updateTime;



    public void copy(SGeography source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
