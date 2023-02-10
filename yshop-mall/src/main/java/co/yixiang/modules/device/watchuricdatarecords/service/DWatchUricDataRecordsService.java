/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.device.watchuricdatarecords.service;

import co.yixiang.common.service.BaseService;
import co.yixiang.domain.PageResult;
import co.yixiang.modules.device.watchuricdatarecords.domain.DWatchUricDataRecords;
import co.yixiang.modules.device.watchuricdatarecords.service.dto.DWatchUricDataRecordsDto;
import co.yixiang.modules.device.watchuricdatarecords.service.dto.DWatchUricDataRecordsQueryCriteria;
import com.alibaba.fastjson.JSONObject;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
* @author jiansun
* @date 2023-01-29
*/
public interface DWatchUricDataRecordsService  extends BaseService<DWatchUricDataRecords>{

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    PageResult<DWatchUricDataRecordsDto>  queryAll(DWatchUricDataRecordsQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<DWatchUricDataRecordsDto>
    */
    List<DWatchUricDataRecords> queryAll(DWatchUricDataRecordsQueryCriteria criteria);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<DWatchUricDataRecordsDto> all, HttpServletResponse response) throws IOException;


    /**
     * 处理终端上传过来的数据
     * @param json
     * @throws Exception
     */
    void handleWatchAndUricReportData(JSONObject json) throws Exception;


}
