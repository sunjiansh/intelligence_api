package co.yixiang.modules.health.rest;

import co.yixiang.api.ApiResult;
import co.yixiang.common.interceptor.AuthCheck;
import co.yixiang.modules.articlemanage.article.domain.SArticle;
import co.yixiang.modules.articlemanage.article.service.SArticleService;
import co.yixiang.modules.device.watchuricdatarecords.service.dto.DWatchUricDataRecordsDto;
import co.yixiang.modules.health.service.HealthSummaryService;
import co.yixiang.modules.logging.aop.log.AppLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    private final SArticleService sArticleService;



    @AuthCheck
    @GetMapping(value = "/getSleepDataByDay")
    @AppLog("按天查询睡眠数据")
    @ApiOperation("按天查询睡眠数据")
    public ApiResult<DWatchUricDataRecordsDto> getSleepDataByDay(@RequestParam("day") Date day, @RequestParam("uid") Long uid){
        //Long uid = LocalUser.getUser().getUid();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dayStr = simpleDateFormat.format(day);
        DWatchUricDataRecordsDto record = healthSummaryService.getSleepRecordByDay(dayStr,uid);
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
    @GetMapping(value = "/getTemperatureByDay")
    @AppLog("按天查询体温数据")
    @ApiOperation("按天查询体温数据")
    public ApiResult<List<Map>> getTemperatureByDay(@RequestParam("day") Date day, @RequestParam("uid") Long uid){
        //Long uid = LocalUser.getUser().getUid();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dayStr = simpleDateFormat.format(day);
        List<Map> list = healthSummaryService.getTemperatureByDay(dayStr,uid);
        return  ApiResult.ok(list);
    }


    @AuthCheck
    @GetMapping(value = "/getWeightByDay")
    @AppLog("按天查询体重数据")
    @ApiOperation("按天查询体重数据")
    public ApiResult<List<Map>> getWeightByDay(@RequestParam("day") Date day, @RequestParam("uid") Long uid){
        //Long uid = LocalUser.getUser().getUid();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dayStr = simpleDateFormat.format(day);
        List<Map> list = healthSummaryService.getWeightByDay(dayStr,uid);
        return  ApiResult.ok(list);
    }


    @AuthCheck
    @GetMapping(value = "/getBloodSugarByDay")
    @AppLog("按天查询血糖数据")
    @ApiOperation("按天查询血糖数据")
    public ApiResult<List<Map>> getBloodByDay(@RequestParam("day") Date day, @RequestParam("uid") Long uid){
        //Long uid = LocalUser.getUser().getUid();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dayStr = simpleDateFormat.format(day);
        List<Map> list = healthSummaryService.getBloodSugarByDay(dayStr,uid);
        return  ApiResult.ok(list);
    }

    @AuthCheck
    @GetMapping(value = "/getPulseRateByDay")
    @AppLog("按天查询脉搏数据")
    @ApiOperation("按天查询脉搏数据")
    public ApiResult<List<Map>> getPulseRateByDay(@RequestParam("day") Date day, @RequestParam("uid") Long uid){
        //Long uid = LocalUser.getUser().getUid();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dayStr = simpleDateFormat.format(day);
        List<Map> list = healthSummaryService.getPulseRateByDay(dayStr,uid);
        return  ApiResult.ok(list);
    }


    @AuthCheck
    @GetMapping(value = "/getEcgByDay")
    @AppLog("按天查询心电图数据")
    @ApiOperation("按天查询心电图数据")
    public ApiResult<List<Map>> getEcgByDay(@RequestParam("day") Date day, @RequestParam("uid") Long uid){
        //Long uid = LocalUser.getUser().getUid();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dayStr = simpleDateFormat.format(day);
        List<Map> list = healthSummaryService.getEcgByDay(dayStr,uid);
        return  ApiResult.ok(list);
    }


    @AuthCheck
    @GetMapping(value = "/getUricAcidByDay")
    @AppLog("按天查尿酸数据")
    @ApiOperation("按天查尿酸数据")
    public ApiResult<List<Map>> getUricAcidByDay(@RequestParam("day") Date day, @RequestParam("uid") Long uid){
        //Long uid = LocalUser.getUser().getUid();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dayStr = simpleDateFormat.format(day);
        List<Map> list = healthSummaryService.getUricAcidByDay(dayStr,uid);
        return  ApiResult.ok(list);
    }


    @AuthCheck
    @GetMapping(value = "/getFallDownByDay")
    @AppLog("按天查跌倒数据")
    @ApiOperation("按天查跌倒数据")
    public ApiResult<List<Map>> getFallDownByDay(@RequestParam("day") Date day, @RequestParam("uid") Long uid){
        //Long uid = LocalUser.getUser().getUid();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dayStr = simpleDateFormat.format(day);
        List<Map> list = healthSummaryService.getFallDownByDay(dayStr,uid);
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

    @AuthCheck
    @GetMapping(value = "/getHealthArticle/{id}")
    @AppLog("根据ID查询健康养生文章内容")
    @ApiOperation("根据ID查询健康养生文章内容")
    public ApiResult<SArticle> getHealthArticle(@PathVariable("id") Long id){
        //Long uid = LocalUser.getUser().getUid();
        SArticle article = sArticleService.getById(id);
        return  ApiResult.ok(article);
    }


}
