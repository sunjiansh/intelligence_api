/**
*/
package co.yixiang.modules.watch.service.dto;

import co.yixiang.annotation.Query;
import lombok.Data;

/**
* @author jiansun
* @date 2023-01-10
*/
@Data
public class DWatchQueryCriteria{

    /** 模糊 */
    @Query(type = Query.Type.INNER_LIKE)
    private String name;

    /** 模糊 */
    @Query(type = Query.Type.INNER_LIKE)
    private String imei;
}
