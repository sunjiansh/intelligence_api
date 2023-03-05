/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.articlemanage.article.domain;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.validation.constraints.*;
import java.sql.Timestamp;
import java.util.Date;
import co.yixiang.domain.BaseDomain;

/**
* @author jiansun
* @date 2023-02-15
*/
@Data
@TableName("s_article")
public class SArticle  {

    // 养生帮助
    public static final int ARTICLE_TYPE_HEALTH = 1;

    //帮助中心
    public static final int ARTICLE_TYPE_HELP = 2;


    @TableId
    private Long id;

    /** 标签 */
    private String label;

    /** 文章标题 */
    @NotBlank
    private String title;

    /** 文章类型 */
    @NotNull
    private Integer type;

    /** 正文 */
    @NotBlank
    private String content;


    /** 是否发布0 否 1 是 */
    private Integer status;


    @TableField(fill= FieldFill.INSERT)
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Timestamp createTime;


    public void copy(SArticle source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
