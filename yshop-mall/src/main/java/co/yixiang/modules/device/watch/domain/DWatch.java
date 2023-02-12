/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.watch.domain;
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
* @date 2023-01-10
*/
@Data
@TableName("d_watch")
public class DWatch  {
    @TableId
    private Long id;

    /** 名称 */
    private String name;

    /** imei */
    @NotBlank
    private String imei;

    /** 型号 */
    private String model;

    /** 序列号 */
    private String sn;

    /** 品牌 */
    private String brand;

    /** 备注 */
    private String mark;


    /** 是否已激活 0 未激活 1 已激活 */
    private Integer isActive;

    /**
     * 是否已初始化配置到手环平台  0 未初始化 1 已初始化
     */
    private Integer isConfig;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;


    public void copy(DWatch source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
