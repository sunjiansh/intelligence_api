/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.articlemanage.article.service.dto;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;
import java.io.Serializable;

/**
* @author jiansun
* @date 2023-02-15
*/
@Data
public class SArticleDto implements Serializable {

    private Long id;

    /** 标签 */
    private String label;

    /** 文章标题 */
    private String title;

    /** 文章类型 */
    private String type;

    /** 正文 */
    private String content;

    /** 创建时间 */
    private Timestamp createTime;

    /** 是否发布0 否 1 是 */
    private Integer status;
}
