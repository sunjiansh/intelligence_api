package co.yixiang.modules.device.tumble.domain;

import co.yixiang.modules.device.tumble.service.dto.DTumbleDto;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author sunjian
 * @Date 2023/2/11
 * @license 版权所有，非经许可请勿使用本代码
 */
@Getter
@Setter
@TableName(value = "view_d_tumble_bind_available",autoResultMap = true)
public class ViewDTumbleBindAvailable {
    private Long id;

    /** 名称 */
    private String name;

    /** imei */
    private String imei;

    /** 型号 */
    private String model;

    /** 序列号 */
    private String sn;

    /** 品牌 */
    private String brand;

    /** 备注 */
    private String mark;

    private String imsi;

    private String iccid;

    /** 创建时间 */
    private Date createTime;

    /** 是否已激活 0 未激活 1 已激活 */
    private Integer isActive;
}
