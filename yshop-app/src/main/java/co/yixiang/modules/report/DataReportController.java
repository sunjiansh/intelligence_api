package co.yixiang.modules.report;

import co.yixiang.annotation.AnonymousAccess;
import co.yixiang.common.util.BASE64;
import co.yixiang.modules.device.tumbledatarecords.service.DTumbleDataRecordsService;
import co.yixiang.modules.device.tumbledatarecords.threads.DTumbleDataRecordHandleThread;
import co.yixiang.modules.device.watchuricdatarecords.service.DWatchUricDataRecordsService;
import co.yixiang.modules.device.watchuricdatarecords.threads.DWatchUricDataRecordHandleThread;
import co.yixiang.modules.device.watchuricdatarecords.util.Constans;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * @author sunjian
 * @Date 2023/1/29
 * @license 版权所有，非经许可请勿使用本代码
 */
@RestController
@RequestMapping("/")
@Api(tags = "数据上报Controller")
public class DataReportController {

    private static final AtomicInteger ATOMIC_INTEGER = new AtomicInteger();

    @Autowired
    DWatchUricDataRecordsService dWatchUricDataRecordsService;
    @Autowired
    DTumbleDataRecordsService dTumbleDataRecordsService;


    /**
     * 测试限流注解，下面配置说明该接口 60秒内最多只能访问 10次，保存到redis的键名为 limit_test，
     */
    @GetMapping("/limit")
    @AnonymousAccess
    @ApiOperation("测试")
    public int testLimit() {
        return ATOMIC_INTEGER.incrementAndGet();
    }


    /**
     * 暴露给外部地址 http://sunjiansh.nat123.cc:53750/report
     * 用于接受手环和尿酸检测仪上报数据是的回调
     * @return
     */
    @AnonymousAccess
    @PostMapping("/report")
    public String report(@RequestBody JSONObject jsonObject){
        System.out.println(jsonObject);
//        String imei = jsonObject.getString("imei");
//        String deviceModel = jsonObject.getString("deviceModel");//1 是L18产品
//        String cmd = jsonObject.getString("cmd");// CZW06 测体温 CZW07 定位
//        String temperature = jsonObject.getString("temperature");// 体温值
//        String pushTimestamp = jsonObject.getString("pushTimestamp");
//        Date pushTimes = new Date(jsonObject.getLongValue("pushTimestamp"));
        try {
           // dWatchUricDataRecordsService.handleWatchAndUricReportData(jsonObject);
            //用线程池来处理
            Constans.watchUricDataRecordHandlePool.execute(new DWatchUricDataRecordHandleThread(jsonObject));
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 暴露给外部地址 http://sunjiansh.nat123.cc:53750/monitor
     * 用于接受跌倒报警器的回调（消息流转--目的地/消息路由中的配置地址）
     * @return
     */
    @AnonymousAccess
    @PostMapping("/monitor")
    public String monitor(@RequestBody JSONObject jsonObject) throws Exception{
        System.out.println("YYYYYYYYYYYYYYY"+jsonObject);
//        JSONObject payload = jsonObject.getJSONObject("payload");
//        if(payload!=null){
//            System.out.println("APPdata解密后:"+new String(BASE64.decryptBASE64(payload.getString("APPdata"))));
//        }
        try {
           // dTumbleDataRecordsService.handleTumbleReportData(jsonObject);
            Constans.tumbleDataRecordHandlePool.execute(new DTumbleDataRecordHandleThread(jsonObject));
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 暴露给外部地址 http://sunjiansh.nat123.cc:53750/up
     * 用于接受跌倒报警器的回调（设备-订阅管理里配置的地址）
     * @return
     */
    @AnonymousAccess
    @PostMapping("/up")
    public String up(@RequestBody JSONObject jsonObject) throws Exception{
        System.out.println("XXXXXXXXXXXXXXXX"+jsonObject);
        JSONObject payload = jsonObject.getJSONObject("payload");
        if(payload!=null){
            System.out.println("APPdata解密后:"+new String(BASE64.decryptBASE64(payload.getString("APPdata"))));
        }
        return "up";
    }


    public static void main(String[] args) {
        JSONObject json = new JSONObject();



        Date date = new Date(json.getLongValue("1674993480339"));


        System.out.println(date);
    }

}
