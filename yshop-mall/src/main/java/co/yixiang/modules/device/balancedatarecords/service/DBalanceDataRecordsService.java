/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.device.balancedatarecords.service;
import co.yixiang.common.service.BaseService;
import co.yixiang.modules.device.balancedatarecords.domain.DBalanceDataRecords;
import co.yixiang.modules.device.balancedatarecords.service.dto.DBalanceDataRecordsDto;
import co.yixiang.modules.device.balancedatarecords.service.dto.DBalanceDataRecordsQueryCriteria;
import co.yixiang.modules.device.bloodsugardatarecords.domain.DBloodSugarDataRecords;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import co.yixiang.domain.PageResult;
/**
* @author jiansun
* @date 2023-03-04
*/
public interface DBalanceDataRecordsService  extends BaseService<DBalanceDataRecords>{

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    PageResult<DBalanceDataRecordsDto>  queryAll(DBalanceDataRecordsQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<DBalanceDataRecordsDto>
    */
    List<DBalanceDataRecords> queryAll(DBalanceDataRecordsQueryCriteria criteria);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<DBalanceDataRecordsDto> all, HttpServletResponse response) throws IOException;

    void saveEntity(DBalanceDataRecords entity, String imei);
}
