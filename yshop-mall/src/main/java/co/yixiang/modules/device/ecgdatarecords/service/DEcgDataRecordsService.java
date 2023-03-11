/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.device.ecgdatarecords.service;
import co.yixiang.common.service.BaseService;
import co.yixiang.modules.device.bloodsugardatarecords.domain.DBloodSugarDataRecords;
import co.yixiang.modules.device.ecgdatarecords.domain.DEcgDataRecords;
import co.yixiang.modules.device.ecgdatarecords.service.dto.DEcgDataRecordsDto;
import co.yixiang.modules.device.ecgdatarecords.service.dto.DEcgDataRecordsQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import co.yixiang.domain.PageResult;
/**
* @author jiansun
* @date 2023-03-06
*/
public interface DEcgDataRecordsService  extends BaseService<DEcgDataRecords>{

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    PageResult<DEcgDataRecordsDto>  queryAll(DEcgDataRecordsQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<DEcgDataRecordsDto>
    */
    List<DEcgDataRecords> queryAll(DEcgDataRecordsQueryCriteria criteria);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<DEcgDataRecordsDto> all, HttpServletResponse response) throws IOException;


    void saveEntity(DEcgDataRecords entity, String imei);


}
