package co.yixiang.modules.health.rest;

import co.yixiang.api.ApiResult;
import co.yixiang.common.interceptor.AuthCheck;
import co.yixiang.modules.articlemanage.article.domain.SArticle;
import co.yixiang.modules.device.watchuricdatarecords.domain.DWatchUricDataRecords;
import co.yixiang.modules.health.service.HealthSummaryService;
import co.yixiang.modules.logging.aop.log.AppLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.MacSpi;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author sunjian
 * @Date 2023/2/26
 * @license 版权所有，非经许可请勿使用本代码
 */
@Slf4j
@RestController()
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api(value = "健康数据统计", tags = "健康数据统计")
@RequestMapping("/health")
public class HealthSummaryController {


    private final HealthSummaryService healthSummaryService;



    @AuthCheck
    @GetMapping(value = "/getSleepDataByDay")
    @AppLog("按天查询睡眠数据")
    @ApiOperation("按天查询睡眠数据")
    public ApiResult<DWatchUricDataRecords> getSleepDataByDay(@RequestParam("day") Date day, @RequestParam("uid") Long uid){
        //Long uid = LocalUser.getUser().getUid();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dayStr = simpleDateFormat.format(day);
       DWatchUricDataRecords record = healthSummaryService.getSleepRecordByDay(dayStr,uid);
        return  ApiResult.ok(record);
    }


    @AuthCheck
    @GetMapping(value = "/getBloodPreasureByDay")
    @AppLog("按天查询血压数据")
    @ApiOperation("按天查询血压数据")
    public ApiResult<List<Map>> getBloodPreasureByDay(@RequestParam("day") Date day, @RequestParam("uid") Long uid){
        //Long uid = LocalUser.getUser().getUid();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dayStr = simpleDateFormat.format(day);
        List<Map> list = healthSummaryService.getBloodPreasureByDay(dayStr,uid);
        return  ApiResult.ok(list);
    }


    @AuthCheck
    @GetMapping(value = "/getHeartRateByDay")
    @AppLog("按天查询心率数据")
    @ApiOperation("按天查询心率数据")
    public ApiResult<List<Map>> getHeartRateByDay(@RequestParam("day") Date day, @RequestParam("uid") Long uid){
        //Long uid = LocalUser.getUser().getUid();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dayStr = simpleDateFormat.format(day);
        List<Map> list = healthSummaryService.getHeartRateByDay(dayStr,uid);
        return  ApiResult.ok(list);
    }


    @AuthCheck
    @GetMapping(value = "/getOxygenByDay")
    @AppLog("按天查询血氧数据")
    @ApiOperation("按天查询血氧数据")
    public ApiResult<List<Map>> getOxygenByDay(@RequestParam("day") Date day, @RequestParam("uid") Long uid){
        //Long uid = LocalUser.getUser().getUid();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dayStr = simpleDateFormat.format(day);
        List<Map> list = healthSummaryService.getOxygenByDay(dayStr,uid);
        return  ApiResult.ok(list);
    }



    @AuthCheck
    @GetMapping(value = "/getHealthArticleTop5")
    @AppLog("查询前5条健康养生文章")
    @ApiOperation("查询前5条健康养生文章")
    public ApiResult<List<SArticle>> getHealthArticleTop5(){
        //Long uid = LocalUser.getUser().getUid();
        List<SArticle> list = healthSummaryService.getHealthArticlesForPage(0,5);
        return  ApiResult.ok(list);
    }

    @AuthCheck
    @GetMapping(value = "/getHealthArticlePage")
    @AppLog("查询健康养生文章分页数据")
    @ApiOperation("查询健康养生文章分页数据")
    public ApiResult<List<SArticle>> getHealthArticlePage(@RequestParam("start") Integer start, @RequestParam("pageSize") Integer pageSize){
        //Long uid = LocalUser.getUser().getUid();
        List<SArticle> list = healthSummaryService.getHealthArticlesForPage(start,pageSize);
        return  ApiResult.ok(list);
    }




}
