package co.yixiang.modules.device.ecg.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * @author sunjian
 * @Date 2023/2/11
 * @license 版权所有，非经许可请勿使用本代码
 */
@Getter
@Setter
@TableName(value = "view_d_ecg_bind_available",autoResultMap = true)
public class ViewDEcgBindAvailable extends DEcg{

}
