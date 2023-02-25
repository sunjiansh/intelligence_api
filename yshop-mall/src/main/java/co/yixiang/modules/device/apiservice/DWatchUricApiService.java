package co.yixiang.modules.device.apiservice;

import co.yixiang.common.util.HexUtil;
import co.yixiang.modules.device.watchuricdatarecords.domain.DWatchUricDataRecords;
import co.yixiang.modules.user.domain.YxUser;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author sunjian
 * @Date 2023/1/30
 * @license 版权所有，非经许可请勿使用本代码
 */
@Service
public class DWatchUricApiService {

    @Value("${watchUricApi.baseurl}")
    private String url;

    @Value("${watchUricApi.appId}")
    private String appId;

    @Value("${watchUricApi.appSecret}")
    private String appSecret;



    /**
     * 查询用户列表
     * @throws Exception
     */
    public  void queryUserList() throws Exception{
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url+"/user/queryUserList")
                    .get()
                    .addHeader("appId", appId)
                    .addHeader("appSecret", appSecret)
                    .addHeader("content-type", "application/x-www-form-urlencoded")
                    .build();
                Response response = client.newCall(request).execute();
                JSONObject jsonResult = (JSONObject) JSONObject.parse(response.body().string());
                System.out.println(jsonResult);
        }

    /**
     * 同步用户信息
     * @throws Exception
     */
    public  JSONObject syncUserInfo(YxUser user) throws Exception{
        OkHttpClient client = new OkHttpClient();
       // System.out.println(dWatchUricProperties.getBaseURL());
        MediaType mediaType = MediaType.parse("application/json");
        JSONObject bodyJson = new JSONObject();
        bodyJson.put("uid",user.getUid());
        bodyJson.put("sex",user.getSex());
        bodyJson.put("birthday",user.getBirthday());
       /// bodyJson.put("height",180);
        //bodyJson.put("weight",65);
        RequestBody body = RequestBody.create(mediaType, bodyJson.toJSONString());

        //RequestBody body = RequestBody.create(mediaType, "{\r\n\t\"uid\": \"20221227001\",\r\n\t\"sex\": \"1\",\r\n\t\"birthday\": \"2022-12-12\",\r\n\t\"height\": \"180\",\r\n\t\"weight\": \"60\"\r\n}");
        Request request = new Request.Builder()
                .url(url+"/user/sync")
                .post(body)
                .addHeader("appId", appId)
                .addHeader("appSecret", appSecret)
                .addHeader("content-type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        JSONObject jsonResult = (JSONObject) JSONObject.parse(response.body().string());
        System.out.println(jsonResult);
        return jsonResult;
    }

    /**
     * 根据用户id查询该用户已绑定设备的IMEI
     * @param userId
     * @return
     * @throws Exception
     */
    public String  queryBindedImeByUid(Long userId) throws Exception{
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url+"/device/queryDeviceInfoByUser?uid="+userId)
                .get()
                .addHeader("appId", appId)
                .addHeader("appSecret", appSecret)
                .addHeader("content-type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        JSONObject jsonResult = (JSONObject) JSONObject.parse(response.body().string());
        System.out.println(jsonResult);
        if(jsonResult.getInteger("code") != 200){
            return null;
        }
        JSONObject data = jsonResult.getJSONObject("data");
        if(data!=null){
            return   data.getString("imei");
        }
        return null;
    }

    /**
     * 初始化配置手表信息，例如睡眠开关和监测时间
     * @param imei
     * @return
     * @throws Exception
     */
    public JSONObject configWatchSleepInfo(String imei) throws Exception{
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        JSONObject bodyJson = new JSONObject();
        JSONObject userConfig = new JSONObject();
        JSONObject sendBody = new JSONObject();

        userConfig.put("imei",imei);
        userConfig.put("sleepSwitch",1);//睡眠监测开关打开。0是关闭
        userConfig.put("sleepTimeStart","23:00");
        userConfig.put("sleepTimeEnd","08:00");

        sendBody.put("imei",imei);
        sendBody.put("cmd","BP96");//睡眠设置指令

        bodyJson.put("userConfig",userConfig);
        bodyJson.put("sendBody",sendBody);
        RequestBody body = RequestBody.create(mediaType, bodyJson.toJSONString());

     //   RequestBody body = RequestBody.create(mediaType, "{\r\n    \"userConfig\":{\r\n        \"imei\":\"356221322018563\",\r\n        \"sleepSwitch\":1,\r\n        \"sleepTimeStart\":\"23:00\",\r\n        \"sleepTimeEnd\":\"08:00\"\r\n    },\r\n    \"sendBody\":{\r\n        \"imei\":\"356221322018563\",\r\n        \"cmd\":\"BP96\"\r\n    }\r\n}");
        Request request = new Request.Builder()
                .url("https://api-xintai-beta.colofoo.com/openapi/device/updateConfig")
                .post(body)
                .addHeader("appId", appId)
                .addHeader("appSecret", appSecret)
                .addHeader("content-type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        JSONObject jsonResult = (JSONObject) JSONObject.parse(response.body().string());
        System.out.println(jsonResult);
        return jsonResult;
    }

    /**
     * 初始化配置手表sos信息
     * @param imei
     * @return
     * @throws Exception
     */
    public JSONObject configWatchSOSInfo(String imei,String sos) throws Exception{
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        JSONObject bodyJson = new JSONObject();
        JSONObject userConfig = new JSONObject();
        JSONObject sendBody = new JSONObject();

        userConfig.put("imei",imei);
        userConfig.put("sos",sos);//sos号码，最多3个，sos无更新，通过覆盖来设置sos号码。空字符串 代表移除所有设置sos号码

        sendBody.put("imei",imei);
        sendBody.put("cmd","BP12");//SOS号码设置指令，

        bodyJson.put("userConfig",userConfig);
        bodyJson.put("sendBody",sendBody);
        RequestBody body = RequestBody.create(mediaType, bodyJson.toJSONString());

        Request request = new Request.Builder()
                .url("https://api-xintai-beta.colofoo.com/openapi/device/updateConfig")
                .post(body)
                .addHeader("appId", appId)
                .addHeader("appSecret", appSecret)
                .addHeader("content-type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        JSONObject jsonResult = (JSONObject) JSONObject.parse(response.body().string());
        System.out.println(jsonResult);
        return jsonResult;
    }


    /**
     * 配置体温上报时间间隔信息
     * @param imei
     * @param wdstart
     * @return
     * @throws Exception
     */
    public JSONObject configWatchTemperature(String imei,Integer wdstart) throws Exception{
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        JSONObject bodyJson = new JSONObject();
        JSONObject userConfig = new JSONObject();
        JSONObject sendBody = new JSONObject();

        userConfig.put("imei",imei);
        userConfig.put("wdstart",wdstart);

        sendBody.put("imei",imei);
        sendBody.put("cmd","BP87");//体温上传设置指令，

        bodyJson.put("userConfig",userConfig);
        bodyJson.put("sendBody",sendBody);
        RequestBody body = RequestBody.create(mediaType, bodyJson.toJSONString());

        Request request = new Request.Builder()
                .url("https://api-xintai-beta.colofoo.com/openapi/device/updateConfig")
                .post(body)
                .addHeader("appId", appId)
                .addHeader("appSecret", appSecret)
                .addHeader("content-type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        JSONObject jsonResult = (JSONObject) JSONObject.parse(response.body().string());
        System.out.println(jsonResult);
        return jsonResult;
    }

    /**
     * 配置心率、血压、血氧、血糖上传频率(单位秒,连续上传时最小时间不小于 300 秒，最大不超过 65535)0：关闭 1：单次上传
     * @param imei
     * @param bldstart
     * @return
     * @throws Exception
     */
    public JSONObject configWatchHr(String imei,Integer bldstart) throws Exception{
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        JSONObject bodyJson = new JSONObject();
        JSONObject userConfig = new JSONObject();
        JSONObject sendBody = new JSONObject();

        userConfig.put("imei",imei);
        userConfig.put("bldstart",bldstart);

        sendBody.put("imei",imei);
        sendBody.put("cmd","BP86");//设置心率/血压/血氧/血糖测量周期 上传设置指令，

        bodyJson.put("userConfig",userConfig);
        bodyJson.put("sendBody",sendBody);
        RequestBody body = RequestBody.create(mediaType, bodyJson.toJSONString());

        Request request = new Request.Builder()
                .url("https://api-xintai-beta.colofoo.com/openapi/device/updateConfig")
                .post(body)
                .addHeader("appId", appId)
                .addHeader("appSecret", appSecret)
                .addHeader("content-type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        JSONObject jsonResult = (JSONObject) JSONObject.parse(response.body().string());
        System.out.println(jsonResult);
        return jsonResult;
    }



    /**
     * 配置睡眠开关和监测时间
     * @param imei
     * @param sleepTimeStart
     * @param sleepTimeEnd
     * @return JSONObject
     * @throws Exception
     */
    public JSONObject configWatchSleep(String imei, String sleepTimeStart,String  sleepTimeEnd) throws Exception{
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        JSONObject bodyJson = new JSONObject();
        JSONObject userConfig = new JSONObject();
        JSONObject sendBody = new JSONObject();

        userConfig.put("imei",imei);
        userConfig.put("sleepSwitch",1);//睡眠监测开关打开。0是关闭
        userConfig.put("sleepTimeStart",sleepTimeStart);//必须是"23:00"这种格式
        userConfig.put("sleepTimeEnd",sleepTimeEnd);//"08:00"

        sendBody.put("imei",imei);
        sendBody.put("cmd","BP96");//睡眠设置指令

        bodyJson.put("userConfig",userConfig);
        bodyJson.put("sendBody",sendBody);
        RequestBody body = RequestBody.create(mediaType, bodyJson.toJSONString());

        //   RequestBody body = RequestBody.create(mediaType, "{\r\n    \"userConfig\":{\r\n        \"imei\":\"356221322018563\",\r\n        \"sleepSwitch\":1,\r\n        \"sleepTimeStart\":\"23:00\",\r\n        \"sleepTimeEnd\":\"08:00\"\r\n    },\r\n    \"sendBody\":{\r\n        \"imei\":\"356221322018563\",\r\n        \"cmd\":\"BP96\"\r\n    }\r\n}");
        Request request = new Request.Builder()
                .url("https://api-xintai-beta.colofoo.com/openapi/device/updateConfig")
                .post(body)
                .addHeader("appId", appId)
                .addHeader("appSecret", appSecret)
                .addHeader("content-type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        JSONObject jsonResult = (JSONObject) JSONObject.parse(response.body().string());
        System.out.println(jsonResult);
        return jsonResult;
    }



    /**
     * 配置位置信息上传频率
     * @param imei
     * @param location
     * @return JSONObject
     * @throws Exception
     */
    public JSONObject configWatchLocation(String imei, Integer location) throws Exception{
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        JSONObject bodyJson = new JSONObject();
        JSONObject userConfig = new JSONObject();
        JSONObject sendBody = new JSONObject();

        userConfig.put("imei",imei);
        userConfig.put("gpsswitch",0);//必须设置为0，关闭gps才会上报位置数据
        userConfig.put("location",location);//单位： 秒

        sendBody.put("imei",imei);
        sendBody.put("cmd","BP34");//定位设置指令

        bodyJson.put("userConfig",userConfig);
        bodyJson.put("sendBody",sendBody);
        RequestBody body = RequestBody.create(mediaType, bodyJson.toJSONString());

        //   RequestBody body = RequestBody.create(mediaType, "{\r\n    \"userConfig\":{\r\n        \"imei\":\"356221322018563\",\r\n        \"sleepSwitch\":1,\r\n        \"sleepTimeStart\":\"23:00\",\r\n        \"sleepTimeEnd\":\"08:00\"\r\n    },\r\n    \"sendBody\":{\r\n        \"imei\":\"356221322018563\",\r\n        \"cmd\":\"BP96\"\r\n    }\r\n}");
        Request request = new Request.Builder()
                .url("https://api-xintai-beta.colofoo.com/openapi/device/updateConfig")
                .post(body)
                .addHeader("appId", appId)
                .addHeader("appSecret", appSecret)
                .addHeader("content-type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        JSONObject jsonResult = (JSONObject) JSONObject.parse(response.body().string());
        System.out.println(jsonResult);
        return jsonResult;
    }

    /**
     * 配置通讯录
     * @param imei
     * @param contacts
     * @return JSONObject
     * @throws Exception
     */
    public JSONObject configWatchContacts(String imei, JSONArray contacts) throws Exception{
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        JSONObject bodyJson = new JSONObject();
        JSONObject userConfig = new JSONObject();
        JSONObject sendBody = new JSONObject();

        userConfig.put("imei",imei);
        userConfig.put("whiteList",contacts);//

        sendBody.put("imei",imei);
        sendBody.put("cmd","BP14");//通讯录设置指令

        bodyJson.put("userConfig",userConfig);
        bodyJson.put("sendBody",sendBody);
        RequestBody body = RequestBody.create(mediaType, bodyJson.toJSONString());

        //   RequestBody body = RequestBody.create(mediaType, "{\r\n    \"userConfig\":{\r\n        \"imei\":\"356221322018563\",\r\n        \"sleepSwitch\":1,\r\n        \"sleepTimeStart\":\"23:00\",\r\n        \"sleepTimeEnd\":\"08:00\"\r\n    },\r\n    \"sendBody\":{\r\n        \"imei\":\"356221322018563\",\r\n        \"cmd\":\"BP96\"\r\n    }\r\n}");
        Request request = new Request.Builder()
                .url("https://api-xintai-beta.colofoo.com/openapi/device/updateConfig")
                .post(body)
                .addHeader("appId", appId)
                .addHeader("appSecret", appSecret)
                .addHeader("content-type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        JSONObject jsonResult = (JSONObject) JSONObject.parse(response.body().string());
        System.out.println(jsonResult);
        return jsonResult;
    }


    /**
     * 绑定手环
     * @param userId
     * @param imei
     * @return
     */
    public  JSONObject bindWatch(Long userId,String imei) throws Exception{
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        JSONObject bodyJson = new JSONObject();
        bodyJson.put("uid",userId);
        bodyJson.put("uuid",imei);

        RequestBody body = RequestBody.create(mediaType, bodyJson.toJSONString());
       // RequestBody body = RequestBody.create(mediaType, "{\r\n\t\"uid\": \"1\",\r\n\t\"uuid\": \"356221322018563\"\r\n}");
        Request request = new Request.Builder()
                .url(url+"/device/postRegisterDevice")
                .post(body)
                .addHeader("appId", appId)
                .addHeader("appSecret", appSecret)
                .addHeader("content-type", "application/json")
                .build();

        Response response = client.newCall(request).execute();
        JSONObject jsonResult = (JSONObject) JSONObject.parse(response.body().string());
        System.out.println(jsonResult);
        return jsonResult;
    }


    /***
     * 解绑手表
     * @return
     * @throws Exception
     */
    public JSONObject unBindWatch(Long userId,String imei) throws Exception{
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        JSONObject bodyJson = new JSONObject();
        bodyJson.put("uid",userId);
        bodyJson.put("uuid",imei);
        RequestBody body = RequestBody.create(mediaType, bodyJson.toJSONString());
        //RequestBody body = RequestBody.create(mediaType, "{\r\n\t\"uid\": \"20221227001\",\r\n\t\"uuid\": \"356221322018563\"\r\n}");
        Request request = new Request.Builder()
                .url(url+"/device/removeMatch")
                .post(body)
                .addHeader("appId", appId)
                .addHeader("appSecret", appSecret)
                .addHeader("content-type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        JSONObject jsonResult = (JSONObject) JSONObject.parse(response.body().string());
        System.out.println(jsonResult);
        return jsonResult;
    }




    /**
     * 绑定尿酸分析仪
     * @param userId
     * @param sn
     * @return
     */
    public  JSONObject bindUricDevice(Long userId,String sn) throws Exception{
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        JSONObject bodyJson = new JSONObject();
        bodyJson.put("uid",userId);
        bodyJson.put("imei",sn);
        RequestBody body = RequestBody.create(mediaType, bodyJson.toJSONString());
        // RequestBody body = RequestBody.create(mediaType, "{\r\n\t\"uid\": \"20221227001\",\r\n\t\"imei\": \"356221322018563\"\r\n}");
        Request request = new Request.Builder()
                .url("https://api-xintai.colofoo.com/openapi/device/bsDevice/add")
                .post(body)
                .addHeader("appId", appId)
                .addHeader("appSecret", appSecret)
                .addHeader("content-type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        JSONObject jsonResult = (JSONObject) JSONObject.parse(response.body().string());
        System.out.println(jsonResult);
        return jsonResult;
    }


    /**
     * 解绑定尿酸分析仪
     * @param userId
     * @return
     */
    public  JSONObject unBindUricDevice(Long userId) throws Exception{
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        JSONObject bodyJson = new JSONObject();
        bodyJson.put("uid",userId);
        RequestBody body = RequestBody.create(mediaType, bodyJson.toJSONString());
       // RequestBody body = RequestBody.create(mediaType, "{\r\n    \"uid\":\"1\"\r\n}");
        Request request = new Request.Builder()
                .url("https://api-xintai.colofoo.com/openapi/device/bsDevice/del")
                .post(body)
                .addHeader("appId", appId)
                .addHeader("appSecret", appSecret)
                .addHeader("content-type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        JSONObject jsonResult = (JSONObject) JSONObject.parse(response.body().string());
        System.out.println(jsonResult);
        return jsonResult;
    }



    /**
     * 上传血压数据
     * @param records
     * @return
     */
    public  JSONObject  batchUploadBpInfo(DWatchUricDataRecords records) throws Exception{
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");

        JSONObject bodyJson = new JSONObject();
        bodyJson.put("uid",records.getUserId());
        bodyJson.put("sbp",records.getSbp());
        bodyJson.put("dbp",records.getDbp());
        bodyJson.put("recordTime",new Date().getTime());
        bodyJson.put("measureMode",records.getMeasureMode());

        JSONArray list = new JSONArray();
        list.add(bodyJson);

        RequestBody body = RequestBody.create(mediaType, list.toJSONString());
        //RequestBody body = RequestBody.create(mediaType, "[{\r\n  \"sbp\": 110,\r\n  \"dbp\": 50,\r\n  \"uid\": \"1\",\r\n  \"recordTime\": \"1625885067101\",\r\n  \"measureMode\": 1\r\n}]");
        Request request = new Request.Builder()
                .url("https://api-xintai-beta.colofoo.com/openapi/bp/upload/batch")
                .post(body)
                .addHeader("appId", appId)
                .addHeader("appSecret", appSecret)
                .addHeader("content-type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        JSONObject jsonResult = (JSONObject) JSONObject.parse(response.body().string());
        System.out.println(jsonResult);
        return jsonResult;
    }


    /**
     * 上传心率数据
     */
    public  JSONObject batchUploadHeart(DWatchUricDataRecords records) throws Exception{
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        JSONObject bodyJson = new JSONObject();
        bodyJson.put("uid",records.getUserId());
        bodyJson.put("heartRate",records.getHeartRate());
        bodyJson.put("recordTime",records.getPushTime().getTime());
        bodyJson.put("measureMode",records.getMeasureMode());

        JSONArray list = new JSONArray();
        list.add(bodyJson);

        RequestBody body = RequestBody.create(mediaType, list.toJSONString());

        //RequestBody body = RequestBody.create(mediaType, "[{\r\n  \"heartRate\": 110,\r\n  \"uid\": \"1\",\r\n  \"recordTime\": \"1625885067101\",\r\n  \"measureMode\": 1\r\n}]");
        Request request = new Request.Builder()
                .url("https://api-xintai-beta.colofoo.com/openapi/heart/upload/batch")
                .post(body)
                .addHeader("appId", appId)
                .addHeader("appSecret", appSecret)
                .addHeader("content-type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        JSONObject jsonResult = (JSONObject) JSONObject.parse(response.body().string());
        System.out.println(jsonResult);
        return jsonResult;
    }


    /**
     * 上传睡眠数据
     * @return
     */
    public  JSONObject batchUploadSleep(DWatchUricDataRecords records) throws Exception{
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        JSONObject bodyJson = new JSONObject();
        bodyJson.put("uid",records.getUserId());
        bodyJson.put("allSleepTime",records.getAllSleepTime());
        bodyJson.put("sleepDate",records.getSleepDate());
        bodyJson.put("lowSleepTime",records.getLowSleepTime());
        bodyJson.put("deepSleepTime",records.getDeepSleepTime());
        bodyJson.put("sleepQuality",records.getSleepQuality());
        bodyJson.put("wakeCount",records.getWakeCount());
        bodyJson.put("sleepDown",records.getSleepDown());
        bodyJson.put("sleepUp",records.getSleepUp());
        bodyJson.put("recordTime",records.getPushTime().getTime());
        bodyJson.put("measureMode",records.getMeasureMode());

        JSONArray list = new JSONArray();
        list.add(bodyJson);

        RequestBody body = RequestBody.create(mediaType, list.toJSONString());

       // RequestBody body = RequestBody.create(mediaType, "[{\r\n    \"allSleepTime\": 90,\r\n    \"sleepDate\": \"2021-11-24\",\r\n    \"lowSleepTime\" : 80,\r\n    \"deepSleepTime\" : 80,\r\n    \"sleepLine\" : \"\",\r\n    \"sleepQuality\" : 2,\r\n    \"wakeCount\": 2,\r\n    \"sleepDown\": 1625885067102,\r\n    \"sleepUp\": 1625885067102,\r\n    \"uid\": \"11\",\r\n    \"recordTime\": \"1625885067102\",\r\n    \"measureMode\": 1\r\n}]");
        Request request = new Request.Builder()
                .url("https://api-xintai-beta.colofoo.com/openapi/sleep/upload/batch")
                .post(body)
                .addHeader("appId", appId)
                .addHeader("appSecret", appSecret)
                .addHeader("content-type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        JSONObject jsonResult = (JSONObject) JSONObject.parse(response.body().string());
        System.out.println(jsonResult);
        return jsonResult;
    }

    /**
     * 上传体温数据
     * @return
     */
    public  JSONObject batchUploadTemperature(DWatchUricDataRecords records) throws Exception{
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");

        JSONObject bodyJson = new JSONObject();
        bodyJson.put("uid",records.getUserId());
        bodyJson.put("temperature",records.getTemperature());
        bodyJson.put("recordTime",records.getPushTime().getTime());
        bodyJson.put("measureMode",records.getMeasureMode());

        JSONArray list = new JSONArray();
        list.add(bodyJson);

        RequestBody body = RequestBody.create(mediaType, list.toJSONString());

        //RequestBody body = RequestBody.create(mediaType, "[{\r\n    \"temperature\": 36.8,\r\n    \"uid\": \"11\",\r\n    \"recordTime\": \"1625885067102\",\r\n    \"measureMode\": 1\r\n}]");
        Request request = new Request.Builder()
                .url("https://api-xintai-beta.colofoo.com/openapi/tw/upload/batch")
                .post(body)
                .addHeader("appId", appId)
                .addHeader("appSecret", appSecret)
                .addHeader("content-type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        JSONObject jsonResult = (JSONObject) JSONObject.parse(response.body().string());
        System.out.println(jsonResult);
        return jsonResult;
    }

    /**
     * 上传血糖数据
     * @return
     */
    public  JSONObject batchUploadBs(DWatchUricDataRecords records) throws Exception{
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        JSONObject bodyJson = new JSONObject();
        bodyJson.put("uid",records.getUserId());
        bodyJson.put("ghb",records.getGhb());
        bodyJson.put("recordTime",records.getRecordTime());
        bodyJson.put("timePeriod",1);//1：空腹测量 2：餐后2小时测量 3：餐前测量 4：其它测量时间

        JSONArray list = new JSONArray();
        list.add(bodyJson);

        RequestBody body = RequestBody.create(mediaType, list.toJSONString());

        //RequestBody body = RequestBody.create(mediaType, "[{\r\n    \"ghb\": 11.11,\r\n    \"uid\": \"11\",\r\n    \"recordTime\": \"1625885067102\",\r\n    \"timePeriod\": 1\r\n}]");
        Request request = new Request.Builder()
                .url("https://api-xintai-beta.colofoo.com/openapi/bs/upload/batch")
                .post(body)
                .addHeader("appId", appId)
                .addHeader("appSecret", appSecret)
                .addHeader("content-type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        JSONObject jsonResult = (JSONObject) JSONObject.parse(response.body().string());
        System.out.println(jsonResult);
        return jsonResult;
    }

    /**
     * 上传血氧数据
     * @return
     */
    public  JSONObject batchUploadSpo2(DWatchUricDataRecords records) throws Exception{
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");

        JSONObject bodyJson = new JSONObject();
        bodyJson.put("uid",records.getUserId());
        bodyJson.put("oxygen",records.getOxygen());
        bodyJson.put("heartRate",records.getHeartRate());
        bodyJson.put("recordTime",records.getPushTime().getTime());
        bodyJson.put("measureMode",records.getMeasureMode());

        JSONArray list = new JSONArray();
        list.add(bodyJson);

        RequestBody body = RequestBody.create(mediaType, list.toJSONString());


       // RequestBody body = RequestBody.create(mediaType, "[{\r\n    \"oxygen\": 90,\r\n    \"heartRate\" : 80,\r\n    \"uid\": \"11\",\r\n    \"recordTime\": \"1625885067102\",\r\n    \"measureMode\": 1\r\n}]");
        Request request = new Request.Builder()
                .url("https://api-xintai-beta.colofoo.com/openapi/spo2/upload/batch")
                .post(body)
                .addHeader("appId", appId)
                .addHeader("appSecret", appSecret)
                .addHeader("content-type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        JSONObject jsonResult = (JSONObject) JSONObject.parse(response.body().string());
        System.out.println(jsonResult);
        return jsonResult;
    }




    public static void main(String[] args)  throws Exception{
    String xx = "xxx|1341312";
            //queryUserList();
        System.out.println(xx.split("\\|")[1]);



    }








}
