package co.yixiang.modules.device.watchuricdatarecords.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author sunjian
 * @Date 2023/2/14
 * @license 版权所有，非经许可请勿使用本代码
 */
@Data
@TableName(value = "view_device_location")
public class ViewDeviceLocation  {

    @TableId
    private Long id;


    String avatar;

    private String realName;

    private String phone;

    private Integer sex;

    private String locationDesc;

    /** 数据上传时间 */
    private Date pushTime;

    private String imei;
    /**
     * 纬度
     */
    private Double lat;

    /**
     * 经度
     */
    private Double lon;



}
