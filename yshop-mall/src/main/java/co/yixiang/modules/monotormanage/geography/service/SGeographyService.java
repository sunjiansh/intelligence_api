/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.monotormanage.geography.service;
import co.yixiang.common.service.BaseService;
import co.yixiang.modules.device.watchuricdatarecords.domain.ViewDeviceLocation;
import co.yixiang.modules.monotormanage.geography.domain.SGeography;
import co.yixiang.modules.monotormanage.geography.service.dto.SGeographyDto;
import co.yixiang.modules.monotormanage.geography.service.dto.SGeographyQueryCriteria;
import co.yixiang.modules.user.domain.YxUser;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import co.yixiang.domain.PageResult;
/**
* @author jiansun
* @date 2023-02-12
*/
public interface SGeographyService  extends BaseService<SGeography>{

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    PageResult<SGeographyDto>  queryAll(SGeographyQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<SGeographyDto>
    */
    List<SGeography> queryAll(SGeographyQueryCriteria criteria);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<SGeographyDto> all, HttpServletResponse response) throws IOException;

    /**
     * 查询设备定位列表
     * @return
     */
    List<ViewDeviceLocation> queryDeviceLocationList();

}
